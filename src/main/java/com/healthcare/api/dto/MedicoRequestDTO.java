package com.healthcare.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicoRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CRM é obrigatório")
    private String crm;

    @NotBlank(message = "Especialidade é obrigatória")
    private String especialidade;

    private String telefone;

    private Long usuarioId;
}