import React from 'react';
import './ChannelChartWidget.css'; // CSS novo

// Widget: Gráfico de Canais (RF13)
const ChannelChartWidget = () => {
    // Mock de dados (do seu HTML) - RF13 traria isso da API
    const data = [
        { name: 'WhatsApp', percent: 72, color: 'var(--color-blue-500)' },
        { name: 'Ligação (Voz)', percent: 18, color: 'var(--color-green-500)' },
        { name: 'SMS', percent: 10, color: 'var(--color-yellow-500)' },
    ];

    return (
        <div className="widget-card">
            <h3 className="widget-title">Denúncias por Canal (Últimos 30d)</h3>
            <div className="chart-container">
                {data.map((item) => (
                    <div key={item.name} className="chart-item">
                        <div className="chart-labels">
                            <span>{item.name}</span>
                            <span>{item.percent}%</span>
                        </div>
                        <div className="progress-bar-bg">
                            <div
                                className="progress-bar-fg"
                                style={{ width: `${item.percent}%`, backgroundColor: item.color }}
                            ></div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default ChannelChartWidget;