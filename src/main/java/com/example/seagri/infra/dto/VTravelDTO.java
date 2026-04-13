package com.example.seagri.infra.dto;

import java.math.BigDecimal;

import com.example.seagri.infra.model.Driver;
import com.example.seagri.infra.model.Fuel;
import com.example.seagri.infra.model.Vehicle;
import com.example.seagri.infra.model.VehicleTravel;

public record VTravelDTO(
    Long id,
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
    String status
) {
    
    public static VTravelDTO DTOFromEntity(VehicleTravel travel) {
        VTravelDTO dto = new VTravelDTO(
            travel.getId(),
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
            travel.getStatus()
        );
        return dto;
    }
}
