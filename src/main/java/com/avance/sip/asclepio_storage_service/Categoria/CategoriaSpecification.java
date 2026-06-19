package com.avance.sip.asclepio_storage_service.Categoria;

import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaFiltro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CategoriaSpecification {

    private CategoriaSpecification() {
    }

    public static Specification<Categoria> filtrar(CategoriaFiltro filtro) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nomeCategoria")), "%" + filtro.nome().toLowerCase().trim() + "%"));
            }

            if (filtro.categoriaPaiId() != null) {
                predicates.add(cb.equal(root.get("categoriaPai").get("id"), filtro.categoriaPaiId()));
            }

            if (Boolean.TRUE.equals(filtro.somentePrincipais())) {
                predicates.add(cb.isNull(root.get("categoriaPai")));
            }

            if (filtro.ativa() != null) {
                predicates.add(cb.equal(root.get("ativa"), filtro.ativa()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}