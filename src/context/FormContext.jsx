import React, { createContext, useState, useContext } from 'react';

const FormContext = createContext();
export function FormProvider({ children }) {
    const [formData, setFormData] = useState({
        isAnonymous: null,
        name: '',
        email: '',
        phone: '',
        cpf: '',
        description: '',
        date: '',
        time: '',
        channel: '',
        attackerName: '',
        value: '',
        city: '',
        state: '',
        files: [],
    });


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    return (
        <FormContext.Provider value={{ formData, setFormData, handleChange }}>
            {children}
        </FormContext.Provider>
    );
}

export const useForm = () => {
    return useContext(FormContext);
};