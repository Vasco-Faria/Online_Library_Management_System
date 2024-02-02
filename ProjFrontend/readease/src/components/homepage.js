import React, { useState, useEffect } from 'react';
import { Card, Button, Alert } from 'react-bootstrap';
import '../css/homepage.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; 

const Homepage = () => {
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate(); // Use useNavigate em vez de useHistory

  const handleViewReservation = (reservationId) => {
    // Redireciona para a página RoomReservationsLibrarian.js com o reservationId
    navigate(`/RoomReservationsLibrarian`);
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
  }, []);

  // Filtrar notificações com alertType igual a "Expiring Soon"
  const expiringSoonNotifications = notifications.filter(
    notification => notification.alertType === 'Expiring Soon'
  );

  return (
    <div className="homepage">
    <h1>Hi bibliotecario!!</h1>
    <p>Good Morning!</p>

    {/* Alerts */}
    <div className="alert-container">
      <h1>Notifications</h1>
      {expiringSoonNotifications.map(notification => (
        <Alert
          key={notification.reservationId}
          variant="info"
          className="alert"
        >
          <Alert.Heading>{notification.alertType}</Alert.Heading>
          <p>Reservation ID: {notification.reservationId}</p>
          <p>Alert Time: {notification.alertTime}</p>
          <Button
              variant="primary"
              onClick={() => handleViewReservation(notification.reservationId)}
            >
              View Reservation
            </Button>
        </Alert>
      ))}
    </div>

    {/* Card */}
    <div className="cardss-container">
      <Card>
        <Card.Body >
          <Card.Title>Lists of reservations waiting for approval</Card.Title>
          <Card.Text>
            View and approve reservations waiting for your approval.
          </Card.Text>
          <Button variant="primary">View Reservations</Button>
        </Card.Body>
      </Card>
    </div>
  </div>
);
};

export default Homepage;
