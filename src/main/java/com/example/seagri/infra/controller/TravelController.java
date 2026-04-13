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

import com.example.seagri.infra.dto.ChecklistDTO;
import com.example.seagri.infra.dto.TravelDTO;
import com.example.seagri.infra.model.VehicleTravel;
import com.example.seagri.infra.service.VehicleTravelService;

@RestController
@RequestMapping("/travel")
public class TravelController {
    
    @Autowired
    private VehicleTravelService service;

    @GetMapping("/")
    public ResponseEntity<List<TravelDTO>> getAll() {
        List<TravelDTO> travels = service.getAll();
        return new ResponseEntity<>(travels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelDTO> getById(@PathVariable Long id) {
        TravelDTO VehicleTravel = service.getById(id);
        return new ResponseEntity<>(VehicleTravel, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<VehicleTravel> insert(@RequestBody VehicleTravel object) {
        VehicleTravel instance = service.postSave(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    // @PutMapping("/")
    // public ResponseEntity<VehicleTravel> update(@RequestBody VehicleTravel object) {
    //     VehicleTravel instance = service.updateSave(object);
    //     return new ResponseEntity<>(instance, HttpStatus.OK);
    // }

    @PutMapping("/dto/")
    public ResponseEntity<VehicleTravel> updateD(@RequestBody ChecklistDTO object) {
        VehicleTravel instance = service.saveWithSignatures(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<VehicleTravel> update(@RequestBody ChecklistDTO object) {
        VehicleTravel instance = service.saveWithSignatures(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }
    
}
