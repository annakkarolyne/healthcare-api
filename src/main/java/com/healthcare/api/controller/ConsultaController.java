package com.healthcare.api.controller;

import com.healthcare.api.dto.ConsultaRequestDTO;
import com.healthcare.api.dto.ConsultaResponseDTO;
import com.healthcare.api.service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public List<ConsultaResponseDTO> listar() {
        return consultaService.listar();
    }

    @GetMapping("/{id}")
    public ConsultaResponseDTO buscarPorId(@PathVariable Long id) {
        return consultaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponseDTO agendar(@Valid @RequestBody ConsultaRequestDTO dto) {
        return consultaService.agendar(dto);
    }

    @PutMapping("/{id}/cancelar")
    public ConsultaResponseDTO cancelar(@PathVariable Long id) {
        return consultaService.cancelar(id);
    }

    @PutMapping("/{id}/remarcar")
    public ConsultaResponseDTO remarcar(@PathVariable Long id, @Valid @RequestBody ConsultaRequestDTO dto) {
        return consultaService.remarcar(id, dto);
    }
}