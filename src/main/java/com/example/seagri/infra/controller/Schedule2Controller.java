package com.example.seagri.infra.controller;


import java.time.LocalDate;
import java.util.List;

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

import com.example.seagri.infra.model.Schedule2;
import com.example.seagri.infra.service.Schedule2Service;

@RestController
@RequestMapping("/schedule2")
public class Schedule2Controller {
    
    private final Schedule2Service service;
    public Schedule2Controller(Schedule2Service schedule2Service) {
        this.service = schedule2Service;
    }

    // Busca período GERAL (Admin)
    @GetMapping("/")
    public ResponseEntity<List<Schedule2>> getAll(){
        List<Schedule2> schedule2s = service.getAll();
        return new ResponseEntity<>(schedule2s, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Schedule2> getById(@PathVariable Long id) {
        Schedule2 schedule2 = service.getById(id);
        return new ResponseEntity<>(schedule2, HttpStatus.OK);
    }

    // Busca período PESSOAL (Usuário Comum)
    @GetMapping("/period/user/{id}")
    public ResponseEntity<List<Schedule2>> getPeriodSchedulesByUserId(
        @PathVariable Long id,
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date1,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date2
    ){
        List<Schedule2> schedule2s = service.getPeriodSchedulesByUserId(date1, date2, id);
        return new ResponseEntity<>(schedule2s, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Schedule2>> getByUserId(@PathVariable Long id) {
        List<Schedule2> userSchedules = service.getByUserId(id);
        return new ResponseEntity<>(userSchedules, HttpStatus.OK);
    }

    @GetMapping("/period/")
    public ResponseEntity<List<Schedule2>> getPeriodSchedule2s(
        @RequestParam("date1") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date1,
        @RequestParam("date2") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date2
    ){
        List<Schedule2> schedule2s = service.getPeriodSchedules(date1, date2);
        return new ResponseEntity<>(schedule2s, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Schedule2> insert(@RequestBody Schedule2 object) {
        Schedule2 instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Schedule2> update(@RequestBody Schedule2 object) {
        Schedule2 instance = service.save(object);
        return new ResponseEntity<>(instance, HttpStatus.OK);
    }

}
