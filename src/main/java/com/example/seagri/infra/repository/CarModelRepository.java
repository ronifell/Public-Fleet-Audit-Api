package com.example.seagri.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.CarModel;

public interface CarModelRepository extends JpaRepository<CarModel, Long>{
    
    @Query("SELECT model FROM CarModel model WHERE model.brand.id = ?1")
    public List<CarModel> getByBrand(Integer id);

    @Query("SELECT m FROM CarModel m" +
        " WHERE m.name LIKE %?1%")
    List<CarModel> search(String searchTerm);
    
}
