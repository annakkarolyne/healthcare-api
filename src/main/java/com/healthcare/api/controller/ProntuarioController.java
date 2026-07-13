package com.healthcare.api.controller;

import com.healthcare.api.dto.ProntuarioRequestDTO;
import com.healthcare.api.dto.ProntuarioResponseDTO;
import com.healthcare.api.service.ProntuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prontuarios")
public class ProntuarioController {

    private final ProntuarioService prontuarioService;

    public ProntuarioController(ProntuarioService prontuarioService) {
        this.prontuarioService = prontuarioService;
    }

    @GetMapping
    public List<ProntuarioResponseDTO> listar() {
        return prontuarioService.listar();
    }

    @GetMapping("/{id}")
    public ProntuarioResponseDTO buscarPorId(@PathVariable Long id) {
        return prontuarioService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProntuarioResponseDTO criar(@Valid @RequestBody ProntuarioRequestDTO dto) {
        return prontuarioService.criar(dto);
    }

    @PutMapping("/{id}")
    public ProntuarioResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody ProntuarioRequestDTO dto) {
        return prontuarioService.atualizar(id, dto);
    }
}