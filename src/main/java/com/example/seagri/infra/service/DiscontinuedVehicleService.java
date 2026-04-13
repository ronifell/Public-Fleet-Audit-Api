package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.DiscontinuedVehicle;
import com.example.seagri.infra.repository.DiscontinuedVehicleRepository;
import com.example.seagri.infra.repository.VehicleRepository;

@Service
public class DiscontinuedVehicleService {
    
    @Autowired
    private DiscontinuedVehicleRepository repository;
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<DiscontinuedVehicle> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public DiscontinuedVehicle getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public DiscontinuedVehicle postSave(DiscontinuedVehicle objeto) {
        objeto.getVehicle().setActive(false);
        // objeto.getVehicle().setStatus("DISCONTINUED");
        vehicleRepository.save(objeto.getVehicle());
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public DiscontinuedVehicle putSave(DiscontinuedVehicle objeto) {
        return repository.save(objeto);
    }

}
