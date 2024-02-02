import React, { useState, useEffect } from 'react';
import { Card, Button, Alert } from 'react-bootstrap';
import '../css/userHome.css';
import { useAuth } from "../context/AuthContext";
import axios from 'axios';
import { useNavigate } from 'react-router-dom'; 

const UserHome = () => {
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate();
  const { userInfo } = useAuth();

  const handleViewReservation = (reservationId) => {
   
    navigate(`/RoomReservationsUser`);
  };

  useEffect(() => {
   
    if (userInfo && userInfo.isLibrarian) {
     
      axios.get(`http://localhost:8080/alerts/user/${userInfo.userId}`)
        .then(response => {
          setNotifications(response.data);
        })
        .catch(error => {
          console.error('Erro ao obter notificações:', error);
        });
    }
  }, [userInfo]);

  const handleOnClick = () => {
    window.location.href="/reservations"
  }

 
  const expiringSoonNotifications = notifications.filter(
    notification => notification.alertType === 'Expiring Soon'
  );

  return (
    <div className="user-home-background"> 
      <h2 className="user-home-title">
        Welcome{userInfo ? `, ${userInfo.nome}` : ''}
      </h2>

      {userInfo && userInfo.isLibrarian && (
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
      )}

    {userInfo && userInfo.isLibrarian && (
      <div className="card-container"  style={{"backgroundColor":"#2c293d"}}>
        <Card  style={{"backgroundColor":"#2c293d"}}>
          <Card.Body  style={{"backgroundColor":"#2c293d"}}>
            <Card.Title>Your Reservations</Card.Title>
            <Card.Text>
              View and manage your reservations.
            </Card.Text>
            <Button variant="primary" onClick={handleOnClick}>View Reservations</Button>
          </Card.Body>
        </Card>
      </div>
    )}
     </div>
  );
};

export default UserHome;
