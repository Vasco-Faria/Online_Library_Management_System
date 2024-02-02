// components/FeedbackModal.js
import React from 'react';
import '../css/feedbackModal.css';

const FeedbackModal = ({ message, onClose }) => {
  return (
    <div className="feedback-modal">
      <div className="feedback-modal-content">
        <p>{message}</p>
        <button onClick={onClose}>Close</button>
      </div>
    </div>
  );
};

export default FeedbackModal;
