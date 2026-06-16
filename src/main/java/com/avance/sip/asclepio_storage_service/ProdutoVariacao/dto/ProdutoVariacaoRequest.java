package com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto;

public record ProdutoVariacaoRequest(
        String nomeVariacao,
        String codigoBarras,
        String dosagem,
        String apresentacao
) {
}