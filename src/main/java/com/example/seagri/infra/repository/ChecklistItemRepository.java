package com.example.seagri.infra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.ChecklistItem;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long>{
    
}
