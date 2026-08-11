package com.avance.sip.asclepio_storage_service.ProdutoVariacao.controller.api;

import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoFiltro;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/variacoes")
@Tag(name = "Produto Variações")
public interface ProdutoVariacaoApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerProduto') or hasAuthority('CriarPedido')")
    ResponseEntity<Page<ProdutoVariacaoResponse>> listar(
            @ParameterObject ProdutoVariacaoFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @PostMapping("/produtos/{produtoId}")
    @PreAuthorize("hasAuthority('CriarProduto')")
    ResponseEntity<ProdutoVariacaoResponse> criar(
            @PathVariable Long produtoId,
            @RequestBody ProdutoVariacaoRequest dto
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarProduto')")
    ResponseEntity<ProdutoVariacaoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody ProdutoVariacaoUpdateRequest dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirProduto')")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}