import React, { useState } from 'react';
import { getComplaintByProtocol } from '../services/complaintService';
import './TrackComplaintPage.css';

function TrackComplaintPage() {
    const [protocolInput, setProtocolInput] = useState('');
    const [complaintData, setComplaintData] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!protocolInput) return;

        setLoading(true);
        setError(null);
        setComplaintData(null);

        try {
            const data = await getComplaintByProtocol(protocolInput);
            setComplaintData(data);
        } catch (err) {
            if (err.response && err.response.status === 404) {
                setError('Protocolo não encontrado. Verifique o número e tente novamente.');
            } else {
                setError('Houve um erro ao buscar sua denúncia. Tente mais tarde.');
            }
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="track-page-container">
            <header className="page-header">
                <h1>Acompanhe sua Denúncia</h1>
                <p>Insira o número do protocolo recebido para consultar o status.</p>
            </header>

            <form className="track-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="protocol">Número do Protocolo</label>
                    <input
                        type="text"
                        id="protocol"
                        value={protocolInput}
                        onChange={(e) => setProtocolInput(e.target.value)}
                        placeholder="Ex: ABC01112025"
                    />
                </div>
                <button type="submit" className="cta-button" disabled={loading}>
                    {loading ? 'Buscando...' : 'Buscar'}
                </button>
            </form>

            {error && (
                <div className="alert-box error track-result">
                    {error}
                </div>
            )}

            {complaintData && (
                <div className="track-result-card">
                    <h3>Detalhes da Denúncia</h3>
                    <div className="result-item">
                        <strong>Protocolo:</strong>
                        <span>{complaintData.protocolNumber}</span>
                    </div>
                    <div className="result-item">
                        <strong>Status:</strong>
                        <span className="status-badge">{complaintData.statusComplaint}</span>
                    </div>
                    <div className="result-item">
                        <strong>Data de Criação:</strong>
                        <span>{complaintData.cratedDate}</span>
                    </div>
                    <div className="result-item">
                        <strong>Descrição:</strong>
                        <p>{complaintData.descriptionComplaint}</p>
                    </div>
                    {complaintData.message && (
                        <div className="result-item">
                            <strong>Mensagem do Atendente:</strong>
                            <p className="message-box">{complaintData.message}</p>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default TrackComplaintPage;