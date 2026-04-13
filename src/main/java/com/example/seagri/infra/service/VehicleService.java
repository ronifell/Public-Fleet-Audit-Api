package com.example.seagri.infra.service;

import java.time.LocalDate;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Vehicle;
import com.example.seagri.infra.repository.VehicleRepository;

@Service
public class VehicleService {
    
    @Autowired
    private VehicleRepository repository;

    public List<Vehicle> getAllActive() {
        return repository.findByActiveTrue();
    }

    @SuppressWarnings("null")
    public Vehicle getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Vehicle save(Vehicle objeto) {
        return repository.save(objeto);
    }

    public List<Vehicle> getAvailableVehicles(LocalDate date1, LocalDate date2) {
        return repository.getAvailableVehicles(date1, date2);
    }

    public List<Vehicle> get(String searchTerm) {
        return repository.search(searchTerm);
    }

}
