CREATE TABLE location_city (
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255)
);

CREATE TABLE complaints (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    attacker_name VARCHAR(255),
    channel VARCHAR(255),
    created_date VARCHAR(255),
    customer_id VARCHAR(255),
    complaint_date VARCHAR(255),
    description VARCHAR(255),
    description_complaint VARCHAR(255),
    message VARCHAR(255),
    protocol_number VARCHAR(255),
    status_complaint VARCHAR(255),
    complaint_time VARCHAR(255),
    value VARCHAR(255),
    location_city_id BIGINT UNIQUE,
    CONSTRAINT FK_complaints_location_city
        FOREIGN KEY (location_city_id)
        REFERENCES location_city(id)
);

-- Dados de teste
INSERT INTO location_city (city, state, country) VALUES
('São Paulo', 'SP', 'Brasil');

INSERT INTO complaints (description_complaint, customer_id, status_complaint, protocol_number, message, location_city_id) VALUES
('Test complaint', 'CUST001', 'OPEN', 'ABC01112025', 'Test message', 1);
