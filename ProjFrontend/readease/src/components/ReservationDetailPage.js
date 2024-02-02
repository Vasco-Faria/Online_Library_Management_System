import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import '../css/ReservationDetailPage.css';

const ReservationDetailPage = () => {
  const { reservationId } = useParams();
  const [reservationDetails, setReservationDetails] = useState(null);
  const [error, setError] = useState(null);
  const [notes, setNotes] = useState(""); // Adiciona estado para as notas

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString(); 
  };

  useEffect(() => {
    if (!reservationId) {
      setError("Reservation ID is missing.");
      return;
    }

    fetch(`http://localhost:8080/RoomReservations/${reservationId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
        return response.json();
      })
      .then(data => {
        setReservationDetails(data);
      })
      .catch(error => {
        setError(`Error fetching reservation details: ${error.message}`);
      });
  }, [reservationId]);

  const handleStatusChange = async (reservationId) => {
    try {
      const response = await fetch(`http://localhost:8080/RoomReservations/${reservationId}/updateStatus`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (response.ok) {
        // Atualizar os detalhes da reserva após a alteração do status
        fetch(`http://localhost:8080/RoomReservations/${reservationId}`)
          .then(response => response.json())
          .then(data => {
            setReservationDetails(data);
          })
          .catch(error => {
            setError(`Error fetching reservation details: ${error.message}`);
          });
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
        // Atualizar os detalhes da reserva após a adição de tempo
        fetch(`http://localhost:8080/RoomReservations/${reservationId}`)
          .then(response => response.json())
          .then(data => {
            setReservationDetails(data);
          })
          .catch(error => {
            setError(`Error fetching reservation details: ${error.message}`);
          });
      } else {
        console.error('Failed to extend reservation time');
      }
    } catch (error) {
      console.error('Error extending reservation time:', error);
    }
  };

  const handleNotesChange = (event) => {
    setNotes(event.target.value);
  };

  const handleSaveNotes = async () => {
    try {
      const response = await fetch(`http://localhost:8080/RoomReservations/${reservationId}/addNotes`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ notes }),
      });
  
      if (response.ok) {
        // Atualizar os detalhes da reserva após a adição de notas
        const detailsResponse = await fetch(`http://localhost:8080/RoomReservations/${reservationId}`);
        const detailsData = await detailsResponse.json();
  
        setReservationDetails(detailsData);
  
        // Exibir mensagem de sucesso
        window.alert('Mensagem enviada com sucesso!');
        
        // Limpar as notas
        setNotes("");
      } else {
        console.error('Failed to add notes');
      }
    } catch (error) {
      console.error('Error adding notes:', error);
    }
  };
  
  
  if (error) {
    return <div>{error}</div>;
  }

  if (!reservationDetails) {
    return <div>Carregando...</div>;
  }

  return (
    <div className='reservation-detai-page'>
    <div className="reservation-card" id='detail-card'>
      <h2>Detalhes da Reserva</h2>
      <p>Reservation ID: {reservationDetails.reservationid}</p>
      <p>User Name: {reservationDetails.user.nome}</p>
      <p>User Email: {reservationDetails.user.email}</p>
      <p>Room Number: {reservationDetails.room.number}</p>
      <p>Start Time: {formatDateTime(reservationDetails.starttime)}</p>
      <p>End Time: {formatDateTime(reservationDetails.endtime)}</p>
      <p>Status: {reservationDetails.status}</p>
      
      {/* Adicione outros detalhes conforme necessário */}
      
     

      <div className='reservation-buttons'>
        {reservationDetails.status === 'reserved' && (
          <button className='check_in' onClick={() => handleStatusChange(reservationDetails.reservationid)}>
            Check-In
          </button>
        )}
        {reservationDetails.status === 'ongoing' && (
          <button className='check_out' onClick={() => handleStatusChange(reservationDetails.reservationid)}>
            Check-Out
          </button>
        )}
        {reservationDetails.status === 'closed' && (
          <p className="checked-out-message">Already checked Out</p>
        )}
        {['reserved', 'ongoing'].includes(reservationDetails.status) && (
          reservationDetails.isExtraTimeAdded 
            ? <p className="added-time-message">Already Added 30 Minutes</p>
            : <button className='add-time' onClick={() => handleAddTime(reservationDetails.reservationid)}>
                Add 30 Minutes
              </button> 
        )}
      </div>
       {/* Caixa de texto para notas */}
       <div className='notes-container'>
      <label htmlFor="notes">Notes:</label>
      <textarea id="notes" value={notes} onChange={handleNotesChange} />
      <br /> {/* Adiciona uma quebra de linha */}
      <button id="save-notes-button" onClick={handleSaveNotes}>Save Notes</button>
    </div>

    </div>
    </div>
  );
};

export default ReservationDetailPage;
