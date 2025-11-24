import React from 'react';

function StepSuccess({ protocol }) {
    return (
        <div className="form-step success">
            <h2>Denúncia Registrada!</h2>
            <p>Obrigado por sua colaboração. Sua denúncia foi registrada com sucesso.</p>
            <p>Guarde seu número de protocolo para acompanhar:</p>
            <div className="protocol-box">
                {protocol}
            </div>
            <p><small>Em breve, você poderá usar a seção "Acompanhe sua denúncia" para ver o status.</small></p>
        </div>
    );
}
export default StepSuccess;