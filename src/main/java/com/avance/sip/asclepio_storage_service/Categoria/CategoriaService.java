package com.avance.sip.asclepio_storage_service.Categoria;

import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaFiltro;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaRequest;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaResponse;
import com.avance.sip.asclepio_storage_service.exception.BadRequestException;
import com.avance.sip.asclepio_storage_service.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;

    private static final Set<String> CATEGORIAS_PROTEGIDAS = Set.of(
            "Medicamentos",
            "Beleza",
            "Higiene",
            "Infantil",
            "Vitaminas",
            "Promoções"
    );

    public CategoriaService(CategoriaRepository repository) {
        this.repository = repository;
    }

    public Page<CategoriaResponse> listar(
            CategoriaFiltro filtro,
            Pageable pageable
    ) {
        return repository
                .findAll(CategoriaSpecification.filtrar(filtro), pageable)
                .map(CategoriaResponse::fromEntity);
    }

    public Categoria criar(CategoriaRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados da categoria são obrigatórios");
        }

        validarNome(dto.nomeCategoria());

        repository.findByNomeCategoria(dto.nomeCategoria().trim()).ifPresent(c -> {
            throw new BadRequestException("Categoria já existe");
        });

        Categoria categoriaPai = null;

        if (dto.categoriaPaiId() != null) {
            categoriaPai = buscarPorId(dto.categoriaPaiId());
        }

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.nomeCategoria().trim());
        categoria.setDescricao(dto.descricao());
        categoria.setIcone(dto.icone());
        categoria.setCategoriaPai(categoriaPai);
        categoria.setAtiva(true);

        return repository.save(categoria);
    }

    public Categoria editar(Long id, CategoriaRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados da categoria são obrigatórios");
        }

        Categoria categoria = buscarPorId(id);

        validarCategoriaProtegida(categoria);

        if (deveAtualizarTexto(dto.nomeCategoria())) {

            String novoNome = dto.nomeCategoria().trim();

            repository.findByNomeCategoria(novoNome).ifPresent(existente -> {
                if (!existente.getId().equals(categoria.getId())) {
                    throw new BadRequestException("Categoria já existe");
                }
            });

            categoria.setNomeCategoria(novoNome);
        }

        if (deveAtualizarTexto(dto.descricao())) {
            categoria.setDescricao(dto.descricao().trim());
        }

        if (deveAtualizarTexto(dto.icone())) {
            categoria.setIcone(dto.icone().trim());
        }

        if (dto.categoriaPaiId() != null) {

            Categoria categoriaPai = buscarPorId(dto.categoriaPaiId());

            if (categoriaPai.getId().equals(categoria.getId())) {
                throw new BadRequestException("A categoria não pode ser pai dela mesma");
            }

            categoria.setCategoriaPai(categoriaPai);
        }

        return repository.save(categoria);
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

        Categoria categoria = buscarPorId(id);

        validarCategoriaProtegida(categoria);

        if (!categoria.getSubcategorias().isEmpty()) {
            throw new BadRequestException("Categoria possui subcategorias vinculadas");
        }

        try {
            repository.delete(categoria);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Categoria possui produtos vinculados");
        }
    }

    private Categoria buscarPorId(Long id) {

        if (id == null) {
            throw new BadRequestException("ID da categoria é obrigatório");
        }

        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com id: " + id));
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new BadRequestException("Nome da categoria é obrigatório");
        }
    }

    private void validarCategoriaProtegida(Categoria categoria) {

        if (CATEGORIAS_PROTEGIDAS.contains(categoria.getNomeCategoria())) {
            throw new BadRequestException("Essa categoria é fundamental e não pode ser alterada");
        }
    }
}