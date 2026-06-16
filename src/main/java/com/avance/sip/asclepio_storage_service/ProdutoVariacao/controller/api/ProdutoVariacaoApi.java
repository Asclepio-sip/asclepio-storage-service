package com.avance.sip.asclepio_storage_service.ProdutoVariacao.controller.api;

import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/produtos/{produtoId}/variacoes")
@Tag(name = "Produto Variações")
public interface ProdutoVariacaoApi {

    @PostMapping
    ResponseEntity<ProdutoVariacaoResponse> criar(@PathVariable Long produtoId, @RequestBody ProdutoVariacaoRequest dto);

    @GetMapping
    ResponseEntity<List<ProdutoVariacaoResponse>> listarPorProduto(@PathVariable Long produtoId);

    @PutMapping("/{id}")
    ResponseEntity<ProdutoVariacaoResponse> atualizar(@PathVariable Long produtoId, @PathVariable Long id, @RequestBody ProdutoVariacaoUpdateRequest dto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletar(@PathVariable Long produtoId, @PathVariable Long id);
}