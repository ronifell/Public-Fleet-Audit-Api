package com.example.seagri.infra.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Maintenance;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>{

    @Query("SELECT SUM(m.ValorTotal) FROM Maintenance m WHERE (m.DataOS BETWEEN ?1 AND ?2)")
    public BigDecimal getAllMaintenanceCosts(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Maintenance m WHERE (m.DataOS BETWEEN ?1 AND ?2)")
    public List<Maintenance> getAllMaintenances(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(m.ValorTotal) FROM Maintenance m WHERE m.Placa LIKE ?1 AND (m.DataOS BETWEEN ?2 AND ?3)")
    public BigDecimal getMaintenanceCosts(String vehiclePlate, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Maintenance m WHERE m.Placa LIKE ?1 AND (m.DataOS BETWEEN ?2 AND ?3)")
    public List<Maintenance> getVehicleMaintenances(String vehiclePlate, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM Maintenance m LEFT JOIN Vehicle v ON v.licensePlate LIKE m.Placa WHERE v.type.id = ?1 AND (m.DataOS BETWEEN ?2 AND ?3)")
    public List<Maintenance> getVehicleTypeMaintenance(Integer vehicleType, LocalDateTime startDate, LocalDateTime endDate);

}
