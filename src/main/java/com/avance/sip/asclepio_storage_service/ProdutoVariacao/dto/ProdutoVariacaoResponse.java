package com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto;


import com.avance.sip.asclepio_storage_service.ProdutoVariacao.ProdutoVariacao;

public record ProdutoVariacaoResponse(
        Long id,
        Long produtoId,
        String nomeProduto,
        String nomeVariacao,
        String codigoBarras,
        String dosagem,
        String apresentacao,
        Boolean ativo
) {

    public static ProdutoVariacaoResponse fromEntity(ProdutoVariacao variacao) {
        return new ProdutoVariacaoResponse(
                variacao.getId(),
                variacao.getProduto().getId(),
                variacao.getProduto().getNome(),
                variacao.getNomeVariacao(),
                variacao.getCodigoBarras(),
                variacao.getDosagem(),
                variacao.getApresentacao(),
                variacao.getAtivo()
        );
    }
}