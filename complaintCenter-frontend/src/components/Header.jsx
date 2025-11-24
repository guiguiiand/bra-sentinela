import React from 'react';
import './Header.css';
import logoImage from '../assets/logotipo.png';

function Header() {
    return (
        <header className="header">
            <a href="/" className="logo-link">
                <img
                    src={logoImage}
                    alt="Logotipo - Central de Denúncias"
                    className="header-logo"
                />
            </a>
            <nav>
                <a href="#home" className="link">Início</a>
                <a href="#form" className="link">Acompanhe sua denúncia</a>
            </nav>
        </header>
    );
}

export default Header;