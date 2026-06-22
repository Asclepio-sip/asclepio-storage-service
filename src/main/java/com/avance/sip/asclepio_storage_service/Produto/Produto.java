package com.avance.sip.asclepio_storage_service.Produto;
import com.avance.sip.asclepio_storage_service.Categoria.Categoria;
import com.avance.sip.asclepio_storage_service.ProdutoVariacao.ProdutoVariacao;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "TB_PRODUTO",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_PRODUTO_EMPRESA_NOME",
                        columnNames = {"PRO_EMPRESA_ID", "PRO_NOME"}
                )
        }
)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRO_ID")
    private Long id;

    @Column(name = "PRO_NOME", nullable = false, length = 150)
    private String nome;

    @Column(name = "PRO_DESCRICAO", length = 3000)
    private String descricao;

    @Column(name = "PRO_MARCA", length = 100)
    private String marca;

    @Column(name = "PRO_IMAGEM_URL", length = 500)
    private String imagemUrl;

    @Column(name = "PRO_CRIADO_EM", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "PRO_ATUALIZADO_EM")
    private LocalDateTime atualizadoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PRO_CATEGORIA_ID", nullable = false)
    private Categoria categoria;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProdutoVariacao> variacoes = new ArrayList<>();

    @Column(name = "PRO_EMPRESA_ID", nullable = false)
    private Long empresaId;

    public Produto() {
    }

    public void alterarNome(String nome) {
        validarNome(nome);
        this.nome = nome.trim();
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarImagem(String imagemUrl) {
        this.imagemUrl = imagemUrl;
        this.atualizadoEm = LocalDateTime.now();
    }


    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (nome.length() > 150) {
            throw new IllegalArgumentException("Nome do produto deve ter no máximo 150 caracteres");
        }
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<ProdutoVariacao> getVariacoes() {
        return variacoes;
    }

    public void setVariacoes(List<ProdutoVariacao> variacoes) {
        this.variacoes = variacoes;
    }
}