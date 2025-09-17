import React, { useEffect, useState } from 'react';
import { shipmentService } from '../services/shipmentService';
import { useAuth } from '../context/AuthContext';

const AllShipments = () => {
  const [shipments, setShipments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { user } = useAuth();

  useEffect(() => {
    if (user?.username) {
      shipmentService.getAllShipments(user.username)
        .then(res => {
          setShipments(res.data);
          setLoading(false);
        })
        .catch(err => {
          setError('Failed to fetch shipments');
          setLoading(false);
        });
    }
  }, [user]);

  if (loading) return (
    <div className="loading text-center p-5">
      <div className="loading-spinner mb-3"></div>
      <p>Loading shipments...</p>
    </div>
  );
  
  if (error) return (
    <div className="alert alert-error">
      <span>⚠️</span>
      {error}
    </div>
  );

  return (
    <div className="container fade-in">
      <div className="card">
        <div className="card-header">
          <h2 className="card-title text-gradient">
            {user?.role === 'CUSTOMER' ? 'My Shipments' : 'All Shipments'}
          </h2>
          <p className="card-subtitle">
            {user?.role === 'CUSTOMER' 
              ? 'Track and manage your shipments' 
              : 'Manage all shipments in the system'
            }
          </p>
        </div>
        
        {shipments.length === 0 ? (
          <div className="text-center p-5">
            <p className="mb-3">No shipments found.</p>
            <a href="/create-shipment" className="btn btn-primary">
              Create New Shipment
            </a>
          </div>
        ) : (
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Tracking Number</th>
                  <th>Origin</th>
                  <th>Destination</th>
                  <th>Status</th>
                  <th>Fee Paid</th>
                  <th>Created</th>
                  <th>Last Updated</th>
                </tr>
              </thead>
              <tbody>
                {shipments.map(s => (
                  <tr key={s.id} className="slide-in">
                    <td><span className="badge badge-primary">#{s.id}</span></td>
                    <td><strong>{s.trackingNumber}</strong></td>
                    <td>{s.origin}</td>
                    <td>{s.destination}</td>
                    <td>
                      <span className={`status status-${s.status?.toLowerCase()?.replace('_', '-') || 'pending'}`}>
                        {s.status || 'PENDING'}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${s.feePaid ? 'badge-success' : 'badge-warning'}`}>
                        {s.feePaid ? '✓ Yes' : '✗ No'}
                      </span>
                    </td>
                    <td>{s.createdDate}</td>
                    <td>{s.lastUpdatedDate}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default AllShipments;
