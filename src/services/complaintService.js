import api from './api';

export const registerUser = async (userData) => {
    try {
        const response = await api.post('/v1/user', userData);
        return response.data;
    } catch (error) {
        console.error("Erro ao registrar usuário:", error.response || error.message);
        throw error;
    }
};

export const registerComplaint = async (complaintData) => {
    try {
        const response = await api.post('/v1/complaint', complaintData);
        return response.data;
    } catch (error) {
        console.error("Erro ao registrar denúncia:", error.response || error.message);
        throw error;
    }
};

export const getComplaintByProtocol = async (protocolId) => {
    try {
        const response = await api.get(`/v1/complaint/${protocolId}`);
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar denúncia:", error.response || error.message);
        throw error;
    }
};

const getComplaints = async (filters) => {
    const params = new URLSearchParams();

    if (filters && filters.type) params.append('tipo', filters.type);
    if (filters && filters.date) params.append('data', filters.date);
    if (filters && filters.location) params.append('local', filters.location);
    if (filters && filters.status) params.append('status', filters.status);
    if (filters && filters.start) params.append('start', filters.start);
    if (filters && filters.end) params.append('end', filters.end);

    try {
        const response = await api.get('/v1/complaint', { params });
        return response.data;
    } catch (error) {
        console.error('Erro ao buscar denúncias:', error);
        return [];
    }
};

const updateComplaintStatus = async (complaintId, analysisData) => {
    try {
        const response = await api.put(`/v1/analysis/${complaintId}`, analysisData);
        if (response.status >= 200 && response.status < 300) {
            return { success: true, message: response.data?.message };
        }
        return { success: false, message: response.data?.message };
    } catch (error) {
        console.error('Error updating status:', error);
        return { success: false, error: error.message };
    }
};

export const complaintService = {
    getComplaints,
    updateComplaintStatus,
    getComplaintByProtocol,
    registerComplaint,
    registerUser,
};