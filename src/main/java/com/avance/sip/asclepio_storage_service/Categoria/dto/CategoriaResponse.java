package com.avance.sip.asclepio_storage_service.Categoria.dto;


import com.avance.sip.asclepio_storage_service.Categoria.Categoria;

public record CategoriaResponse(
        Long id,
        String nomeCategoria,
        String descricao,
        String icone,
        Boolean ativa,
        Long categoriaPaiId,
        String nomeCategoriaPai
) {

    public static CategoriaResponse fromEntity(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNomeCategoria(),
                categoria.getDescricao(),
                categoria.getIcone(),
                categoria.getAtiva(),
                categoria.getCategoriaPai() != null ? categoria.getCategoriaPai().getId() : null,
                categoria.getCategoriaPai() != null ? categoria.getCategoriaPai().getNomeCategoria() : null
        );
    }
}