package com.healthcare.api.service;

import com.healthcare.api.dto.PagamentoRequestDTO;
import com.healthcare.api.dto.PagamentoResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Medico;
import com.healthcare.api.entity.Paciente;
import com.healthcare.api.entity.Pagamento;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Consulta consultaAgendada;
    private Consulta consultaCancelada;
    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        Paciente paciente = Paciente.builder()
                .id(1L)
                .nome("João Silva")
                .build();

        Medico medico = Medico.builder()
                .id(1L)
                .nome("Dra. Ana Souza")
                .build();

        consultaAgendada = Consulta.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .data(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .status(Consulta.Status.AGENDADA)
                .build();

        consultaCancelada = Consulta.builder()
                .id(2L)
                .paciente(paciente)
                .medico(medico)
                .data(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(11, 0))
                .status(Consulta.Status.CANCELADA)
                .build();

        pagamento = Pagamento.builder()
                .id(1L)
                .consulta(consultaAgendada)
                .valor(BigDecimal.valueOf(150))
                .formaPagamento(Pagamento.FormaPagamento.PIX)
                .pago(true)
                .dataPagamento(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PagamentoRequestDTO criarDto(Long consultaId) {
        PagamentoRequestDTO dto = new PagamentoRequestDTO();
        dto.setConsultaId(consultaId);
        dto.setValor(BigDecimal.valueOf(150));
        dto.setFormaPagamento(Pagamento.FormaPagamento.PIX);
        return dto;
    }

    @Test
    void deveRegistrarPagamentoComSucesso() {
        PagamentoRequestDTO dto = criarDto(1L);

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consultaAgendada));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        PagamentoResponseDTO response = pagamentoService.registrar(dto);

        assertThat(response.getPacienteNome()).isEqualTo("João Silva");
        assertThat(response.getMedicoNome()).isEqualTo("Dra. Ana Souza");
        assertThat(response.getValor()).isEqualByComparingTo(BigDecimal.valueOf(150));
        verify(pagamentoRepository).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoAoRegistrarPagamentoDeConsultaCancelada() {
        PagamentoRequestDTO dto = criarDto(2L);

        when(consultaRepository.findById(2L)).thenReturn(Optional.of(consultaCancelada));

        assertThatThrownBy(() -> pagamentoService.registrar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Consulta cancelada não pode receber pagamento");

        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoAoRegistrarPagamentoComConsultaInexistente() {
        PagamentoRequestDTO dto = criarDto(99L);

        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagamentoService.registrar(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Consulta não encontrada");

        verify(pagamentoRepository, never()).save(any(Pagamento.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarPagamentoInexistente() {
        when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagamentoService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Pagamento não encontrado");
    }
}