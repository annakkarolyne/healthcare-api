package com.healthcare.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProntuarioRequestDTO {

    @NotNull(message = "Consulta é obrigatória")
    private Long consultaId;

    @NotBlank(message = "Sintomas são obrigatórios")
    private String sintomas;

    @NotBlank(message = "Diagnóstico é obrigatório")
    private String diagnostico;

    private String prescricao;
}