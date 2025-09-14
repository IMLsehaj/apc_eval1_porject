import axios from 'axios';

const API_BASE_URL = 'http://localhost:8000/api/users';

export const userService = {
  login: async (username, password) => {
    return await axios.post(`${API_BASE_URL}/login`, {
      username,
      password
    });
  },

  register: async (userData) => {
    return await axios.post(`${API_BASE_URL}/register`, userData);
  },

  getUserByUsername: async (username) => {
    return await axios.get(`${API_BASE_URL}/${username}`);
  },

  getAllUsers: async () => {
    return await axios.get(API_BASE_URL);
  },

  updateUser: async (username, userData, password) => {
    return await axios.put(`${API_BASE_URL}/${username}?password=${password}`, userData);
  },

  deleteUser: async (username) => {
    return await axios.delete(`${API_BASE_URL}/${username}`);
  }
};