import React from 'react';
import './ComplaintList.css';


const StatusBadge = ({ status }) => {
    const statusMap = {
        aberta: { text: 'Aberta', className: "status-blue" },
        em_analise: { text: 'Em Análise', className: "status-yellow" },
        validada: { text: 'Validada', className: "status-green" },
        inconsistente: { text: 'Inconsistente', className: "status-red" },
    };

    const s = statusMap[status] || { text: status, className: "status-gray" };

    return (
        <span className={`status-badge ${s.className}`}>
      {s.text}
    </span>
    );
};

const ComplaintList = ({ complaints, onAnalyzeClick }) => {
    return (
        <div className="complaint-list-card">
            <h2 className="complaint-list-title">Fila de Análise (Denúncias Pendentes)</h2>

            <div className="table-wrapper">
                <table className="complaint-table">
                    <thead>
                    <tr>
                        <th>Protocolo</th>
                        <th>Data</th>
                        <th>Canal</th>
                        <th>Status</th>
                        <th>Ação</th>
                    </tr>
                    </thead>
                    <tbody>
                    {complaints && complaints.length > 0 ? (
                        complaints.map((complaint) => (
                            <tr key={complaint.id}>
                                <td data-label="Protocolo">{complaint.protocol || complaint.id}</td>
                                <td data-label="Data">
                                    {new Date(complaint.createdAt || complaint.date).toLocaleDateString('pt-BR')}
                                </td>
                                <td data-label="Canal">{complaint.channel}</td>
                                <td data-label="Status">
                                    <StatusBadge status={complaint.status} />
                                </td>
                                <td data-label="Ação">
                                    <button
                                        onClick={() => onAnalyzeClick(complaint)}
                                        className="button button-primary"
                                    >
                                        Analisar
                                    </button>
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan="5" className="empty-row">
                                Nenhuma denúncia encontrada.
                            </td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </div>

            <div className="complaint-list-footer">
                <a href="#">
                    Ver todas as denúncias &rarr;
                </a>
            </div>
        </div>
    );
};

export default ComplaintList;