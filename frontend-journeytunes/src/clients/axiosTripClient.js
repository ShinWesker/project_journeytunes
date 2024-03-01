import axios from "axios";
import router from "@/router";

const axiosTripClient = axios.create({
  baseURL : 'http://localhost:8222/trips/api/v1',
});

axiosTripClient.interceptors.request.use((config) => {
  const authToken = localStorage.getItem('authToken');
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

axiosTripClient.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response && (error.response.status === 403)) {
    localStorage.clear();
    router.push('/login');
  }
  return Promise.reject(error);
});

export default axiosTripClient;
