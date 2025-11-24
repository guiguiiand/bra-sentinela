import React from 'react';
import { useForm } from '../../context/FormContext';

function StepUserDetails({ nextStep, prevStep }) {
    const { formData, handleChange } = useForm();

    return (
        <div className="form-step">
            <h2>Seus Dados (Opcional)</h2>
            <p>Preencha os campos abaixo. Seus dados serão mantidos em sigilo.</p>
            <div className="form-group">
                <label htmlFor="name">Nome Completo</label>
                <input type="text" id="name" name="name" value={formData.name} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label htmlFor="email">E-mail</label>
                <input type="email" id="email" name="email" value={formData.email} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label htmlFor="phone">Telefone para Contato</label>
                <input type="tel" id="phone" name="phone" value={formData.phone} onChange={handleChange} />
            </div>
            <div className="form-group">
                <label htmlFor="cpf">CPF</label>
                <input type="text" id="cpf" name="cpf" value={formData.cpf} onChange={handleChange} />
            </div>
            <div className="step-actions">
                <button type="button" className="cta-button secondary" onClick={prevStep}>Voltar</button>
                <button type="button" className="cta-button" onClick={nextStep}>Próximo</button>
            </div>
        </div>
    );
}
export default StepUserDetails;