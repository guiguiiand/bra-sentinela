import React from 'react';
import './HomePage.css';
import heroImage from '../assets/heroImageHomePage.png';

function HomePage() {
    return (
        <div className="homepage-container">

            <div className="main-content">

                <div className="text-column">
                    <header className="page-header">
                        <h1>Central de Denúncias: <span className="realce-header">Golpe da Falsa Central</span></h1>
                    </header>

                    <section className="intro-section">
                        <p>
                            Registre sua denúncia se você passou por alguma das seguintes situações:
                        </p>
                    </section>

                    <section className="scenarios-list">
                        <ul>
                            <li>
                                Recebeu uma ligação de um suposto funcionário do banco...
                            </li>
                            <li>
                                O falso atendente solicitou que você instalasse algum
                                aplicativo...
                            </li>
                            <li>
                                Foi instruído(a) a realizar uma "transação de teste"...
                            </li>
                            <li>
                                Pediram suas senhas, dados completos do cartão ou códigos...
                            </li>
                        </ul>
                    </section>

                    <div className="alert-box warning">
                        <strong>Atenção: NUNCA</strong> ligamos
                        pedindo senhas, dados do cartão ou a instalação de aplicativos.
                    </div>

                    <div className="alert-box info">
                        <strong>ℹ️ Em perigo agora?</strong> Se você está{' '}
                        <strong>atualmente em uma ligação</strong> suspeita,{' '}
                        <strong>desligue imediatamente</strong>.
                    </div>

                    <section className="cta-section">
                        <button className="cta-button">
                            Iniciar Denúncia Agora
                        </button>
                    </section>
                </div>

                <div className="image-column">

                </div>

            </div>
        </div>
    );
}

export default HomePage;