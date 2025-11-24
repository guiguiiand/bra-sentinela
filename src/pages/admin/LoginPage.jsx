import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext.jsx';
import './LoginPage.css';

const LoginPage = () => {
    const [user, setUser] = useState('');
    const [password, setPassword] = useState('');
    const { setToken } = useAuth();
    const navigate = useNavigate();

    const handleFakeLogin = (e) => {
        e.preventDefault();

        const fakeToken = 'mock-admin-token-12345';

        setToken(fakeToken);

        navigate('/dashboard');
    };

    return (
        <div className="login-container">
            <form onSubmit={handleFakeLogin} className="login-form">
                <h2>Acesso do Analista</h2>
                <input
                    type="text"
                    placeholder="Usuário (teste)"
                    onChange={(e) => setUser(e.target.value)}
                />
                <input
                    type="password"
                    placeholder="Senha (teste)"
                    onChange={(e) => setPassword(e.target.value)}
                />
                <button type="submit" className="cta-button">
                    Entrar (Modo de Teste)
                </button>
            </form>
        </div>
    );
};

export default LoginPage;