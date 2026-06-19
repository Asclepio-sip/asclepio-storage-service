package com.avance.sip.asclepio_storage_service.Produto.Controller.api;

import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoFiltro;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoRequest;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoResponse;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/produtos")
@Tag(name = "Produtos")
public interface ProdutoApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerProduto')")
    @Operation(summary = "Listar produtos")
    ResponseEntity<Page<ProdutoResponse>> listar(@ParameterObject ProdutoFiltro filtro, @ParameterObject Pageable pageable);

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CriarProduto')")
    @Operation(summary = "Criar produto com imagem")
    ResponseEntity<ProdutoResponse> criar(@RequestParam String nome, @RequestParam(required = false) String descricao, @RequestParam(required = false) String marca, @RequestParam Long categoriaId, @RequestParam(required = false) MultipartFile imagem);

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarProduto')")
    @Operation(summary = "Editar produto")
    ResponseEntity<ProdutoResponse> editar(@PathVariable Long id, @RequestBody ProdutoUpdateRequest dto);

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirProduto')")
    @Operation(summary = "Deletar produto")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}