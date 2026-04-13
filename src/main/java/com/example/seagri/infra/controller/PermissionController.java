package com.example.seagri.infra.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.model.Permission;
import com.example.seagri.infra.service.PermissionService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/permissions")
public class PermissionController {

  @Autowired
  private PermissionService service;

  @GetMapping("/")
  public ResponseEntity<List<Permission>> getAll() {
    List<Permission> allPermissions = service.getAll();
    return new ResponseEntity<>(allPermissions, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Permission> getById(Integer id) {
    Permission instance = service.getById(id);
    return new ResponseEntity<>(instance, HttpStatus.OK);
  }

  @PostMapping("/")
  public ResponseEntity<Permission> insert(Permission object) {
    Permission instance = service.save(object);
    return new ResponseEntity<>(instance, HttpStatus.CREATED);
  }

  @PutMapping("/")
  public ResponseEntity<Permission> update(Permission object) {
    Permission instance = service.save(object);
    return new ResponseEntity<>(instance, HttpStatus.OK);
  }
  
  
}
