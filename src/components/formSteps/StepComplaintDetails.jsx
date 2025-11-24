import React, { useState, useEffect } from 'react';
import { useForm } from '../../context/FormContext';
import CurrencyInput from 'react-currency-input-field';
import { getStates, getCitiesByState } from '../../services/locationService';
import axios from 'axios';

const channelOptions = [
    "Mensagem no WhatsApp",
    "Ligação Telefônica",
    "SMS",
    "E-mail",
    "Redes Sociais",
    "Outros"
];

function StepComplaintDetails({ nextStep, prevStep }) {
    const { formData, handleChange, setFormData } = useForm();
    const [states, setStates] = useState([]);
    const [cities, setCities] = useState([]);
    const [loadingCities, setLoadingCities] = useState(false);
    const [locationError, setLocationError] = useState(null);

    useEffect(() => {
        const loadStates = async () => {
            try {
                const data = await getStates();
                setStates(data);
                setLocationError(null);
            } catch (error) {
                console.error("Falha ao carregar estados:", error);
                setLocationError('Não foi possível carregar as localidades.');
            }
        };
        loadStates();
    }, []);

    useEffect(() => {
        if (formData.state && !locationError) {
            const controller = new AbortController();
            const loadCities = async () => {
                setLoadingCities(true);
                setCities([]); // Limpa cidades antigas
                try {
                    const data = await getCitiesByState(formData.state, controller.signal);
                    setCities(data);
                    setLocationError(null);
                } catch (error) {
                    if (!axios.isCancel(error)) {
                        console.error("Falha ao carregar cidades:", error);
                        setLocationError('Não foi possível carregar as cidades.');
                    }
                } finally {
                    setLoadingCities(false);
                }
            };

            loadCities();

            return () => {
                controller.abort();
            };

        } else {
            setCities([]);
        }
    }, [formData.state, locationError]);


    const handleStateChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            state: value,
            city: ''
        }));
    };

    const handleValueChange = (value) => {
        setFormData(prev => ({ ...prev, value: value || '' }));
    };


    return (
        <div className="form-step">
            <h2>Detalhes da Ocorrência</h2>

            {locationError && (
                <div className="alert-box error" style={{ marginBottom: '20px' }}>
                    {locationError} Por favor, preencha os campos de localidade manualmente.
                </div>
            )}

            <div className="form-group">
                <label htmlFor="channel">Canal utilizado pelo golpista*</label>
                <select
                    id="channel"
                    name="channel"
                    value={formData.channel}
                    onChange={handleChange}
                    required
                >
                    <option value="">Selecione um canal...</option>
                    {channelOptions.map(option => (
                        <option key={option} value={option}>
                            {option}
                        </option>
                    ))}
                </select>
            </div>


            <div className="form-row">
                {locationError ? (
                    <>
                        <div className="form-group">
                            <label htmlFor="state">Estado (UF)*</label>
                            <input
                                type="text"
                                id="state"
                                name="state"
                                value={formData.state}
                                onChange={handleStateChange}
                                maxLength="2"
                                placeholder="UF"
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="city">Cidade*</label>
                            <input
                                type="text"
                                id="city"
                                name="city"
                                value={formData.city}
                                onChange={handleChange}
                                placeholder="Nome da cidade"
                                required
                            />
                        </div>
                    </>
                ) : (
                    <>
                        <div className="form-group">
                            <label htmlFor="state">Estado*</label>
                            <select
                                id="state"
                                name="state"
                                value={formData.state}
                                onChange={handleStateChange}
                                required
                                disabled={states.length === 0}
                            >
                                <option value="">Selecione um estado...</option>
                                {states.map(state => (
                                    <option key={state.id} value={state.sigla}>
                                        {state.nome}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="form-group">
                            <label htmlFor="city">Cidade*</label>
                            <select
                                id="city"
                                name="city"
                                value={formData.city}
                                onChange={handleChange}
                                required
                                disabled={loadingCities || !formData.state}
                            >
                                <option value="">
                                    {loadingCities ? "Carregando..." : (formData.state ? "Selecione uma cidade..." : "Selecione um estado primeiro")}
                                </option>
                                {cities.map(city => (
                                    <option key={city.id} value={city.nome}>
                                        {city.nome}
                                    </option>
                                ))}
                            </select>
                        </div>
                    </>
                )}
            </div>


            <div className="form-row">
                <div className="form-group">
                    <label htmlFor="date">Data Aproximada*</label>
                    <input type="date" id="date" name="date" value={formData.date} onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label htmlFor="time">Hora Aproximada*</label>
                    <input type="time" id="time" name="time" value={formData.time} onChange={handleChange} required />
                </div>
            </div>

            <div className="form-row">
                <div className="form-group">
                    <label htmlFor="attackerName">Nome ou identificação do golpista</label>
                    <input type="text" id="attackerName" name="attackerName" value={formData.attackerName} onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label htmlFor="value">Valor envolvido (se houver)</label>
                    <CurrencyInput
                        id="value"
                        name="value"
                        placeholder="R$ 0,00"
                        value={formData.value}
                        onValueChange={handleValueChange}
                        prefix="R$ "
                        decimalsLimit={2}
                        decimalSeparator=","
                        groupSeparator="."
                    />
                </div>
            </div>

            <div className="form-group">
                <label htmlFor="description">Descrição do Ocorrido*</label>
                <textarea id="description" name="description" rows="5" value={formData.description} onChange={handleChange} required></textarea>
            </div>

            <div className="step-actions">
                <button type="button" className="cta-button secondary" onClick={prevStep}>Voltar</button>
                <button type="button" className="cta-button" onClick={nextStep}>Próximo</button>
            </div>
        </div>
    );
}
export default StepComplaintDetails;