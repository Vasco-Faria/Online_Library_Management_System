
import React from "react";
import '../css/ConfirmationModal.css';

function ConfirmationModal({ text,onConfirm, onCancel }) {
    return (
    <div className="modal-overlay">
        <div className="modal">
            <p>{text}</p>
            <button onClick={onConfirm}>Yes</button>
            <button onClick={onCancel}>No</button>
        </div>
    </div>
    );
  }
  export default ConfirmationModal;