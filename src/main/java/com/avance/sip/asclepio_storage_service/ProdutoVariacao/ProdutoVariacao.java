package com.avance.sip.asclepio_storage_service.ProdutoVariacao;
import com.avance.sip.asclepio_storage_service.Produto.Produto;
import jakarta.persistence.*;

@Entity
@Table(
        name = "TB_PRODUTO_VARIACAO",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_PRODUTO_VARIACAO_NOME",
                        columnNames = {"PROV_PRODUTO_ID", "PROV_NOME"}
                ),
                @UniqueConstraint(
                        name = "UK_PRODUTO_VARIACAO_CODIGO_BARRAS",
                        columnNames = "PROV_CODIGO_BARRAS"
                )
        },
        indexes = {
                @Index(name = "IDX_PRODUTO_VARIACAO_PRODUTO", columnList = "PROV_PRODUTO_ID"),
                @Index(name = "IDX_PRODUTO_VARIACAO_CODIGO_BARRAS", columnList = "PROV_CODIGO_BARRAS")
        }
)
public class ProdutoVariacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROV_ID")
    private Long id;

    @Column(name = "PROV_NOME", nullable = false, length = 150)
    private String nomeVariacao;

    @Column(name = "PROV_CODIGO_BARRAS", length = 100, unique = true)
    private String codigoBarras;

    @Column(name = "PROV_DOSAGEM", length = 100)
    private String dosagem;

    @Column(name = "PROV_APRESENTACAO", length = 150)
    private String apresentacao;

    @Column(name = "PROV_ATIVO", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROV_PRODUTO_ID", nullable = false)
    private Produto produto;

    public ProdutoVariacao() {
    }

    public void alterarNome(String nomeVariacao) {
        validarNome(nomeVariacao);
        this.nomeVariacao = nomeVariacao.trim();
    }

    public void alterarCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    private void validarNome(String nomeVariacao) {
        if (nomeVariacao == null || nomeVariacao.isBlank()) {
            throw new IllegalArgumentException("Nome da variação é obrigatório");
        }

        if (nomeVariacao.length() > 150) {
            throw new IllegalArgumentException("Nome da variação deve ter no máximo 150 caracteres");
        }
    }

    // getters e setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeVariacao() {
        return nomeVariacao;
    }

    public void setNomeVariacao(String nomeVariacao) {
        this.nomeVariacao = nomeVariacao;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDosagem() {
        return dosagem;
    }

    public void setDosagem(String dosagem) {
        this.dosagem = dosagem;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}