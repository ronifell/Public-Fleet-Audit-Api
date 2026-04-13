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

import com.example.seagri.infra.model.DiscontinuedVehicle;
import com.example.seagri.infra.service.DiscontinuedVehicleService;

@RestController
@RequestMapping("/discontinuedVehicle")
public class DiscontinuedVehicleController {
    
    @Autowired
    private DiscontinuedVehicleService service;

    @GetMapping("/")
    public ResponseEntity<List<DiscontinuedVehicle>> getAll() {
        List<DiscontinuedVehicle> allDiscontinuedVehicles = service.getAll();
        return new ResponseEntity<>(allDiscontinuedVehicles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscontinuedVehicle> getById(@PathVariable Long id) {
        DiscontinuedVehicle discontinuedVehicle = service.getById(id);
        return new ResponseEntity<>(discontinuedVehicle, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<DiscontinuedVehicle> insert(@RequestBody DiscontinuedVehicle object) {
        DiscontinuedVehicle instance = service.postSave(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<DiscontinuedVehicle> update(@RequestBody DiscontinuedVehicle object) {
        DiscontinuedVehicle instance = service.putSave(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

}
