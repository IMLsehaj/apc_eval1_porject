import axios from 'axios';

const API_BASE_URL = 'http://localhost:8000/api/shipments';

export const shipmentService = {
  createShipment: async (origin, destination, username) => {
  return await axios.post(
    `${API_BASE_URL}/createshipment`, // The base URL
    { // The request body (for @RequestBody)
      origin,
      destination
    },
    { // The config object (for @RequestParam)
      params: {
        username
      }
    }
  );
},

  trackShipment: async (trackingNumber, username) => {
    return await axios.get(`${API_BASE_URL}/track/${trackingNumber}?username=${username}`);
  },

  updateShipmentStatus: async (trackingNumber, status, username) => {
    return await axios.put(`${API_BASE_URL}/track/${trackingNumber}/status?status=${status}&username=${username}`);
  },

  changeFeesStatus: async (trackingNumber, paid, username) => {
    return await axios.put(`${API_BASE_URL}/track/${trackingNumber}/fees?paid=${paid}&username=${username}`);
  }
};