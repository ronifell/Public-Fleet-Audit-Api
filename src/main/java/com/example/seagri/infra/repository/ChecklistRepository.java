package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.seagri.infra.model.Checklist;

public interface ChecklistRepository extends JpaRepository<Checklist, Integer>{

    @Query("SELECT vt.checklist FROM VehicleTravel vt WHERE vt.id = ?1")
    public Checklist getChecklistByTravel(Long travel_id);

}
