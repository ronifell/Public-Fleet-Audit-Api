package com.example.seagri.infra.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PatrimonyAssetInputDTO(
    String tombo,
    String descricao,
    String categoria,
    BigDecimal lat,
    BigDecimal lng,
    @JsonProperty("conservacao_percent") BigDecimal conservacaoPercent,
    String responsavel,
    String situacao,
    @JsonProperty("valor_patrimonial") BigDecimal valorPatrimonial,
    @JsonProperty("documento_anexo") String documentoAnexo
) {}
