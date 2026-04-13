package com.example.seagri.infra.dto;

import java.math.BigDecimal;

/**
 * DTO do Dashboard de ROI do Motor de Glosa.
 * Status: APROVADO | GLOSADO | ALERTA
 */
public record GlosaDashboardDTO(
    long totalProcessado,
    long totalAprovado,
    long totalGlosado,
    long totalAlerta,
    long totalNaoAprovado,
    BigDecimal valorTotalGlosado,
    double percentualConformidade
) {}
