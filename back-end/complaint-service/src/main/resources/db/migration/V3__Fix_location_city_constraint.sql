-- Remove a constraint UNIQUE do location_city_id
ALTER TABLE complaints DROP CONSTRAINT IF EXISTS complaints_location_city_id_key;

-- Remove os dados de teste existentes que podem estar causando conflito
DELETE FROM complaints WHERE protocol_number = 'ABC01112025';

-- Insere dados de teste corretos
INSERT INTO complaints (
    description_complaint,
    customer_id,
    status_complaint,
    protocol_number,
    message,
    location_city_id,
    created_date,
    complaint_date,
    complaint_time,
    channel,
    attacker_name,
    value
) VALUES (
    'Produto com defeito de fabricação',
    'CUST001',
    'IN_PROGRESS',
    'ABC01112025-' || extract(epoch from now())::bigint,
    'Reclamação de teste',
    1,
    now()::text,
    '2025-01-19',
    '14:30:00',
    'ONLINE',
    'Empresa XYZ Ltda',
    '299.99'
);
