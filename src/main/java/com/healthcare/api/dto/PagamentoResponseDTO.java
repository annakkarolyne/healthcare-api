package com.healthcare.api.dto;

import com.healthcare.api.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PagamentoResponseDTO {
    private Long id;
    private Long consultaId;
    private String pacienteNome;
    private String medicoNome;
    private BigDecimal valor;
    private Pagamento.FormaPagamento formaPagamento;
    private Boolean pago;
    private LocalDateTime dataPagamento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}