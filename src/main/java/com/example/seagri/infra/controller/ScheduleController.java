package com.example.seagri.infra.controller;


import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.dto.ScheduleDTO;
import com.example.seagri.infra.model.Schedule;
import com.example.seagri.infra.service.ScheduleService;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    
    @Autowired
    private ScheduleService service;

    @GetMapping("/")
    public ResponseEntity<List<ScheduleDTO>> getAll(){
        List<ScheduleDTO> schedules = service.getAll();
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> getById(@PathVariable Long id) {
        ScheduleDTO schedule = service.getById(id);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @GetMapping("/period/")
    public ResponseEntity<List<ScheduleDTO>> getPeriodSchedules(
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date1,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date2
    ){
        List<ScheduleDTO> schedules = service.getPeriodSchedules(date1, date2);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Schedule> insert(@RequestBody Schedule object) {
        Schedule instance = service.postSave(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Schedule> update(@RequestBody Schedule object) {
        Schedule instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

}
