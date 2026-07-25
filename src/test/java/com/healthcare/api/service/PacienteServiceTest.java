package com.healthcare.api.service;

import com.healthcare.api.dto.PacienteRequestDTO;
import com.healthcare.api.dto.PacienteResponseDTO;
import com.healthcare.api.entity.Paciente;
import com.healthcare.api.exception.RecursoNaoEncontradoException;
import com.healthcare.api.repository.PacienteRepository;
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
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678900")
                .nascimento(LocalDate.now().minusYears(30))
                .telefone("11988887777")
                .endereco("Rua das Flores, 123")
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private PacienteRequestDTO criarDtoAdulto() {
        PacienteRequestDTO dto = new PacienteRequestDTO();
        dto.setNome("João Silva");
        dto.setCpf("12345678900");
        dto.setNascimento(LocalDate.now().minusYears(30));
        dto.setTelefone("11988887777");
        dto.setEndereco("Rua das Flores, 123");
        return dto;
    }

    @Test
    void deveCriarPacienteAdultoComSucesso() {
        PacienteRequestDTO dto = criarDtoAdulto();

        when(pacienteRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);

        PacienteResponseDTO response = pacienteService.criar(dto);

        assertThat(response.getNome()).isEqualTo("João Silva");
        assertThat(response.getCpf()).isEqualTo("12345678900");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoAoCriarPacienteComCpfJaExistente() {
        PacienteRequestDTO dto = criarDtoAdulto();

        when(pacienteRepository.existsByCpf(dto.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> pacienteService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Já existe um paciente cadastrado com esse CPF");

        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoAoCriarPacienteMenorDeIdadeSemResponsavel() {
        PacienteRequestDTO dto = criarDtoAdulto();
        dto.setNascimento(LocalDate.now().minusYears(10));
        dto.setResponsavelNome(null);

        when(pacienteRepository.existsByCpf(dto.getCpf())).thenReturn(false);

        assertThatThrownBy(() -> pacienteService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Paciente menor de idade precisa de responsável");

        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void deveCriarPacienteMenorDeIdadeComResponsavelComSucesso() {
        PacienteRequestDTO dto = criarDtoAdulto();
        dto.setNascimento(LocalDate.now().minusYears(10));
        dto.setResponsavelNome("Maria Silva");
        dto.setResponsavelCpf("98765432100");
        dto.setResponsavelTelefone("11977776666");

        Paciente menorSalvo = Paciente.builder()
                .id(2L)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .nascimento(dto.getNascimento())
                .responsavelNome(dto.getResponsavelNome())
                .ativo(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(pacienteRepository.existsByCpf(dto.getCpf())).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(menorSalvo);

        PacienteResponseDTO response = pacienteService.criar(dto);

        assertThat(response.getResponsavelNome()).isEqualTo("Maria Silva");
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoAoCriarPacienteComNascimentoNulo() {
        PacienteRequestDTO dto = criarDtoAdulto();
        dto.setNascimento(null);

        when(pacienteRepository.existsByCpf(dto.getCpf())).thenReturn(false);

        assertThatThrownBy(() -> pacienteService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Data de nascimento é obrigatória");

        verify(pacienteRepository, never()).save(any(Paciente.class));
    }

    @Test
    void deveLancarExcecaoAoBuscarPacienteInexistente() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pacienteService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Paciente não encontrado");
    }

    @Test
    void deveExcluirPacienteComSucesso() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        pacienteService.excluir(1L);

        assertThat(paciente.getAtivo()).isFalse();
        verify(pacienteRepository).save(paciente);
    }
}