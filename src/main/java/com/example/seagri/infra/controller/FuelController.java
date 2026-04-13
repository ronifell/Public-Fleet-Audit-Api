package com.example.seagri.infra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.seagri.infra.model.Fuel;
import com.example.seagri.infra.service.FuelService;

@RestController
@RequestMapping("/fuel")
public class FuelController {
    
    @Autowired
    private FuelService service;

    @GetMapping("/")
    public ResponseEntity<List<Fuel>> getAll() {
        List<Fuel> allFuels = service.getAll();
        return new ResponseEntity<>(allFuels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fuel> getById(@PathVariable Integer id) {
        Fuel instance = service.getById(id);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Fuel> insert(@RequestBody Fuel object) {
        Fuel instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Fuel> update(@RequestBody Fuel object) {
        Fuel instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<Fuel>> get(@PathVariable("searchTerm") String searchTerm) {
        List<Fuel> registros = service.get(searchTerm);
        return new ResponseEntity<>(registros, HttpStatus.OK);
    }
}
