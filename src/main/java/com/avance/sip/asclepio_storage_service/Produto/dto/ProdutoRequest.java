package com.avance.sip.asclepio_storage_service.Produto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoRequest(
        @NotBlank
        String nome,
        String descricao,
        String marca,
        String imagemUrl,
        @NotNull
        Long categoriaId
) {
}