package com.avance.sip.asclepio_storage_service.Categoria.dto;

public record CategoriaFiltro(
        String nome,
        Long categoriaPaiId,
        Boolean somentePrincipais,
        Boolean ativa
) {
}