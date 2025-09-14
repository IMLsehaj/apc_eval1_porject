import React from 'react';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  const getMenuItemsByRole = () => {
    const baseItems = [
      { title: 'Profile', path: '/profile', description: 'View and update your profile' },
      { title: 'Track Shipment', path: '/track', description: 'Track your shipments' }
    ];

    switch (user?.role) {
      case 'ADMIN':
        return [
          { title: 'Create Shipment', path: '/create-shipment', description: 'Create a new shipment' },
          ...baseItems,
          { title: 'Update Shipment Status', path: '/update-status', description: 'Update shipment tracking status' },
          { title: 'Change Fees Status', path: '/update-fees', description: 'Update payment status of shipments' },
          { title: 'User Management', path: '/users', description: 'Manage users and roles' }
        ];
      
      case 'MANAGER':
        return [
          { title: 'Create Shipment', path: '/create-shipment', description: 'Create a new shipment' },
          ...baseItems,
          { title: 'Update Shipment Status', path: '/update-status', description: 'Update shipment tracking status' }
        ];
      
      case 'CUSTOMER':
      default:
        return [
          { title: 'Create Shipment', path: '/create-shipment', description: 'Create a new shipment' },
          ...baseItems
        ];
    }
  };

  const menuItems = getMenuItemsByRole();

  return (
    <div className="container">
      <div className="card">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <div>
            <h2>Welcome, {user?.username}!</h2>
            <p style={{ color: '#666', margin: 0 }}>
              Role: <strong>{user?.role}</strong>
            </p>
          </div>
          <button 
            className="btn btn-secondary"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </div>

      <div className="grid">
        {menuItems.map((item, index) => (
          <div 
            key={index}
            className="card"
            style={{ 
              cursor: 'pointer',
              transition: 'transform 0.2s, box-shadow 0.2s',
              ':hover': {
                transform: 'translateY(-2px)',
                boxShadow: '0 4px 20px rgba(0, 0, 0, 0.15)'
              }
            }}
            onClick={() => handleNavigation(item.path)}
            onMouseEnter={(e) => {
              e.currentTarget.style.transform = 'translateY(-2px)';
              e.currentTarget.style.boxShadow = '0 4px 20px rgba(0, 0, 0, 0.15)';
            }}
            onMouseLeave={(e) => {
              e.currentTarget.style.transform = 'translateY(0)';
              e.currentTarget.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
            }}
          >
            <h3 style={{ margin: '0 0 10px 0', color: '#007bff' }}>
              {item.title}
            </h3>
            <p style={{ margin: 0, color: '#666' }}>
              {item.description}
            </p>
          </div>
        ))}
      </div>

      <div className="card mt-3" style={{ backgroundColor: '#f8f9fa' }}>
        <h4>Role Permissions:</h4>
        {user?.role === 'ADMIN' && (
          <ul>
            <li>Create and track shipments</li>
            <li>Update shipment status</li>
            <li>Change fees status (mark as paid/pending)</li>
            <li>Manage users and assign roles</li>
            <li>Full system access</li>
          </ul>
        )}
        {user?.role === 'MANAGER' && (
          <ul>
            <li>Create and track shipments</li>
            <li>Update shipment status (only for paid shipments)</li>
            <li>Cannot change fees status</li>
            <li>Cannot manage users</li>
          </ul>
        )}
        {user?.role === 'CUSTOMER' && (
          <ul>
            <li>Create shipments</li>
            <li>Track own shipments (only if fees are paid)</li>
            <li>Cannot update shipment status</li>
            <li>Cannot change fees status</li>
            <li>Cannot manage users</li>
          </ul>
        )}
      </div>
    </div>
  );
};

export default Dashboard;