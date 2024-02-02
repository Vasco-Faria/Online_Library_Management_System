import React,{ useEffect, useState } from "react"
import { useAuth } from "../context/AuthContext";
import '../css/myreserves.css'
import ConfirmationModal from './ConfirmationModal';

const MyReserves = () => {
    const {userInfo} = useAuth();
    const [bookReservations, setBookReservations] = useState([]);
    const [roomReservations, setRoomReservations] = useState([]);
    const [bookReservationsFound, setBookReservationsFound] = useState(true);
    const [roomReservationsFound, setRoomReservationsFound] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [selectedReservationId, setSelectedReservationId] = useState(null);
    const [refreshRooms, setRefreshRooms] = useState(false);

    useEffect(() => {
        if (userInfo && userInfo.id) {
            fetchRoomReservations();
            fetchBookReservations();
        }
    }, [userInfo]);

    const handleCancelClick = (reservationId) => {
        setShowModal(true);
        setSelectedReservationId(reservationId); 
    };
    

    const handleConfirmCancellation = () => {
        cancelReservation(selectedReservationId);
        setShowModal(false);
    };

    const handleCloseModal = () => {
        setShowModal(false);
    };

    function fetchBookDetails(bookId) {
        return fetch(`http://localhost:8080/books/details/${bookId}`)
            .then(response => response.json())
            .catch(error => console.error('Error fetching book details:', error));
    }

    function fetchRoomReservations() {
        fetch(`http://localhost:8080/RoomReservations/user/${userInfo.id}`)
            .then(response => {
                if(response.status === 204) { 
                    setRoomReservationsFound(false);
                    return;
                }
                return response.json();
            })
            .then(async reservations => {
                if(reservations) {
                    const detailedReservations = await Promise.all(
                        reservations.map(async reservation => {
                            console.log(reservation);
                            return reservation;
                        })
                    );
                    setRoomReservations(detailedReservations);
                }
            })
            .catch(error => {
                console.error('Error fetching room reservations:', error);
                setRoomReservationsFound(false);
            });
    }

    function fetchBookReservations() {
        fetch(`http://localhost:8080/BookReservations/user/${userInfo.id}`)
            .then(response => {
                if(response.status === 204) { 
                    setBookReservationsFound(false);
                    return;
                }
                return response.json();
            })
            .then(async reservations => {
                if(reservations) {
                    const detailedReservations = await Promise.all(
                        reservations.map(async reservation => {
                            const bookDetails = await fetchBookDetails(reservation.bookId);
                            return { ...reservation, bookDetails };
                        })
                    );
                    setBookReservations(detailedReservations); 
                }
            })
            .catch(error => {
                console.error('Error fetching book reservations:', error);
                setBookReservationsFound(false);
            });
    }


    const cancelReservation = async (reservationId) => {
        try {
            const response = await fetch(`http://localhost:8080/RoomReservations/reserve/cancel/${reservationId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
            });
    
            if (!response.ok) {
                throw new Error('Failed to cancel the reservation');
            }
    
            const result = await response.json();
            alert(result.message); 
            setRefreshRooms(true);
            
        } catch (error) {
            console.error('Error canceling reservation:', error);
            alert(error.message); 
        }
    };


    function handleBookExtension(reservationId) {
        
        console.log('Requesting book extension for reservation', reservationId);
    }

    function handleRoomExtension(reservationId, extensionTime) {
        
        console.log('Requesting room extension for reservation', reservationId, 'by', extensionTime);
    }

    const handleViewDetails = (bookId) => {
        window.location.href = `/book/${bookId}`;
    };

    function formatDate(isoDateString) {
        const date = new Date(isoDateString);
        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0'); 
        const year = date.getFullYear();
      
        return `${day}/${month}/${year}`;
      }

      function formatDateTime(dateTimeString) {
        if (!dateTimeString) return '';
        
        
        const dateObj = new Date(dateTimeString);
    
        
        const day = dateObj.getDate().toString().padStart(2, '0');
        const month = (dateObj.getMonth() + 1).toString().padStart(2, '0'); 
        const year = dateObj.getFullYear();
    
       
        const [, time] = dateTimeString.split('T');
    
        
        return `${day}/${month}/${year} ${time}`;
    }

    useEffect(() => {
        if (refreshRooms) {
            fetchRoomReservations();
            setRefreshRooms(false);
        }
    }, [refreshRooms]);

    return (
        <div className="user-reserves-page">
            <h2 id="h2">My Reserves</h2>
            <div className="reserves-grid">
                <div className="book-reserves">
                    <h3>Book</h3>
                    {bookReservationsFound ? (
                        bookReservations.map(reservation => (
                        <div key={reservation.id}>
                            {reservation.bookDetails ? (
                                <div className="book-details-reserves">
                                    <p>Book Reservation Id: {reservation.reservationId}</p>
                                    <p>Title: {reservation.bookDetails.volumeInfo.title}</p>
                                    <p>Author: {reservation.bookDetails.volumeInfo.authors && reservation.bookDetails.volumeInfo.authors.join(', ')}</p>
                                    <p>Reserved on: {formatDate(reservation.startTime)}</p>
                                    <p>Due date: {formatDate(reservation.endTime)}</p>
                                    <div className='book-image'>
                                        <img src={reservation.bookDetails.volumeInfo.imageLinks?.thumbnail || ''} alt={reservation.bookDetails.volumeInfo.title}/>
                                    </div>
                                    <button className="button-extend" onClick={() => handleBookExtension(reservation.reservationId)}>Extend 7 days</button>
                                    <button onClick={() => handleViewDetails(reservation.bookId)}>View Details</button>
                                </div>
                            ) : (
                                <p style={{"color":"white"}}>Loading book details...</p>
                            )}
                        </div>
                    ))
                    ) : (
                        <p style={{"color":"white"}}>No book reservations available.</p>
                    )}
                </div>
                <div className="room-reserves">
                    <h3>Room</h3>
                    {roomReservationsFound ? (
                     roomReservations.map(reservation => (
                        <div key={reservation.id}>
                            <div className="room-details-reserves">
                            <p>Room Reservation id: {reservation.reservationid}</p>
                            <p>Room Number: {reservation.room.number}</p>
                            <p>StartTime: {formatDateTime(reservation.starttime)}</p>
                            <p>EndTime: {formatDateTime(reservation.endtime)}</p>
                            <button  className="button-extend" onClick={() => handleRoomExtension(reservation.id,'30min')}>Extend</button>
                            <select onChange={(event) => handleRoomExtension(reservation.id, event)}>
                            <option value="30">Extend 30 min</option>
                            <option value="60">Extend 1 hour</option>
                            <option value="90">Extend 1 hour and 30 min</option>
                            <option value="120">Extend 2 hours</option>
                            <option value="150">Extend 2 hours and 30 min</option>
                            <option value="180">Extend 3 hours</option>
                            <option value="210">Extend 3 hours and 30 min</option>
                            <option value="240">Extend 4 hours</option>
                        </select>
                        {reservation.status === "reserved" && (
                            <button className="button-extend2" onClick={() => handleCancelClick(reservation.reservationid)}>Cancel Reservation</button>
                        )}
                        
                        </div>
                        </div>
                    ))
                    ) : (
                        <p>No room reservations available.</p>
                    )}
                </div>
            </div>
            {showModal && <ConfirmationModal text="Are you sure you want to cancel this reservation?" onConfirm={handleConfirmCancellation} onCancel={handleCloseModal} />}
        </div>
        
    );
}
export default MyReserves