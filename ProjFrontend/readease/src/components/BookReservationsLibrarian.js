import React, { useState, useEffect } from 'react';
import '../css/BookReservationsLibrarian.css';

const BookReservationsPage = () => {
  const [reservations, setReservations] = useState([]);
  const [itemsPerPage, setItemsPerPage] = useState(5); // Default items per page
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    fetchReservations();
  }, []);

  useEffect(() => {
    const handleResize = () => {
      setItemsPerPage(window.innerWidth <= 600 ? 2 : 5); // Change items per page based on window size
    };

    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await fetch('http://localhost:8080/BookReservations/all');
      const data = await response.json();
      setReservations(data);
    } catch (error) {
      console.error('Error fetching reservations:', error);
    }
  };

  const handleStatusChange = async (reservationId, status) => {
    try {
      const endpoint = status === 'with-user' 
        ? `/mark-as-with-user/${reservationId}`
        : `/mark-as-collected/${reservationId}`;
      const response = await fetch(`http://localhost:8080/BookReservations${endpoint}`, { method: 'PUT' });
      if (response.ok) {
        fetchReservations();
      }
    } catch (error) {
      console.error('Error updating reservation status:', error);
    }
  };

  const lastIndex = currentPage * itemsPerPage;
  const firstIndex = lastIndex - itemsPerPage;
  const currentReservations = reservations.slice(firstIndex, lastIndex);
  const totalPages = Math.ceil(reservations.length / itemsPerPage);

  return (
    <div className='book-reservation-page'>
      <h2>Book Reservations</h2>
      {currentReservations.map((reservation) => (
        <div key={reservation.reservationId} className="reservation-card">
          <p>Reservation ID: {reservation.reservationId}</p>
          {/* Other reservation details */}
          <div className='reservation-buttons'>
            {reservation.status === 'RESERVED' && (
              <button className='with-user-button' onClick={() => handleStatusChange(reservation.reservationId, 'with-user')}>
                Mark as With User
              </button>
            )}
            {reservation.status === 'WITH_USER' && (
              <button className='collected-button' onClick={() => handleStatusChange(reservation.reservationId, 'collected')}>
                Mark as Collected
              </button>
            )}
            {reservation.status === 'COLLECTED' && (
              <p className='collected-label'>Already Collected</p>
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

export default BookReservationsPage;
