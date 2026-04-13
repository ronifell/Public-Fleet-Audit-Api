package com.example.seagri.infra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Long>{

    @Query
    public List<Driver> findByActiveTrue();

    @Query(" SELECT d FROM Driver d WHERE d.active = true " + 
    " AND not exists(select 1 from Vehicle v WHERE v.fixedDriver = d) " +
    " AND not exists(select 1 from VehicleTravel vt INNER JOIN Schedule s ON s.id = vt.schedule_id WHERE vt.driver=d " +
    "       AND (s.startDate <= ?1 AND s.endDate >= ?1 " +
    "            OR s.startDate <= ?2 AND s.endDate >= ?2)) ")
    public List<Driver> getAvailableDrivers(LocalDate startDate, LocalDate endDate);

    @Query(" SELECT d FROM Driver d WHERE d.active = true " + 
    " AND NOT EXISTS(select 1 from Vehicle v WHERE v.fixedDriver = d) ")
    public List<Driver> noFixedDrivers();
    
    @Query("SELECT d FROM Driver d" +
        " WHERE d.name LIKE %?1%")
    List<Driver> search(String searchTerm);

}
