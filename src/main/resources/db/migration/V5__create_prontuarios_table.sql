CREATE TABLE prontuarios (
    id BIGSERIAL PRIMARY KEY,
    consulta_id BIGINT NOT NULL UNIQUE REFERENCES consultas(id),
    sintomas TEXT,
    diagnostico TEXT,
    prescricao TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);