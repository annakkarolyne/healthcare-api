package com.healthcare.api.controller;

import com.healthcare.api.dto.PagamentoRequestDTO;
import com.healthcare.api.dto.PagamentoResponseDTO;
import com.healthcare.api.service.PagamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public List<PagamentoResponseDTO> listar() {
        return pagamentoService.listar();
    }

    @GetMapping("/{id}")
    public PagamentoResponseDTO buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoResponseDTO registrar(@Valid @RequestBody PagamentoRequestDTO dto) {
        return pagamentoService.registrar(dto);
    }

    @GetMapping("/relatorio")
    public Map<String, Object> relatorioMensal(@RequestParam int ano, @RequestParam int mes) {
        return pagamentoService.relatorioMensal(ano, mes);
    }
}