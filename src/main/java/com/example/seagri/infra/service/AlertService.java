package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Alert;
import com.example.seagri.infra.repository.AlertRepository;

@Service
public class AlertService {
    
    @Autowired
    private AlertRepository repository;

    public List<Alert> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Alert getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Alert save(Alert objeto) {
        return repository.save(objeto);
    }

}
