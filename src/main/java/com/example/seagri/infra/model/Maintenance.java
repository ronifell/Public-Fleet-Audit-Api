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

@Entity
@Table(name = "MAINTENANCES")
public class Maintenance extends BaseEntity implements Hashable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "CHAR(64)", updatable = false)
    private String integrityHash;

    @Column(nullable = false, unique = true)
    private Long CodOS;

    @Column(nullable = false, updatable = false)
    private LocalDateTime DataOS;

    @Column
    private LocalDateTime DataAprovacaoOS;
    
    @Column
    private LocalDateTime DataFinalizacaoOS;
    
    @Column
    private LocalDateTime DataEntregaVeiculo;
    
    @Column
    private LocalDateTime DataOrcamentoOS;
    
    @Column
    private LocalDateTime DataRejeitaOS;
    
    @Column
    private LocalDateTime DataCancelamentoOS;

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(45)")
    private String Marca;

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(45)")
    private String Modelo;

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(8)")
    private String Placa;

    @Column(columnDefinition = "CHAR(2)")
    private String Prefixo;

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(255)")
    private String Cidade;

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(255)")
    private String Base;

    @Column(nullable = false, updatable = false, columnDefinition = "VARCHAR(255)")
    private String Subunidade;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String Status;

    @Column(nullable = false, columnDefinition = "VARCHAR(80)")
    private String Posto;

    @Column(nullable = false, columnDefinition = "CHAR(3)")
    private String OptanteSimplesNacional;

    @Column(nullable = false, columnDefinition = "DECIMAL(7, 2)")
    private BigDecimal ValorPecas;

    @Column(nullable = false, columnDefinition = "DECIMAL(7, 2)")
    private BigDecimal ValorMaoDeObra;

    @Column(nullable = false, columnDefinition = "DECIMAL(8, 2)")
    private BigDecimal ValorTotal;

    @Column
    private String SituacaoOS;

    @Column
    private String StatusSituacaoOS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCodOS() {
        return CodOS;
    }

    public void setCodOS(Long codOS) {
        CodOS = codOS;
    }

    public LocalDateTime getDataOS() {
        return DataOS;
    }

    public void setDataOS(LocalDateTime dataOS) {
        DataOS = dataOS;
    }

    public LocalDateTime getDataAprovacaoOS() {
        return DataAprovacaoOS;
    }

    public void setDataAprovacaoOS(LocalDateTime dataAprovacaoOS) {
        DataAprovacaoOS = dataAprovacaoOS;
    }

    public LocalDateTime getDataFinalizacaoOS() {
        return DataFinalizacaoOS;
    }

    public void setDataFinalizacaoOS(LocalDateTime dataFinalizacaoOS) {
        DataFinalizacaoOS = dataFinalizacaoOS;
    }

    public LocalDateTime getDataEntregaVeiculo() {
        return DataEntregaVeiculo;
    }

    public void setDataEntregaVeiculo(LocalDateTime dataEntregaVeiculo) {
        DataEntregaVeiculo = dataEntregaVeiculo;
    }

    public LocalDateTime getDataOrcamentoOS() {
        return DataOrcamentoOS;
    }

    public void setDataOrcamentoOS(LocalDateTime dataOrcamentoOS) {
        DataOrcamentoOS = dataOrcamentoOS;
    }

    public LocalDateTime getDataRejeitaOS() {
        return DataRejeitaOS;
    }

    public void setDataRejeitaOS(LocalDateTime dataRejeitaOS) {
        DataRejeitaOS = dataRejeitaOS;
    }

    public LocalDateTime getDataCancelamentoOS() {
        return DataCancelamentoOS;
    }

    public void setDataCancelamentoOS(LocalDateTime dataCancelamentoOS) {
        DataCancelamentoOS = dataCancelamentoOS;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getModelo() {
        return Modelo;
    }

    public void setModelo(String modelo) {
        Modelo = modelo;
    }

    public String getPlaca() {
        return Placa;
    }

    public void setPlaca(String placa) {
        Placa = placa;
    }

    public String getPrefixo() {
        return Prefixo;
    }

    public void setPrefixo(String prefixo) {
        Prefixo = prefixo;
    }

    public String getCidade() {
        return Cidade;
    }

    public void setCidade(String cidade) {
        Cidade = cidade;
    }

    public String getBase() {
        return Base;
    }

    public void setBase(String base) {
        Base = base;
    }

    public String getSubunidade() {
        return Subunidade;
    }

    public void setSubunidade(String subunidade) {
        Subunidade = subunidade;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPosto() {
        return Posto;
    }

    public void setPosto(String posto) {
        Posto = posto;
    }

    public String getOptanteSimplesNacional() {
        return OptanteSimplesNacional;
    }

    public void setOptanteSimplesNacional(String optanteSimplesNacional) {
        OptanteSimplesNacional = optanteSimplesNacional;
    }

    public BigDecimal getValorPecas() {
        return ValorPecas;
    }

    public void setValorPecas(BigDecimal valorPecas) {
        ValorPecas = valorPecas;
    }

    public BigDecimal getValorMaoDeObra() {
        return ValorMaoDeObra;
    }

    public void setValorMaoDeObra(BigDecimal valorMaoDeObra) {
        ValorMaoDeObra = valorMaoDeObra;
    }

    public BigDecimal getValorTotal() {
        return ValorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        ValorTotal = valorTotal;
    }

    public String getSituacaoOS() {
        return SituacaoOS;
    }

    public void setSituacaoOS(String situacaoOS) {
        SituacaoOS = situacaoOS;
    }

    public String getStatusSituacaoOS() {
        return StatusSituacaoOS;
    }

    public void setStatusSituacaoOS(String statusSituacaoOS) {
        StatusSituacaoOS = statusSituacaoOS;
    }

    public String getIntegrityHash() {
        return integrityHash;
    }

    public void setIntegrityHash(String integrityHash) {
        this.integrityHash = integrityHash;
    }

    @Override
    public String toHashString() {
        return String.join("|",
            CodOS    == null ? "" : CodOS.toString(),
            DataOS   == null ? "" : DataOS.toString(),
            Placa    == null ? "" : Placa,
            Marca    == null ? "" : Marca,
            Modelo   == null ? "" : Modelo,
            ValorTotal == null ? "" : ValorTotal.toPlainString(),
            Status   == null ? "" : Status
        );
    }

}

