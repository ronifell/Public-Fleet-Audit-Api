package com.example.seagri.infra.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.seagri.infra.model.Schedule2;

public interface Schedule2Repository extends JpaRepository<Schedule2, Long> {

    // Busca geral de agendamentos no período
    @Query("""
          SELECT s FROM Schedule2 s 
          WHERE (s.startDate >= :first_day AND s.startDate < :last_day) 
          OR (s.endDate >= :first_day AND s.endDate < :last_day)
        """)
    public List<Schedule2> getPeriodSchedules(
        @Param("first_day") LocalDate first_day, 
        @Param("last_day") LocalDate last_day
    );

    // Busca de agendamentos no período por usuário
    @Query("""
            SELECT s FROM Schedule2 s
            WHERE (
                (s.startDate >= :first_day AND s.startDate < :last_day)
                OR
                (s.endDate >= :first_day AND s.endDate < :last_day)
            )
            AND s.requestUser.id = :userId
        """)
    public List<Schedule2> getPeriodSchedulesByUserId(
        @Param("first_day") LocalDate first_day,
        @Param("last_day") LocalDate last_day,
        @Param("userId") Long userId
    );

    List<Schedule2> findByRequestUserId(Long userId);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Schedule2 s
            SET s.status = 'EXPIRADO'
            WHERE s.status = 'SOLICITADO'
            AND s.startDate <= :today
        """)
    int updateSolicitadosToExpirado(@Param("today") LocalDate today);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Schedule2 s
            SET s.status = 'NAO_INICIADO'
            WHERE s.status = 'AGENDADO'
            AND s.startDate <= :today
        """)
    int updateAgendadosToNaoIniciado(@Param("today") LocalDate today);
}
