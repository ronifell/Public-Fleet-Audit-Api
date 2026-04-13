package com.example.seagri.infra.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Supply;

public interface SupplyRepository extends JpaRepository<Supply, Long>{

    @Query("SELECT SUM(s.emission_value) FROM Supply s WHERE (s.transaction_date BETWEEN ?1 AND ?2)")
    public BigDecimal getAllSupplyCosts(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Supply s WHERE (s.transaction_date BETWEEN ?1 AND ?2)")
    public List<Supply> getAllSupplies(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT SUM(s.emission_value) FROM Supply s WHERE s.license_plate LIKE ?1 AND (s.transaction_date BETWEEN ?2 AND ?3)")
    public BigDecimal getSupplyCosts(String vehiclePlate, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Supply s WHERE s.license_plate LIKE ?1 AND (s.transaction_date BETWEEN ?2 AND ?3)")
    public List<Supply> getVehicleSupplies(String vehiclePlate, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Supply s LEFT JOIN Vehicle v ON v.licensePlate LIKE s.license_plate WHERE v.type.id = ?1 AND (s.transaction_date BETWEEN ?2 AND ?3)")
    public List<Supply> getVehicleTypeSupplies(Integer vehicleType, LocalDateTime startDate, LocalDateTime endDate);

    // Motor de Glosa: busca o abastecimento imediatamente anterior a uma data para calcular autonomia
    @Query("SELECT s FROM Supply s WHERE s.license_plate = ?1 AND s.transaction_date < ?2 ORDER BY s.transaction_date DESC")
    List<Supply> findLatestSupplyByPlate(String licensePlate, LocalDateTime before, Pageable pageable);

}
