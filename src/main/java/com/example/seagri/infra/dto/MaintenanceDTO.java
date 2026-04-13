package com.example.seagri.infra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import com.example.seagri.infra.model.Maintenance;

public class MaintenanceDTO {
    private final Long cod_os;
    private final String date;
    private final String car;
    private final BigDecimal total;
    private final BigDecimal parts_value;
    private final BigDecimal service_value;

    public MaintenanceDTO() {
        this.cod_os = 0L;
        this.date = "";
        this.car = "";
        this.total = new BigDecimal(0);
        this.parts_value = new BigDecimal(0);
        this.service_value = new BigDecimal(0);
    }

    public MaintenanceDTO(Maintenance maintenance) {
        this.cod_os = maintenance.getCodOS();
        this.date = MaintenanceDTO.turnDateString(maintenance.getDataOS());
        this.car = maintenance.getModelo() + " - " + maintenance.getPlaca();
        this.total = maintenance.getValorTotal();
        this.parts_value = maintenance.getValorPecas();
        this.service_value = maintenance.getValorMaoDeObra();
      }

      public static String turnDateString(LocalDateTime dateTime){        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
          .withResolverStyle(ResolverStyle.STRICT);
        String date = formatter.format(dateTime).toString(); 
        return date;
      }

    public Long getCod_os() {
        return cod_os;
    }

    public String getDate() {
        return date;
    }

    public String getCar() {
        return car;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getParts_value() {
        return parts_value;
    }

    public BigDecimal getService_value() {
        return service_value;
    }
      
}
