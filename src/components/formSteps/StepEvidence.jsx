import React, { useState } from 'react';
import { useForm } from '../../context/FormContext';

const MAX_FILES = 3;
const MAX_FILE_SIZE = 5 * 1024 * 1024;
const ALLOWED_FILE_TYPES = ['image/jpeg', 'image/png', 'application/pdf'];

function StepEvidence({ prevStep, loading }) {
    const { formData, setFormData } = useForm();
    const [fileError, setFileError] = useState(null);

    const handleFileChange = (e) => {
        setFileError(null);
        const selectedFiles = Array.from(e.target.files);

        if (selectedFiles.length > MAX_FILES) {
            setFileError(`Você só pode enviar no máximo ${MAX_FILES} arquivos.`);
            e.target.value = null;
            return;
        }

        const filePromises = selectedFiles.map(file => {
            return new Promise((resolve, reject) => {
                if (file.size > MAX_FILE_SIZE) {
                    return reject(`O arquivo ${file.name} excede o limite de 5MB.`);
                }
                if (!ALLOWED_FILE_TYPES.includes(file.type)) {
                    return reject(`O tipo de arquivo ${file.name} não é permitido.`);
                }
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => resolve(reader.result);
                reader.onerror = (error) => reject(error);
            });
        });

        Promise.all(filePromises)
            .then(base64Files => {
                setFormData(prev => ({ ...prev, files: base64Files }));
            })
            .catch(err => {
                setFileError(err);
                e.target.value = null;
                setFormData(prev => ({ ...prev, files: [] }));
            });
    };

    return (
        <div className="form-step">
            <h2>Evidências (Opcional)</h2>
            <p>Anexe até 3 arquivos (prints, fotos, PDFs) com no máximo 5MB cada.</p>
            <div className="form-group">
                <label htmlFor="files">Anexar arquivos</label>
                <input
                    type="file"
                    id="files"
                    name="files"
                    multiple
                    onChange={handleFileChange}
                    accept="image/png, image/jpeg, application/pdf"
                />
                {formData.files.length > 0 && (
                    <p><i>{formData.files.length} arquivo(s) selecionado(s).</i></p>
                )}

                {fileError && <div className="alert-box error" style={{marginTop: '15px'}}>{fileError}</div>}
            </div>

            <div className="step-actions">
                <button type="button" className="cta-button secondary" onClick={prevStep} disabled={loading}>Voltar</button>
                <button type="submit" className="cta-button" disabled={loading}>
                    {loading ? 'Enviando...' : 'Finalizar Denúncia'}
                </button>
            </div>
        </div>
    );
}
export default StepEvidence;