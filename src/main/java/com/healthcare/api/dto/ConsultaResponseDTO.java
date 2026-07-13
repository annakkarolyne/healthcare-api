package com.healthcare.api.dto;

import com.healthcare.api.entity.Consulta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class ConsultaResponseDTO {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long medicoId;
    private String medicoNome;
    private LocalDate data;
    private LocalTime hora;
    private Consulta.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}