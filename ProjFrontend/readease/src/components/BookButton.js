import React from 'react';
import '../css/BookButton.css'

const BookButton = ({ color,onClick, children }) => {
  const buttonStyle = {
    '--color': color,
  };


  const additionalClass = color === 'white' ? 'white-button' : '';
  return (
    <a className={`button-book ${additionalClass}`} onClick={onClick} style={buttonStyle}>
      <span></span>
      <span></span>
      <span></span>
      <span></span>
      {children}
    </a>
  );
};

export default BookButton;