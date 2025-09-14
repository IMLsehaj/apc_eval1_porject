import axios from 'axios';

const API_BASE_URL = 'http://localhost:8000/api/roles';

export const roleService = {
  getAllUsersWithRoles: async () => {
    return await axios.get(API_BASE_URL);
  },

  assignRole: async (username, role) => {
    return await axios.post(`${API_BASE_URL}/assign`, {
      username,
      role
    });
  },

  removeRole: async (username) => {
    return await axios.delete(`${API_BASE_URL}/remove`, {
      data: { username }
    });
  }
};