package com.example.seagri.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de entrada para o Motor de Glosa — corresponde exatamente ao JSON
 * enviado pela operadora (AP 03), conforme memorando técnico AP 04.
 *
 * Exemplo de payload:
 * {
 *   "transacao_id": "1001",
 *   "timestamp": "2026-03-27T10:15:00Z",
 *   "placa": "NXX-0J00",
 *   "posto_cnpj": "01.234.567/0001-99",
 *   "posto_coordenadas": { "lat": -9.974, "lng": -67.807 },
 *   "combustivel": "Diesel S10",
 *   "volume_litros": 45.50,
 *   "odometro_informado": 120500,
 *   "valor_total": 250.75,          // opcional
 *   "veiculo_coordenadas": { "lat": -9.971, "lng": -67.805 }  // opcional — GPS do veículo
 * }
 */
public record GlosaTransactionInputDTO(

    @JsonProperty("transacao_id")
    String transacaoId,

    String timestamp,

    String placa,

    @JsonProperty("posto_cnpj")
    String postoCnpj,

    @JsonProperty("posto_coordenadas")
    CoordenadaDTO postoCoordenadas,

    String combustivel,

    @JsonProperty("volume_litros")
    Double volumeLitros,

    @JsonProperty("odometro_informado")
    Integer odomteroInformado,

    @JsonProperty("valor_total")
    Double valorTotal,

    /** GPS do veículo no momento do abastecimento — opcional.
     *  Se ausente, o protocolo de contingência é aplicado. */
    @JsonProperty("veiculo_coordenadas")
    CoordenadaDTO veiculoCoordenadas,

    /** Quando true, classifica como "Reserva em Trânsito" (galões para viagens longas).
     *  Geofencing é validado normalmente, mas KSD e limite de tanque são ignorados.
     *  O volume é registrado como estoque vinculado à OS da viagem. */
    @JsonProperty("reserva_transito")
    Boolean reservaTransito

) {
    public record CoordenadaDTO(double lat, double lng) {}

    public boolean isReservaTransito() {
        return reservaTransito != null && reservaTransito;
    }
}
