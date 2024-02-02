// PrivateRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const PrivateRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();

  return isAuthenticated ? children : <Navigate to="/login" replace />;
};

export default PrivateRoute;


// // PrivateRoute.js
// import React from 'react';
// import { Navigate } from 'react-router-dom';
// import { useAuth } from '../context/AuthContext';

// const PrivateRoute = ({ children, allowedRoles }) => {
//   const { isAuthenticated, userInfo } = useAuth();

//   console.log(userInfo);

//   const userRole = userInfo?.tipo;
//   console.log(userRole);
//   console.log(isAuthenticated);

//   // Check if the route is accessible based on the user's role
//   const isRoleAllowed = (role) => {
//     if (!allowedRoles || allowedRoles.length === 0) {
//       return true; // If no specific roles are required, allow access
//     }
//     return allowedRoles.includes(role);
//   };

//   if (!isAuthenticated) {
//     // Non-authenticated users
//     return <Navigate to="/login" replace />;
//   } else if (isAuthenticated && userRole === 'bibliotecario') {
//     // Authenticated librarian users (full access)
//     return children;
//   } else if (isAuthenticated && isRoleAllowed(userRole)) {
//     // Authenticated normal users with allowed role
//     return children;
//   } else {
//     // Authenticated users without the required role
//     return <Navigate to="/userHome" replace />;
//   }
// };

// export default PrivateRoute;
