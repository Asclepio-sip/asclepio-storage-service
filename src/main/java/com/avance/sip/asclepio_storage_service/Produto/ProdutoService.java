package com.avance.sip.asclepio_storage_service.Produto;

import com.avance.sip.asclepio_storage_service.Categoria.Categoria;
import com.avance.sip.asclepio_storage_service.Categoria.CategoriaRepository;
import com.avance.sip.asclepio_storage_service.Config.EmpresaContext;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Consumer;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final StorageService storageService;
    private final EmpresaContext empresaContext;

    public ProdutoService(
            ProdutoRepository repository,
            CategoriaRepository categoriaRepository,
            StorageService storageService,
            EmpresaContext empresaContext
    ) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
        this.storageService = storageService;
        this.empresaContext = empresaContext;
    }

    public Produto criar(ProdutoRequest dto) {

        validarCriacao(dto);

        Long empresaId = empresaContext.getEmpresaId();
        String nomeTratado = dto.nome().trim();

        if (repository.existsByNomeIgnoreCaseAndEmpresaId(nomeTratado, empresaId)) {
            throw new BadRequestException("Produto já existe nessa empresa");
        }

        Categoria categoria = buscarCategoriaPorId(dto.categoriaId());

        Produto produto = new Produto();
        produto.setNome(nomeTratado);
        produto.setDescricao(dto.descricao());
        produto.setMarca(dto.marca());
        produto.setImagemUrl(dto.imagemUrl());
        produto.setCategoria(categoria);
        produto.setEmpresaId(empresaId);

        return repository.save(produto);
    }

    public Page<ProdutoResponse> listarTodos(ProdutoFiltro filtro, Pageable pageable) {

        Long empresaId = empresaContext.getEmpresaId();

        return repository
                .findAll(ProdutoSpecification.filtrar(filtro, empresaId), pageable)
                .map(ProdutoResponse::fromEntity);
    }

    @Transactional
    public Produto editar(Long id, ProdutoUpdateRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados para edição do produto são obrigatórios");
        }

        Produto produto = buscarPorId(id);
        Long empresaId = empresaContext.getEmpresaId();

        if (deveAtualizarTexto(dto.nome())) {
            String novoNome = dto.nome().trim();

            repository.findByNomeIgnoreCaseAndEmpresaId(novoNome, empresaId)
                    .ifPresent(existente -> {
                        if (!existente.getId().equals(produto.getId())) {
                            throw new BadRequestException("Produto já existe nessa empresa");
                        }
                    });

            produto.setNome(novoNome);
        }

        atualizarTexto(dto.descricao(), produto::setDescricao);
        atualizarTexto(dto.marca(), produto::setMarca);
        atualizarTexto(dto.imagemUrl(), produto::setImagemUrl);

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

        if (categoriaId == null) {
            throw new BadRequestException("Categoria é obrigatória");
        }

        Long empresaId = empresaContext.getEmpresaId();
        String nomeTratado = nome.trim();

        if (repository.existsByNomeIgnoreCaseAndEmpresaId(nomeTratado, empresaId)) {
            throw new BadRequestException("Produto já existe nessa empresa");
        }

        Categoria categoria = buscarCategoriaPorId(categoriaId);

        String imagemUrl = null;

        if (imagem != null && !imagem.isEmpty()) {
            try {
                imagemUrl = storageService.upload(imagem);
            } catch (Exception e) {
                throw new BadRequestException("Erro ao enviar imagem do produto: " + e.getMessage());
            }
        }

        Produto produto = new Produto();
        produto.setNome(nomeTratado);
        produto.setDescricao(descricao);
        produto.setMarca(marca);
        produto.setCategoria(categoria);
        produto.setImagemUrl(imagemUrl);
        produto.setEmpresaId(empresaId);

        return repository.save(produto);
    }

    public Produto buscarPorId(Long id) {

        if (id == null) {
            throw new BadRequestException("ID do produto é obrigatório");
        }

        return repository.findByIdAndEmpresaId(id, empresaContext.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + id));
    }

    private Categoria buscarCategoriaPorId(Long categoriaId) {

        if (categoriaId == null) {
            throw new BadRequestException("Categoria é obrigatória");
        }

        return categoriaRepository
                .findByIdAndEmpresaId(categoriaId, empresaContext.getEmpresaId())
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

    private void atualizarTexto(String valor, Consumer<String> setter) {

        if (!deveAtualizarTexto(valor)) {
            return;
        }

        setter.accept(valor.trim());
    }

    private boolean deveAtualizarTexto(String valor) {

        if (valor == null) {
            return false;
        }

        String valorTratado = valor.trim();

        return !valorTratado.isBlank()
                && !valorTratado.equalsIgnoreCase("string");
    }
}