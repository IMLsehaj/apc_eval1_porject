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

  if (loading) return <div>Loading shipments...</div>;
  if (error) return <div style={{color: 'red'}}>{error}</div>;

  return (
    <div className="container">
      <h2>{user?.role === 'CUSTOMER' ? 'My Shipments' : 'All Shipments'}</h2>
      <table border="1" cellPadding="8" style={{width: '100%', borderCollapse: 'collapse'}}>
        <thead>
          <tr>
            <th>ID</th>
            <th>Tracking Number</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Status</th>            
            <th>Created</th>
            <th>Last Updated</th>
          </tr>
        </thead>
        <tbody>
          {shipments.map(s => (
            <tr key={s.id}>
              <td>{s.id}</td>
              <td>{s.trackingNumber}</td>
              <td>{s.origin}</td>
              <td>{s.destination}</td>
              <td>{s.status}</td>
              <td>{s.createdDate}</td>
              <td>{s.lastUpdatedDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AllShipments;
