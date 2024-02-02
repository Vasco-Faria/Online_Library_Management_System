import React from 'react';
import GoBackButton from '../components/GobackButton.js';

function Layout({ children }) {
  return (
    <div>
      <GoBackButton />
      {children}
    </div>
  );
}

export default Layout;
