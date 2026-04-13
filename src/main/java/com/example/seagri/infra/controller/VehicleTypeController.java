package com.example.seagri.infra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.model.VehicleType;
import com.example.seagri.infra.service.VehicleTypeService;

@RestController
@RequestMapping("/vehicleType")
public class VehicleTypeController {
    
    @Autowired
    private VehicleTypeService service;

    @GetMapping("/")
    public ResponseEntity<List<VehicleType>> getAll() {
        List<VehicleType> allVehicleTypes = service.getAll();
        return new ResponseEntity<>(allVehicleTypes, HttpStatus.OK);
    }

    @GetMapping("/paged/")
    public ResponseEntity<Page<VehicleType>> getPage(Pageable page) {
        Page<VehicleType> pagedVehicleTypes = service.getPage(page);
        return new ResponseEntity<>(pagedVehicleTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleType> getById(@PathVariable Integer id) {
        VehicleType instance = service.getById(id);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<VehicleType> insert(@RequestBody VehicleType object) {
        VehicleType instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<VehicleType> update(@RequestBody VehicleType object) {
        VehicleType instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<VehicleType>> get(@PathVariable("searchTerm") String searchTerm) {
        List<VehicleType> registros = service.get(searchTerm);
        return new ResponseEntity<>(registros, HttpStatus.OK);
    }
}
