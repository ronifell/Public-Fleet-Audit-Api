package com.example.seagri.infra.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.model.Driver;
import com.example.seagri.infra.model.Phone;
import com.example.seagri.infra.service.DriverService;
import com.example.seagri.infra.service.PhoneService;

@RestController
@RequestMapping("/driver")
public class DriverController {
    
    @Autowired
    private DriverService service;
    @Autowired
    private PhoneService phoneService;

    @GetMapping("/")
    public ResponseEntity<List<Driver>> getAll() {
        List<Driver> allDrivers = service.getAllActive();
        return new ResponseEntity<>(allDrivers, HttpStatus.OK);
    }

    @GetMapping("/paged/")
    public ResponseEntity<Page<Driver>> getPage(Pageable page) {
        Page<Driver> pagedDrivers = service.getPage(page);
        return new ResponseEntity<>(pagedDrivers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Driver> getById(@PathVariable Long id) {
        Driver driver = service.getById(id);
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @GetMapping("/phones")
    public ResponseEntity<List<Phone>> getAllPhones() {
        List<Phone> phones = phoneService.getAll();
        return new ResponseEntity<>(phones, HttpStatus.OK);
    }
    
    @PostMapping("/")
    public ResponseEntity<Driver> insert(@RequestBody Driver object) {
        Driver instance = service.postSave(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Driver> update(@RequestBody Driver object) {
        Driver instance = service.putSave(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PutMapping("/item/update")
    public ResponseEntity<Phone> updatePhone(@RequestBody Phone object) {
        phoneService.save(object);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/retire/{id}")
    public ResponseEntity<?> retire(@PathVariable("id") Long id) {
        service.retireDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/fire/{id}")
    public ResponseEntity<?> fireDriver(@PathVariable("id") Long id) {
        service.fireDriver(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/phones/{phoneId}")
    public ResponseEntity<?> deletePhone(@PathVariable("phoneId") Long id) {
        phoneService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/phones/")
    public ResponseEntity<?> deletePhones(@RequestBody List<Phone> phones) {
        phoneService.removePhones(phones);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/available/")
    public ResponseEntity<List<Driver>> getAvailableDrivers(
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date1,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date2
    ){
        List<Driver> drivers = service.getAvailableDrivers(date1, date2);
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }

    @GetMapping("/notExclusive/")
    public ResponseEntity<List<Driver>> getAvailableDrivers(){
        List<Driver> drivers = service.getNoFixedDrivers();
        return new ResponseEntity<>(drivers, HttpStatus.OK);
    }
    
    @GetMapping("/search/{searchTerm}")
    public ResponseEntity<List<Driver>> get(@PathVariable("searchTerm") String searchTerm) {
        List<Driver> registros = service.get(searchTerm);
        return new ResponseEntity<>(registros, HttpStatus.OK);
    }

}   
