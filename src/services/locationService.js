import axios from 'axios';

const IBGE_API_URL = 'https://servicodados.ibge.gov.br/api/v1/localidades';

export const getStates = async () => {
    try {
        const response = await axios.get(`${IBGE_API_URL}/estados?orderBy=nome`);
        return response.data;
    } catch (error) {
        console.error("Erro ao buscar estados:", error);
        throw error;
    }
};

export const getCitiesByState = async (uf, signal) => {
    if (!uf) return [];

    try {
        const response = await axios.get(`${IBGE_API_URL}/estados/${uf}/municipios`, { signal });
        return response.data;
    } catch (error) {
        if (axios.isCancel(error)) {
            console.log('Requisição de cidade cancelada');
        } else {
            console.error("Erro ao buscar cidades:", error);
            throw error;
        }
        return [];
    }
};