import { api } from './api';

export const authService = {
    login: async (user, password) => {
        try {

            const response = await api.post('/v1/login/', {
                user,
                password,
            });


            if (response.status === 203 && response.data.token) {
                localStorage.setItem('adminToken', response.data.token);
                api.defaults.headers.Authorization = `Bearer ${response.data.token}`;
                return { success: true, token: response.data.token };
            }
            return { success: false, error: 'Login failed' };
        } catch (error) {
            console.error('Login error:', error);
            return { success: false, error: error.message };
        }
    },

    logout: () => {
        localStorage.removeItem('adminToken');
        api.defaults.headers.Authorization = null;
    },
};