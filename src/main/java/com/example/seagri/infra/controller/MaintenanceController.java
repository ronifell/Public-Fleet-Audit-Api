package com.example.seagri.infra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.model.Maintenance;
import com.example.seagri.infra.service.MaintenanceService;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    
    @Autowired
    private MaintenanceService service;

    @GetMapping("/")
    public ResponseEntity<List<Maintenance>> getAll() {
        List<Maintenance> allMaintenances = service.getAll();
        return new ResponseEntity<>(allMaintenances, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Maintenance> getById(@PathVariable Long id) {
        Maintenance maintenance = service.getById(id);
        return new ResponseEntity<>(maintenance, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Maintenance> insert(@RequestBody Maintenance object) {
        Maintenance instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Maintenance> update(@RequestBody Maintenance object) {
        Maintenance instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

}
