package com.healthcare.api.controller;

import com.healthcare.api.dto.MedicoRequestDTO;
import com.healthcare.api.dto.MedicoResponseDTO;
import com.healthcare.api.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public List<MedicoResponseDTO> listar() {
        return medicoService.listar();
    }

    @GetMapping("/{id}")
    public MedicoResponseDTO buscarPorId(@PathVariable Long id) {
        return medicoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicoResponseDTO criar(@Valid @RequestBody MedicoRequestDTO dto) {
        return medicoService.criar(dto);
    }

    @PutMapping("/{id}")
    public MedicoResponseDTO atualizar(@PathVariable Long id, @Valid @RequestBody MedicoRequestDTO dto) {
        return medicoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        medicoService.excluir(id);
    }
}