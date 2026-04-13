package com.example.seagri.infra.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.dto.ScheduleDTO;
import com.example.seagri.infra.model.Schedule;
import com.example.seagri.infra.model.VehicleTravel;
import com.example.seagri.infra.repository.ScheduleRepository;

@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository repository;

    @Autowired
    private VehicleTravelService travelService;

    public List<ScheduleDTO> getAll() {
        List<Schedule> schedules = repository.findAll();
        return schedules.stream().map(ScheduleDTO::DTOFromEntity).toList();
    }

    @SuppressWarnings("null")
    public ScheduleDTO getById(Long id) {
        return ScheduleDTO.DTOFromEntity(repository.findById(id).orElse(null));
    }

    @SuppressWarnings("null")
    public Schedule save(Schedule objeto) {
        return repository.save(objeto);
    }

    public Schedule postSave(Schedule objeto) {
        if(objeto.getTravels().isEmpty()) {
            return repository.save(objeto);
        }
        
        Schedule schedule = repository.save(objeto);
        Long schedule_id = schedule.getId();
        
        for(VehicleTravel travel : schedule.getTravels()){
            travel.setSchedule_id(schedule_id);
        }
        travelService.postSave(schedule.getTravels());
        return schedule;
    }

    public List<ScheduleDTO> getPeriodSchedules(LocalDate date1, LocalDate date2) {
        List<Schedule> schedules =  repository.getPeriodSchedules(date1, date2);
        return schedules.stream().map(ScheduleDTO::DTOFromEntity).toList();
    }

}
