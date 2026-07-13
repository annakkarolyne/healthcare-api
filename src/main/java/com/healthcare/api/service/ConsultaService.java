package com.healthcare.api.service;

import com.healthcare.api.dto.ConsultaRequestDTO;
import com.healthcare.api.dto.ConsultaResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Medico;
import com.healthcare.api.entity.Paciente;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.MedicoRepository;
import com.healthcare.api.repository.PacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
                            PacienteRepository pacienteRepository,
                            MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public List<ConsultaResponseDTO> listar() {
        return consultaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ConsultaResponseDTO buscarPorId(Long id) {
        Consulta consulta = buscarEntidadePorId(id);
        return toResponseDTO(consulta);
    }

    public ConsultaResponseDTO agendar(ConsultaRequestDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado"));

        Medico medico = medicoRepository.findById(dto.getMedicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado"));

        boolean horarioOcupado = consultaRepository
                .existsByMedicoIdAndDataAndHoraAndStatusNot(
                        dto.getMedicoId(), dto.getData(), dto.getHora(), Consulta.Status.CANCELADA
                );

        if (horarioOcupado) {
            throw new IllegalArgumentException("Médico já possui consulta agendada nesse horário");
        }

        Consulta consulta = Consulta.builder()
                .paciente(paciente)
                .medico(medico)
                .data(dto.getData())
                .hora(dto.getHora())
                .status(Consulta.Status.AGENDADA)
                .build();

        Consulta salva = consultaRepository.save(consulta);
        return toResponseDTO(salva);
    }

    public ConsultaResponseDTO cancelar(Long id) {
        Consulta consulta = buscarEntidadePorId(id);
        consulta.setStatus(Consulta.Status.CANCELADA);
        Consulta atualizada = consultaRepository.save(consulta);
        return toResponseDTO(atualizada);
    }

    public ConsultaResponseDTO remarcar(Long id, ConsultaRequestDTO dto) {
        Consulta consulta = buscarEntidadePorId(id);

        boolean horarioOcupado = consultaRepository
                .existsByMedicoIdAndDataAndHoraAndStatusNot(
                        dto.getMedicoId(), dto.getData(), dto.getHora(), Consulta.Status.CANCELADA
                );

        if (horarioOcupado) {
            throw new IllegalArgumentException("Médico já possui consulta agendada nesse horário");
        }

        consulta.setData(dto.getData());
        consulta.setHora(dto.getHora());
        consulta.setStatus(Consulta.Status.AGENDADA);

        Consulta atualizada = consultaRepository.save(consulta);
        return toResponseDTO(atualizada);
    }

    private Consulta buscarEntidadePorId(Long id) {
        return consultaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));
    }

    private ConsultaResponseDTO toResponseDTO(Consulta c) {
        return new ConsultaResponseDTO(
                c.getId(),
                c.getPaciente().getId(),
                c.getPaciente().getNome(),
                c.getMedico().getId(),
                c.getMedico().getNome(),
                c.getData(),
                c.getHora(),
                c.getStatus(),
                c.getCreatedAt(),
                c.getUpdatedAt()
        );
    }
}