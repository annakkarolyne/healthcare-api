CREATE TABLE pagamentos (
    id BIGSERIAL PRIMARY KEY,
    consulta_id BIGINT NOT NULL REFERENCES consultas(id),
    valor NUMERIC(10,2) NOT NULL,
    forma_pagamento VARCHAR(20) NOT NULL,
    pago BOOLEAN NOT NULL DEFAULT FALSE,
    data_pagamento TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);