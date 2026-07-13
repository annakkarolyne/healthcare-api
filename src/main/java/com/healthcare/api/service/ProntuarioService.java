package com.healthcare.api.service;

import com.healthcare.api.dto.ProntuarioRequestDTO;
import com.healthcare.api.dto.ProntuarioResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Medico;
import com.healthcare.api.entity.Prontuario;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.MedicoRepository;
import com.healthcare.api.repository.ProntuarioRepository;
import com.healthcare.api.security.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProntuarioService {

    private final ProntuarioRepository prontuarioRepository;
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;

    public ProntuarioService(ProntuarioRepository prontuarioRepository,
                              ConsultaRepository consultaRepository,
                              MedicoRepository medicoRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.consultaRepository = consultaRepository;
        this.medicoRepository = medicoRepository;
    }

    public List<ProntuarioResponseDTO> listar() {
        List<Prontuario> prontuarios;

        Medico medicoLogado = buscarMedicoLogadoSeHouver();

        if (medicoLogado != null) {
            // MEDICO só vê prontuários das consultas em que ele é o médico responsável
            prontuarios = prontuarioRepository.findByConsultaMedicoId(medicoLogado.getId());
        } else {
            // ADMIN vê todos
            prontuarios = prontuarioRepository.findAll();
        }

        return prontuarios.stream().map(this::toResponseDTO).toList();
    }

    public ProntuarioResponseDTO buscarPorId(Long id) {
        Prontuario prontuario = buscarEntidadePorId(id);
        validarAcessoMedico(prontuario);
        return toResponseDTO(prontuario);
    }

    public ProntuarioResponseDTO criar(ProntuarioRequestDTO dto) {
        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Consulta não encontrada"));

        if (prontuarioRepository.existsByConsultaId(dto.getConsultaId())) {
            throw new IllegalArgumentException("Já existe um prontuário para essa consulta");
        }

        Medico medicoLogado = buscarMedicoLogadoSeHouver();
        if (medicoLogado != null && !medicoLogado.getId().equals(consulta.getMedico().getId())) {
            throw new IllegalArgumentException("Médico só pode criar prontuário de suas próprias consultas");
        }

        Prontuario prontuario = Prontuario.builder()
                .consulta(consulta)
                .sintomas(dto.getSintomas())
                .diagnostico(dto.getDiagnostico())
                .prescricao(dto.getPrescricao())
                .build();

        Prontuario salvo = prontuarioRepository.save(prontuario);
        return toResponseDTO(salvo);
    }

    public ProntuarioResponseDTO atualizar(Long id, ProntuarioRequestDTO dto) {
        Prontuario prontuario = buscarEntidadePorId(id);
        validarAcessoMedico(prontuario);

        prontuario.setSintomas(dto.getSintomas());
        prontuario.setDiagnostico(dto.getDiagnostico());
        prontuario.setPrescricao(dto.getPrescricao());

        Prontuario atualizado = prontuarioRepository.save(prontuario);
        return toResponseDTO(atualizado);
    }

    private Prontuario buscarEntidadePorId(Long id) {
        return prontuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prontuário não encontrado"));
    }

    private void validarAcessoMedico(Prontuario prontuario) {
        Medico medicoLogado = buscarMedicoLogadoSeHouver();
        if (medicoLogado != null && !medicoLogado.getId().equals(prontuario.getConsulta().getMedico().getId())) {
            throw new IllegalArgumentException("Médico só pode acessar prontuários de suas próprias consultas");
        }
    }

    private Medico buscarMedicoLogadoSeHouver() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            Long usuarioId = userDetails.getUsuario().getId();
            return medicoRepository.findByUsuarioId(usuarioId).orElse(null);
        }

        return null;
    }

    private ProntuarioResponseDTO toResponseDTO(Prontuario p) {
        return new ProntuarioResponseDTO(
                p.getId(),
                p.getConsulta().getId(),
                p.getConsulta().getPaciente().getNome(),
                p.getConsulta().getMedico().getNome(),
                p.getSintomas(),
                p.getDiagnostico(),
                p.getPrescricao(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}