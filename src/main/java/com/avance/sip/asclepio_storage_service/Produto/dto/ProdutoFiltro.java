package com.avance.sip.asclepio_storage_service.Produto.dto;



public record ProdutoFiltro(
        String nome,
        String marca,
        String variacao,
        String codigoBarras,
        Long categoriaId,
        String nomeCategoria) {
}