import { defineStore } from 'pinia';
import api from '@/plugins/axios';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    // Persiste la sesión ante recargas de página
    user: JSON.parse(localStorage.getItem('fsm_user')) || null,
    loading: false,
    error: null,
  }),

  getters: {
    isAuthenticated: (state) => !!state.user,
  },

  actions: {
    async login(email, password) {
      this.loading = true;
      this.error = null;
      try {
        const response = await api.post('/auth/login', { email, password });
        this.user = {
          idUser: response.data.idUser,
          email: response.data.email,
          role: response.data.role,
          name: response.data.name || ''
        };
        localStorage.setItem('fsm_user', JSON.stringify(this.user));
        return true;
      } catch (err) {
        // El GlobalExceptionHandler devuelve 400 con message en el body
        this.error = err.response?.data?.message || 'Error al conectar con el servidor';
        return false;
      } finally {
        this.loading = false;
      }
    },

    logout() {
      this.user = null;
      localStorage.removeItem('fsm_user');
      localStorage.removeItem('fsm_user_name');
      localStorage.removeItem('fsm_user_avatar');
    }
  }
});