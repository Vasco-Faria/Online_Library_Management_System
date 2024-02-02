// ReservationsPage.js
import React, { useState } from 'react';
import { useLocation } from 'react-router-dom'; 
import '../css/reservations.css';
import BookReservationsLibrarian from './BookReservationsLibrarian';
import RoomReservationsLibrarian from './RoomReservationsLibrarian';

const ReservationsPage = () => {
  const location = useLocation(); 
  const defaultView = location.state?.defaultView || 'books'; 

  const [showBooks, setShowBooks] = useState(defaultView === 'books');
  const [showRooms, setShowRooms] = useState(defaultView === 'rooms');
  

  const handleShowBooks = () => {
    setShowBooks(true);
    setShowRooms(false);
  };

  const handleShowRooms = () => {
    setShowBooks(false);
    setShowRooms(true);
  };

  return (
    <div className="reservations-page">
      <h1>Reservations</h1>
      {/* Add date toggle component here */}

      <div className="toggle-buttons">
        <button className={showBooks ? 'active' : ''} onClick={handleShowBooks}>
          Books
        </button>
        <button className={showRooms ? 'active' : ''} onClick={handleShowRooms}>
          Rooms
        </button>
      </div>

      {/* Display books or rooms based on the selected button */}
      {showBooks && (
        <div className="reservations-content">
          <BookReservationsLibrarian /> {/* Render BookReservationsLibrarian component */}
        </div>
      )}

      {showRooms && (
        <div className="reservations-content">
          <RoomReservationsLibrarian /> 
        </div>
      )}
    </div>
  );
};

export default ReservationsPage;
