package com.avance.sip.asclepio_storage_service.Categoria.Controller;

import com.avance.sip.asclepio_storage_service.Categoria.CategoriaService;
import com.avance.sip.asclepio_storage_service.Categoria.Controller.api.CategoriaApi;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaRequest;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoriaController implements CategoriaApi {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(
                categoriaService.listarTodas()
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> listarPrincipais() {
        return ResponseEntity.ok(
                categoriaService.listarCategoriasPrincipais()
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<List<CategoriaResponse>> listarSubcategorias(Long categoriaPaiId) {
        return ResponseEntity.ok(
                categoriaService.listarSubcategorias(categoriaPaiId)
                        .stream()
                        .map(CategoriaResponse::fromEntity)
                        .toList()
        );
    }

    @Override
    public ResponseEntity<CategoriaResponse> criar(CategoriaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CategoriaResponse.fromEntity(categoriaService.criar(dto))
        );
    }

    @Override
    public ResponseEntity<CategoriaResponse> editar(Long id, CategoriaRequest dto) {
        return ResponseEntity.ok(
                CategoriaResponse.fromEntity(categoriaService.editar(id, dto))
        );
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}