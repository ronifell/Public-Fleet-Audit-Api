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

import com.example.seagri.infra.model.CarModel;
import com.example.seagri.infra.service.CarModelService;

@RestController
@RequestMapping("/carModel")
public class CarModelController {
    
    @Autowired
    private CarModelService service;

    @GetMapping("/")
    public ResponseEntity<List<CarModel>> getAll() {
        List<CarModel> allCarModels = service.getAll();
        return new ResponseEntity<>(allCarModels, HttpStatus.OK);
    }

    @GetMapping("/paged/")
    public ResponseEntity<Page<CarModel>> getPage(Pageable page) {
        Page<CarModel> pagedCarModels = service.getPage(page);
        return new ResponseEntity<>(pagedCarModels, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarModel> getById(@PathVariable Long id) {
        CarModel instance = service.getById(id);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @GetMapping("/brand/{id}")
    public ResponseEntity<List<CarModel>> getByBrand(@PathVariable("id") Integer id) {
        List<CarModel> models = service.getByBrand(id);
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<CarModel> insert(@RequestBody CarModel object) {
        CarModel instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<CarModel> update(@RequestBody CarModel object) {
        CarModel instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<CarModel>> get(@PathVariable("searchTerm") String searchTerm) {
        List<CarModel> registros = service.get(searchTerm);
        return new ResponseEntity<>(registros, HttpStatus.OK);
    }
}
