package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.VehicleTravel;

public interface VehicleTravelRepository extends JpaRepository<VehicleTravel, Long>{
    
    
}
