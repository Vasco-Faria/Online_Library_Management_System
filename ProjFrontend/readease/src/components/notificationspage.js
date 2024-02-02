// NotificationsPage.js

import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Alert, Button } from 'react-bootstrap'; 
import '../css/NotificationsPage.css';
import { useNavigate } from 'react-router-dom'; 



const NotificationsPage = () => {
  const [notifications, setNotifications] = useState([]); 
  const navigate = useNavigate(); // Use useNavigate em vez de useHistory

  const handleViewReservation = (reservationId) => {
    // Redireciona para a página ReservationDetailPage com o reservationId
    navigate(`/ReservationDetailPage/${reservationId}`);
    
  };
  useEffect(() => {
    // Chamar a API para obter todas as notificações
    axios.get('http://localhost:8080/alerts/today')
      .then(response => {
        setNotifications(response.data);
      })
      .catch(error => {
        console.error('Erro ao obter notificações:', error);
      });
  }, []); // A lista vazia de dependências garante que o efeito é executado apenas uma vez após a montagem do componente

  const formatDateTime = (dateTimeString) => {
    const date = new Date(dateTimeString);
    return date.toLocaleString(); 
  };

  return (
    <div className="notifications-container">
      <h1>Notifications</h1>
      {notifications.map(notification => (
        <Alert key={notification.reservationId} variant="info">
          <Alert.Heading>{notification.alertType}</Alert.Heading>
          <p>Reservation ID: {notification.reservationId}</p>
          <p>Alert Time: {formatDateTime(notification.alertTime)}</p>
          <Button
              variant="primary"
              onClick={() => handleViewReservation(notification.reservationId)}
            >
              View Reservation
            </Button>
        </Alert>
      ))}
    </div>
  );
};

export default NotificationsPage;
