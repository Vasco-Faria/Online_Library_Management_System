import React, { useState } from 'react';
import '../css/register.css'; 
import { useAuth } from '../context/AuthContext';
import FeedbackModal from './feedbackModal';

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { setAuthToken, setUserInfo } = useAuth();
  const [feedbackMessage, setFeedbackMessage] = useState('');
  const [showModal, setShowModal] = useState(false);

  const handleLogin = async (event) => {
    event.preventDefault();

    const loginPayload = {
      "email": email,
      "senha": password
    };

    try {
      const response = await fetch("http://localhost:8080/login", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(loginPayload),
      });

      if (!response.ok) {
        throw new Error('Login failed. Please check your credentials.');
      }

      const data = await response.json();
      const token = data.jwt;

      localStorage.setItem('token', token);
      setAuthToken(token);

      setFeedbackMessage('Login successful! Redirecting...');
      setShowModal(true);
      setTimeout(() => {
        window.location.href = '/userhome';
      }, 2000);
    } catch (error) {
      setFeedbackMessage(`Login error: ${error.message}`);
      setShowModal(true);
    }
  };

  const closeModal = () => {
    setShowModal(false);
  };


  return (
    <div className="register-page">
      <div className="register-container">
        <h1>Login</h1>
        <form onSubmit={handleLogin}>
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
          <button type="submit" className="register-button2">Login</button>
        </form>
      </div>
      {showModal && (
        <FeedbackModal message={feedbackMessage} onClose={closeModal} />
      )}
    </div>
  );
};

export default Login;