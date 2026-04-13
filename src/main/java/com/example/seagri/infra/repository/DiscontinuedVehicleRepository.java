package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.DiscontinuedVehicle;

public interface DiscontinuedVehicleRepository extends JpaRepository<DiscontinuedVehicle, Long>{
    
}
