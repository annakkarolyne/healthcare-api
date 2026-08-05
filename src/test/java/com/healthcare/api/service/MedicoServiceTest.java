package com.healthcare.api.service;

import com.healthcare.api.dto.MedicoRequestDTO;
import com.healthcare.api.dto.MedicoResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Medico;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.MedicoRepository;
import com.healthcare.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicoServiceTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private MedicoService medicoService;

    private Medico medico;

    @BeforeEach
    void setUp() {
        medico = Medico.builder()
                .id(1L)
                .nome("Dra. Ana Souza")
                .crm("123456")
                .especialidade("Cardiologia")
                .telefone("11988887777")
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private MedicoRequestDTO criarDto() {
        MedicoRequestDTO dto = new MedicoRequestDTO();
        dto.setNome("Dra. Ana Souza");
        dto.setCrm("123456");
        dto.setEspecialidade("Cardiologia");
        dto.setTelefone("11988887777");
        return dto;
    }

    @Test
    void deveCriarMedicoComSucesso() {
        MedicoRequestDTO dto = criarDto();

        when(medicoRepository.existsByCrm(dto.getCrm())).thenReturn(false);
        when(medicoRepository.save(any(Medico.class))).thenReturn(medico);

        MedicoResponseDTO response = medicoService.criar(dto);

        assertThat(response.getNome()).isEqualTo("Dra. Ana Souza");
        assertThat(response.getCrm()).isEqualTo("123456");
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    void deveLancarExcecaoAoCriarMedicoComCrmJaExistente() {
        MedicoRequestDTO dto = criarDto();

        when(medicoRepository.existsByCrm(dto.getCrm())).thenReturn(true);

        assertThatThrownBy(() -> medicoService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Já existe um médico cadastrado com esse CRM");

        verify(medicoRepository, never()).save(any(Medico.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarMedicoInexistente() {
        when(medicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicoService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Médico não encontrado");
    }

    @Test
    void deveExcluirMedicoComSucessoQuandoNaoTemConsultaFutura() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataGreaterThanEqualAndStatusNot(
                1L, LocalDate.now(), Consulta.Status.CANCELADA))
                .thenReturn(false);

        medicoService.excluir(1L);

        assertThat(medico.getAtivo()).isFalse();
        verify(medicoRepository).save(medico);
    }

    @Test
    void deveLancarExcecaoAoExcluirMedicoComConsultaFutura() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataGreaterThanEqualAndStatusNot(
                1L, LocalDate.now(), Consulta.Status.CANCELADA))
                .thenReturn(true);

        assertThatThrownBy(() -> medicoService.excluir(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Não é possível excluir médico com consulta futura agendada");

        verify(medicoRepository, never()).save(any(Medico.class));
    }
}