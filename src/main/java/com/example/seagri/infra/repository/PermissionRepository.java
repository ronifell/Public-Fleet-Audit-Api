package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer>{
  
}
