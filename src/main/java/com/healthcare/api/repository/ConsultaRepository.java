package com.healthcare.api.repository;

import com.healthcare.api.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByMedicoIdAndDataAndHoraAndStatusNot(
            Long medicoId, LocalDate data, LocalTime hora, Consulta.Status status);

    boolean existsByMedicoIdAndDataAndHoraAndStatusNotAndIdNot(
            Long medicoId, LocalDate data, LocalTime hora, Consulta.Status status, Long id);

    boolean existsByMedicoIdAndDataGreaterThanEqualAndStatusNot(
            Long medicoId, LocalDate data, Consulta.Status status);

    List<Consulta> findByPacienteId(Long pacienteId);

    List<Consulta> findByMedicoId(Long medicoId);

    List<Consulta> findByData(LocalDate data);
}