package com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto;

public record ProdutoVariacaoFiltro(
        Long id,
        Long produtoId,
        String nomeProduto,
        String nomeVariacao,
        String codigoBarras,
        Boolean ativo
) {
}