package com.example.seagri.infra.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.seagri.infra.model.Permission;
import com.example.seagri.infra.repository.PermissionRepository;

@Service
public class PermissionService {
  
  @Autowired
  private PermissionRepository repository;

  public List<Permission> getAll() {
    return repository.findAll();
  }

  @SuppressWarnings("null")
public Permission getById(Integer id) {
    return repository.findById(id).orElse(null);
  }

  @SuppressWarnings("null")
public Permission save(Permission objeto) {
    return repository.save(objeto);
  }

}
