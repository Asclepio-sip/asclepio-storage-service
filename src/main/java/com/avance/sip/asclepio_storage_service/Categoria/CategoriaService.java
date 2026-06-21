package com.avance.sip.asclepio_storage_service.Categoria;

import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaFiltro;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaRequest;
import com.avance.sip.asclepio_storage_service.Categoria.dto.CategoriaResponse;
import com.avance.sip.asclepio_storage_service.Config.EmpresaContext;
import com.avance.sip.asclepio_storage_service.exception.BadRequestException;
import com.avance.sip.asclepio_storage_service.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final EmpresaContext  empresaContext;

    public CategoriaService(
            CategoriaRepository repository,
            EmpresaContext empresaContext
    ) {
        this.repository = repository;
        this.empresaContext = empresaContext;
    }

    public Page<CategoriaResponse> listar(CategoriaFiltro filtro, Pageable pageable) {

        Long empresaId = empresaContext.getEmpresaId();

        return repository
                .findAll(CategoriaSpecification.filtrar(filtro, empresaId), pageable)
                .map(CategoriaResponse::fromEntity);
    }

    public Categoria criar(CategoriaRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados da categoria são obrigatórios");
        }

        validarNome(dto.nomeCategoria());

        Long empresaId = empresaContext.getEmpresaId();
        String nomeTratado = dto.nomeCategoria().trim();

        if (repository.existsByNomeCategoriaAndEmpresaId(nomeTratado, empresaId)) {
            throw new BadRequestException("Categoria já existe nessa empresa");
        }

        Categoria categoriaPai = null;

        if (dto.categoriaPaiId() != null) {
            categoriaPai = buscarPorId(dto.categoriaPaiId());

            if (!categoriaPai.getEmpresaId().equals(empresaId)) {
                throw new BadRequestException("Categoria pai não pertence à empresa logada");
            }
        }

        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(nomeTratado);
        categoria.setDescricao(dto.descricao());
        categoria.setIcone(dto.icone());
        categoria.setCategoriaPai(categoriaPai);
        categoria.setAtiva(true);
        categoria.setEmpresaId(empresaId);

        return repository.save(categoria);
    }

    public Categoria editar(Long id, CategoriaRequest dto) {

        if (dto == null) {
            throw new BadRequestException("Dados da categoria são obrigatórios");
        }

        Categoria categoria = buscarPorId(id);

        Long empresaId = empresaContext.getEmpresaId();

        if (deveAtualizarTexto(dto.nomeCategoria())) {

            String novoNome = dto.nomeCategoria().trim();

            repository.findByNomeCategoriaAndEmpresaId(novoNome, empresaId)
                    .ifPresent(existente -> {
                        if (!existente.getId().equals(categoria.getId())) {
                            throw new BadRequestException("Categoria já existe nessa empresa");
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

            if (!categoriaPai.getEmpresaId().equals(empresaId)) {
                throw new BadRequestException("Categoria pai não pertence à empresa logada");
            }

            categoria.setCategoriaPai(categoriaPai);
        }

        return repository.save(categoria);
    }

    public void deletar(Long id) {

        Categoria categoria = buscarPorId(id);

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

        return repository.findByIdAndEmpresaId(id, empresaContext.getEmpresaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada com id: " + id));
    }

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new BadRequestException("Nome da categoria é obrigatório");
        }
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