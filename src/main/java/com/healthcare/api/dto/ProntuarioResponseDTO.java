package com.healthcare.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProntuarioResponseDTO {
    private Long id;
    private Long consultaId;
    private String pacienteNome;
    private String medicoNome;
    private String sintomas;
    private String diagnostico;
    private String prescricao;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}