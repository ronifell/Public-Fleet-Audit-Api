package com.example.seagri.infra.model;


import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ALERTS")
public class Alert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = true)
    private Vehicle vehicle;
    
    @ManyToOne(optional = true)
    private Maintenance maintenance;
    
    @ManyToOne(optional = true)
    private Driver driver;

    @Column(nullable = false, updatable = false)
    private LocalDate creationDate;

    @Column
    private LocalDate alertDate;

    @Column(precision = 8, scale = 2)
    private BigDecimal vehicleQuilometers;
    
    @Column(precision = 8, scale = 2)
    private BigDecimal alertQuilometers;

    @Column(nullable = false)
    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Maintenance getMaintenance() {
        return maintenance;
    }

    public void setMaintenance(Maintenance maintenance) {
        this.maintenance = maintenance;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDate alertDate) {
        this.alertDate = alertDate;
    }

    public BigDecimal getVehicleQuilometers() {
        return vehicleQuilometers;
    }

    public void setVehicleQuilometers(BigDecimal vehicleQuilometers) {
        this.vehicleQuilometers = vehicleQuilometers;
    }

    public BigDecimal getAlertQuilometers() {
        return alertQuilometers;
    }

    public void setAlertQuilometers(BigDecimal alertQuilometers) {
        this.alertQuilometers = alertQuilometers;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
