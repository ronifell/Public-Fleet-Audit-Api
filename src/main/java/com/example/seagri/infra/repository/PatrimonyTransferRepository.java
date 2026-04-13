package com.example.seagri.infra.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.seagri.infra.model.PatrimonyTransfer;

public interface PatrimonyTransferRepository extends JpaRepository<PatrimonyTransfer, Long> {

    List<PatrimonyTransfer> findByTomboOrderByDataTransferenciaDesc(String tombo);

    Optional<PatrimonyTransfer> findTopByOrderByIdDesc();

    List<PatrimonyTransfer> findAllByOrderByDataTransferenciaDesc();
}
