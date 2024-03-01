import axios from "axios";
import router from "@/router";

const axiosRoutesClient = axios.create({
  baseURL : 'http://localhost:2020/routes/api/v1/',
});


axiosRoutesClient.interceptors.request.use((config) => {
  const authToken = localStorage.getItem('authToken');
  if (authToken) {
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

axiosRoutesClient.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response && (error.response.status === 403)) {
    localStorage.clear();
    router.push('/login');
  }
  return Promise.reject(error);
});

export default axiosRoutesClient;
