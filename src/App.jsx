import React from 'react';
import { Routes, Route } from 'react-router-dom';
import './App.css';

import PublicLayout from './layouts/PublicLayout';
import AdminLayout from './layouts/AdminLayout';
import HomePage from './pages/HomePage';
import TrackComplaintPage from './pages/TrackComplaintPage';
import LoginPage from './pages/admin/LoginPage';
import DashboardPage from './pages/admin/DashboardPage';
import FilaPage from './pages/admin/FilaPage';
import RelatoriosPage from './pages/admin/RelatoriosPage';

import ProtectedRoute from './routes/ProtectedRoute';

function App() {
    return (
        <Routes>
            <Route element={<PublicLayout />}>
                <Route path="/" element={<HomePage />} />
                <Route path="/acompanhar" element={<TrackComplaintPage />} />
            </Route>

            <Route path="/login" element={<LoginPage />} />

            <Route element={<ProtectedRoute />}>
                <Route element={<AdminLayout />}>
                    <Route path="/dashboard" element={<DashboardPage />} />
                    <Route path="/fila" element={<FilaPage />} />
                    <Route path="/relatorios" element={<RelatoriosPage />} />
                </Route>
            </Route>
        </Routes>
    );
}

export default App;