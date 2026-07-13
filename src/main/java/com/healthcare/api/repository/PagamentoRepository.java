package com.healthcare.api.repository;

import com.healthcare.api.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    List<Pagamento> findByConsultaId(Long consultaId);
}