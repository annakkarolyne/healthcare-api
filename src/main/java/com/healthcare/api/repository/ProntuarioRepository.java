package com.healthcare.api.repository;

import com.healthcare.api.entity.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {

    Optional<Prontuario> findByConsultaId(Long consultaId);

    boolean existsByConsultaId(Long consultaId);

    List<Prontuario> findByConsultaMedicoId(Long medicoId);
}