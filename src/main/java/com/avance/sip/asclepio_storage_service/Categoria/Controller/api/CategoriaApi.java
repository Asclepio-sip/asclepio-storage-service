package com.avance.sip.asclepio_storage_service.Categoria.Controller.api;


import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaRequest;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/categorias")
@Tag(name = "Categorias")
public interface CategoriaApi {

    @GetMapping
    @Operation(summary = "Listar todas as categorias")
    ResponseEntity<List<CategoriaResponse>> listar();

    @GetMapping("/principais")
    @Operation(summary = "Listar categorias principais")
    ResponseEntity<List<CategoriaResponse>> listarPrincipais();

    @GetMapping("/{categoriaPaiId}/subcategorias")
    @Operation(summary = "Listar subcategorias de uma categoria")
    ResponseEntity<List<CategoriaResponse>> listarSubcategorias(@PathVariable Long categoriaPaiId);

    @PostMapping
    @Operation(summary = "Criar categoria ou subcategoria")
    ResponseEntity<CategoriaResponse> criar(@RequestBody CategoriaRequest dto);

    @PutMapping("/{id}")
    @Operation(summary = "Editar categoria")
    ResponseEntity<CategoriaResponse> editar(@PathVariable Long id, @RequestBody CategoriaRequest dto);

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar categoria")
    ResponseEntity<Void> deletar(@PathVariable Long id);
}