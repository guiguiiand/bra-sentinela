import React from 'react';
import { Outlet } from 'react-router-dom';
import Sidebar from '../components/admin/Sidebar';
import './AdminLayout.css';

const AdminLayout = () => {
    return (
        <div className="admin-layout">
            <Sidebar />
            <main className="admin-layout-content">
                <Outlet />
            </main>
        </div>
    );
};

export default AdminLayout;