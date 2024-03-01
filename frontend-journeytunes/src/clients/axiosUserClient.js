import axios from "axios";

const axiosUserClient = axios.create({
  baseURL : 'http://localhost:8222/users/api/v1/',
});

export default axiosUserClient;

