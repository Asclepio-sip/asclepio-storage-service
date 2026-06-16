package com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto;

public record ProdutoVariacaoUpdateRequest(
        String nomeVariacao,
        String codigoBarras,
        String dosagem,
        String apresentacao,
        Boolean ativo
) {
}