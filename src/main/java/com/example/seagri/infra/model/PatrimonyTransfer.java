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
 * Registro append-only de transferência de bem patrimonial.
 * Guarda quem enviou, quem recebeu, de onde saiu e para onde foi.
 * Todos os campos são updatable = false para garantir a imutabilidade.
 */
@Entity
@Table(name = "PATRIMONY_TRANSFERS")
public class PatrimonyTransfer implements Hashable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false, length = 20)
    private String tombo;

    @Column(nullable = false, updatable = false, length = 120)
    private String remetenteNome;

    @Column(nullable = false, updatable = false, length = 120)
    private String remetenteUnidade;

    @Column(updatable = false, precision = 10, scale = 6)
    private BigDecimal remetenteLat;

    @Column(updatable = false, precision = 10, scale = 6)
    private BigDecimal remetenteLng;

    @Column(nullable = false, updatable = false, length = 120)
    private String destinatarioNome;

    @Column(nullable = false, updatable = false, length = 120)
    private String destinatarioUnidade;

    @Column(updatable = false, precision = 10, scale = 6)
    private BigDecimal destinatarioLat;

    @Column(updatable = false, precision = 10, scale = 6)
    private BigDecimal destinatarioLng;

    @Column(updatable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataTransferencia;

    @Column(nullable = false, updatable = false, columnDefinition = "CHAR(64)")
    private String integrityHash;

    public PatrimonyTransfer() {}

    public PatrimonyTransfer(String tombo,
                             String remetenteNome, String remetenteUnidade,
                             BigDecimal remetenteLat, BigDecimal remetenteLng,
                             String destinatarioNome, String destinatarioUnidade,
                             BigDecimal destinatarioLat, BigDecimal destinatarioLng,
                             String motivo, LocalDateTime dataTransferencia,
                             String integrityHash) {
        this.tombo = tombo;
        this.remetenteNome = remetenteNome;
        this.remetenteUnidade = remetenteUnidade;
        this.remetenteLat = remetenteLat;
        this.remetenteLng = remetenteLng;
        this.destinatarioNome = destinatarioNome;
        this.destinatarioUnidade = destinatarioUnidade;
        this.destinatarioLat = destinatarioLat;
        this.destinatarioLng = destinatarioLng;
        this.motivo = motivo;
        this.dataTransferencia = dataTransferencia;
        this.integrityHash = integrityHash;
    }

    @Override
    public String toHashString() {
        return String.join("|",
                tombo != null ? tombo : "",
                remetenteNome != null ? remetenteNome : "",
                remetenteUnidade != null ? remetenteUnidade : "",
                destinatarioNome != null ? destinatarioNome : "",
                destinatarioUnidade != null ? destinatarioUnidade : "",
                motivo != null ? motivo : "",
                dataTransferencia != null ? dataTransferencia.toString() : "");
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public String getTombo() { return tombo; }
    public String getRemetenteNome() { return remetenteNome; }
    public String getRemetenteUnidade() { return remetenteUnidade; }
    public BigDecimal getRemetenteLat() { return remetenteLat; }
    public BigDecimal getRemetenteLng() { return remetenteLng; }
    public String getDestinatarioNome() { return destinatarioNome; }
    public String getDestinatarioUnidade() { return destinatarioUnidade; }
    public BigDecimal getDestinatarioLat() { return destinatarioLat; }
    public BigDecimal getDestinatarioLng() { return destinatarioLng; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getDataTransferencia() { return dataTransferencia; }
    public String getIntegrityHash() { return integrityHash; }

    public void setIntegrityHash(String integrityHash) { this.integrityHash = integrityHash; }
}
