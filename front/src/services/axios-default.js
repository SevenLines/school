import axios from 'axios';

axios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    const extraConfig = {};
    if (token) {
      extraConfig['headers'] = {
        Authorization: `Bearer ${token}`,
      };
    }
    return {
      ...config,
      ...extraConfig,
    };
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default axios;
