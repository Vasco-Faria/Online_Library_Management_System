// Navbar.js
import React, { useState, useEffect } from 'react';
import '../css/navbar.css';
import { useAuth } from "../context/AuthContext";
import { format } from 'date-fns';


const Navbar = () => {
  const [currentDateTime, setCurrentDateTime] = useState(new Date());
  const [isMobile, setIsMobile] = useState(window.innerWidth <= 768);
  const [showManagementOptions, setShowManagementOptions] = useState(false);
  const [showMobileMenu, setShowMobileMenu] = useState(false);
  const { userInfo,logout } = useAuth();

  const [notifications, setNotifications] = useState([]);
  const [showNotifications, setShowNotifications] = useState(false);

  const toggleNotifications = () => {
    setShowNotifications(!showNotifications);
  };


  const [hamburgerOpen, setHamburgerOpen] = useState(false);

  const fetchNotifications = () => {
    fetch(`http://localhost:8080/notifications/user/${userInfo.id}/`) 
      .then(response => response.json())
      .then(data => {
        setNotifications(data);
      })
      .catch(error => console.error('Erro ao buscar notificações:', error));
  };

  useEffect(() => {
   
    const fetchNotifications = () => {
        fetch(`http://localhost:8080/notifications/user/${userInfo.id}/`) 
            .then(response => response.json())
            .then(data => {
                setNotifications(data);
            })
            .catch(error => console.error('Erro ao buscar notificações:', error));
    };

    
    if (userInfo) {
        
        fetchNotifications();

       
        const intervalId = setInterval(fetchNotifications, 10000);

        
        return () => clearInterval(intervalId);
    }
}, [userInfo]);


  const toggleHamburger = () => {
    setHamburgerOpen(!hamburgerOpen);
  };

  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };

  const isLibrarian = userInfo?.tipo === "bibliotecario";
  const isUserLoggedIn = userInfo != null;




  useEffect(() => {
    const handleResize = () => {
      setIsMobile(window.innerWidth <= 768);
    };

    window.addEventListener('resize', handleResize);

    return () => window.removeEventListener('resize', handleResize);
  }, []);

  useEffect(() => {
    const intervalId = setInterval(() => {
      setCurrentDateTime(new Date());
    }, 1000);

    return () => clearInterval(intervalId);
  }, []);

  const handleMouseOver = () => {
    setShowManagementOptions(true);
  };

  const handleMouseLeave = () => {
    setShowManagementOptions(false);
  };


  const navUrl = isLibrarian ? "/userhome" : (isUserLoggedIn ? "/userhome" : "/");

  return (
    <nav className={`navbar ${isMobile ? 'mobile' : ''} ${hamburgerOpen ? 'open' : ''}`}>
      <div className="navbar-left">
        <a href={navUrl} className="company-name">
          <span>ReadEase</span>
        </a>
      </div>

      {isMobile && (
        <div className="hamburger" onClick={toggleHamburger}>
          <div></div>
          <div></div>
          <div></div>
        </div>
      )}
      
      {isLibrarian && (
        <div className="navbar-management">
          <div className="management-dropdown" onMouseOver={handleMouseOver} onMouseLeave={handleMouseLeave}>
            {showManagementOptions ? <i className='fas fa-folder-open fa-2xl' style={{'color':'white'}}></i> : <i className='fas fa-folder-open fa-2xl' style={{'color':'white'}}></i>}
              {showManagementOptions && (
                <div className="management-options">
                  <ul>
                    <li><a href="/booksearch">Books</a></li>
                    <li><a href="/reservationPage">Rooms</a></li>
                    <li><a href="/reservations">Reservations</a></li>

                  </ul>
                </div>
              )}
          </div>
          <div className="navbar-center-right">
            <div className="alert-icon" >
                <a href="/statistics"><i class="fa fa-bar-chart fa-2xl" aria-hidden="true"></i></a>
            </div>
          </div>
          <div className="navbar-center-right">
            <div className="alert-icon" >
                <a href="/notifications"><i className="fa-solid fa-bell fa-2xl"></i></a>
            </div>
          </div>
          <div className="management-dropdown" onMouseOver={handleMouseOver} onMouseLeave={handleMouseLeave}>
            {showManagementOptions ? <i className="fa-solid fa-user fa-2xl"></i> : <i className="fa-solid fa-user fa-2xl"></i>}
              {showManagementOptions && (
                <div className="management-options">
                  <ul>
                    <li><a href="/profile">My Profile</a></li>
                    <li onClick={handleLogout}><a>Logout</a></li>
                  </ul>
                </div>
              )}
          </div>
        </div>
      )}

      {isUserLoggedIn && !isLibrarian && (
        <div className="navbar-user">
           <div className="management-dropdown" onMouseOver={handleMouseOver} onMouseLeave={handleMouseLeave}>
            {showManagementOptions ? <i className='fas fa-folder-open fa-2xl' style={{'color':'white'}}></i> : <i className='fas fa-folder-open fa-2xl' style={{'color':'white'}}></i>}
              {showManagementOptions && (
                <div className="management-options">
                  <ul>
                    <li><a href="/booksearch">Books</a></li>
                    <li><a href="/reservationPage">Reserve a room</a></li>
                  </ul>
                </div>
              )}
          </div>
          <div className="navbar-center-right">
            <div className="navbar-ebook">
              <a href='/myebook' style={{'textDecoration':'none','color':'white'}}><i className='fab fa-readme fa-2xl' style={{'color':'white','paddingRight':'1rem'}}></i></a>
            </div>
          </div>
          <div className="navbar-center-right">
            <div className="navbar-favorites">
            <a href='/favorites'>
              <i className='fas fa-heart fa-2xl' style={{'color':'white'}}></i>
              </a>
            </div>
          </div>
          <div className="navbar-center-right">
            <div className="management-dropdown" onMouseOver={handleMouseOver} onMouseLeave={handleMouseLeave}>
              <i className="fa-solid fa-bell fa-2xl" onClick={() => setShowNotifications(!showNotifications)}></i>
              {showNotifications ? (
                <div className="notifications-dropdown">
                  {notifications.length > 0 ? (
                    <div className='notifications'>
                      {notifications.map((notification) => (
                        <a key={notification.id} href={`/book/${notification.bookId}`} > 
                          <div key={notification.id} className='notificacao'>
                            <p>{notification.message}</p>
                            <p>{format(new Date(notification.timestamp), 'dd/MM/yyyy HH:mm:ss')}</p>
                            </div>
                        </a>
                      ))}
                    </div>
                  ) : (
                    <p>No notifications available</p>
                  )}
                </div>
              ) : null}
            </div>
          </div>
          <div className="management-dropdown" onMouseOver={handleMouseOver} onMouseLeave={handleMouseLeave}>
            {showManagementOptions ? <i className="fa-solid fa-user fa-2xl"></i> : <i className="fa-solid fa-user fa-2xl"></i>}
              {showManagementOptions && (
                <div className="management-options">
                  <ul>
                    <li><a href="/myreserves">My Reserves</a></li>
                    <li><a href="/historic">Historic of reservations</a></li>
                    <li><a href="/profile">My Profile</a></li>
                    <li onClick={handleLogout}><a>Logout</a></li>
                  </ul>
                </div>
              )}
          </div>
        </div>
      )}

      {!isUserLoggedIn && (
         <div className="navbar-visitor">
          <div className='navbar-left'>
            <div className='navbar-books'>
              <a href='/booksearch'>
                <i className='fa-solid fa-book fa-2xl'>
              </i>
              Books
              </a>
            </div>
          </div>
          <div className="navbar-right">
            <div className="profile">
              <div className="profile-image">
                <a href='homelog'>
                  <i className="fa-solid fa-user fa-2xl"></i>
                  Login/Register
                </a>
              </div>
            </div>
          </div>
         </div>
      )}


      <div className="navbar-clock">
        <div className="date-info">
          <div>{currentDateTime.toLocaleDateString()}</div>
          <div>{currentDateTime.toLocaleTimeString()}</div>
        </div>
      </div>
      


    </nav>
  );
};

export default Navbar;
