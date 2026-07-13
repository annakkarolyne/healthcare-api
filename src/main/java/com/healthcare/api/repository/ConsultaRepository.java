package com.healthcare.api.repository;

import com.healthcare.api.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByMedicoIdAndDataGreaterThanEqualAndStatusNot(
            Long medicoId, LocalDate data, Consulta.Status status
    );

    boolean existsByMedicoIdAndDataAndHoraAndStatusNot(
            Long medicoId, LocalDate data, LocalTime hora, Consulta.Status status
    );
}

