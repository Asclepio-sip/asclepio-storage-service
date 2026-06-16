package com.avance.sip.asclepio_storage_service.ProdutoVariacao;

import com.avance.sip.asclepio_storage_service.Produto.Produto;
import com.avance.sip.asclepio_storage_service.Produto.ProdutoRepository;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoRequest;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoResponse;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.dto.ProdutoVariacaoUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

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
            throw new RuntimeException("Já existe uma variação com esse nome para este produto");
        }

        if (dto.codigoBarras() != null && !dto.codigoBarras().isBlank()
                && repository.existsByCodigoBarras(dto.codigoBarras().trim())) {
            throw new RuntimeException("Já existe uma variação com esse código de barras");
        }

        ProdutoVariacao variacao = new ProdutoVariacao();
        variacao.setProduto(produto);
        variacao.setNomeVariacao(dto.nomeVariacao().trim());
        variacao.setCodigoBarras(dto.codigoBarras());
        variacao.setDosagem(dto.dosagem());
        variacao.setApresentacao(dto.apresentacao());
        variacao.setAtivo(true);

        repository.save(variacao);

        return ProdutoVariacaoResponse.fromEntity(variacao);
    }

    public List<ProdutoVariacaoResponse> listarPorProduto(Long produtoId) {
        buscarProduto(produtoId);

        return repository.findByProduto_Id(produtoId)
                .stream()
                .map(ProdutoVariacaoResponse::fromEntity)
                .toList();
    }

    public ProdutoVariacaoResponse atualizar(Long id, ProdutoVariacaoUpdateRequest dto) {
        if (dto == null) {
            throw new RuntimeException("Dados da variação são obrigatórios");
        }

        ProdutoVariacao variacao = buscarVariacao(id);

        if (dto.nomeVariacao() != null && !dto.nomeVariacao().isBlank()) {
            boolean existe = repository.existsByProduto_IdAndNomeVariacaoIgnoreCase(
                    variacao.getProduto().getId(),
                    dto.nomeVariacao().trim()
            );

            if (existe && !dto.nomeVariacao().equalsIgnoreCase(variacao.getNomeVariacao())) {
                throw new RuntimeException("Já existe uma variação com esse nome");
            }

            variacao.setNomeVariacao(dto.nomeVariacao().trim());
        }

        if (dto.codigoBarras() != null && !dto.codigoBarras().isBlank()) {
            repository.findByCodigoBarras(dto.codigoBarras().trim())
                    .ifPresent(existente -> {
                        if (!existente.getId().equals(variacao.getId())) {
                            throw new RuntimeException("Já existe uma variação com esse código de barras");
                        }
                    });

            variacao.setCodigoBarras(dto.codigoBarras().trim());
        }

        if (dto.dosagem() != null) {
            variacao.setDosagem(dto.dosagem());
        }

        if (dto.apresentacao() != null) {
            variacao.setApresentacao(dto.apresentacao());
        }

        if (dto.ativo() != null) {
            variacao.setAtivo(dto.ativo());
        }

        repository.save(variacao);

        return ProdutoVariacaoResponse.fromEntity(variacao);
    }

    public void deletar(Long id) {
        ProdutoVariacao variacao = buscarVariacao(id);
        repository.delete(variacao);
    }

    private Produto buscarProduto(Long produtoId) {
        if (produtoId == null) {
            throw new RuntimeException("Produto é obrigatório");
        }

        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    private ProdutoVariacao buscarVariacao(Long id) {
        if (id == null) {
            throw new RuntimeException("ID da variação é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variação não encontrada"));
    }

    private void validarCriacao(Long produtoId, ProdutoVariacaoRequest dto) {
        if (produtoId == null) {
            throw new RuntimeException("Produto é obrigatório");
        }

        if (dto == null) {
            throw new RuntimeException("Dados da variação são obrigatórios");
        }

        if (dto.nomeVariacao() == null || dto.nomeVariacao().isBlank()) {
            throw new RuntimeException("Nome da variação é obrigatório");
        }
    }
}