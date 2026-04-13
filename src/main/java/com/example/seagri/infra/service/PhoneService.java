package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.model.Phone;
import com.example.seagri.infra.repository.PhoneRepository;

@Service
public class PhoneService {
    
    @Autowired
    private PhoneRepository repository;

    public List<Phone> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Phone getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Phone save(Phone objeto) {
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public void remove(Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("null")
    public void removePhones(List<Phone> phones) {
        repository.deleteAll(phones);
    }
}
