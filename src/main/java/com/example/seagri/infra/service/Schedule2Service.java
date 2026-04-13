package com.example.seagri.infra.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.seagri.infra.model.Schedule2;
import com.example.seagri.infra.repository.Schedule2Repository;

@Service
public class Schedule2Service {
    
    private final Schedule2Repository repository;
    public Schedule2Service(Schedule2Repository repository) {
        this.repository = repository;
    }

    public List<Schedule2> getAll() {
        updateSchedulesByStartDate(); // Atualiza os status antes de retornar os dados
        List<Schedule2> schedules = repository.findAll();
        return schedules;
    }

    @SuppressWarnings("null")
    public Schedule2 getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Schedule2> getByUserId(Long userId) {
        return repository.findByRequestUserId(userId);
    }

    public List<Schedule2> getPeriodSchedulesByUserId(LocalDate date1, LocalDate date2, Long userId) {
        return repository.getPeriodSchedulesByUserId(date1, date2, userId);
    }
    
    @SuppressWarnings("null")
    public Schedule2 save(Schedule2 objeto) {
        return repository.save(objeto);
    }

    public List<Schedule2> getPeriodSchedules(LocalDate date1, LocalDate date2) {
        List<Schedule2> schedules =  repository.getPeriodSchedules(date1, date2);
        return schedules;
    }

    // Essa função é chamada pelo scheduler para atualizar os status dos agendamentos com base na data atual. 
    // Ela executa duas atualizações e retorna o total de registros atualizados.
    @Transactional
    public int updateSchedulesByStartDate() {
        LocalDate today = LocalDate.now(ZoneId.of("America/Rio_Branco"));

        int solicitadosAtualizados = repository.updateSolicitadosToExpirado(today);
        int agendadosAtualizados = repository.updateAgendadosToNaoIniciado(today);

        return solicitadosAtualizados + agendadosAtualizados;
    }

}
