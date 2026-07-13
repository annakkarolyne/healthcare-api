package com.healthcare.api.service;

import com.healthcare.api.dto.MedicoRequestDTO;
import com.healthcare.api.dto.MedicoResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Medico;
import com.healthcare.api.entity.Usuario;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.MedicoRepository;
import com.healthcare.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;
    private final UsuarioRepository usuarioRepository;

    public MedicoService(MedicoRepository medicoRepository, ConsultaRepository consultaRepository, UsuarioRepository usuarioRepository) {
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<MedicoResponseDTO> listar() {
        return medicoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public MedicoResponseDTO buscarPorId(Long id) {
        Medico medico = buscarEntidadePorId(id);
        return toResponseDTO(medico);
    }

    public MedicoResponseDTO criar(MedicoRequestDTO dto) {
        if (medicoRepository.existsByCrm(dto.getCrm())) {
            throw new IllegalArgumentException("Já existe um médico cadastrado com esse CRM");
        }

        Medico medico = Medico.builder()
                .nome(dto.getNome())
                .crm(dto.getCrm())
                .especialidade(dto.getEspecialidade())
                .telefone(dto.getTelefone())
                .ativo(true)
                .build();

        Medico salvo = medicoRepository.save(medico);
        return toResponseDTO(salvo);
    }

    public MedicoResponseDTO atualizar(Long id, MedicoRequestDTO dto) {
        Medico medico = buscarEntidadePorId(id);

        medico.setNome(dto.getNome());
        medico.setEspecialidade(dto.getEspecialidade());
        medico.setTelefone(dto.getTelefone());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
            medico.setUsuario(usuario);
        }

        Medico atualizado = medicoRepository.save(medico);
        return toResponseDTO(atualizado);
    }

    public void excluir(Long id) {
        Medico medico = buscarEntidadePorId(id);

        boolean temConsultaFutura = consultaRepository
                .existsByMedicoIdAndDataGreaterThanEqualAndStatusNot(
                        id, LocalDate.now(), Consulta.Status.CANCELADA
                );

        if (temConsultaFutura) {
            throw new IllegalArgumentException("Não é possível excluir médico com consulta futura agendada");
        }

        medico.setAtivo(false);
        medicoRepository.save(medico);
    }

    private Medico buscarEntidadePorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Médico não encontrado"));
    }

    private MedicoResponseDTO toResponseDTO(Medico m) {
        return new MedicoResponseDTO(
                m.getId(), m.getNome(), m.getCrm(), m.getEspecialidade(),
                m.getTelefone(), m.getAtivo(), m.getCreatedAt(), m.getUpdatedAt()
        );
    }
}