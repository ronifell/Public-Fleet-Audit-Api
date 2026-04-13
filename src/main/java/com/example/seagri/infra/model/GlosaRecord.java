package com.example.seagri.infra.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Tabela Append-Only de Fé Pública — cada linha representa o resultado
 * da análise do Motor de Glosa para uma transação de abastecimento.
 *
 * Nenhum campo é atualizável após a inserção (updatable = false).
 * O hash SHA-256 garante a imutabilidade do registro.
 */
@Entity
@Table(name = "GLOSA_RECORDS")
public class GlosaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    // ── Dados da transação (vindos da operadora AP 03) ──────────────────────

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(30)")
    private String transacaoId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transacaoTimestamp;

    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(8)")
    private String placa;

    @Column(updatable = false, columnDefinition = "VARCHAR(20)")
    private String postoCnpj;

    @Column(updatable = false, precision = 10, scale = 6)
    private BigDecimal postoLat;

    @Column(updatable = false, precision = 10, scale = 6)
    private BigDecimal postoLng;

    @Column(updatable = false, columnDefinition = "VARCHAR(35)")
    private String combustivel;

    @Column(nullable = false, updatable = false, precision = 8, scale = 2)
    private BigDecimal volumeLitros;

    @Column(updatable = false)
    private Integer odomteroInformado;

    @Column(updatable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    // ── Resultado da análise ────────────────────────────────────────────────

    /**
     * Status final: APROVADO | GLOSA_AUTOMATICA | GLOSA_VOLUMETRICA |
     *               AUDITORIA_MANUAL | AUDITORIA_INFERENCIA
     */
    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(25)")
    private String glosaStatus;

    /** Descrição de todas as regras violadas, separadas por " | " */
    @Column(updatable = false, columnDefinition = "TEXT")
    private String observacao;

    // ── Imutabilidade (Fé Pública) ──────────────────────────────────────────

    /** SHA-256 de: transacaoId | placa | timestamp | glosaStatus */
    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(64)")
    private String integrityHash;

    @Column(nullable = false, updatable = false)
    private LocalDateTime processedAt;

    @Column(updatable = false, columnDefinition = "VARCHAR(100)")
    private String processedBy;

    // ── Construtores ────────────────────────────────────────────────────────

    public GlosaRecord() {}

    public GlosaRecord(String transacaoId, LocalDateTime transacaoTimestamp, String placa,
                       String postoCnpj, BigDecimal postoLat, BigDecimal postoLng,
                       String combustivel, BigDecimal volumeLitros, Integer odomteroInformado,
                       BigDecimal valorTotal, String glosaStatus, String observacao,
                       String integrityHash, LocalDateTime processedAt, String processedBy) {
        this.transacaoId = transacaoId;
        this.transacaoTimestamp = transacaoTimestamp;
        this.placa = placa;
        this.postoCnpj = postoCnpj;
        this.postoLat = postoLat;
        this.postoLng = postoLng;
        this.combustivel = combustivel;
        this.volumeLitros = volumeLitros;
        this.odomteroInformado = odomteroInformado;
        this.valorTotal = valorTotal;
        this.glosaStatus = glosaStatus;
        this.observacao = observacao;
        this.integrityHash = integrityHash;
        this.processedAt = processedAt;
        this.processedBy = processedBy;
    }

    // ── Getters ─────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public String getTransacaoId() { return transacaoId; }
    public LocalDateTime getTransacaoTimestamp() { return transacaoTimestamp; }
    public String getPlaca() { return placa; }
    public String getPostoCnpj() { return postoCnpj; }
    public BigDecimal getPostoLat() { return postoLat; }
    public BigDecimal getPostoLng() { return postoLng; }
    public String getCombustivel() { return combustivel; }
    public BigDecimal getVolumeLitros() { return volumeLitros; }
    public Integer getOdomteroInformado() { return odomteroInformado; }
    public BigDecimal getValorTotal() { return valorTotal; }
    public String getGlosaStatus() { return glosaStatus; }
    public String getObservacao() { return observacao; }
    public String getIntegrityHash() { return integrityHash; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public String getProcessedBy() { return processedBy; }
}
