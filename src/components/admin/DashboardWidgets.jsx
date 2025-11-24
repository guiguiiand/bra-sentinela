import './DashboardWidgets.css';
import React, { useState } from 'react';
import { downloadReport } from '../../services/reportService';



const ReportWidget = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleDownload = async (e) => {
        e.preventDefault();
        setError(null);

        const formData = new FormData(e.target);
        const reportParams = {
            type: formData.get('report_type'),
            start: formData.get('start_date'),
            end: formData.get('end_date'),
        };

        console.log('Gerando relatório:', reportParams);

        setLoading(true);
        try {
            await downloadReport(reportParams);
            console.log('Relatório baixado com sucesso!');
        } catch (error) {
            console.error('Erro ao baixar relatório:', error);
            setError(error?.message || 'Falha ao gerar o relatório.');
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className="widget-card">
            <h3 className="widget-title">Gerar Relatório Rápido</h3>
            <form className="widget-form" onSubmit={handleDownload}>
                <div className="form-group">
                    <label htmlFor="report_type" className="form-label">Tipo de Relatório</label>
                    <select id="report_type" name="report_type" className="form-select">
                        <option value="resumo_mensal">Resumo Mensal</option>
                        <option value="por_canal">Denúncias por Canal</option>
                        <option value="status_validacao">Status de Validação</option>
                        <option value="export_csv">Exportação Completa (CSV)</option>
                    </select>
                </div>
                <div className="form-group">
                    <label htmlFor="start_date" className="form-label">Data Início</label>
                    <input type="date" id="start_date" name="start_date" className="form-input" />
                </div>
                <div className="form-group">
                    <label htmlFor="end_date" className="form-label">Data Fim</label>
                    <input type="date" id="end_date" name="end_date" className="form-input" />
                </div>
                <button type="submit" className="button button-dark" disabled={loading}>
                    {loading ? 'Gerando...' : 'Gerar Relatório'}
                </button>
            </form>
        </div>
    );
};


const DashboardWidgets = () => {
    return (
        <div className="widgets-container">
            <ReportWidget />

        </div>
    );
};

export default DashboardWidgets;