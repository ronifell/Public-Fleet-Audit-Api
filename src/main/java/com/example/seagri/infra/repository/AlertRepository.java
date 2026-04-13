package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long>{
    
}

