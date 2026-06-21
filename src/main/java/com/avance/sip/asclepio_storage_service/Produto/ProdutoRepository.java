package com.avance.sip.asclepio_storage_service.Produto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long>,
        JpaSpecificationExecutor<Produto> {

    Optional<Produto> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Produto> findByNomeIgnoreCaseAndEmpresaId(String nome, Long empresaId);

    boolean existsByNomeIgnoreCaseAndEmpresaId(String nome, Long empresaId);
}