package com.healthcare.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PacienteResponseDTO {
    private Long id;
    private String nome;
    private String cpf;
    private LocalDate nascimento;
    private String telefone;
    private String endereco;
    private String responsavelNome;
    private String responsavelCpf;
    private String responsavelTelefone;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
