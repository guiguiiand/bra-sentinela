import React from 'react';
import { useState, useEffect } from 'react';
import { complaintService } from '../../services/complaintService';

import './DashboardPage.css';

import ComplaintList from '../../components/admin/ComplaintList';
import AnalysisModal from '../../components/admin/AnalysisModal';

const FilaPage = () => {
    const [complaints, setComplaints] = useState([]);
    const [selectedComplaint, setSelectedComplaint] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const loadComplaints = async () => {
        try {
            const complaintData = await complaintService.getComplaints({});
            setComplaints(Array.isArray(complaintData) ? complaintData : []);

        } catch (error) {
            console.warn("API falhou, usando mock de dados:", error.message);
            setComplaints([
                { id: '#83B-451', createdAt: '2025-11-15T10:00:00Z', channel: 'WhatsApp', status: 'aberta' },
                { id: '#83B-450', createdAt: '2025-11-15T09:00:00Z', channel: 'Ligação (Voz)', status: 'em_analise' },
                { id: '#83B-449', createdAt: '2025-11-14T14:00:00Z', channel: 'SMS', status: 'aberta' },
                { id: '#83B-448', createdAt: '2025-11-14T12:00:00Z', channel: 'WhatsApp', status: 'aberta' },
                { id: '#83B-447', createdAt: '2025-11-13T11:00:00Z', channel: 'E-mail', status: 'validada' },
                { id: '#83B-446', createdAt: '2025-11-13T10:00:00Z', channel: 'WhatsApp', status: 'inconsistente' },
            ]);
        }
    };

    useEffect(() => {
        loadComplaints();
    }, []);

    const handleAnalyzeClick = (complaint) => {
        setSelectedComplaint(complaint);
        setIsModalOpen(true);
    };

    const handleCloseModal = (shouldReload = false) => {
        setIsModalOpen(false);
        setSelectedComplaint(null);
        if (shouldReload) {
            loadComplaints();
        }
    };

    return (
        <>
            <h1 className="main-title">Fila de Denúncias</h1>

            <div className="main-column">
                <ComplaintList
                    complaints={complaints}
                    onAnalyzeClick={handleAnalyzeClick}
                />
            </div>

            {isModalOpen && (
                <AnalysisModal
                    complaint={selectedComplaint}
                    onClose={handleCloseModal}
                />
            )}
        </>
    );
};

export default FilaPage;