import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

const Profile = () => {
  const { user, updateUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    username: user?.username || '',
    email: user?.email || '',
    phoneNumber: user?.phoneNumber || '',
    houseAddress: user?.houseAddress || ''
  });
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setMessage('');

    if (!password) {
      setError('Please enter your current password to update details');
      setLoading(false);
      return;
    }

    const result = await updateUser(user.username, formData, password);
    
    if (result.success) {
      setMessage('Profile updated successfully!');
      setIsEditing(false);
      setPassword('');
    } else {
      setError(result.message);
    }
    
    setLoading(false);
  };

  const cancelEdit = () => {
    setFormData({
      username: user?.username || '',
      email: user?.email || '',
      phoneNumber: user?.phoneNumber || '',
      houseAddress: user?.houseAddress || ''
    });
    setIsEditing(false);
    setPassword('');
    setError('');
    setMessage('');
  };

  if (!user) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="container">
      <div className="card">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h2>User Profile</h2>
          {!isEditing && (
            <button 
              className="btn btn-primary"
              onClick={() => setIsEditing(true)}
            >
              Edit Profile
            </button>
          )}
        </div>

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

        {!isEditing ? (
          <div>
            <div className="form-group">
              <label><strong>Username:</strong></label>
              <p>{user.username}</p>
            </div>
            <div className="form-group">
              <label><strong>Email:</strong></label>
              <p>{user.email}</p>
            </div>
            <div className="form-group">
              <label><strong>Phone Number:</strong></label>
              <p>{user.phoneNumber}</p>
            </div>
            <div className="form-group">
              <label><strong>House Address:</strong></label>
              <p>{user.houseAddress || 'Not provided'}</p>
            </div>
            <div className="form-group">
              <label><strong>Role:</strong></label>
              <p>{user.role}</p>
            </div>
          </div>
        ) : (
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Username:</label>
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label>Phone Number:</label>
              <input
                type="text"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                pattern="[0-9]{10}"
                title="Please enter exactly 10 digits"
                required
              />
            </div>

            <div className="form-group">
              <label>House Address:</label>
              <input
                type="text"
                name="houseAddress"
                value={formData.houseAddress}
                onChange={handleChange}
              />
            </div>

            <div className="form-group">
              <label>Current Password (required for update):</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>

            <div className="d-flex gap-2">
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={loading}
              >
                {loading ? 'Updating...' : 'Update Profile'}
              </button>
              <button 
                type="button" 
                className="btn btn-secondary"
                onClick={cancelEdit}
              >
                Cancel
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default Profile;