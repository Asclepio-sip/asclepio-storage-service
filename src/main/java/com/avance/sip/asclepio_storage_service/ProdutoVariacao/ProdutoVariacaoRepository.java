package com.avance.sip.asclepio_storage_service.ProdutoVariacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao, Long>, JpaSpecificationExecutor<ProdutoVariacao> {

    boolean existsByProduto_IdAndNomeVariacaoIgnoreCase(Long produtoId, String nomeVariacao);

    boolean existsByCodigoBarras(String codigoBarras);

    Optional<ProdutoVariacao> findByCodigoBarras(String codigoBarras);
}