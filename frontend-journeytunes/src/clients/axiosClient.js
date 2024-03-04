import axios from "axios";
import router from "@/router";

const axiosClient = axios.create({
  baseURL: 'http://10.50.15.51:8222/',
});

axiosClient.interceptors.request.use((config) => {
  const authToken = localStorage.getItem('authToken');
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

axiosClient.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response && (error.response.status === 401)) {
    localStorage.clear();
    router.push('/login');
  }
  return Promise.reject(error);
});

export default axiosClient;
