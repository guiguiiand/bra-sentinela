CREATE TABLE complaint_files (
    complaint_id UUID NOT NULL,
    file_path VARCHAR(255),  -- Mudou de 'files' para 'file_path'
    CONSTRAINT FK_complaint_files_complaint
        FOREIGN KEY (complaint_id)
        REFERENCES complaints(id)
        ON DELETE CASCADE
);
