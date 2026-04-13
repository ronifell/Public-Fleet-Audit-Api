package com.example.seagri.infra.model;


import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "CAR_MODELS")
public class CarModel extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    private Brand brand;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "`year`", nullable = false, updatable = false, columnDefinition="CHAR(4)")
    private String year;

    @Column(nullable = false, columnDefinition = "CHAR(1)")
    private String requiredLicense;

    @Column(nullable = false, updatable = false, precision = 5, scale = 2)
    private BigDecimal fuelCapacity;

    @Column(name = "engine", precision = 2, scale = 1)
    private BigDecimal enginePower;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRequiredLicense() {
        return requiredLicense;
    }

    public void setRequiredLicense(String requiredLicense) {
        this.requiredLicense = requiredLicense;
    }

    public BigDecimal getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(BigDecimal fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public BigDecimal getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(BigDecimal enginePower) {
        this.enginePower = enginePower;
    }
    
}
