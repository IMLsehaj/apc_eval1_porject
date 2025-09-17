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
      { title: 'Track Shipment', path: '/track', description: 'Track individual shipments' },
      { title: 'View All Shipments', path: '/all-shipments', description: user?.role === 'CUSTOMER' ? 'View your shipments' : 'View all shipments in system' }
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
    <div className="dashboard fade-in">
      <div className="dashboard-header">
        <h1>Welcome back, {user?.username}! 👋</h1>
        <p>Manage your logistics operations from your personalized dashboard</p>
        <div className="d-flex align-center justify-center gap-3 mt-4">
          <span className="badge badge-primary">
            Role: {user?.role}
          </span>
          <button 
            className="btn btn-danger"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </div>

      <div className="dashboard-menu">
        {menuItems.map((item, index) => (
          <div 
            key={index}
            className="menu-item hover-lift"
            onClick={() => handleNavigation(item.path)}
          >
            <h3>{item.title}</h3>
            <p>{item.description}</p>
          </div>
        ))}
      </div>

      <div className="card glass-card mt-5">
        <div className="card-header">
          <h4 className="card-title">🔐 Role Permissions</h4>
          <p className="card-subtitle">Your current access level and capabilities</p>
        </div>
        {user?.role === 'ADMIN' && (
          <div className="alert alert-info">
            <span>👑</span>
            <div>
              <strong>Administrator Access</strong>
              <ul className="mt-2 mb-0" style={{ paddingLeft: '20px' }}>
                <li>Create and track shipments</li>
                <li>Update shipment status</li>
                <li>Change fees status (mark as paid/pending)</li>
                <li>Manage users and assign roles</li>
                <li>Full system access</li>
              </ul>
            </div>
          </div>
        )}
        {user?.role === 'MANAGER' && (
          <div className="alert alert-success">
            <span>👔</span>
            <div>
              <strong>Manager Access</strong>
              <ul className="mt-2 mb-0" style={{ paddingLeft: '20px' }}>
                <li>Create and track shipments</li>
                <li>Update shipment status (only for paid shipments)</li>
                <li>Cannot change fees status</li>
                <li>Cannot manage users</li>
              </ul>
            </div>
          </div>
        )}
        {user?.role === 'CUSTOMER' && (
          <div className="alert alert-info">
            <span>👤</span>
            <div>
              <strong>Customer Access</strong>
              <ul className="mt-2 mb-0" style={{ paddingLeft: '20px' }}>
                <li>Create shipments</li>
                <li>Track own shipments (only if fees are paid)</li>
                <li>Cannot update shipment status</li>
                <li>Cannot change fees status</li>
                <li>Cannot manage users</li>
              </ul>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;