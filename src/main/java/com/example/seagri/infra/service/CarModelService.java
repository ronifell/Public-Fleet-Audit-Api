package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.CarModel;
import com.example.seagri.infra.repository.CarModelRepository;

@Service
public class CarModelService {
    
    @Autowired
    private CarModelRepository repository;

    public List<CarModel> getAll() {
        return repository.findAll();
    }
    
    @SuppressWarnings("null")
    public Page<CarModel> getPage(Pageable page) {
        return repository.findAll(page);
    }
    
    @SuppressWarnings("null")
    public CarModel getById(Long id) {
        return repository.findById(id).orElse(null);
    }
    
    public List<CarModel> getByBrand(Integer id) {
        return repository.getByBrand(id);
    }

    @SuppressWarnings("null")
    public CarModel save(CarModel objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<CarModel> get(String searchTerm) {
        return repository.search(searchTerm);
    }

}
