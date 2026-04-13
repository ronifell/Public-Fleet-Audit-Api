package com.example.seagri.infra.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.model.Vehicle;
import com.example.seagri.infra.service.VehicleService;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    
    @Autowired
    private VehicleService service;

    @GetMapping("/")
    public ResponseEntity<List<Vehicle>> getAllActive() {
        List<Vehicle> allVehicles = service.getAllActive();
        return new ResponseEntity<>(allVehicles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        Vehicle vehicle = service.getById(id);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Vehicle> insert(@RequestBody Vehicle objeto) {
        Vehicle registro = service.save(objeto);
        return new ResponseEntity<>(registro, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Vehicle> update(@RequestBody Vehicle objeto) {
        Vehicle registro = service.save(objeto);
        
        return new ResponseEntity<>(registro, HttpStatus.OK);
    }

    @GetMapping("/available/")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles(
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date1,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date2
    ){
        List<Vehicle> vehicles = service.getAvailableVehicles(date1, date2);
        return new ResponseEntity<>(vehicles, HttpStatus.OK);
    }
    
    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<Vehicle>> get(@PathVariable("searchTerm") String searchTerm) {
        List<Vehicle> registros = service.get(searchTerm);
        return new ResponseEntity<>(registros, HttpStatus.OK);
    }

}
