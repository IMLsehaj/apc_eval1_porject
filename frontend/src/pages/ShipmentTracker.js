import React, { useState } from 'react';
import { shipmentService } from '../services/shipmentService';
import { useAuth } from '../context/AuthContext';

const ShipmentTracker = () => {
  const [trackingNumber, setTrackingNumber] = useState('');
  const [shipment, setShipment] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  
  // Debug: First thing - component is mounting
  console.log('🚀 ShipmentTracker component is mounting!');
  
  const { user } = useAuth();

  // Debug: Check if component is rendering
  console.log('📊 ShipmentTracker component rendering', { user });
  console.log('🔍 User object:', user);
  console.log('❓ User exists:', !!user);

  // If user is not available, show loading or error
  if (!user) {
    return (
      <div className="container" style={{ minHeight: '500px', backgroundColor: '#ffffff', padding: '20px', margin: '20px auto', maxWidth: '800px' }}>
        <div className="card" style={{ padding: '30px', backgroundColor: '#fff', borderRadius: '8px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)' }}>
          <h2>Loading...</h2>
          <p>Please wait while we load your information.</p>
        </div>
      </div>
    );
  }

  const handleTrack = async (e) => {
    e.preventDefault();
    if (!trackingNumber.trim()) {
      setError('Please enter a tracking number');
      return;
    }

    setLoading(true);
    setError('');
    setShipment(null);

    try {
      const response = await shipmentService.trackShipment(trackingNumber, user.username);
      setShipment(response.data);
    } catch (error) {
      if (error.response?.status === 403) {
        setError(error.response.data);
      } else if (error.response?.status === 404) {
        setError('Shipment not found with this tracking number');
      } else {
        setError('Failed to track shipment');
      }
    }
    
    setLoading(false);
  };

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': '#ffc107',
      'IN_TRANSIT': '#17a2b8',
      'OUT_FOR_DELIVERY': '#fd7e14',
      'DELIVERED': '#28a745',
      'CANCELLED': '#dc3545',
      'EXCEPTION': '#dc3545'
    };
    return colors[status] || '#6c757d';
  };

  return (
    <div className="container" style={{ minHeight: '500px', backgroundColor: '#ffffff', padding: '20px', margin: '20px auto', maxWidth: '800px' }}>
      <div className="card" style={{ padding: '30px', backgroundColor: '#fff', borderRadius: '8px', boxShadow: '0 2px 10px rgba(0,0,0,0.1)' }}>
        <h2 style={{ color: '#2c3e50', marginBottom: '20px' }}>Track Shipment</h2>
        
        <form onSubmit={handleTrack} style={{ marginBottom: '20px' }}>
          <div className="form-group" style={{ marginBottom: '15px' }}>
            <label style={{ display: 'block', marginBottom: '8px', fontWeight: 'bold', color: '#333' }}>Tracking Number:</label>
            <input
              type="text"
              value={trackingNumber}
              onChange={(e) => setTrackingNumber(e.target.value)}
              placeholder="Enter your tracking number"
              required
              style={{ 
                width: '100%', 
                padding: '10px', 
                border: '1px solid #ddd', 
                borderRadius: '4px', 
                fontSize: '16px' 
              }}
            />
          </div>

          <button 
            type="submit" 
            className="btn btn-primary"
            disabled={loading}
            style={{ 
              backgroundColor: '#007bff', 
              color: 'white', 
              padding: '10px 20px', 
              border: 'none', 
              borderRadius: '4px', 
              cursor: 'pointer',
              fontSize: '16px'
            }}
          >
            {loading ? 'Tracking...' : 'Track Shipment'}
          </button>
        </form>

        {error && (
          <div className="alert alert-error mt-3">
            {error}
          </div>
        )}

        {shipment && (
          <div className="card mt-3" style={{ backgroundColor: '#f8f9fa' }}>
            <h3>Shipment Details</h3>
            
            <div className="grid">
              <div>
                <div className="form-group">
                  <label><strong>Tracking Number:</strong></label>
                  <p style={{ fontSize: '16px', fontWeight: 'bold' }}>
                    {shipment.trackingNumber}
                  </p>
                </div>
                
                <div className="form-group">
                  <label><strong>Origin:</strong></label>
                  <p>{shipment.origin}</p>
                </div>
                
                <div className="form-group">
                  <label><strong>Destination:</strong></label>
                  <p>{shipment.destination}</p>
                </div>
              </div>
              
              <div>
                <div className="form-group">
                  <label><strong>Status:</strong></label>
                  <p style={{ 
                    color: getStatusColor(shipment.status),
                    fontWeight: 'bold',
                    fontSize: '16px'
                  }}>
                    {shipment.status.replace('_', ' ')}
                  </p>
                </div>
                
                <div className="form-group">
                  <label><strong>Fees Amount:</strong></label>
                  <p>₹{shipment.feesAmount}</p>
                </div>
                
                <div className="form-group">
                  <label><strong>Fees Status:</strong></label>
                  <p style={{ 
                    color: shipment.feesPaid ? '#28a745' : '#dc3545',
                    fontWeight: 'bold'
                  }}>
                    {shipment.feesPaid ? 'Paid' : 'Pending'}
                  </p>
                </div>
              </div>
            </div>
            
            <div className="grid">
              <div className="form-group">
                <label><strong>Created Date:</strong></label>
                <p>{new Date(shipment.createdDate).toLocaleString()}</p>
              </div>
              
              <div className="form-group">
                <label><strong>Last Updated:</strong></label>
                <p>{new Date(shipment.lastUpdatedDate).toLocaleString()}</p>
              </div>
            </div>

            {shipment.createdBy && (
              <div className="form-group">
                <label><strong>Created By:</strong></label>
                <p>{shipment.createdBy}</p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default ShipmentTracker;