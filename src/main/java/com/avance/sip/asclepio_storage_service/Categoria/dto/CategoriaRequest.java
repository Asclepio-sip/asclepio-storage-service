package com.avance.sip.asclepio_storage_service.Categoria.dto;

public record CategoriaRequest(
        String nomeCategoria,
        String descricao,
        String icone,
        Long categoriaPaiId
) {
}