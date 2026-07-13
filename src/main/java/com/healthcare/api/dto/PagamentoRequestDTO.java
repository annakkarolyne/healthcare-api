package com.healthcare.api.dto;

import com.healthcare.api.entity.Pagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PagamentoRequestDTO {

    @NotNull(message = "Consulta é obrigatória")
    private Long consultaId;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "Forma de pagamento é obrigatória")
    private Pagamento.FormaPagamento formaPagamento;
}