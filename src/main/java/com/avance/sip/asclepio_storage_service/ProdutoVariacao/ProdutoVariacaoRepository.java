package com.avance.sip.asclepio_storage_service.ProdutoVariacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoVariacaoRepository extends JpaRepository<ProdutoVariacao, Long> {

    List<ProdutoVariacao> findByProduto_Id(Long produtoId);

    boolean existsByProduto_IdAndNomeVariacaoIgnoreCase(Long produtoId, String nomeVariacao);

    boolean existsByCodigoBarras(String codigoBarras);

    Optional<ProdutoVariacao> findByCodigoBarras(String codigoBarras);
}