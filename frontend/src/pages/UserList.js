import React, { useState, useEffect } from 'react';
import { userService } from '../services/userService';
import { roleService } from '../services/roleService';
import { useAuth } from '../context/AuthContext';

const UserList = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const { user: currentUser } = useAuth();

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await userService.getAllUsers();
      setUsers(response.data);
    } catch (error) {
      setError('Failed to fetch users');
    } finally {
      setLoading(false);
    }
  };

  const handleRoleChange = async (username, newRole) => {
    try {
      await roleService.assignRole(username, newRole);
      setMessage(`Role updated successfully for ${username}`);
      fetchUsers(); // Refresh the list
      setTimeout(() => setMessage(''), 3000);
    } catch (error) {
      setError(`Failed to update role for ${username}`);
      setTimeout(() => setError(''), 3000);
    }
  };

  const handleDeleteUser = async (username) => {
    if (window.confirm(`Are you sure you want to delete user ${username}?`)) {
      try {
        await userService.deleteUser(username);
        setMessage(`User ${username} deleted successfully`);
        fetchUsers(); // Refresh the list
        setTimeout(() => setMessage(''), 3000);
      } catch (error) {
        setError(`Failed to delete user ${username}`);
        setTimeout(() => setError(''), 3000);
      }
    }
  };

  if (loading) {
    return <div className="loading">Loading users...</div>;
  }

  // Only allow ADMIN to access this page
  if (currentUser?.role !== 'ADMIN') {
    return (
      <div className="container">
        <div className="alert alert-error">
          Access denied. Only administrators can view user list.
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="card">
        <h2>User Management</h2>
        
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

        {users.length === 0 ? (
          <p>No users found.</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>Username</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Address</th>
                <th>Current Role</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.phoneNumber}</td>
                  <td>{user.houseAddress || 'N/A'}</td>
                  <td>
                    <span className={`badge ${user.role.toLowerCase()}`}>
                      {user.role}
                    </span>
                  </td>
                  <td>
                    <div className="d-flex gap-2">
                      <select
                        value={user.role}
                        onChange={(e) => handleRoleChange(user.username, e.target.value)}
                        className="form-control"
                        style={{ width: 'auto', display: 'inline-block' }}
                      >
                        <option value="CUSTOMER">Customer</option>
                        <option value="MANAGER">Manager</option>
                        <option value="ADMIN">Admin</option>
                      </select>
                      
                      {user.username !== currentUser.username && (
                        <button
                          className="btn btn-danger"
                          onClick={() => handleDeleteUser(user.username)}
                          style={{ fontSize: '12px', padding: '4px 8px' }}
                        >
                          Delete
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default UserList;