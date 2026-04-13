package com.example.seagri.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.VehicleType;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Integer>{

    @Query("SELECT vt FROM VehicleType vt" +
        " WHERE vt.name LIKE %?1%")
    List<VehicleType> search(String searchTerm);
    
}
