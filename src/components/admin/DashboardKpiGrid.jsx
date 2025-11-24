import React from 'react';
import './DashboardKpiGrid.css';
import { AlertCircle, Clock, CheckCircle, XCircle } from 'lucide-react';

const KpiCard = ({ title, value, icon, colorClass, iconBgClass, iconTextClass }) => {
    return (
        <div className={`kpi-card ${colorClass}`}>
            <div className="kpi-card-content">
                <div>
                    <p className="kpi-card-title">{title}</p>
                    <p className="kpi-card-value">{value}</p>
                </div>
                <div className={`kpi-icon-wrapper ${iconBgClass}`}>
                    {React.cloneElement(icon, { className: `kpi-icon ${iconTextClass}` })}
                </div>
            </div>
        </div>
    );
};

const DashboardKpiGrid = ({ data }) => {
    const kpis = data || { abertas: 0, emAnalise: 0, validadas: 0, inconsistentes: 0 };

    return (
        <div className="kpi-grid">
            <KpiCard
                title="Abertas"
                value={kpis.abertas}
                icon={<AlertCircle />}
                colorClass="card-blue"
                iconBgClass="icon-bg-blue"
                iconTextClass="icon-text-blue"
            />
            <KpiCard
                title="Em Análise"
                value={kpis.emAnalise}
                icon={<Clock />}
                colorClass="card-yellow"
                iconBgClass="icon-bg-yellow"
                iconTextClass="icon-text-yellow"
            />
            <KpiCard
                title="Validadas"
                value={kpis.validadas}
                icon={<CheckCircle />}
                colorClass="card-green"
                iconBgClass="icon-bg-green"
                iconTextClass="icon-text-green"
            />
            <KpiCard
                title="Inconsistentes"
                value={kpis.inconsistentes}
                icon={<XCircle />}
                colorClass="card-red"
                iconBgClass="icon-bg-red"
                iconTextClass="icon-text-red"
            />
        </div>
    );
};

export default DashboardKpiGrid;