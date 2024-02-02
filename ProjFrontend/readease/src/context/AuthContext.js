import React, { createContext, useState, useContext, useCallback, useEffect } from 'react';
import axios from 'axios';
import {jwtDecode} from 'jwt-decode';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [authToken, setAuthToken] = useState(localStorage.getItem('token'));
  const [userInfo, setUserInfo] = useState(null);

  const isAuthenticated = !!authToken;

  const fetchUserInfo = useCallback(async (token) => {
    try {
      
      const decodedToken = jwtDecode(token);
      const userEmail = decodedToken.sub;
  
      const response = await axios.get(`http://localhost:8080/api/users/loggedinfo?email=${userEmail}`, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
  
      setUserInfo(response.data);
    } catch (error) {
      console.error('Error fetching user info:', error);
      localStorage.removeItem('token'); 
      setAuthToken(null); 
    }
  }, []);

  const updateAuthToken = (token) => {
    setAuthToken(token);
    if (token) {
      localStorage.setItem('token', token); 
      fetchUserInfo(token);
    } else {
      localStorage.removeItem('token'); 
      setUserInfo(null);
    }
  };


  const logout = () => {
    localStorage.removeItem('token');
    setAuthToken(null);
    setUserInfo(null);
  };


  useEffect(() => {
    if (authToken) {
      fetchUserInfo(authToken);
    }
  }, [authToken, fetchUserInfo]);

  const value = {
    authToken,
    setAuthToken: updateAuthToken,
    userInfo,
    isAuthenticated,
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  return useContext(AuthContext);
};