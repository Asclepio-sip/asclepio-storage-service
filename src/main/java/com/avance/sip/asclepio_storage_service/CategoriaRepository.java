package com.avance.sip.asclepio_storage_service;

import com.avance.sip.asclepio_storage_service.Categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeCategoria(String nomeCategoria);

    List<Categoria> findByCategoriaPaiIsNull();

    List<Categoria> findByCategoriaPaiId(Long categoriaPaiId);
}