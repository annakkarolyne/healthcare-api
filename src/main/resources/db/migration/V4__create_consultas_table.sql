CREATE TABLE consultas (
    id BIGSERIAL PRIMARY KEY,
    paciente_id BIGINT NOT NULL REFERENCES pacientes(id),
    medico_id BIGINT NOT NULL REFERENCES medicos(id),
    data DATE NOT NULL,
    hora TIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now(),
    CONSTRAINT uk_medico_data_hora UNIQUE (medico_id, data, hora)
);