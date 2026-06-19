package com.avance.sip.asclepio_storage_service.ProdutoVariacao;

import com.avance.sip.asclepio_storage_service.Produto.Produto;
import com.avance.sip.asclepio_storage_service.Produto.ProdutoRepository;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoFiltro;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoUpdateRequest;
import com.avance.sip.asclepio_storage_service.exception.BadRequestException;
import com.avance.sip.asclepio_storage_service.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProdutoVariacaoService {

    private final ProdutoVariacaoRepository repository;
    private final ProdutoRepository produtoRepository;

    public ProdutoVariacaoService(
            ProdutoVariacaoRepository repository,
            ProdutoRepository produtoRepository
    ) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    public ProdutoVariacaoResponse criar(Long produtoId, ProdutoVariacaoRequest dto) {

        validarCriacao(produtoId, dto);

        Produto produto = buscarProduto(produtoId);

        if (repository.existsByProduto_IdAndNomeVariacaoIgnoreCase(produtoId, dto.nomeVariacao().trim())) {
            throw new BadRequestException("Já existe uma variação com esse nome para este produto");
        }

        if (dto.codigoBarras() != null
                && !dto.codigoBarras().isBlank()
                && repository.existsByCodigoBarras(dto.codigoBarras().trim())) {
            throw new BadRequestException("Já existe uma variação com esse código de barras");
        }

        ProdutoVariacao variacao = new ProdutoVariacao();
        variacao.setProduto(produto);
        variacao.setNomeVariacao(dto.nomeVariacao().trim());

        if (dto.codigoBarras() != null && !dto.codigoBarras().isBlank()) {
            variacao.setCodigoBarras(dto.codigoBarras().trim());
        }

        variacao.setAtivo(true);

        repository.save(variacao);

        return ProdutoVariacaoResponse.fromEntity(variacao);
    }

    public Page<ProdutoVariacaoResponse> listar(
            ProdutoVariacaoFiltro filtro,
            Pageable pageable
    ) {
        return repository
                .findAll(ProdutoVariacaoSpecification.filtrar(filtro), pageable)
                .map(ProdutoVariacaoResponse::fromEntity);
    }

    public ProdutoVariacaoResponse atualizar(Long id, ProdutoVariacaoUpdateRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados da variação são obrigatórios");
        }

        ProdutoVariacao variacao = buscarVariacao(id);

        if (deveAtualizarTexto(dto.nomeVariacao())) {

            String novoNome = dto.nomeVariacao().trim();

            boolean existe = repository.existsByProduto_IdAndNomeVariacaoIgnoreCase(
                    variacao.getProduto().getId(),
                    novoNome
            );

            if (existe && !novoNome.equalsIgnoreCase(variacao.getNomeVariacao())) {
                throw new BadRequestException("Já existe uma variação com esse nome para este produto");
            }

            variacao.setNomeVariacao(novoNome);
        }

        if (deveAtualizarTexto(dto.codigoBarras())) {

            String novoCodigoBarras = dto.codigoBarras().trim();

            repository.findByCodigoBarras(novoCodigoBarras).ifPresent(existente -> {
                if (!existente.getId().equals(variacao.getId())) {
                    throw new BadRequestException("Já existe uma variação com esse código de barras");
                }
            });

            variacao.setCodigoBarras(novoCodigoBarras);
        }

        if (dto.ativo() != null) {
            variacao.setAtivo(dto.ativo());
        }

        repository.save(variacao);

        return ProdutoVariacaoResponse.fromEntity(variacao);
    }

    private boolean deveAtualizarTexto(String valor) {
        if (valor == null) {
            return false;
        }

        String valorTratado = valor.trim();

        if (valorTratado.isBlank()) {
            return false;
        }

        return !valorTratado.equalsIgnoreCase("string");
    }

    public void deletar(Long id) {
        ProdutoVariacao variacao = buscarVariacao(id);
        repository.delete(variacao);
    }

    private Produto buscarProduto(Long produtoId) {

        if (produtoId == null) {
            throw new BadRequestException("Produto é obrigatório");
        }

        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + produtoId));
    }

    private ProdutoVariacao buscarVariacao(Long id) {

        if (id == null) {
            throw new BadRequestException("ID da variação é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Variação não encontrada com id: " + id));
    }

    private void validarCriacao(
            Long produtoId,
            ProdutoVariacaoRequest dto
    ) {

        if (produtoId == null) {
            throw new BadRequestException("Produto é obrigatório");
        }

        if (dto == null) {
            throw new BadRequestException("Dados da variação são obrigatórios");
        }

        if (dto.nomeVariacao() == null || dto.nomeVariacao().isBlank()) {
            throw new BadRequestException("Nome da variação é obrigatório");
        }
    }
}