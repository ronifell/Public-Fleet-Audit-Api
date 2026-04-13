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
@Table(name = "SUPPLIES")
public class Supply extends BaseEntity implements Hashable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "CHAR(64)", updatable = false)
    private String integrityHash;
    
    @Column(columnDefinition = "VARCHAR(10)")
    private String cod_transaction;
    
    @Column
    private LocalDateTime transaction_date;
    
    @Column(columnDefinition = "CHAR(7)")
    private String license_plate;
    
    @Column(columnDefinition = "VARCHAR(55)")
    private String car_model;
    
    @Column(name = "`year`", columnDefinition = "CHAR(4)")
    private String year;
    
    @Column(columnDefinition = "VARCHAR(10)")
    private String matricula;
    
    @Column(columnDefinition = "VARCHAR(75)")
    private String driver_name;
    
    @Column(columnDefinition = "VARCHAR(35)")
    private String fuel_type;
    
    @Column(columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal liters;
    
    @Column(columnDefinition = "DECIMAL(5, 3)")
    private BigDecimal value_liter;
    
    @Column
    private Integer hodometro;
    
    @Column
    private Integer kms_or_hours;
    
    @Column(columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal km_per_liter;
    
    @Column(columnDefinition = "DECIMAL(6, 2)")
    private BigDecimal emission_value;
    
    @Column(columnDefinition = "VARCHAR(8)")
    private String cod_estabelecimento;
    
    @Column(columnDefinition = "VARCHAR(80)")
    private String nome_estabelecimento;
    
    @Column(columnDefinition = "VARCHAR(255)")
    private String endereco;
    
    @Column(columnDefinition = "VARCHAR(65)")
    private String bairro;
    
    @Column(columnDefinition = "VARCHAR(60)")
    private String cidade;
    
    @Column(columnDefinition = "CHAR(2)")
    private String UF;
    
    @Column(columnDefinition = "VARCHAR(5)")
    private String forma_transacao;
    
    @Column(columnDefinition = "VARCHAR(10)")
    private String serie_pos;
    
    @Column(columnDefinition = "CHAR(17)")
    private String numero_cartao;
    
    @Column(columnDefinition = "VARCHAR(25)")
    private String familia_veiculo;

    public void setValue_liter(BigDecimal value_liter) {
        this.value_liter = value_liter;
    }

    public void setValue_liter(String value_liter) {
        value_liter = value_liter.replace(",", ".");
        value_liter = value_liter.replace("R$", "");
        value_liter = value_liter.replace("$", "");
        value_liter = value_liter.replace(" ", "");
        this.value_liter = new BigDecimal(value_liter);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCod_transaction() {
        return cod_transaction;
    }

    public void setCod_transaction(String cod_transaction) {
        this.cod_transaction = cod_transaction;
    }

    public LocalDateTime getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(LocalDateTime transaction_date) {
        this.transaction_date = transaction_date;
    }

    public String getLicense_plate() {
        return license_plate;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public BigDecimal getLiters() {
        return liters;
    }

    public void setLiters(BigDecimal liters) {
        this.liters = liters;
    }

    public BigDecimal getValue_liter() {
        return value_liter;
    }

    public Integer getHodometro() {
        return hodometro;
    }

    public void setHodometro(Integer hodometro) {
        this.hodometro = hodometro;
    }

    public Integer getKms_or_hours() {
        return kms_or_hours;
    }

    public void setKms_or_hours(Integer kms_or_hours) {
        this.kms_or_hours = kms_or_hours;
    }

    public BigDecimal getKm_per_liter() {
        return km_per_liter;
    }

    public void setKm_per_liter(BigDecimal km_per_liter) {
        this.km_per_liter = km_per_liter;
    }

    public BigDecimal getEmission_value() {
        return emission_value;
    }

    public void setEmission_value(BigDecimal emission_value) {
        this.emission_value = emission_value;
    }

    public String getCod_estabelecimento() {
        return cod_estabelecimento;
    }

    public void setCod_estabelecimento(String cod_estabelecimento) {
        this.cod_estabelecimento = cod_estabelecimento;
    }

    public String getNome_estabelecimento() {
        return nome_estabelecimento;
    }

    public void setNome_estabelecimento(String nome_estabelecimento) {
        this.nome_estabelecimento = nome_estabelecimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUF() {
        return UF;
    }

    public void setUF(String uF) {
        UF = uF;
    }

    public String getForma_transacao() {
        return forma_transacao;
    }

    public void setForma_transacao(String forma_transacao) {
        this.forma_transacao = forma_transacao;
    }

    public String getSerie_pos() {
        return serie_pos;
    }

    public void setSerie_pos(String serie_pos) {
        this.serie_pos = serie_pos;
    }

    public String getNumero_cartao() {
        return numero_cartao;
    }

    public void setNumero_cartao(String numero_cartao) {
        this.numero_cartao = numero_cartao;
    }

    public String getFamilia_veiculo() {
        return familia_veiculo;
    }

    public void setFamilia_veiculo(String familia_veiculo) {
        this.familia_veiculo = familia_veiculo;
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
            cod_transaction   == null ? "" : cod_transaction,
            license_plate     == null ? "" : license_plate,
            transaction_date  == null ? "" : transaction_date.toString(),
            liters            == null ? "" : liters.toPlainString(),
            value_liter       == null ? "" : value_liter.toPlainString(),
            driver_name       == null ? "" : driver_name,
            fuel_type         == null ? "" : fuel_type
        );
    }

}
