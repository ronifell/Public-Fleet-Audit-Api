package com.example.seagri.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Fuel;

public interface FuelRepository extends JpaRepository<Fuel, Integer>{
    
    @Query("SELECT f FROM Fuel f" +
        " WHERE f.name LIKE %?1%")
    List<Fuel> search(String searchTerm);

}
