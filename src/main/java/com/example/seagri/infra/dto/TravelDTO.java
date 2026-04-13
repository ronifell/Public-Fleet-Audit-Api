package com.example.seagri.infra.dto;

import java.math.BigDecimal;

import com.example.seagri.infra.model.Checklist;
import com.example.seagri.infra.model.Driver;
import com.example.seagri.infra.model.Fuel;
import com.example.seagri.infra.model.Vehicle;
import com.example.seagri.infra.model.VehicleTravel;

public record TravelDTO(
    Long id,
    Long schedule_id,
    Vehicle vehicle,
    Driver driver,
    Fuel fuel,
    Integer passengersAmount,
    BigDecimal diaryValue,
    BigDecimal departureQuilometers,
    BigDecimal arrivalQuilometers,
    BigDecimal drivenQuilometers,
    String usedFuelAmount,
    String registry,
    String status,
    Checklist checklist
) {
    
    public static TravelDTO DTOFromEntity(VehicleTravel travel) {
        TravelDTO dto = new TravelDTO(
            travel.getId(),
            travel.getSchedule_id(),
            travel.getVehicle(),
            travel.getDriver(),
            travel.getFuel(),
            travel.getPassengersAmount(),
            travel.getDiaryValue(),
            travel.getDepartureQuilometers(),
            travel.getArrivalQuilometers(),
            travel.getDrivenQuilometers(),
            travel.getUsedFuelAmount(),
            travel.getRegistry(),
            travel.getStatus(),
            travel.getChecklist()
        );
        return dto;
    }

    public static VehicleTravel DTOToEntity(TravelDTO dto) {
        VehicleTravel travel = new VehicleTravel(
            dto.id,
            null, // integrityHash — gerado automaticamente no save
            dto.schedule_id,
            dto.vehicle,
            dto.driver,
            dto.fuel,
            dto.passengersAmount,
            dto.diaryValue,
            dto.departureQuilometers,
            dto.arrivalQuilometers,
            dto.drivenQuilometers,
            dto.usedFuelAmount,
            dto.registry,
            dto.status,
            dto.checklist
        );
        return travel;
    }
    
}
