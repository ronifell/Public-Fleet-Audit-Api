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

import com.example.seagri.infra.model.Checklist;
import com.example.seagri.infra.service.ChecklistService;

@RestController
@RequestMapping("/checklist")
public class ChecklistController {
    
    @Autowired
    private ChecklistService service;    

    @GetMapping("/")
    public ResponseEntity<List<Checklist>> getAll() {
        List<Checklist> allChecklists = service.getAll();
        return new ResponseEntity<>(allChecklists, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Checklist> getById(@PathVariable("id") Integer id) {
        Checklist checklist = service.getById(id);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }

    @GetMapping("/travel/{id}")
    public ResponseEntity<Checklist> getByTravelId(@PathVariable Long travel_id) {
        Checklist checklist = service.getByTravelId(travel_id);
        return new ResponseEntity<>(checklist, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Checklist> insert(@RequestBody Checklist object) {
        Checklist checklist = service.save(object);
        return new ResponseEntity<>(checklist, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Checklist> update(@RequestBody Checklist object) {
        Checklist checklist = service.save(object);
        return new ResponseEntity<>(checklist, HttpStatus.CREATED);
    }

}
