package com.example.seagri.infra.model;


import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SCHEDULES2")
public class Schedule2 extends BaseEntity {
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

    @Column(nullable = false, updatable = false)
    private String arrival;

    @Column(nullable = false, updatable = false)
    private String departure;

    @Column(nullable = false, updatable = false)
    private String personInChargeName;

    @Column(nullable = false, updatable = false)
    private String personInChargePhone;

    @Column(nullable = false)
    private Integer passengersAmount;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(optional = false)
    private Vehicle vehicle;

    @ManyToOne(optional = true)
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "request_user_id", nullable = false)
    private User requestUser;

    @ManyToOne
    @JoinColumn(name = "approval_user_id")
    private User approvalUser;

}
