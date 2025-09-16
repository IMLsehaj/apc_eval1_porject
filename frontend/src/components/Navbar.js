import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (!user) {
    return (
      <nav className="navbar">
        <div className="container">
          <h1>Logistics Tracking System</h1>
          <div className="user-info">
            <button 
              className="btn btn-primary"
              onClick={() => navigate('/login')}
            >
              Login
            </button>
          </div>
        </div>
      </nav>
    );
  }

  return (
    <nav className="navbar">
      <div className="container">
        <h1 
          style={{ cursor: 'pointer' }}
          onClick={() => navigate('/dashboard')}
        >
          Logistics Tracking System
        </h1>
        <button className="btn btn-link" onClick={() => navigate('/all-shipments')}>
          View All Shipments
        </button>
        <div className="user-info">
          <span>Welcome, {user.username} ({user.role})</span>
          <button 
            className="btn btn-secondary"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;