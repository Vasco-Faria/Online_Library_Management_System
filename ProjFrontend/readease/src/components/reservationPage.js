import React, { useState, useEffect } from 'react';
import '../css/reservationPage.css';
import { useAuth } from '../context/AuthContext';

const ReservationPage = () => {
  const { isAuthenticated, userInfo } = useAuth();
  const [startTime, setStartTime] = useState('');
  const [endTime, setEndTime] = useState('');
  const [selectedDay, setSelectedDay] = useState('');
  const [availableRooms, setAvailableRooms] = useState([]);
  const [dateValidationMessage, setDateValidationMessage] = useState('');
  const [timeValidationMessage, setTimeValidationMessage] = useState('');
  const [isDateTouched, setIsDateTouched] = useState(false);
  const [isTimeTouched, setIsTimeTouched] = useState(false);
  const [itemsPerPage, setItemsPerPage] = useState(6);
  const [currentPage, setCurrentPage] = useState(1);

  const lastIndex = currentPage * itemsPerPage;
  const firstIndex = lastIndex - itemsPerPage;
  const currentRooms = availableRooms.slice(firstIndex, lastIndex);

  const totalPages = Math.ceil(availableRooms.length / itemsPerPage);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth <= 600) {
        setItemsPerPage(3);
      } else {
        setItemsPerPage(6);
      }
    };
  
    handleResize();
  
    window.addEventListener('resize', handleResize);
  
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    console.log("userInfo:", userInfo);
  }, [userInfo]);

  const validateTime = (start, end) => {
    const startTime = new Date(`01/01/2021 ${start}`);
    const endTime = new Date(`01/01/2021 ${end}`);
    const duration = (endTime - startTime) / (1000 * 60); // Duration in minutes

    return duration >= 30 && duration <= 240 && // Check if duration is between 30 mins and 4 hours
           ['00', '30'].includes(start.split(':')[1]) && // Check if start time is on the hour or half-hour
           ['00', '30'].includes(end.split(':')[1]);    // Check if end time is on the hour or half-hour
  };

  const fetchAvailableRooms = async () => {
    try {
      const formattedStartTime = `${selectedDay}T${startTime}`;
      const formattedEndTime = `${selectedDay}T${endTime}`;

      console.log(' Start Time:', formattedStartTime);
      console.log(' End Time:', formattedEndTime);
  
      const response = await fetch(
        `http://localhost:8080/api/rooms/available?start_time=${formattedStartTime}&end_time=${formattedEndTime}`,
        {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            
          }
        }
      );
  
      if (!response.ok) {
        throw new Error('Error fetching available rooms');
      }
  
      const rooms = await response.json();
      setAvailableRooms(rooms);
    } catch (error) {
      console.error('Error fetching available rooms:', error);
    }
  };
  

  const handleReservation = async (roomNumber) => {
    try {
        const formattedStartTime = new Date(selectedDay + 'T' + startTime).toISOString();
        const formattedEndTime = new Date(selectedDay + 'T' + endTime).toISOString();

        const response = await fetch(`http://localhost:8080/RoomReservations/reserve`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                roomNumber,
                userId: userInfo.id,
                startTime: formattedStartTime,
                endTime: formattedEndTime,
            }),
        });

        const result = await response.json();
        if (response.ok) {
            console.log('Reservation successful:', result);
            fetchAvailableRooms();
        } else {
            throw new Error(result.message);
        }
    } catch (error) {
        console.error('Error making reservation:', error.message);
        alert(error.message);
    }
};
  
  
      
  
  
  useEffect(() => {
    const currentDate = new Date();
    const maxDate = new Date();
    maxDate.setDate(currentDate.getDate() + 3);

    const selectedDate = new Date(selectedDay);
    const isDateValid = selectedDate >= currentDate && selectedDate <= maxDate;
    const isTimeValid = validateTime(startTime, endTime);

    if (isDateTouched && !isDateValid) {
      setDateValidationMessage('Selected date is not valid. Please choose a date within the next 3 days.');
    } else {
      setDateValidationMessage('');
    }      


    if (isTimeTouched && !isTimeValid) {
      setTimeValidationMessage('Selected time is not valid. Please choose a time between 30 mins and 4 hours, at 00 or 30 minutes.');
    } else {
      setTimeValidationMessage('');
    }

    if (isDateValid && isTimeValid) {
      fetchAvailableRooms();
    }
  }, [startTime, endTime, selectedDay, isDateTouched, isTimeTouched]);

  return (
    <div className="reservation-page">
      <div className='day'>
        <h1>Room Reservation</h1>
        <div>
          <label htmlFor="selectedDay">Selected Day:</label>
          <input
            type="date"
            id="selectedDay"
            value={selectedDay}
            onChange={(e) => {
              setSelectedDay(e.target.value);
              setIsDateTouched(true);
            }}
          />
        </div>      
        {isDateTouched && dateValidationMessage && <p className="validation-message">{dateValidationMessage}</p>}
      </div>
      <div className="timeslot">
        <div>
          <label htmlFor="startTime">Start Time:</label>
          <input
            type="time"
            id="startTime"
            value={startTime}
            onChange={(e) => {
              setStartTime(e.target.value);
              setIsTimeTouched(true);
            }}
          />
        </div>
        <div>
          <label htmlFor="endTime">End Time:</label>
          <input
            type="time"
            id="endTime"
            value={endTime}
            onChange={(e) => {
              setEndTime(e.target.value);
              setIsTimeTouched(true);
            }}
          />
        </div>
      </div>
      {isTimeTouched && timeValidationMessage && <p className="validation-message">{timeValidationMessage}</p>}
      <div className="available-rooms">
        {currentRooms.map((room) => (
        <div key={room.number} className="room-card">
          <p>Room Number: {room.number}</p>
          <p>Status: {room.disponibilidade ? 'Available' : 'Unavailable'}</p>
          <button onClick={() => handleReservation(room.number)}>
            Reserve Room
          </button>
        </div>
        ))}
      </div>
      {availableRooms.length > itemsPerPage && (
        <div className="pagination-controls">
          <button
            onClick={() => setCurrentPage(currentPage - 1)}
            disabled={currentPage === 1}
          >
            Previous
          </button>
          <p>Page {currentPage} of {totalPages}</p>
          <button
            onClick={() => setCurrentPage(currentPage + 1)}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
};

export default ReservationPage;
