import React, { useEffect, useState } from 'react';
import '../css/profile.css'; 
import Loading from './Loading'; 
import { useAuth } from '../context/AuthContext';

const Profile = () => {
  const { userInfo } = useAuth();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    console.log("userInfo:", userInfo);
    if (userInfo) {
      setLoading(false);
    }
  }, [userInfo]);

  if (loading) {
    return <Loading />;
  }

  if (!userInfo) {
    return <div>Error: User information not available.</div>;
  }

  // Format the date of birth
  const formattedDateOfBirth = userInfo.data_de_nascimento 
    ? new Date(userInfo.data_de_nascimento).toLocaleDateString() 
    : 'N/A';

    return (
        <div className="profile-page">
          <h1>User Profile</h1>
          <div className="profile-details">
            <div className="profile-section">
              <label>Username :</label>
              <div className="profile-item">
                <p>{userInfo?.nome || 'N/A'}</p>
              </div>
            </div>
    
            <div className="profile-section">
              <label>Email :</label>
              <div className="profile-item">
                <p>{userInfo?.email || 'N/A'}</p>
              </div>
            </div>
    
            <div className="profile-section">
              <label>Phone :</label>
              <div className="profile-item">
                <p>{userInfo?.telemovel || 'N/A'}</p>
              </div>
            </div>

            <div className="profile-section">
              <label>Birth Date :</label>
              <div className="profile-item">
                <p>{formattedDateOfBirth}</p>
              </div>
            </div>
          </div>
        </div>
      );
    };
    
    export default Profile;
