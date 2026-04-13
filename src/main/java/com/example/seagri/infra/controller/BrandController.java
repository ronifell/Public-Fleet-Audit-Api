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

import com.example.seagri.infra.model.Brand;
import com.example.seagri.infra.service.BrandService;

@RestController
@RequestMapping("/brand")
public class BrandController {
    
    @Autowired
    private BrandService service;

    @GetMapping("/")
    public ResponseEntity<List<Brand>> getAll() {
        List<Brand> allBrands = service.getAll();
        return new ResponseEntity<>(allBrands, HttpStatus.OK);
    }

    @GetMapping("/paged/")
    public ResponseEntity<Page<Brand>> getPage(Pageable page) {
        Page<Brand> pagedBrands = service.getPage(page);
        return new ResponseEntity<>(pagedBrands, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getById(@PathVariable Long id) {
        Brand instance = service.getById(id);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Brand> insert(@RequestBody Brand object) {
        Brand instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Brand> update(@RequestBody Brand object) {
        Brand instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<Brand>> get(@PathVariable("searchTerm") String searchTerm) {
        List<Brand> registros = service.get(searchTerm);
        return new ResponseEntity<>(registros, HttpStatus.OK);
    }
}
