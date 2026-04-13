package com.example.seagri.infra.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Driver;
import com.example.seagri.infra.model.Phone;
import com.example.seagri.infra.repository.DriverRepository;
import com.example.seagri.infra.repository.PhoneRepository;

@Service
public class DriverService {
    
    @Autowired
    private DriverRepository repository;
    @Autowired
    private PhoneRepository phoneRepository;

    public List<Driver> getAllActive() {
        return repository.findByActiveTrue();
    }

    @SuppressWarnings("null")
    public Page<Driver> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public Driver getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Driver postSave(Driver objeto) {
        @SuppressWarnings("null")
        List<Phone> savedPhones = phoneRepository.saveAll(objeto.getPhones());
        objeto.setPhones(savedPhones);
        return repository.save(objeto);
    }

    public Driver putSave(Driver driver) {
        @SuppressWarnings("null")
        List<Phone> savedPhones = phoneRepository.saveAll(driver.getPhones());
        driver.setPhones(savedPhones);
        return repository.save(driver);
    }
    
    public void retireDriver(Long id) {
        Driver driver = this.getById(id);
        driver.setStatus("RETIRED");
        repository.save(driver);
    }

    public void fireDriver(Long id) {
        Driver driver = this.getById(id);
        driver.setStatus("FIRED");
        repository.save(driver);
    }

    public List<Driver> getAvailableDrivers(LocalDate date1, LocalDate date2) {
        return repository.getAvailableDrivers(date1, date2);
    }

    public List<Driver> getNoFixedDrivers() {
        return repository.noFixedDrivers();
    }
    
    public List<Driver> get(String searchTerm) {
        return repository.search(searchTerm);
    }

}
