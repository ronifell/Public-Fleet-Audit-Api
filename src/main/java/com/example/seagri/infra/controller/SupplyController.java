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

import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.service.SupplyService;

@RestController
@RequestMapping("/supply")
public class SupplyController {
    
    @Autowired
    private SupplyService service;

    @GetMapping("/")
    public ResponseEntity<List<Supply>> getAll() {
        List<Supply> allSupplies = service.getAll();
        return new ResponseEntity<>(allSupplies, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supply> getById(@PathVariable Long id) {
        Supply supply = service.getById(id);
        return new ResponseEntity<>(supply, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Supply> insert(@RequestBody Supply object) {
        Supply instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Supply> update(@RequestBody Supply object) {
        Supply instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

}
