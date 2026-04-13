package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Brand;
import com.example.seagri.infra.repository.BrandRepository;

@Service
public class BrandService {
    
    @Autowired
    private BrandRepository repository;

    public List<Brand> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Page<Brand> getPage(Pageable page) {
        return repository.findAll(page);
    }

    @SuppressWarnings("null")
    public Brand getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Brand save(Brand objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Brand> get(String searchTerm) {
        return repository.search(searchTerm);
    }

}
