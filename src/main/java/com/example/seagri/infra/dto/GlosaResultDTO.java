package com.example.seagri.infra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.seagri.infra.model.GlosaRecord;

/**
 * DTO de saída do Motor de Glosa — retornado pela API após processar uma transação.
 */
public record GlosaResultDTO(
    Long id,
    String transacaoId,
    String placa,
    String combustivel,
    BigDecimal volumeLitros,
    Integer odomteroInformado,
    BigDecimal valorTotal,
    String glosaStatus,
    String observacao,
    String integrityHash,
    LocalDateTime processedAt,
    String processedBy
) {
    public static GlosaResultDTO from(GlosaRecord r) {
        return new GlosaResultDTO(
            r.getId(), r.getTransacaoId(), r.getPlaca(), r.getCombustivel(),
            r.getVolumeLitros(), r.getOdomteroInformado(), r.getValorTotal(),
            r.getGlosaStatus(), r.getObservacao(), r.getIntegrityHash(),
            r.getProcessedAt(), r.getProcessedBy()
        );
    }
}
