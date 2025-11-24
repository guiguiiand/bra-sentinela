import React from 'react';
import { useState, useEffect } from 'react';
import { getReport } from '../../services/reportService';

import './DashboardPage.css';

import DashboardKpiGrid from '../../components/admin/DashboardKpiGrid';
import ComplaintList from '../../components/admin/ComplaintList';
import AnalysisModal from '../../components/admin/AnalysisModal';
import DashboardWidgets from '../../components/admin/DashboardWidgets';
import ChannelChartWidget from '../../components/admin/ChannelChartWidget';

const DashboardPage = () => {
    const [kpiData, setKpiData] = useState(null);
    const [complaints, setComplaints] = useState([]);
    const [selectedComplaint, setSelectedComplaint] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const loadDashboardData = async () => {
        try {
            const reportData = await getReport();
            setKpiData({
                abertas: reportData.open ?? reportData.abertas ?? 0,
                emAnalise: reportData.pendingReview ?? reportData.emAnalise ?? 0,
                validadas: reportData.validated ?? reportData.validadas ?? 0,
                inconsistentes: reportData.inconsistent ?? reportData.inconsistentes ?? 0,
            });

            const complaintData = await (await import('../../services/complaintService')).complaintService.getComplaints({ status: 'aberta' });
            setComplaints(Array.isArray(complaintData) ? complaintData : []);

        } catch (error) {
            console.warn("API falhou, usando mock de dados:", error?.message || error);
            setKpiData({
                abertas: 12,
                emAnalise: 5,
                validadas: 83,
                inconsistentes: 21
            });
            setComplaints([
                { id: '#83B-451', createdAt: '2025-11-15T10:00:00Z', channel: 'WhatsApp', status: 'aberta' },
                { id: '#83B-450', createdAt: '2025-11-15T09:00:00Z', channel: 'Ligação (Voz)', status: 'em_analise' },
                { id: '#83B-449', createdAt: '2025-11-14T14:00:00Z', channel: 'SMS', status: 'aberta' },
                { id: '#83B-448', createdAt: '2025-11-14T12:00:00Z', channel: 'WhatsApp', status: 'aberta' },
            ]);
        }
    };

    useEffect(() => {
        loadDashboardData();
    }, []);

    const handleAnalyzeClick = (complaint) => {
        setSelectedComplaint(complaint);
        setIsModalOpen(true);
    };

    const handleCloseModal = (shouldReload = false) => {
        setIsModalOpen(false);
        setSelectedComplaint(null);
        if (shouldReload) {
            loadDashboardData();
        }
    };

    return (
        <>
            <h1 className="main-title">Painel do Analista</h1>
            <DashboardKpiGrid data={kpiData} />
            <ChannelChartWidget />

            <div className="main-grid">

                <div className="main-column">
                    <ComplaintList
                        complaints={complaints}
                        onAnalyzeClick={handleAnalyzeClick}
                    />
                </div>

                <div className="sidebar-column">
                    <DashboardWidgets />
                </div>
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

export default DashboardPage;