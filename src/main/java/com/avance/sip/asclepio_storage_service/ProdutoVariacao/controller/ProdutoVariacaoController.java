package com.avance.sip.asclepio_storage_service.ProdutoVariacao.controller;

import com.avance.sip.asclepio_storage_service.ProdutoVariacao.ProdutoVariacaoService;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.controller.api.ProdutoVariacaoApi;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProdutoVariacaoController implements ProdutoVariacaoApi {

    private final ProdutoVariacaoService service;

    public ProdutoVariacaoController(ProdutoVariacaoService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponse> criar(Long produtoId, ProdutoVariacaoRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(produtoId, dto));
    }

    @Override
    public ResponseEntity<List<ProdutoVariacaoResponse>> listarPorProduto(Long produtoId) {
        return ResponseEntity.ok(service.listarPorProduto(produtoId));
    }

    @Override
    public ResponseEntity<ProdutoVariacaoResponse> atualizar(Long produtoId, Long id, ProdutoVariacaoUpdateRequest dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Override
    public ResponseEntity<Void> deletar(Long produtoId, Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}