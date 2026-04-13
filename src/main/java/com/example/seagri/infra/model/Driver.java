package com.example.seagri.infra.model;


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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "DRIVERS")
public class Driver extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany
    @JoinTable(name = "DRIVER_PHONES",
        joinColumns = @JoinColumn(name = "DRIVER_ID"),
        inverseJoinColumns = @JoinColumn(name = "PHONE_ID"))
    List<Phone> phones;
    
    @Column(nullable = false, columnDefinition = "char(1)")
    private String gender;

    @Column(name = "email")
    String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true, columnDefinition = "char(11)")
    private String cpf;

    @Column(nullable = false, unique = true, columnDefinition = "char(10)")
    private String rg;

    @Column
    private String naturality;
    
    @Column
    private String nationality;

    @Column(nullable = false, unique = true)
    private String licenseNumber;

    @Column(nullable = false)
    private LocalDate licenseIssueDate;

    @Column(nullable = false)
    private LocalDate licenseExpireDate;

    @Column
    private String licenseIssuingBody;

    @Column(nullable = false)
    private String licenseCategory;

    @Column(nullable = false)
    private String status = "AVAILABLE";

    @Column(nullable = false)
    @Value("true")
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Phone> getPhones() {
        return phones;
    }

    public void setPhones(List<Phone> phones) {
        this.phones = phones;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getNaturality() {
        return naturality;
    }

    public void setNaturality(String naturality) {
        this.naturality = naturality;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public LocalDate getLicenseIssueDate() {
        return licenseIssueDate;
    }

    public void setLicenseIssueDate(LocalDate licenseIssueDate) {
        this.licenseIssueDate = licenseIssueDate;
    }

    public LocalDate getLicenseExpireDate() {
        return licenseExpireDate;
    }

    public void setLicenseExpireDate(LocalDate licenseExpireDate) {
        this.licenseExpireDate = licenseExpireDate;
    }

    public String getLicenseIssuingBody() {
        return licenseIssuingBody;
    }

    public void setLicenseIssuingBody(String licenseIssuingBody) {
        this.licenseIssuingBody = licenseIssuingBody;
    }

    public String getLicenseCategory() {
        return licenseCategory;
    }

    public void setLicenseCategory(String licenseCategory) {
        this.licenseCategory = licenseCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
