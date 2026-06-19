package com.avance.sip.asclepio_storage_service.Produto.dto;

import com.avance.sip.asclepio_storage_service.Produto.Produto;

import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        String marca,
        String imagemUrl, Long categoriaId,
        String categoriaNome,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getMarca(),
                produto.getImagemUrl(),
                produto.getCategoria() != null ? produto.getCategoria().getId() : null,
                produto.getCategoria() != null ? produto.getCategoria().getNomeCategoria() : null,
                produto.getCriadoEm(),
                produto.getAtualizadoEm()
        );
    }
}