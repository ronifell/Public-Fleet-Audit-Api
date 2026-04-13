package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
    
}
