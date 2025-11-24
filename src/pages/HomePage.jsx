import React, { useState } from 'react';
import './HomePage.css';
import ComplaintForm from '../components/ComplaintForm';

function HomePage() {
    const [showForm, setShowForm] = useState(false);
    return (
        <div className="homepage-container">
            <div className="main-content">
                <div className="text-column">
                    <header className="page-header">
                        <h1>Central de Denúncias: <span className="realce-header">Golpe da Falsa Central</span></h1>
                    </header>
                       {!showForm ? (
                        <>
                            <section className="intro-section">
                                <p>
                                    Registre sua denúncia se você passou por alguma das seguintes situações:
                                </p>
                            </section>
                            <section className="scenarios-list">
                                <ul>
                                    <li>
                                        Recebeu contato sobre uma suposta transação indevida.
                                    </li>
                                    <li>
                                        Pediram que você realizasse uma transação (PIX, TED, boleto).
                                    </li>
                                    <li>
                                        Pressionaram você para agir imediatamente, usando um falso senso de urgência ou medo.
                                    </li>
                                    <li>
                                        Solicitaram senhas, códigos de acesso ou dados completos da conta/cartão.
                                    </li>
                                </ul>
                            </section>
                            <div className="alert-box warning">
                                Atenção: <strong>NUNCA</strong> ligamos
                                pedindo senhas, dados do cartão ou a instalação de aplicativos.
                            </div>
                            <div className="alert-box info">
                                <strong>ℹ️ Em perigo agora?</strong> Se você está{' '}
                                <strong>atualmente em uma ligação</strong> suspeita,{' '}
                                <strong>desligue imediatamente</strong>.
                            </div>
                            <section className="cta-section">
                                <button className="cta-button" onClick={() => setShowForm(true)}>
                                    Iniciar Denúncia Agora
                                </button>
                            </section>
                        </>
                    ) : (

                        <ComplaintForm />
                    )}
                </div>
                <div className="image-column">
                </div>
            </div>
        </div>
    );
}

export default HomePage;