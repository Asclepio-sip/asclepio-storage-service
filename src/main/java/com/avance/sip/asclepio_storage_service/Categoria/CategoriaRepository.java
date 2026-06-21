package com.avance.sip.asclepio_storage_service.Categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {

    Optional<Categoria> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Categoria> findByNomeCategoriaAndEmpresaId(String nomeCategoria, Long empresaId);

    boolean existsByNomeCategoriaAndEmpresaId(String nomeCategoria, Long empresaId);
}