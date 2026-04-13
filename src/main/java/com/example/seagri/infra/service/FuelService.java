package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Fuel;
import com.example.seagri.infra.repository.FuelRepository;

@Service
public class FuelService {
    
    @Autowired
    private FuelRepository repository;

    public List<Fuel> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Page<Fuel> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public Fuel getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Fuel save(Fuel objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Fuel> get(String searchTerm) {
        return repository.search(searchTerm);
    }

}
