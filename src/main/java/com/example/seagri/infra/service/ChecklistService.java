package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Checklist;
import com.example.seagri.infra.repository.ChecklistRepository;

@Service
public class ChecklistService {
    
    @Autowired
    private ChecklistRepository repository;

    public List<Checklist> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Checklist getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Checklist getByTravelId(Long id) {
        return repository.getChecklistByTravel(id);
    }

    @SuppressWarnings("null")
    public Checklist save(Checklist objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void delete(Integer id) {
        repository.deleteById(id);
    }

}
