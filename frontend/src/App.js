import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Profile from './pages/Profile';
import CreateShipment from './pages/CreateShipment';
import ShipmentTracker from './pages/ShipmentTracker';
import UpdateShipmentStatus from './pages/UpdateShipmentStatus';
import UpdateFeesStatus from './pages/UpdateFeesStatus';
import UserList from './pages/UserList';
import AllShipments from './pages/AllShipments';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <Navbar />
          <div className="main-content">
            <Routes>
              {/* Public routes */}
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              
              {/* Protected routes */}
              <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
              <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} />
              <Route path="/create-shipment" element={<ProtectedRoute><CreateShipment /></ProtectedRoute>} />
              <Route path="/track-shipment" element={<ProtectedRoute><ShipmentTracker /></ProtectedRoute>} />
              <Route path="/track" element={<ProtectedRoute><ShipmentTracker /></ProtectedRoute>} />
              <Route path="/update-shipment" element={<ProtectedRoute><UpdateShipmentStatus /></ProtectedRoute>} />
              <Route path="/update-status" element={<ProtectedRoute><UpdateShipmentStatus /></ProtectedRoute>} />
              <Route path="/update-fees" element={<ProtectedRoute><UpdateFeesStatus /></ProtectedRoute>} />
              <Route path="/users" element={<ProtectedRoute><UserList /></ProtectedRoute>} />
              <Route path="/all-shipments" element={<ProtectedRoute><AllShipments /></ProtectedRoute>} />
              {/* Default redirect */}
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
            </Routes>
          </div>
        </div>
      </Router>
    </AuthProvider>
  );
}

// Protected Route component
const ProtectedRoute = ({ children }) => {
  const isAuthenticated = localStorage.getItem('user');
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

export default App;