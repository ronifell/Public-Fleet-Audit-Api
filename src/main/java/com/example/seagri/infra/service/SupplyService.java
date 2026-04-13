package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.repository.SupplyRepository;

@Service
public class SupplyService {

    @Autowired
    private SupplyRepository repository;

    @Autowired
    private HashService hashService;

    public List<Supply> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Supply getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Supply save(Supply objeto) {
        if (objeto.getIntegrityHash() == null) {
            objeto.setIntegrityHash(hashService.generate(objeto));
        }
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public List<Supply> saveList(List<Supply> supplies) {
        for (Supply s : supplies) {
            if (s.getIntegrityHash() == null) {
                s.setIntegrityHash(hashService.generate(s));
            }
        }
        return repository.saveAll(supplies);
    }
}
