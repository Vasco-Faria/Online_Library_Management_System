// Register.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../css/register.css';
import { useAuth } from '../context/AuthContext'; 
import FeedbackModal from './feedbackModal';

const Register = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  
  const navigate = useNavigate();
  const { setAuthToken, setUserInfo } = useAuth(); 

  const [feedbackMessage, setFeedbackMessage] = useState('');
  const [showModal, setShowModal] = useState(false);

  const handleRegister = async () => {
    try {
      if (password !== confirmPassword) {
        throw new Error('Passwords do not match');
      }

      const response = await fetch('http://localhost:8080/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nome: username,
          email: email,
          senha: password,
        }),
      });

      if (!response.ok) {
        throw new Error('Failed to register user');
      }

      setFeedbackMessage('Registration successful! Redirecting to login...');
      setShowModal(true);
      setTimeout(() => {
        navigate('/login');
      }, 2000);

    } catch (error) {
      setFeedbackMessage(`Registration error: ${error.message}`);
      setShowModal(true);
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };



  return (
    <div className="register-page">
      <div className="register-container">
        <h1>Register</h1>
        <div className="input-group">
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div className="input-group">
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        <div className="input-group">
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <div className="input-group">
          <label htmlFor="confirmPassword">Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </div>
        <button className="register-button2" onClick={handleRegister}>
          Register
        </button>
      </div>
      {showModal && (
        <FeedbackModal message={feedbackMessage} onClose={closeModal} />
      )}
    </div>
  );
};

export default Register;
