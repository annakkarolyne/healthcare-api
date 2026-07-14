package com.healthcare.api.service;

import com.healthcare.api.dto.PacienteRequestDTO;
import com.healthcare.api.dto.PacienteResponseDTO;
import com.healthcare.api.entity.Paciente;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<PacienteResponseDTO> listar() {
        return pacienteRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PacienteResponseDTO buscarPorId(Long id) {
        Paciente paciente = buscarEntidadePorId(id);
        return toResponseDTO(paciente);
    }

    public PacienteResponseDTO criar(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Já existe um paciente cadastrado com esse CPF");
        }

        validarResponsavelSeMenor(dto);

        Paciente paciente = Paciente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .nascimento(dto.getNascimento())
                .telefone(dto.getTelefone())
                .endereco(dto.getEndereco())
                .responsavelNome(dto.getResponsavelNome())
                .responsavelCpf(dto.getResponsavelCpf())
                .responsavelTelefone(dto.getResponsavelTelefone())
                .ativo(true)
                .build();

        Paciente salvo = pacienteRepository.save(paciente);
        return toResponseDTO(salvo);
    }

    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        Paciente paciente = buscarEntidadePorId(id);

        validarResponsavelSeMenor(dto);

        paciente.setNome(dto.getNome());
        paciente.setNascimento(dto.getNascimento());
        paciente.setTelefone(dto.getTelefone());
        paciente.setEndereco(dto.getEndereco());
        paciente.setResponsavelNome(dto.getResponsavelNome());
        paciente.setResponsavelCpf(dto.getResponsavelCpf());
        paciente.setResponsavelTelefone(dto.getResponsavelTelefone());

        Paciente atualizado = pacienteRepository.save(paciente);
        return toResponseDTO(atualizado);
    }

    public void excluir(Long id) {
        Paciente paciente = buscarEntidadePorId(id);
        paciente.setAtivo(false);
        pacienteRepository.save(paciente);
    }

    private void validarResponsavelSeMenor(PacienteRequestDTO dto) {
        if (dto.getNascimento() == null) {
            throw new IllegalArgumentException("Data de nascimento é obrigatória");
        }

        boolean menorDeIdade = dto.getNascimento().plusYears(18).isAfter(java.time.LocalDate.now());
        if (menorDeIdade && (dto.getResponsavelNome() == null || dto.getResponsavelNome().isBlank())) {
            throw new IllegalArgumentException("Paciente menor de idade precisa de responsável");
        }
    }

    private Paciente buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));
    }

    private PacienteResponseDTO toResponseDTO(Paciente p) {
        return new PacienteResponseDTO(
                p.getId(), p.getNome(), p.getCpf(), p.getNascimento(),
                p.getTelefone(), p.getEndereco(),
                p.getResponsavelNome(), p.getResponsavelCpf(), p.getResponsavelTelefone(),
                p.getAtivo(), p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}