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

import com.example.seagri.infra.model.Group;
import com.example.seagri.infra.service.GroupService;

@RestController
@RequestMapping("/group")
public class GroupController {
    
    @Autowired
    private GroupService service;

    @GetMapping("/")
    public ResponseEntity<List<Group>> getAll() {
        List<Group> allGroups = service.getAll();
        return new ResponseEntity<>(allGroups, HttpStatus.OK);
    }

    @GetMapping("/paged/")
    public ResponseEntity<Page<Group>> getPage(Pageable page) {
        Page<Group> pagedGroups = service.getPage(page);
        return new ResponseEntity<>(pagedGroups, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getById(@PathVariable Long id) {
        Group instance = service.getById(id);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Group> insert(@RequestBody Group object) {
        Group instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Group> update(@RequestBody Group object) {
        Group instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
