// homelog.js
import React from 'react';
import '../css/homelog.css';

const Homelog = () => {
  return (
    <div className="homelog">
      <h1 className='welcome'>Welcome</h1>
      <h1 className="readease-title">ReadEase</h1>
      <div className="login-register-buttons">
        <a href="/login">
          <button className="login-button">Login</button>
        </a>
        <a href="/register">
          <button className="register-button">Register</button>
        </a>
      </div>
    </div>
  );
};

export default Homelog;
