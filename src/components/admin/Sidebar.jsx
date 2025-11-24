import React from 'react';
import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Sidebar.css';

const IconFila = () => (
    <svg className="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"></path></svg>
);
const IconRelatorios = () => (
    <svg className="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 17v-2m3 2v-4m3 4v-6m2 10H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"></path></svg>
);
const IconPainel = () => (
    <svg className="icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 10h18M3 14h18m-9-4v8m-7 0h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z"></path></svg>
);
const IconLogo = () => (
    <svg className="logo-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"></path></svg>
);

const Sidebar = () => {
    const { logout } = useAuth();

    const getNavLinkClass = ({ isActive }) =>
        isActive
            ? "nav-link nav-link-active"
            : "nav-link";

    return (
        <aside className="sidebar">

            <div className="logo-container">
                <IconLogo />
                <span className="logo-title">Denúncias</span>
            </div>

            <nav className="nav">
                <NavLink to="/dashboard" className={getNavLinkClass} end>
                    <IconPainel />
                    Painel
                </NavLink>
                <NavLink to="/fila" className={getNavLinkClass}>
                    <IconFila />
                    Fila de Denúncias
                </NavLink>
                <NavLink to="/relatorios" className={getNavLinkClass}>
                    <IconRelatorios />
                    Relatórios
                </NavLink>
            </nav>

            <div className="user-profile">
                <div className="profile-content">
                    <img
                        className="avatar"
                        src="https://placehold.co/100x100/667eea/e2e8f0?text=A"
                        alt="Avatar do Analista"
                    />
                    <div className="profile-info">
                        <p className="profile-name">Analista Admin</p>
                        <button
                            onClick={logout}
                            className="logout-button"
                        >
                            Sair
                        </button>
                    </div>
                </div>
            </div>
        </aside>
    );
};

export default Sidebar;