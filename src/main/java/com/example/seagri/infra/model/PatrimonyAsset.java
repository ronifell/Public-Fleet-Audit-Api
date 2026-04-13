package com.example.seagri.infra.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.seagri.infra.integrity.Hashable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Bem patrimonial sob custódia do Estado.
 * Cada ativo recebe um número de tombo único e um hash SHA-256 de integridade.
 */
@Entity
@Table(name = "PATRIMONY_ASSETS")
public class PatrimonyAsset implements Hashable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false, length = 20)
    private String tombo;

    @Column(nullable = false, length = 200)
    private String descricao;

    @Column(length = 60)
    private String categoria;

    @Column(precision = 10, scale = 6)
    private BigDecimal lat;

    @Column(precision = 10, scale = 6)
    private BigDecimal lng;

    @Column(precision = 5, scale = 2)
    private BigDecimal conservacaoPercent;

    @Column(length = 120)
    private String responsavel;

    /** ATIVO | EM_REVISAO | BAIXA | TRANSFERIDO */
    @Column(nullable = false, length = 20)
    private String situacao;

    @Column(precision = 12, scale = 2)
    private BigDecimal valorPatrimonial;

    /** Path/URL of the attached photo or document (required for "baixa"). */
    @Column(length = 500)
    private String documentoAnexo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataRegistro;

    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(64)")
    private String integrityHash;

    public PatrimonyAsset() {}

    public PatrimonyAsset(String tombo, String descricao, String categoria,
                          BigDecimal lat, BigDecimal lng, BigDecimal conservacaoPercent,
                          String responsavel, String situacao, BigDecimal valorPatrimonial,
                          String documentoAnexo, LocalDateTime dataRegistro, String integrityHash) {
        this.tombo = tombo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.lat = lat;
        this.lng = lng;
        this.conservacaoPercent = conservacaoPercent;
        this.responsavel = responsavel;
        this.situacao = situacao;
        this.valorPatrimonial = valorPatrimonial;
        this.documentoAnexo = documentoAnexo;
        this.dataRegistro = dataRegistro;
        this.integrityHash = integrityHash;
    }

    @Override
    public String toHashString() {
        return String.join("|",
                tombo != null ? tombo : "",
                descricao != null ? descricao : "",
                categoria != null ? categoria : "",
                lat != null ? lat.toPlainString() : "",
                lng != null ? lng.toPlainString() : "",
                responsavel != null ? responsavel : "",
                situacao != null ? situacao : "",
                valorPatrimonial != null ? valorPatrimonial.toPlainString() : "",
                dataRegistro != null ? dataRegistro.toString() : "");
    }

    // ── Getters & Setters ────────────────────────────────────────────────────

    public Long getId() { return id; }
    public String getTombo() { return tombo; }
    public String getDescricao() { return descricao; }
    public String getCategoria() { return categoria; }
    public BigDecimal getLat() { return lat; }
    public BigDecimal getLng() { return lng; }
    public BigDecimal getConservacaoPercent() { return conservacaoPercent; }
    public String getResponsavel() { return responsavel; }
    public String getSituacao() { return situacao; }
    public BigDecimal getValorPatrimonial() { return valorPatrimonial; }
    public String getDocumentoAnexo() { return documentoAnexo; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public String getIntegrityHash() { return integrityHash; }

    public void setId(Long id) { this.id = id; }
    public void setTombo(String tombo) { this.tombo = tombo; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setLat(BigDecimal lat) { this.lat = lat; }
    public void setLng(BigDecimal lng) { this.lng = lng; }
    public void setConservacaoPercent(BigDecimal conservacaoPercent) { this.conservacaoPercent = conservacaoPercent; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public void setValorPatrimonial(BigDecimal valorPatrimonial) { this.valorPatrimonial = valorPatrimonial; }
    public void setDocumentoAnexo(String documentoAnexo) { this.documentoAnexo = documentoAnexo; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
    public void setIntegrityHash(String integrityHash) { this.integrityHash = integrityHash; }
}
