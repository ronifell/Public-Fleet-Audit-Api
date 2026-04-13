package com.example.seagri.infra.model;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SCHEDULES")
public class Schedule extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate startDate;
    
    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column
    private LocalDate endDate;
    
    @Column
    private LocalTime endTime;

    @Column(nullable = false, updatable = false, unique = true)
    private String processNumber;

    @Column(nullable = false, updatable = false)
    private String arrival;

    @Column(nullable = false, updatable = false)
    private String departure;

    @Column(nullable = false, updatable = false)
    private String personInChargeName;

    @Column(nullable = false, updatable = false)
    private String personInChargePhone;
    
    @Column(nullable = false, updatable = false)
    private String plaintiffUnit;

    @Column(nullable = false)
    private Integer passengersAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "schedule_id")
    private List<VehicleTravel> travels;

    @ManyToOne
    @JoinColumn(name = "request_user_id", nullable = false)
    private User requestUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getProcessNumber() {
        return processNumber;
    }

    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getPersonInChargeName() {
        return personInChargeName;
    }

    public void setPersonInChargeName(String personInChargeName) {
        this.personInChargeName = personInChargeName;
    }

    public String getPersonInChargePhone() {
        return personInChargePhone;
    }

    public void setPersonInChargePhone(String personInChargePhone) {
        this.personInChargePhone = personInChargePhone;
    }

    public String getPlaintiffUnit() {
        return plaintiffUnit;
    }

    public void setPlaintiffUnit(String plaintiffUnit) {
        this.plaintiffUnit = plaintiffUnit;
    }

    public Integer getPassengersAmount() {
        return passengersAmount;
    }

    public void setPassengersAmount(Integer passengersAmount) {
        this.passengersAmount = passengersAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<VehicleTravel> getTravels() {
        return travels;
    }

    public void setTravels(List<VehicleTravel> travels) {
        this.travels = travels;
    }

    public User getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(User requestUser) {
        this.requestUser = requestUser;
    }
    
}
