import React, { useState, useEffect } from 'react';
import '../css/RoomReservationsLibrarian.css';

const RoomReservationsLibrarian = () => {
  const [reservations, setReservations] = useState([]);
  const [filteredReservations, setFilteredReservations] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [itemsPerPage, setItemsPerPage] = useState(4);
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    fetchRoomReservations();
  }, []);

  useEffect(() => {
    const filtered = searchTerm
      ? reservations.filter(reservation =>
          reservation.reservationid.toString().includes(searchTerm)
        )
      : reservations;
    setFilteredReservations(filtered);
  }, [searchTerm, reservations]);

  const fetchRoomReservations = async () => {
    try {
      const response = await fetch('http://localhost:8080/RoomReservations/today');
      const data = await response.json();
      setReservations(data);
      setFilteredReservations(data); 
    } catch (error) {
      console.error('Error fetching room reservations:', error);
    }
  };

  const handleStatusChange = async (reservationId) => {
    try {
      const response = await fetch(`http://localhost:8080/RoomReservations/${reservationId}/updateStatus`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });
  
      if (response.ok) {
        fetchRoomReservations(); 
      } else {
        console.error('Failed to update reservation status');
      }
    } catch (error) {
      console.error('Error updating reservation status:', error);
    }
  };
  

  const handleAddTime = async (reservationId) => {
    try {
      const response = await fetch(`http://localhost:8080/RoomReservations/extendReservation/${reservationId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });
  
      if (response.ok) {
        fetchRoomReservations();
      } else {
        console.error('Failed to extend reservation time');
      }
    } catch (error) {
      console.error('Error extending reservation time:', error);
    }
  };
  

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString(); 
  };

  const lastIndex = currentPage * itemsPerPage;
  const firstIndex = lastIndex - itemsPerPage;
  const currentReservations = reservations.slice(firstIndex, lastIndex);
  const totalPages = Math.ceil(reservations.length / itemsPerPage);

  return (
    <div className='room-reservation-page'>
      <h2>Room Reservations</h2>
      <div className="search__container">
        <input
          type="text"
          className="search__input"
          placeholder="Reservation ID"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </div>
      {filteredReservations
        .slice(firstIndex, lastIndex)
        .map((reservation) => (
          <div key={reservation.reservationid} className="reservation-card">
          <p>Reservation ID: {reservation.reservationid}</p>
          <p>User Name: {reservation.user.nome}</p>
          <p>User Email: {reservation.user.email}</p>
          <p>Room Number: {reservation.room.number}</p>
          <p>Start Time: {formatDateTime(reservation.starttime)}</p>
          <p>End Time: {formatDateTime(reservation.endtime)}</p>
          <p>Status: {reservation.status}</p>
          {/* Add additional buttons and functionalities as needed */}
          <div className='reservation-buttons'>
            {reservation.status === 'reserved' && (
              <button className='check_in' onClick={() => handleStatusChange(reservation.reservationid)}>
                Check-In
              </button>
            )}
            {reservation.status === 'ongoing' && (
              <button className='check_out' onClick={() => handleStatusChange(reservation.reservationid)}>
                Check-Out
              </button>
            )}
            {reservation.status === 'closed' && (
              <p className="checked-out-message">Already checked Out</p>
            )}
            {['reserved', 'ongoing'].includes(reservation.status) && (
              reservation.isExtraTimeAdded 
                ? <p className="added-time-message">Already Added 30 Minutes</p>
                : <button className='add-time' onClick={() => handleAddTime(reservation.reservationid)}>
                    Add 30 Minutes
                  </button> 
            )}
          </div>
        </div>
      ))}


      {reservations.length > itemsPerPage && (
        <div className="pagination-controls">
          <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1}>
            Previous
          </button>
          <p>Page {currentPage} of {totalPages}</p>
          <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages}>
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default RoomReservationsLibrarian;
