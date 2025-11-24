import React from 'react';
import './Footer.css';
import logoImage from '../assets/logotipo.png';

function Footer() {
    return (
        <footer className="footer-container">

            <div className="footer-main">
                <div className="footer-info">
                    <p>Central de Denúncias (Projeto Acadêmico)</p>
                    <p>CNPJ: 00.000.000/0001-00</p>
                    <p>São Paulo, SP - Brasil | CEP 01000-000</p>
                </div>

                <div className="footer-logo-area">
                    <a href="/" title="Voltar para a Home">
                        <img src={logoImage} alt="Logotipo" className="footer-logo" />
                    </a>
                </div>
            </div>

            {/* 2. Barra Inferior (Copyright) */}
            <div className="footer-bottom">
                <p>© 2025 Central de Denúncias. Todos os direitos reservados.</p>
            </div>

        </footer>
    );
}

export default Footer;