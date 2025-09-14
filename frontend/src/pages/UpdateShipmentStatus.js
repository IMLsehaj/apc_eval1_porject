import React, { useState } from 'react';
import { shipmentService } from '../services/shipmentService';
import { useAuth } from '../context/AuthContext';

const UpdateShipmentStatus = () => {
  const [trackingNumber, setTrackingNumber] = useState('');
  const [newStatus, setNewStatus] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  
  const { user } = useAuth();

  const statusOptions = [
    'PENDING',
    'IN_TRANSIT',
    'OUT_FOR_DELIVERY',
    'DELIVERED',
    'CANCELLED',
    'EXCEPTION'
  ];

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');

    try {
      await shipmentService.updateShipmentStatus(trackingNumber, newStatus, user.username);
      setMessage(`Shipment status updated to ${newStatus.replace('_', ' ')} successfully!`);
      setTrackingNumber('');
      setNewStatus('');
    } catch (error) {
      if (error.response?.status === 403) {
        setError(error.response.data);
      } else if (error.response?.status === 404) {
        setError('Shipment not found with this tracking number');
      } else {
        setError('Failed to update shipment status');
      }
    }
    
    setLoading(false);
  };

  // Only allow ADMIN and MANAGER to access this page
  if (user?.role !== 'ADMIN' && user?.role !== 'MANAGER') {
    return (
      <div className="container">
        <div className="alert alert-error">
          Access denied. Only administrators and managers can update shipment status.
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="card">
        <h2>Update Shipment Status</h2>
        <p>Update the tracking status of shipments (Admin/Manager only)</p>
        
        {message && (
          <div className="alert alert-success">
            {message}
          </div>
        )}

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Tracking Number:</label>
            <input
              type="text"
              value={trackingNumber}
              onChange={(e) => setTrackingNumber(e.target.value)}
              placeholder="Enter tracking number"
              required
            />
          </div>

          <div className="form-group">
            <label>New Status:</label>
            <select
              value={newStatus}
              onChange={(e) => setNewStatus(e.target.value)}
              required
            >
              <option value="">Select new status</option>
              {statusOptions.map(status => (
                <option key={status} value={status}>
                  {status.replace('_', ' ')}
                </option>
              ))}
            </select>
          </div>

          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? 'Updating Status...' : 'Update Status'}
          </button>
        </form>

        <div className="card mt-3" style={{ backgroundColor: '#f8f9fa' }}>
          <h4>Status Definitions:</h4>
          <ul>
            <li><strong>PENDING:</strong> Shipment is awaiting processing</li>
            <li><strong>IN_TRANSIT:</strong> Shipment is on its way</li>
            <li><strong>OUT_FOR_DELIVERY:</strong> Shipment is out for final delivery</li>
            <li><strong>DELIVERED:</strong> Shipment has been delivered</li>
            <li><strong>CANCELLED:</strong> Shipment has been cancelled</li>
            <li><strong>EXCEPTION:</strong> There's an issue with the shipment</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default UpdateShipmentStatus;