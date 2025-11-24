import axios from 'axios';

const apiUrl = 'http://localhost:8090';

console.log("URL:", apiUrl);

const api = axios.create({
    baseURL: apiUrl,
    headers: {
        'Content-Type': 'application/json',
    }
});

export default api;