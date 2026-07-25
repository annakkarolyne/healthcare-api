package com.healthcare.api.service;

import com.healthcare.api.dto.ConsultaRequestDTO;
import com.healthcare.api.dto.ConsultaResponseDTO;
import com.healthcare.api.entity.Consulta;
import com.healthcare.api.entity.Medico;
import com.healthcare.api.entity.Paciente;
import com.healthcare.api.exception.HorarioIndisponivelException;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.ConsultaRepository;
import com.healthcare.api.repository.MedicoRepository;
import com.healthcare.api.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @InjectMocks
    private ConsultaService consultaService;

    private Paciente paciente;
    private Medico medico;
    private Consulta consulta;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .nome("João Silva")
                .build();

        medico = Medico.builder()
                .id(1L)
                .nome("Dra. Ana Souza")
                .build();

        consulta = Consulta.builder()
                .id(1L)
                .paciente(paciente)
                .medico(medico)
                .data(LocalDate.now().plusDays(1))
                .hora(LocalTime.of(10, 0))
                .status(Consulta.Status.AGENDADA)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private ConsultaRequestDTO criarDto() {
        ConsultaRequestDTO dto = new ConsultaRequestDTO();
        dto.setPacienteId(1L);
        dto.setMedicoId(1L);
        dto.setData(LocalDate.now().plusDays(1));
        dto.setHora(LocalTime.of(10, 0));
        return dto;
    }

    @Test
    void deveAgendarConsultaComSucesso() {
        ConsultaRequestDTO dto = criarDto();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataAndHoraAndStatusNot(
                dto.getMedicoId(), dto.getData(), dto.getHora(), Consulta.Status.CANCELADA))
                .thenReturn(false);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseDTO response = consultaService.agendar(dto);

        assertThat(response.getPacienteNome()).isEqualTo("João Silva");
        assertThat(response.getMedicoNome()).isEqualTo("Dra. Ana Souza");
        assertThat(response.getStatus()).isEqualTo(Consulta.Status.AGENDADA);
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoAoAgendarComHorarioOcupado() {
        ConsultaRequestDTO dto = criarDto();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataAndHoraAndStatusNot(
                dto.getMedicoId(), dto.getData(), dto.getHora(), Consulta.Status.CANCELADA))
                .thenReturn(true);

        assertThatThrownBy(() -> consultaService.agendar(dto))
                .isInstanceOf(HorarioIndisponivelException.class)
                .hasMessage("Médico já possui consulta agendada nesse horário");

        verify(consultaRepository, never()).save(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoAoAgendarComPacienteInexistente() {
        ConsultaRequestDTO dto = criarDto();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.agendar(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Paciente não encontrado");

        verify(consultaRepository, never()).save(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoAoAgendarComMedicoInexistente() {
        ConsultaRequestDTO dto = criarDto();

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.agendar(dto))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Médico não encontrado");

        verify(consultaRepository, never()).save(any(Consulta.class));
    }

    @Test
    void deveCancelarConsultaComSucesso() {
        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseDTO response = consultaService.cancelar(1L);

        assertThat(consulta.getStatus()).isEqualTo(Consulta.Status.CANCELADA);
        verify(consultaRepository).save(consulta);
    }

    @Test
    void deveLancarExcecaoAoCancelarConsultaInexistente() {
        when(consultaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultaService.cancelar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Consulta não encontrada");
    }

    @Test
    void deveRemarcarConsultaComSucesso() {
        ConsultaRequestDTO dto = criarDto();
        dto.setData(LocalDate.now().plusDays(2));
        dto.setHora(LocalTime.of(14, 0));

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.existsByMedicoIdAndDataAndHoraAndStatusNotAndIdNot(
                dto.getMedicoId(), dto.getData(), dto.getHora(), Consulta.Status.CANCELADA, 1L))
                .thenReturn(false);
        when(consultaRepository.save(any(Consulta.class))).thenReturn(consulta);

        ConsultaResponseDTO response = consultaService.remarcar(1L, dto);

        assertThat(consulta.getData()).isEqualTo(dto.getData());
        assertThat(consulta.getHora()).isEqualTo(dto.getHora());
        assertThat(consulta.getStatus()).isEqualTo(Consulta.Status.AGENDADA);
        verify(consultaRepository).save(consulta);
    }

    @Test
    void deveLancarExcecaoAoRemarcarParaHorarioOcupadoPorOutraConsulta() {
        ConsultaRequestDTO dto = criarDto();
        dto.setData(LocalDate.now().plusDays(2));
        dto.setHora(LocalTime.of(14, 0));

        when(consultaRepository.findById(1L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.existsByMedicoIdAndDataAndHoraAndStatusNotAndIdNot(
                dto.getMedicoId(), dto.getData(), dto.getHora(), Consulta.Status.CANCELADA, 1L))
                .thenReturn(true);

        assertThatThrownBy(() -> consultaService.remarcar(1L, dto))
                .isInstanceOf(HorarioIndisponivelException.class)
                .hasMessage("Médico já possui consulta agendada nesse horário");

        verify(consultaRepository, never()).save(any(Consulta.class));
    }
}