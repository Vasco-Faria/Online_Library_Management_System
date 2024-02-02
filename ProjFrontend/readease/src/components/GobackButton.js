import React from 'react';
import { useLocation } from 'react-router-dom';
import "../css/GoBackButton.css";
import { useMediaQuery } from 'react-responsive';


function GoBackButton() {

    const location = useLocation();
    const isHomepage = location.pathname === '/';
    const isWideScreen = useMediaQuery({ minWidth: 768 });


    if (isHomepage || !isWideScreen) {
        return null;
      }
    
      return (
        <button onClick={() => window.history.back()} className="gobackbutton">
          <i className="animation"></i> &larr; Go Back <i className="animation"></i>
        </button>
      );
    }

export default GoBackButton;

