package com.healthcare.api.repository;

import com.healthcare.api.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByCrm(String crm);
    boolean existsByCrm(String crm);
    Optional<Medico> findByUsuarioId(Long usuarioId);
}