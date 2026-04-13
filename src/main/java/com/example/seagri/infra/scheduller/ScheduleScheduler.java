package com.example.seagri.infra.scheduller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.seagri.infra.service.Schedule2Service;

@Component
public class ScheduleScheduler {

    private static final Logger log = LoggerFactory.getLogger(ScheduleScheduler.class);

    // Fiz a troca nesse daqui por ter visto que essa é a recomendação 
    // atual do Spring para injeção de dependências em componentes agendados desde 2024.
    private final Schedule2Service schedule2Service;
    public ScheduleScheduler(Schedule2Service schedule2Service) {
        this.schedule2Service = schedule2Service;
    }

    // Executa todos os dias 00:05 no fuso de Rio Branco/AC
    @Scheduled(cron = "0 5 0 * * *", zone = "America/Rio_Branco")
    public void updateSchedulesByStartDate() {
        try {
            int total = schedule2Service.updateSchedulesByStartDate();
            log.info("Scheduler executado com sucesso. Registros atualizados: {}", total);
        } catch (Exception e) {
            log.error("Erro ao executar scheduler de atualização de status.", e);
        }
    }
}