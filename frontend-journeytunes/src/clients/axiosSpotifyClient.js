import axios from "axios";
import router from "@/router";

const axiosSpotifyClient = axios.create({
  baseURL: 'http://localhost:8222/spotify/api/v1/',
});

axiosSpotifyClient.interceptors.request.use((config) => {
  const authToken = localStorage.getItem('authToken');
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

axiosSpotifyClient.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response && (error.response.status === 403)) {
    localStorage.clear();
    router.push('/login');
  }
  return Promise.reject(error);
});

export default axiosSpotifyClient;
