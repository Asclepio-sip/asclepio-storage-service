package com.avance.sip.asclepio_storage_service.Categoria.Controller.api;


import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaFiltro;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaRequest;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/categorias")
@Tag(name = "Categorias")
public interface CategoriaApi {

    @GetMapping
    @PreAuthorize("hasAuthority('VerCategoria')")
    @Operation(summary = "Listar categorias com filtros")
    ResponseEntity<Page<CategoriaResponse>> listar(
            @ParameterObject CategoriaFiltro filtro,
            @ParameterObject Pageable pageable
    );

    @PostMapping
    @PreAuthorize("hasAuthority('CriarCategoria')")
    ResponseEntity<CategoriaResponse> criar(
            @RequestBody CategoriaRequest dto
    );

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EditarCategoria')")
    ResponseEntity<CategoriaResponse> editar(
            @PathVariable Long id,
            @RequestBody CategoriaRequest dto
    );

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ExcluirCategoria')")
    ResponseEntity<Void> deletar(
            @PathVariable Long id
    );
}