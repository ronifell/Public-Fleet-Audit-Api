package com.example.seagri.infra.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long>{
    
    @Query
    public List<Vehicle> findByActiveTrue();

    // @Query(" SELECT v FROM Vehicle v WHERE v.active = true " + 
    // " AND not exists(select 1 from VehicleTravel vt INNER JOIN Schedule s ON s.id = vt.schedule_id WHERE vt.vehicle=v " +
    // "       AND (s.startDate <= ?1 AND s.endDate >= ?1 " +
    // "            OR s.startDate <= ?2 AND s.endDate >= ?2)) ")
    // public List<Vehicle> getAvailableVehicles(LocalDate startDate, LocalDate endDate);

    @Query(" SELECT v FROM Vehicle v WHERE v.active = true " +
           " AND NOT EXISTS ( " +
           "    SELECT s FROM Schedule2 s " +
           "    WHERE s.vehicle = v " +
           "    AND s.status NOT IN ('CANCELADO', 'RECUSADO', 'FINALIZADO', 'SOLICITADO') " + // Ignora viagens canceladas/finalizadas
           "    AND (s.startDate <= ?2 AND s.endDate >= ?1) " + // Lógica de colisão de datas
           " ) ")
    public List<Vehicle> getAvailableVehicles(LocalDate startDate, LocalDate endDate);

    @Query("SELECT v FROM Vehicle v" +
        " LEFT JOIN CarModel m ON m.id = v.carModel.id" +
        " WHERE v.licensePlate LIKE %?1%" +
        " OR m.name LIKE %?1%" )
    // @Query("SELECT v FROM Vehicle v" +
    //     " WHERE v.licensePlate LIKE %?1%")
    List<Vehicle> search(String searchTerm);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

}
