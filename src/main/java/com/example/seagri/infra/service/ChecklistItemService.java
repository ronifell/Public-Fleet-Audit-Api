package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.ChecklistItem;
import com.example.seagri.infra.repository.ChecklistItemRepository;

@Service
public class ChecklistItemService {
    
    @Autowired
    private ChecklistItemRepository repository;

    public List<ChecklistItem> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public ChecklistItem getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public ChecklistItem save(ChecklistItem objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("null")
    public void removeItems(List<ChecklistItem> items) {
        repository.deleteAll(items);
    }

}
