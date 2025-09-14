import React, { useState } from 'react';
import { shipmentService } from '../services/shipmentService';
import { useAuth } from '../context/AuthContext';

const UpdateFeesStatus = () => {
  const [trackingNumber, setTrackingNumber] = useState('');
  const [feesStatus, setFeesStatus] = useState('');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  
  const { user } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');

    try {
      const paid = feesStatus === 'paid';
      await shipmentService.changeFeesStatus(trackingNumber, paid, user.username);
      setMessage(`Fees status updated to ${paid ? 'Paid' : 'Pending'} successfully!`);
      setTrackingNumber('');
      setFeesStatus('');
    } catch (error) {
      if (error.response?.status === 403) {
        setError(error.response.data);
      } else if (error.response?.status === 404) {
        setError('Shipment not found with this tracking number');
      } else {
        setError('Failed to update fees status');
      }
    }
    
    setLoading(false);
  };

  // Only allow ADMIN to access this page
  if (user?.role !== 'ADMIN') {
    return (
      <div className="container">
        <div className="alert alert-error">
          Access denied. Only administrators can update fees status.
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="card">
        <h2>Update Fees Status</h2>
        <p>Change the payment status of shipment fees (Admin only)</p>
        
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
            <label>Fees Status:</label>
            <select
              value={feesStatus}
              onChange={(e) => setFeesStatus(e.target.value)}
              required
            >
              <option value="">Select fees status</option>
              <option value="paid">Mark as Paid</option>
              <option value="pending">Mark as Pending</option>
            </select>
          </div>

          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? 'Updating Fees Status...' : 'Update Fees Status'}
          </button>
        </form>

        <div className="card mt-3" style={{ backgroundColor: '#f8f9fa' }}>
          <h4>Important Notes:</h4>
          <ul>
            <li>Only administrators can change fees status</li>
            <li>Customers can only track shipments with paid fees</li>
            <li>Managers cannot update shipment status until fees are paid</li>
            <li>Default fees amount is ₹1000 for all shipments</li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default UpdateFeesStatus;