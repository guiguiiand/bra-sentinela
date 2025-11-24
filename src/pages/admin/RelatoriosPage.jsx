import React from 'react';
import { useEffect, useState } from 'react';

import './DashboardPage.css';
import './RelatoriosPage.css';

import DashboardWidgets from '../../components/admin/DashboardWidgets';
import ChannelChartWidget from '../../components/admin/ChannelChartWidget';
import DashboardKpiGrid from '../../components/admin/DashboardKpiGrid';
import { getReport, getLastMonthsDetailed } from '../../services/reportService';

const RelatoriosPage = () => {
    const [kpiData, setKpiData] = useState(null);
    const [detailed, setDetailed] = useState([]);
    const [months, setMonths] = useState(6);
    const [loadingDetailed, setLoadingDetailed] = useState(false);
  

    useEffect(() => {
        const load = async () => {
            try {
                const report = await getReport();
                setKpiData({
                    abertas: report.open ?? report.abertas ?? 0,
                    emAnalise: report.pendingReview ?? report.emAnalise ?? 0,
                    validadas: report.validated ?? report.validadas ?? 0,
                    inconsistentes: report.inconsistent ?? report.inconsistentes ?? 0,
                });
            } catch (err) {
                console.warn('Falha ao carregar relatório, usando mock', err?.message || err);
                setKpiData({ abertas: 12, emAnalise: 5, validadas: 83, inconsistentes: 21 });
            }
        };

        load();
        loadDetailed(months);
    }, []);

    const loadDetailed = async (m = 6) => {
        setLoadingDetailed(true);
        try {
            const resp = await getLastMonthsDetailed(m);
            setDetailed(Array.isArray(resp) ? resp : []);
        } catch (err) {
            console.warn('Falha ao carregar relatório detalhado', err?.message || err);
            setDetailed([]);
        } finally {
            setLoadingDetailed(false);
        }
    };

    const handleMonthsChange = (e) => {
        const m = Number(e.target.value) || 6;
        setMonths(m);
        loadDetailed(m);
    };


    return (
        <>
            <h1 className="main-title">Relatórios</h1>

            <div className="reports-layout">
                <DashboardWidgets />
                <DashboardKpiGrid data={kpiData} />
                <ChannelChartWidget />
            </div>
        </>
    );
};

export default RelatoriosPage;