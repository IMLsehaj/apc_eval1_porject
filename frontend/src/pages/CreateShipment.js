import React, { useState } from 'react';
import { shipmentService } from '../services/shipmentService';
import { useAuth } from '../context/AuthContext';

const CreateShipment = () => {
  const [formData, setFormData] = useState({
    origin: '',
    destination: ''
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [createdShipment, setCreatedShipment] = useState(null);
  
  const { user } = useAuth();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  // const handleSubmit = async (e) => {
  //   e.preventDefault();
  //   setLoading(true);
  //   setError('');
  //   setMessage('');

  //   try {
  //     const response = await shipmentService.createShipment(formData.origin, formData.destination);
  //     setCreatedShipment(response.data);
  //     setMessage('Shipment created successfully!');
  //     setFormData({ origin: '', destination: '' });
  //   } catch (error) {
  //     setError(error.response?.data?.message || 'Failed to create shipment');
  //   }
    
  //   setLoading(false);
  // };
  const handleSubmit = async (e) => {
  e.preventDefault();
  setLoading(true);
  setError('');
  setMessage('');

  // Add this check for safety
  if (!user) {
    setError('You must be logged in to create a shipment.');
    setLoading(false);
    return;
  }

  try {
    // Pass user.username as the third argument
    const response = await shipmentService.createShipment(
      formData.origin,
      formData.destination,
      user.username // Assumes the username is on the user object
    );

    setCreatedShipment(response.data);
    setMessage('Shipment created successfully!');
    setFormData({ origin: '', destination: '' });
  } catch (error) {
    setError(error.response?.data?.message || 'Failed to create shipment');
  }
  
  setLoading(false);
};

  return (
    <div className="container">
      <div className="card">
        <h2>Create New Shipment</h2>
        
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
            <label>Origin:</label>
            <input
              type="text"
              name="origin"
              value={formData.origin}
              onChange={handleChange}
              placeholder="Enter pickup location"
              required
            />
          </div>

          <div className="form-group">
            <label>Destination:</label>
            <input
              type="text"
              name="destination"
              value={formData.destination}
              onChange={handleChange}
              placeholder="Enter delivery location"
              required
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={loading}
          >
            {loading ? 'Creating Shipment...' : 'Create Shipment'}
          </button>
        </form>

        {createdShipment && (
          <div className="card mt-3" style={{ backgroundColor: '#f8f9fa' }}>
            <h3>Shipment Created Successfully!</h3>
            <div className="form-group">
              <label><strong>Tracking Number:</strong></label>
              <p style={{ fontSize: '18px', fontWeight: 'bold', color: '#007bff' }}>
                {createdShipment.trackingNumber}
              </p>
            </div>
            <div className="form-group">
              <label><strong>Origin:</strong></label>
              <p>{createdShipment.origin}</p>
            </div>
            <div className="form-group">
              <label><strong>Destination:</strong></label>
              <p>{createdShipment.destination}</p>
            </div>
            <div className="form-group">
              <label><strong>Status:</strong></label>
              <p>{createdShipment.status}</p>
            </div>
            <div className="form-group">
              <label><strong>Fees Amount:</strong></label>
              <p>₹{createdShipment.feesAmount}</p>
            </div>
            <div className="form-group">
              <label><strong>Fees Status:</strong></label>
              <p>{createdShipment.feesPaid ? 'Paid' : 'Pending'}</p>
            </div>
            <div className="form-group">
              <label><strong>Created Date:</strong></label>
              <p>{new Date(createdShipment.createdDate).toLocaleString()}</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default CreateShipment;