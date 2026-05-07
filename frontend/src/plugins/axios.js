import axios from 'axios';
import { toast } from '@/services/toastService';
import router from '@/router';

const api = axios.create({
  baseURL: 'http://127.0.0.1:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  }
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;
    const message = error.response?.data?.message || error.message;

    if (status === 401) {
      toast.error('Tu sesión ha expirado. Por favor, vuelve a iniciar sesión.');
      router.push({ name: 'login' });
    } else if (status === 403) {
      toast.error('No tienes permisos suficientes para esta acción.');
    } else if (status >= 500) {
      toast.error('Error crítico en el servidor. El equipo técnico ha sido notificado.');
    }

    console.error(`[API Error ${status || 'NET'}]: ${message}`);
    return Promise.reject(error);
  }
);

export default api;
