package com.avance.sip.asclepio_storage_service.Produto.dto;

public record ProdutoMultipartRequest(
        String nome,
        String descricao,
        String marca,
        Long categoriaId
) {
}