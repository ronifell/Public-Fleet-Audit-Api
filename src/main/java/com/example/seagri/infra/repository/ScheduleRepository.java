package com.example.seagri.infra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
 
    @Query(" SELECT s FROM Schedule " + 
        "s WHERE (s.startDate >= ?1 AND s.startDate < ?2) OR (s.endDate >= ?1 AND s.endDate < ?2)")
    public List<Schedule> getPeriodSchedules(LocalDate first_day, LocalDate last_day);

}
