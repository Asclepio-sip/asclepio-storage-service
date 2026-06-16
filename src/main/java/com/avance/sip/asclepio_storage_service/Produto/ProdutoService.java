package com.avance.sip.asclepio_storage_service.Produto;

import com.avance.sip.asclepio_storage_service.Categoria.Categoria;
import com.avance.sip.asclepio_storage_service.CategoriaRepository;
import com.avance.sip.asclepio_storage_service.Produto.Enum.StatusProduto;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoFiltro;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoRequest;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoResponse;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.avance.sip.asclepio_storage_service.storage.service.StorageService;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final StorageService storageService;

    public ProdutoService(
            ProdutoRepository repository,
            CategoriaRepository categoriaRepository,
            StorageService storageService
    ) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.storageService = storageService;
    }

    public Produto criar(ProdutoRequest dto) {

        validarCriacao(dto);

        if (repository.existsByNomeIgnoreCase(dto.nome().trim())) {
            throw new RuntimeException("Produto já existe");
        }

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setMarca(dto.marca());
        produto.setImagemUrl(dto.imagemUrl());
        produto.setCategoria(categoria);
        return repository.save(produto);
    }

    public Page<ProdutoResponse> listarTodos(
            ProdutoFiltro filtro,
            Pageable pageable
    ) {
        return repository
                .findAll(ProdutoSpecification.filtrar(filtro), pageable)
                .map(ProdutoResponse::fromEntity);
    }

    public Produto editar(Long id, ProdutoUpdateRequest dto) {

        if (dto == null) {
            throw new RuntimeException("Dados para edição do produto são obrigatórios");
        }

        Produto produto = buscarPorId(id);

        if (dto.nome() != null && !dto.nome().isBlank()) {
            produto.setNome(dto.nome().trim());
        }

        if (dto.descricao() != null) {
            produto.setDescricao(dto.descricao());
        }

        if (dto.marca() != null) {
            produto.setMarca(dto.marca());
        }

        if (dto.imagemUrl() != null) {
            produto.setImagemUrl(dto.imagemUrl());
        }

        if (dto.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            produto.setCategoria(categoria);
        }

        return repository.save(produto);
    }


    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        repository.delete(produto);
    }

    private Produto buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID do produto é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    private void validarCriacao(ProdutoRequest dto) {
        if (dto == null) {
            throw new RuntimeException("Dados do produto são obrigatórios");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new RuntimeException("Nome do produto é obrigatório");
        }

        if (dto.categoriaId() == null) {
            throw new RuntimeException("Categoria obrigatória");
        }
    }


    public Produto criarComImagem(
            String nome,
            String descricao,
            String marca,
            Long categoriaId,
            MultipartFile imagem
    ) {
        if (nome == null || nome.isBlank()) {
            throw new RuntimeException("Nome do produto é obrigatório");
        }

        if (categoriaId == null) {
            throw new RuntimeException("Categoria é obrigatória");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        String imagemUrl = null;

        if (imagem != null && !imagem.isEmpty()) {
            try {
                imagemUrl = storageService.upload(imagem);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao enviar imagem do produto", e);
            }
        }
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        produto.setMarca(marca);
        produto.setCategoria(categoria);
        produto.setImagemUrl(imagemUrl);

        return repository.save(produto);
    }
}