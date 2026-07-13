package com.healthcare.api.service;

import com.healthcare.api.dto.PagamentoRequestDTO;
import com.healthcare.api.dto.PagamentoResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Pagamento;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.PagamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final ConsultaRepository consultaRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository, ConsultaRepository consultaRepository) {
        this.pagamentoRepository = pagamentoRepository;
        this.consultaRepository = consultaRepository;
    }

    public List<PagamentoResponseDTO> listar() {
        return pagamentoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PagamentoResponseDTO buscarPorId(Long id) {
        Pagamento pagamento = buscarEntidadePorId(id);
        return toResponseDTO(pagamento);
    }

    public PagamentoResponseDTO registrar(PagamentoRequestDTO dto) {
        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));

        if (consulta.getStatus() == Consulta.Status.CANCELADA) {
            throw new IllegalArgumentException("Consulta cancelada não pode receber pagamento");
        }

        Pagamento pagamento = Pagamento.builder()
                .consulta(consulta)
                .valor(dto.getValor())
                .formaPagamento(dto.getFormaPagamento())
                .pago(true)
                .dataPagamento(LocalDateTime.now())
                .build();

        Pagamento salvo = pagamentoRepository.save(pagamento);
        return toResponseDTO(salvo);
    }

    public Map<String, Object> relatorioMensal(int ano, int mes) {
        YearMonth yearMonth = YearMonth.of(ano, mes);

        List<Pagamento> pagamentosDoMes = pagamentoRepository.findAll().stream()
                .filter(p -> p.getDataPagamento() != null)
                .filter(p -> YearMonth.from(p.getDataPagamento()).equals(yearMonth))
                .toList();

        BigDecimal total = pagamentosDoMes.stream()
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return Map.of(
                "anoMes", yearMonth.toString(),
                "quantidadePagamentos", pagamentosDoMes.size(),
                "valorTotal", total
        );
    }

    private Pagamento buscarEntidadePorId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado"));
    }

    private PagamentoResponseDTO toResponseDTO(Pagamento p) {
        return new PagamentoResponseDTO(
                p.getId(),
                p.getConsulta().getId(),
                p.getConsulta().getPaciente().getNome(),
                p.getConsulta().getMedico().getNome(),
                p.getValor(),
                p.getFormaPagamento(),
                p.getPago(),
                p.getDataPagamento(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}