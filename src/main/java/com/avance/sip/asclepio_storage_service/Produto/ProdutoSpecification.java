package com.avance.sip.asclepio_storage_service.Produto;

import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoFiltro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProdutoSpecification {

    private ProdutoSpecification() {
    }

    public static Specification<Produto> filtrar(ProdutoFiltro filtro) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filtro == null) {
                return cb.and(predicates.toArray(Predicate[]::new));
            }

            if (filtro.nome() != null && !filtro.nome().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("nome")),
                                "%" + filtro.nome().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.marca() != null && !filtro.marca().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("marca")),
                                "%" + filtro.marca().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }

            if (filtro.categoriaId() != null) {
                predicates.add(
                        cb.equal(root.get("categoria").get("id"), filtro.categoriaId())
                );
            }

            if (filtro.nomeCategoria() != null && !filtro.nomeCategoria().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("categoria").get("nomeCategoria")),
                                "%" + filtro.nomeCategoria().trim().toLowerCase(Locale.ROOT) + "%"
                        )
                );
            }


            if (
                    filtro.variacao() != null && !filtro.variacao().isBlank()
                            || filtro.codigoBarras() != null && !filtro.codigoBarras().isBlank()
            ) {
                Join<Object, Object> variacoes = root.join("variacoes", JoinType.LEFT);

                if (filtro.variacao() != null && !filtro.variacao().isBlank()) {
                    predicates.add(
                            cb.like(
                                    cb.lower(variacoes.get("nomeVariacao")),
                                    "%" + filtro.variacao().trim().toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                if (filtro.codigoBarras() != null && !filtro.codigoBarras().isBlank()) {
                    predicates.add(
                            cb.like(
                                    cb.lower(variacoes.get("codigoBarras")),
                                    "%" + filtro.codigoBarras().trim().toLowerCase(Locale.ROOT) + "%"
                            )
                    );
                }

                query.distinct(true);
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}