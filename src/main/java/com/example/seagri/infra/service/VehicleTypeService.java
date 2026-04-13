package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.VehicleType;
import com.example.seagri.infra.repository.VehicleTypeRepository;

@Service
public class VehicleTypeService {
    
    @Autowired
    private VehicleTypeRepository repository;

    public List<VehicleType> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Page<VehicleType> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public VehicleType getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public VehicleType save(VehicleType objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<VehicleType> get(String searchTerm) {
        return repository.search(searchTerm);
    }

}
