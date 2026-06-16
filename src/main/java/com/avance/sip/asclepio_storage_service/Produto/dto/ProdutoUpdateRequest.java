package com.avance.sip.asclepio_storage_service.Produto.dto;

public record ProdutoUpdateRequest(
        String nome,
        String descricao,
        String marca,
        String imagemUrl,
        Long categoriaId
) {
}