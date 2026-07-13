package com.healthcare.api.controller;

import com.healthcare.api.dto.PacienteRequestDTO;
import com.healthcare.api.dto.PacienteResponseDTO;
import com.healthcare.api.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping
    public List<PacienteResponseDTO> listar() {
        return pacienteService.listar();
    }

    @GetMapping("/{id}")
    public PacienteResponseDTO buscarPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PacienteResponseDTO criar(@Valid @RequestBody PacienteRequestDTO dto) {
        return pacienteService.criar(dto);
    }

    @PutMapping("/{id}")
    public PacienteResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO dto) {
        return pacienteService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        pacienteService.excluir(id);
    }
}