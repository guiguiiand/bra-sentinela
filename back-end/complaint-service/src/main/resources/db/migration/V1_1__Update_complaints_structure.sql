-- Ajustar estrutura da tabela se necessário
ALTER TABLE complaints ALTER COLUMN location_city_id DROP NOT NULL;
ALTER TABLE complaints DROP CONSTRAINT IF EXISTS FK_complaints_location_city;

-- Recriar constraint corretamente
ALTER TABLE complaints
ADD CONSTRAINT FK_complaints_location_city
FOREIGN KEY (location_city_id) REFERENCES location_city(id);

-- Inserir dados de teste se não existem
INSERT INTO location_city (city, state, country)
SELECT 'São Paulo', 'SP', 'Brasil'
WHERE NOT EXISTS (SELECT 1 FROM location_city WHERE city = 'São Paulo');

INSERT INTO complaints (description_complaint, customer_id, status_complaint, protocol_number, message, location_city_id)
SELECT 'Test complaint', 'CUST001', 'OPEN', 'ABC01112025', 'Test message',
       (SELECT id FROM location_city WHERE city = 'São Paulo' LIMIT 1)
WHERE NOT EXISTS (SELECT 1 FROM complaints WHERE customer_id = 'CUST001');
