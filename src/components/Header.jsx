import React from 'react';
import { Link } from 'react-router-dom';
import './Header.css';
import logoImage from '../assets/logotipo.png';

function Header() {
    return (
        <header className="header">
            <Link to="/" className="logo-link">
                <img
                    src={logoImage}
                    alt="Logotipo - Central de Denúncias"
                    className="header-logo"
                />
            </Link>
            <nav>

                <Link to="/" className="link">Início</Link>
                <Link to="/acompanhar" className="link">Acompanhe sua denúncia</Link>
            </nav>
        </header>
    );
}

export default Header;