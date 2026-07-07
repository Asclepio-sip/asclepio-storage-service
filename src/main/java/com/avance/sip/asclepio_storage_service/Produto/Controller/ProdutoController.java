package com.avance.sip.asclepio_storage_service.Produto.Controller;

import com.avance.sip.asclepio_storage_service.Produto.Controller.api.ProdutoApi;
import com.avance.sip.asclepio_storage_service.Produto.Produto;
import com.avance.sip.asclepio_storage_service.Produto.ProdutoService;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoFiltro;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoRequest;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoResponse;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ProdutoController implements ProdutoApi {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public ResponseEntity<Page<ProdutoResponse>> listar(ProdutoFiltro filtro, Pageable pageable) {
        return ResponseEntity.ok(produtoService.listarTodos(filtro, pageable));
    }

    @Override
    public ResponseEntity<ProdutoResponse> criar(String nome, String descricao, String marca, Long categoriaId, MultipartFile imagem) {
        Produto produto = produtoService.criarComImagem(nome, descricao, marca, categoriaId, imagem);

        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponse.fromEntity(produto));
    }

    @Override
    public ResponseEntity<ProdutoResponse> editar(Long id, ProdutoUpdateRequest dto) {
        Produto produto = produtoService.editar(id, dto);
        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }

    @Override
    public ResponseEntity<Void> deletar(Long id) {
        produtoService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ProdutoResponse> buscarPorId(Long id) {

        Produto produto = produtoService.buscarPorId(id);

        return ResponseEntity.ok(ProdutoResponse.fromEntity(produto));
    }
}