import React from 'react';
import { useForm } from '../../context/FormContext';

function StepIdentification({ nextStep, skipToStep }) {
    const { setFormData } = useForm();

    return (
        <div className="form-step">
            <h2>Identificação</h2>
            <p>Você prefere registrar sua denúncia anonimamente ou se identificar?</p>
            <div className="step-actions vertical">
                <button
                    type="button"
                    className="cta-button"
                    onClick={() => {
                        setFormData(prev => ({ ...prev, isAnonymous: false }));
                        nextStep();
                    }}
                >
                    Quero me Identificar
                </button>
                <button
                    type="button"
                    className="cta-button secondary"
                    onClick={() => {
                        setFormData(prev => ({ ...prev, isAnonymous: true }));
                        // "Anonimamente" -> PULE para a Etapa 3
                        skipToStep(3);
                    }}
                >
                    Registrar Anonimamente
                </button>
            </div>
        </div>
    );
}
export default StepIdentification;