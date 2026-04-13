package com.example.seagri.infra.model;


import java.math.BigDecimal;

import com.example.seagri.infra.integrity.Hashable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "VEHICLES_TRAVELS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleTravel extends BaseEntity implements Hashable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(columnDefinition = "CHAR(64)", updatable = false)
    private String integrityHash;

    @Column(name = "schedule_id", nullable = false, updatable = false)
    private Long schedule_id;

    @ManyToOne(optional = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false)
    private Driver driver;

    @ManyToOne(optional = true)
    private Fuel fuel;

    @Column(nullable = false)
    private Integer passengersAmount;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal diaryValue;
    
    @Column(nullable = true, updatable = true, precision = 8, scale = 2)
    private BigDecimal departureQuilometers;

    @Column(precision = 8, scale = 2)
    private BigDecimal arrivalQuilometers;
    
    @Column(precision = 8, scale = 2)
    private BigDecimal drivenQuilometers;

    @Column
    private String usedFuelAmount;

    @Column
    private String registry;

    @Column(nullable = false)
    private String status;

    @OneToOne(mappedBy = "travel", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Checklist checklist;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(Long schedule_id) {
        this.schedule_id = schedule_id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Integer getPassengersAmount() {
        return passengersAmount;
    }

    public void setPassengersAmount(Integer passengersAmount) {
        this.passengersAmount = passengersAmount;
    }

    public BigDecimal getDiaryValue() {
        return diaryValue;
    }

    public void setDiaryValue(BigDecimal diaryValue) {
        this.diaryValue = diaryValue;
    }

    public BigDecimal getDepartureQuilometers() {
        return departureQuilometers;
    }

    public void setDepartureQuilometers(BigDecimal departureQuilometers) {
        this.departureQuilometers = departureQuilometers;
    }

    public BigDecimal getArrivalQuilometers() {
        return arrivalQuilometers;
    }

    public void setArrivalQuilometers(BigDecimal arrivalQuilometers) {
        this.arrivalQuilometers = arrivalQuilometers;
    }

    public BigDecimal getDrivenQuilometers() {
        return drivenQuilometers;
    }

    public void setDrivenQuilometers(BigDecimal drivenQuilometers) {
        this.drivenQuilometers = drivenQuilometers;
    }

    public String getUsedFuelAmount() {
        return usedFuelAmount;
    }

    public void setUsedFuelAmount(String usedFuelAmount) {
        this.usedFuelAmount = usedFuelAmount;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public void setChecklist(Checklist checklist) {
        this.checklist = checklist;
    }

    @Override
    public String toHashString() {
        return String.join("|",
            schedule_id        == null ? "" : schedule_id.toString(),
            vehicle            == null ? "" : vehicle.getId().toString(),
            driver             == null ? "" : driver.getId().toString(),
            diaryValue         == null ? "" : diaryValue.toPlainString(),
            passengersAmount   == null ? "" : passengersAmount.toString(),
            departureQuilometers == null ? "" : departureQuilometers.toPlainString(),
            status             == null ? "" : status
        );
    }

}
