package com.example.seagri.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>{

    @Query("SELECT b FROM Brand b" +
        " WHERE b.name LIKE %?1%")
    List<Brand> search(String searchTerm);
    
}
