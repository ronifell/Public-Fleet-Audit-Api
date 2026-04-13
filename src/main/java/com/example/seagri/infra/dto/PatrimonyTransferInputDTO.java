package com.example.seagri.infra.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatrimonyTransferInputDTO(
    String tombo,
    @JsonProperty("remetente_nome") String remetenteNome,
    @JsonProperty("remetente_unidade") String remetenteUnidade,
    @JsonProperty("remetente_lat") BigDecimal remetenteLat,
    @JsonProperty("remetente_lng") BigDecimal remetenteLng,
    @JsonProperty("destinatario_nome") String destinatarioNome,
    @JsonProperty("destinatario_unidade") String destinatarioUnidade,
    @JsonProperty("destinatario_lat") BigDecimal destinatarioLat,
    @JsonProperty("destinatario_lng") BigDecimal destinatarioLng,
    String motivo
) {}
