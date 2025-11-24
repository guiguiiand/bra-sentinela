import React, { useState } from 'react';
import { FormProvider, useForm } from '../context/FormContext';
import './ComplaintForm.css';
import { registerUser, registerComplaint } from '../services/complaintService';

import StepIdentification from './formSteps/StepIdentification';
import StepUserDetails from './formSteps/StepUserDetails';
import StepComplaintDetails from './formSteps/StepComplaintDetails';
import StepEvidence from './formSteps/StepEvidence';
import StepSuccess from './formSteps/StepSuccess';

function ComplaintFormOrchestrator() {

    const { formData } = useForm();
    const [currentStep, setCurrentStep] = useState(1);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [protocol, setProtocol] = useState(null);

    const nextStep = () => {
        setCurrentStep((prev) => prev + 1);
    };

    const skipToStep = (stepNumber) => {
        setCurrentStep(stepNumber);
    };

    const prevStep = () => {
        if (currentStep === 3 && formData.isAnonymous) {
            setCurrentStep(1);
        } else {
            setCurrentStep((prev) => prev - 1);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError(null);
        let customerId = null;

        console.log("--- MODO DE TESTE (MOCK) ATIVADO ---");
        console.log("Dados do Usuário (se houver):", {
            name: formData.name,
            email: formData.email,
            phone: formData.phone,
            cpf: formData.cpf,
        });
        console.log("Dados da Denúncia:", formData);


        try {

            await new Promise(r => setTimeout(r, 1500));




            if (!formData.isAnonymous) {
                const userPayload = {
                    name: formData.name,
                    email: formData.email,
                    phone: formData.phone,
                    cpf: formData.cpf,
                };
                const userResponse = await registerUser(userPayload);
                customerId = userResponse.id;
            }


            const complaintPayload = {
                description: formData.description,
                date: formData.date,
                time: formData.time,
                channel: formData.channel,
                attackerName: formData.attackerName,
                value: formData.value,
                locationCity: { city: formData.city, state: formData.state },
                files: formData.files.map(base64String => base64String.split(',')[1]),
                customerId: customerId,
            };
            const complaintResponse = await registerComplaint(complaintPayload);
            const newProtocol = complaintResponse.protocol;


            setProtocol(newProtocol);
            setCurrentStep(5);

        } catch (err) {
            console.error("Erro inesperado no modo MOCK:", err);
            setError('Houve um erro inesperado ao testar o formulário.');
        } finally {
            setLoading(false);
        }
    };

    const renderStep = () => {
        switch (currentStep) {
            case 1:
                return <StepIdentification
                    nextStep={nextStep}
                    skipToStep={skipToStep}
                />;
            case 2:
                return <StepUserDetails nextStep={nextStep} prevStep={prevStep} />;
            case 3:
                return <StepComplaintDetails nextStep={nextStep} prevStep={prevStep} />;
            case 4:
                return <StepEvidence prevStep={prevStep} loading={loading} />;
            case 5:
                return <StepSuccess protocol={protocol} />;
            default:
                return null;
        }
    };

    return (
        <form className="complaint-form" onSubmit={handleSubmit}>
            {error && <div className="alert-box error">{error}</div>}
            {renderStep()}
        </form>
    );
}

function ComplaintForm() {
    return (
        <FormProvider>
            <ComplaintFormOrchestrator />
        </FormProvider>
    );
}

export default ComplaintForm;