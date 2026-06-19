package com.avance.sip.asclepio_storage_service.Produto;

import com.avance.sip.asclepio_storage_service.Categoria.Categoria;
import com.avance.sip.asclepio_storage_service.Categoria.CategoriaRepository;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoFiltro;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoRequest;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoResponse;
import com.avance.sip.asclepio_storage_service.Produto.dto.ProdutoUpdateRequest;
import com.avance.sip.asclepio_storage_service.exception.BadRequestException;
import com.avance.sip.asclepio_storage_service.exception.NotFoundException;
import com.avance.sip.asclepio_storage_service.storage.service.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
            throw new BadRequestException("Produto já existe");
        }

        Categoria categoria = buscarCategoriaPorId(dto.categoriaId());

        Produto produto = new Produto();
        produto.setNome(dto.nome().trim());
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
            throw new BadRequestException("Dados para edição do produto são obrigatórios");
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
            Categoria categoria = buscarCategoriaPorId(dto.categoriaId());
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
            throw new BadRequestException("ID do produto é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + id));
    }

    private Categoria buscarCategoriaPorId(Long categoriaId) {

        if (categoriaId == null) {
            throw new BadRequestException("Categoria é obrigatória");
        }

        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com id: " + categoriaId));
    }

    private void validarCriacao(ProdutoRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados do produto são obrigatórios");
        }

        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new BadRequestException("Nome do produto é obrigatório");
        }

        if (dto.categoriaId() == null) {
            throw new BadRequestException("Categoria é obrigatória");
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
            throw new BadRequestException("Nome do produto é obrigatório");
        }

        Categoria categoria = buscarCategoriaPorId(categoriaId);

        String imagemUrl = null;

        if (imagem != null && !imagem.isEmpty()) {
            try {
                imagemUrl = storageService.upload(imagem);
            } catch (Exception e) {
                throw new BadRequestException("Erro ao enviar imagem do produto");
            }
        }

        Produto produto = new Produto();
        produto.setNome(nome.trim());
        produto.setDescricao(descricao);
        produto.setMarca(marca);
        produto.setCategoria(categoria);
        produto.setImagemUrl(imagemUrl);

        return repository.save(produto);
    }
}