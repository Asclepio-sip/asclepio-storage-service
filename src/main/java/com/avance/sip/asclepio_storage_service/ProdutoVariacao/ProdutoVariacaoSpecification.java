package com.avance.sip.asclepio_storage_service.ProdutoVariacao;

import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoFiltro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProdutoVariacaoSpecification {

    private ProdutoVariacaoSpecification() {
    }

    public static Specification<ProdutoVariacao> filtrar(ProdutoVariacaoFiltro filtro) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.id() != null) {
                predicates.add(cb.equal(root.get("id"), filtro.id()));
            }


            if (filtro.produtoId() != null) {
                predicates.add(cb.equal(root.get("produto").get("id"), filtro.produtoId()));
            }

            if (filtro.nomeProduto() != null && !filtro.nomeProduto().isBlank()) {
                Join<Object, Object> produtoJoin = root.join("produto", JoinType.LEFT);

                predicates.add(cb.like(cb.lower(produtoJoin.get("nome")), "%" + filtro.nomeProduto().toLowerCase().trim() + "%"));
            }

            if (filtro.nomeVariacao() != null && !filtro.nomeVariacao().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nomeVariacao")), "%" + filtro.nomeVariacao().toLowerCase().trim() + "%"));
            }

            if (filtro.codigoBarras() != null && !filtro.codigoBarras().isBlank()) {
                predicates.add(cb.like(root.get("codigoBarras"), "%" + filtro.codigoBarras().trim() + "%"));
            }

            if (filtro.ativo() != null) {
                predicates.add(cb.equal(root.get("ativo"), filtro.ativo()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}