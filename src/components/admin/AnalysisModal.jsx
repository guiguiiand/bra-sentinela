import React, { useState } from 'react';
import './AnalysisModal.css';
import { complaintService } from '../../services/complaintService';
import { X } from 'lucide-react';

const AnalysisModal = ({ complaint, onClose }) => {
    const [classification, setClassification] = useState(complaint.status || 'em_analise');
    const [internalComment, setInternalComment] = useState('');
    const [publicComment, setPublicComment] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);

        const analysisData = {
            classification,
            internalComment,
            publicComment,

        };

        try {
            const result = await complaintService.updateComplaintStatus(complaint.id, analysisData);

            if (result.success) {
                alert('Análise salva com sucesso!');
                onClose(true);
            } else {
                throw new Error(result.message || 'Erro desconhecido');
            }
        } catch (error) {
            console.error("Erro ao salvar análise:", error);
            alert(`Falha ao salvar: ${error.message}`);
            setIsSubmitting(false);
        }
    };

    const renderFiles = () => {
        const files = complaint?.files || [];
        if (!files || files.length === 0) return <p>Sem arquivos anexados.</p>;
        return (
            <ul className="files-list">
                {files.map((f, idx) => (
                    <li key={idx}>
                        <a href={f} target="_blank" rel="noreferrer">Arquivo {idx + 1}</a>
                    </li>
                ))}
            </ul>
        );
    };

    if (!complaint) return null;

    return (
        <div className="modal-overlay">
            <div className="modal-content">

                <div className="modal-header">
                    <h2 className="modal-title">
                        Analisar Denúncia: <span>{complaint.id}</span>
                    </h2>
                    <button onClick={() => onClose(false)} className="modal-close-button">
                        <X size={24} />
                    </button>
                </div>

                <form id="analysis-form" onSubmit={handleSubmit} className="modal-body">

                    <div className="history-box">
                        <h4 className="history-title">Histórico (RF12)</h4>
                        <ul className="history-list">
                            <li><span>Analista B</span> mudou status para <span>Em Análise</span> (15/11/2025)</li>
                            <li>Denúncia criada (14/11/2025)</li>
                        </ul>
                    </div>

                    <div className="complaint-details">
                        <h4>Descrição da Denúncia</h4>
                        <p>{complaint.description || complaint.descriptionComplaint || 'Descrição não disponível.'}</p>

                        <h4>Arquivos</h4>
                        {renderFiles()}
                    </div>

                    <div className="form-group">
                        <label htmlFor="classification" className="form-label">
                            Classificação (RF10)
                        </label>
                        <select
                            id="classification"
                            value={classification}
                            onChange={(e) => setClassification(e.target.value)}
                            className="form-select"
                        >
                            <option value="aberta">Aberta</option>
                            <option value="em_analise">Em Análise</option>
                            <option value="validada">Validada</option>
                            <option value="inconsistente">Inconsistente</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label htmlFor="internalComment" className="form-label">
                            Comentário Interno (RF11)
                        </label>
                        <textarea
                            id="internalComment"
                            rows="4"
                            value={internalComment}
                            onChange={(e) => setInternalComment(e.target.value)}
                            placeholder="Notas visíveis apenas para a equipe de análise..."
                            className="form-input"
                        />
                    </div>


                    <div className="form-group">
                        <label htmlFor="publicComment" className="form-label">
                            Comentário Público (RF10)
                        </label>
                        <textarea
                            id="publicComment"
                            rows="3"
                            value={publicComment}
                            onChange={(e) => setPublicComment(e.target.value)}
                            placeholder="Este comentário poderá ser visto pelo denunciante..."
                            className="form-input"
                        />
                    </div>

                </form>

                <div className="modal-footer">
                    <button
                        type="button"
                        onClick={() => onClose(false)}
                        className="button button-cancel"
                    >
                        Cancelar
                    </button>
                    <button
                        type="submit"
                        form="analysis-form"
                        disabled={isSubmitting}
                        className="button button-primary"
                    >
                        {isSubmitting ? 'Salvando...' : 'Salvar Análise'}
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AnalysisModal;