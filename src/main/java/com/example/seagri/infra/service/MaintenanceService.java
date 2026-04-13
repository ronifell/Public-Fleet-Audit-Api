package com.example.seagri.infra.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.Maintenance;
import com.example.seagri.infra.repository.MaintenanceRepository;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository repository;

    @Autowired
    private HashService hashService;

    public List<Maintenance> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Maintenance getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    public Maintenance save(Maintenance objeto) {
        if (objeto.getIntegrityHash() == null) {
            objeto.setIntegrityHash(hashService.generate(objeto));
        }
        return repository.save(objeto);
    }

    @SuppressWarnings("null")
    public List<Maintenance> saveList(List<Maintenance> maintenances) {
        for (Maintenance m : maintenances) {
            if (m.getIntegrityHash() == null) {
                m.setIntegrityHash(hashService.generate(m));
            }
        }
        return repository.saveAll(maintenances);
    }

}
