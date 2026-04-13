package com.example.seagri.infra.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "VEHICLES")
public class Vehicle extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    private VehicleType type;

    @ManyToOne(optional = false)
    private CarModel carModel;

    @Column(nullable = false, unique = true, columnDefinition = "char(7)")
    private String licensePlate;
    
    @Column(updatable = false, unique = true, columnDefinition = "char(17)")
    private String chassi;

    @Column(updatable = false, unique = true, columnDefinition = "char(11)")
    private String renavam;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, updatable = false)
    private LocalDate manufacturingDate;

    @Column(nullable = false, updatable = false)
    private LocalDate purchaseDate;

    @Column(precision = 9, scale = 2)
    private BigDecimal patrimony;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal drivenQuilometers;

    @Column(nullable = false, updatable = false, unique = true, columnDefinition="CHAR(17)")
    private String acquisitionProcessNumber;

    @ManyToMany
    @JoinTable(name = "VEHICLE_FUELS", 
        joinColumns = @JoinColumn(name = "VEHICLE_ID"), 
        inverseJoinColumns = @JoinColumn(name = "FUEL_ID"))
    private List<Fuel> fuels;

    @Column(nullable = false, columnDefinition = "TINYINT(1) NOT NULL DEFAULT 1")
    @Value("true")
    private Boolean active = true;

    @Column(nullable = false)
    private Integer passengersAmount;

    // Capacidade máxima do tanque em litros — usada pelo Motor de Glosa (regra volumétrica)
    @Column(precision = 6, scale = 2)
    private BigDecimal tankCapacity;

    // Consumo mínimo esperado em km/L — usada pelo Motor de Glosa (regra KSD)
    @Column(precision = 5, scale = 2)
    private BigDecimal minKmPerLiter;

    @ManyToOne(optional = true)
    private Driver fixedDriver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getChassi() {
        return chassi;
    }

    public void setChassi(String chassi) {
        this.chassi = chassi;
    }

    public String getRenavam() {
        return renavam;
    }

    public void setRenavam(String renavam) {
        this.renavam = renavam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPatrimony() {
        return patrimony;
    }

    public void setPatrimony(BigDecimal patrimony) {
        this.patrimony = patrimony;
    }

    public BigDecimal getDrivenQuilometers() {
        return drivenQuilometers;
    }

    public void setDrivenQuilometers(BigDecimal drivenQuilometers) {
        this.drivenQuilometers = drivenQuilometers;
    }

    public String getAcquisitionProcessNumber() {
        return acquisitionProcessNumber;
    }

    public void setAcquisitionProcessNumber(String acquisitionProcessNumber) {
        this.acquisitionProcessNumber = acquisitionProcessNumber;
    }

    public List<Fuel> getFuels() {
        return fuels;
    }

    public void setFuels(List<Fuel> fuels) {
        this.fuels = fuels;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getPassengersAmount() {
        return passengersAmount;
    }

    public void setPassengersAmount(Integer passengersAmount) {
        this.passengersAmount = passengersAmount;
    }

    public Driver getFixedDriver() {
        return fixedDriver;
    }

    public void setFixedDriver(Driver fixedDriver) {
        this.fixedDriver = fixedDriver;
    }

    public BigDecimal getTankCapacity() {
        return tankCapacity;
    }

    public void setTankCapacity(BigDecimal tankCapacity) {
        this.tankCapacity = tankCapacity;
    }

    public BigDecimal getMinKmPerLiter() {
        return minKmPerLiter;
    }

    public void setMinKmPerLiter(BigDecimal minKmPerLiter) {
        this.minKmPerLiter = minKmPerLiter;
    }

}
