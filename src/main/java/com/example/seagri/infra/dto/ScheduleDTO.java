package com.example.seagri.infra.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.seagri.infra.model.Schedule;

public record ScheduleDTO(
    Long id,
    LocalDate startDate,
    LocalTime startTime,
    LocalDate endDate,
    LocalTime endTime,
    String processNumber,
    String arrival,
    String departure,
    String personInChargeName,
    String personInChargePhone,
    String plaintiffUnit,
    Integer passengersAmount,
    String status,
    List<TravelDTO> travels,
    UserDTO requestUser
) {
    public static ScheduleDTO DTOFromEntity(Schedule schedule) {
        ScheduleDTO dto = new ScheduleDTO(
            schedule.getId(),
            schedule.getStartDate(),
            schedule.getStartTime(),
            schedule.getEndDate(),
            schedule.getEndTime(),
            schedule.getProcessNumber(),
            schedule.getArrival(),
            schedule.getDeparture(),
            schedule.getPersonInChargeName(),
            schedule.getPersonInChargePhone(),
            schedule.getPlaintiffUnit(),
            schedule.getPassengersAmount(),
            schedule.getStatus(),
            schedule.getTravels().stream().map(TravelDTO::DTOFromEntity).toList(),
            UserDTO.DTOFromEntity(schedule.getRequestUser())
        );
        return dto;
    }

}
