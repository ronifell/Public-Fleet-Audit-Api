package com.example.seagri.infra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import com.example.seagri.infra.model.Supply;

public class SupplyDTO {

  private final String cod_transaction;
  private final String date;
  private final String car;
  private final BigDecimal liters;
  private final BigDecimal value_liter;
  private final BigDecimal emission_value;

  public SupplyDTO() {
    this.cod_transaction = "";
    this.car = "";
    this.liters = new BigDecimal(0);
    this.value_liter = new BigDecimal(0);
    this.emission_value = new BigDecimal(0);
    this.date = "";
  }

  public SupplyDTO(Supply supply) {
    this.cod_transaction = supply.getCod_transaction();
    this.date = SupplyDTO.turnDateString(supply.getTransaction_date());
    this.car = supply.getCar_model() + " - " + supply.getLicense_plate();
    this.liters = supply.getLiters();
    this.value_liter = supply.getValue_liter();
    this.emission_value = supply.getEmission_value();
  }

  public static String turnDateString(LocalDateTime dateTime){        
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu")
      .withResolverStyle(ResolverStyle.STRICT);
    String date = formatter.format(dateTime).toString(); 
    return date;
  }

  public String getCod_transaction() {
    return cod_transaction;
  }

  public String getDate() {
    return date;
  }

  public String getCar() {
    return car;
  }

  public BigDecimal getLiters() {
    return liters;
  }

  public BigDecimal getValue_liter() {
    return value_liter;
  }

  public BigDecimal getEmission_value() {
    return emission_value;
  }

}
