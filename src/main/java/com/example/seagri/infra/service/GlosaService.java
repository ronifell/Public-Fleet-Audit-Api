package com.example.seagri.infra.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.dto.GlosaDashboardDTO;
import com.example.seagri.infra.dto.GlosaResultDTO;
import com.example.seagri.infra.dto.GlosaTransactionInputDTO;
import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.GlosaRecord;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.model.Vehicle;
import com.example.seagri.infra.repository.GlosaRepository;
import com.example.seagri.infra.repository.SupplyRepository;
import com.example.seagri.infra.repository.VehicleRepository;

/**
 * Motor de Glosa — Middleware de Auditoria Independente (AP 04).
 *
 * Status possíveis:
 *   APROVADO           — passou em todas as regras
 *   GLOSADO            — falhou em geofencing ou volumétrica ou KSD
 *   ALERTA             — não foi possível verificar automaticamente (GPS offline, veículo sem cadastro)
 *
 * Regras:
 *   1. Geofencing       — distância veículo vs posto > 500m → GLOSADO
 *   2. Volumétrica      — volume > tankCapacity → GLOSADO
 *   3. KSD (consumo)    — (KM_Atual - KM_Anterior) / Litros < minKmPerLiter → GLOSADO
 */
@Service
public class GlosaService {

    private static final double GEOFENCING_LIMITE_METROS = 500.0;
    private static final double AUTONOMIA_MIN_FALLBACK   = 1.0;   // fallback se veículo sem minKmPerLiter
    private static final double AUTONOMIA_MAX_KM_POR_LITRO = 25.0;

    @Autowired private GlosaRepository    glosaRepository;
    @Autowired private VehicleRepository  vehicleRepository;
    @Autowired private SupplyRepository   supplyRepository;
    @Autowired private HashService        hashService;

    // ── API pública ──────────────────────────────────────────────────────────

    public List<GlosaResultDTO> processar(List<GlosaTransactionInputDTO> transacoes, String processedBy) {
        List<GlosaResultDTO> resultados = new ArrayList<>();
        for (GlosaTransactionInputDTO t : transacoes) {
            resultados.add(GlosaResultDTO.from(processarUma(t, processedBy)));
        }
        return resultados;
    }

    public List<GlosaResultDTO> listarTodos() {
        return glosaRepository.findAll().stream().map(GlosaResultDTO::from).toList();
    }

    public List<GlosaResultDTO> listarPorPlaca(String placa) {
        return glosaRepository.findByPlacaOrderByProcessedAtDesc(placa).stream()
                .map(GlosaResultDTO::from).toList();
    }

    public GlosaDashboardDTO dashboard() {
        long total    = glosaRepository.count();
        long aprovado = glosaRepository.countByStatus("APROVADO");
        long glosado  = glosaRepository.countByStatus("GLOSADO");
        long alerta   = glosaRepository.countByStatus("ALERTA");

        BigDecimal valorGlosado = glosaRepository.sumValorGlosado();
        if (valorGlosado == null) valorGlosado = BigDecimal.ZERO;

        double conformidade = total == 0 ? 100.0 : (aprovado * 100.0) / total;

        return new GlosaDashboardDTO(total, aprovado, glosado, alerta,
                glosado + alerta, valorGlosado, conformidade);
    }

    // ── Motor interno ────────────────────────────────────────────────────────

    private GlosaRecord processarUma(GlosaTransactionInputDTO input, String processedBy) {
        List<String> violacoes = new ArrayList<>();
        String status = "APROVADO";
        boolean isReserva = input.isReservaTransito();

        LocalDateTime timestamp = parseTimestamp(input.timestamp());
        Vehicle vehicle = vehicleRepository.findByLicensePlate(input.placa()).orElse(null);

        if (isReserva) {
            violacoes.add("RESERVA_TRANSITO: volume registrado como estoque vinculado à OS (KSD e tanque ignorados)");
        }

        // ── REGRA 1: Geofencing — aplica-se SEMPRE, inclusive para Reserva em Trânsito
        if (input.veiculoCoordenadas() != null && input.postoCoordenadas() != null) {
            double distancia = haversineMetros(
                    input.veiculoCoordenadas().lat(), input.veiculoCoordenadas().lng(),
                    input.postoCoordenadas().lat(),   input.postoCoordenadas().lng());
            if (distancia > GEOFENCING_LIMITE_METROS) {
                violacoes.add("GEOFENCING: veículo a " + (int) distancia + "m do posto (limite "
                        + (int) GEOFENCING_LIMITE_METROS + "m) — Abastecimento Fantasma");
                status = "GLOSADO";
            }
        } else {
            violacoes.add("GPS offline: geofencing não verificado");
            if (status.equals("APROVADO")) status = "ALERTA";
        }

        // ── REGRA 2: Capacidade Volumétrica — IGNORADA para Reserva em Trânsito
        if (!isReserva) {
            if (vehicle != null && vehicle.getTankCapacity() != null) {
                double capacidade = vehicle.getTankCapacity().doubleValue();
                if (input.volumeLitros() > capacidade) {
                    violacoes.add(String.format(
                            "VOLUMETRICO: %.2fL supera capacidade do tanque %.2fL — Risco de Desvio",
                            input.volumeLitros(), capacidade));
                    if (!status.equals("GLOSADO")) status = "GLOSADO";
                }
            } else {
                String motivo = vehicle == null ? "Placa não cadastrada" : "Capacidade do tanque não cadastrada";
                violacoes.add("VOLUMETRICO: " + motivo + " — revisão manual necessária");
                if (status.equals("APROVADO")) status = "ALERTA";
            }
        }

        // ── REGRA 3: KSD — Consumo Médio (Km/L) — IGNORADA para Reserva em Trânsito
        if (!isReserva && input.odomteroInformado() != null) {
            List<Supply> anteriores = supplyRepository.findLatestSupplyByPlate(
                    input.placa(), timestamp, PageRequest.of(0, 1));

            if (!anteriores.isEmpty()) {
                Supply anterior = anteriores.get(0);
                if (anterior.getHodometro() != null && input.odomteroInformado() > anterior.getHodometro()) {
                    double km = input.odomteroInformado() - anterior.getHodometro();
                    double ksd = km / input.volumeLitros();

                    double minKsd = (vehicle != null && vehicle.getMinKmPerLiter() != null)
                            ? vehicle.getMinKmPerLiter().doubleValue()
                            : AUTONOMIA_MIN_FALLBACK;

                    if (ksd < minKsd || ksd > AUTONOMIA_MAX_KM_POR_LITRO) {
                        violacoes.add(String.format(
                                "KSD: %.2f km/L incompatível com média mínima do veículo (%.2f km/L) — Consumo Anômalo",
                                ksd, minKsd));
                        if (!status.equals("GLOSADO")) status = "GLOSADO";
                    }
                } else if (anterior.getHodometro() != null
                        && input.odomteroInformado() <= anterior.getHodometro()) {
                    violacoes.add(String.format("KSD: odômetro informado (%d) ≤ anterior (%d)",
                            input.odomteroInformado(), anterior.getHodometro()));
                    if (status.equals("APROVADO")) status = "ALERTA";
                }
            }
        }

        // ── Timestamp atômico: sempre do servidor, nunca do dispositivo ─────
        LocalDateTime serverTimestamp = LocalDateTime.now(ZoneId.of("America/Rio_Branco"));

        // ── Hash de Fé Pública com encadeamento ──────────────────────────────
        String observacao = violacoes.isEmpty() ? "Todos os critérios aprovados."
                : String.join(" | ", violacoes);

        String previousHash = getLastRecordHash();
        String hashInput = previousHash + "|" + input.transacaoId() + "|" + input.placa()
                + "|" + serverTimestamp + "|" + status;
        String integrityHash = hashService.generate(hashInput);

        BigDecimal postoLat  = input.postoCoordenadas() != null
                ? BigDecimal.valueOf(input.postoCoordenadas().lat()) : null;
        BigDecimal postoLng  = input.postoCoordenadas() != null
                ? BigDecimal.valueOf(input.postoCoordenadas().lng()) : null;
        BigDecimal valorTotal = input.valorTotal() != null
                ? BigDecimal.valueOf(input.valorTotal()) : null;

        String combustivelLabel = isReserva
                ? "RESERVA_TRANSITO — " + (input.combustivel() != null ? input.combustivel() : "N/I")
                : input.combustivel();

        GlosaRecord record = new GlosaRecord(
                input.transacaoId(), timestamp, input.placa(),
                input.postoCnpj(), postoLat, postoLng,
                combustivelLabel, BigDecimal.valueOf(input.volumeLitros()),
                input.odomteroInformado(), valorTotal,
                status, observacao, integrityHash,
                serverTimestamp, serverTimestamp, processedBy);

        return glosaRepository.save(record);
    }

    private String getLastRecordHash() {
        return glosaRepository.findTopByOrderByIdDesc()
                .map(GlosaRecord::getIntegrityHash)
                .orElse("GENESIS");
    }

    // ── Utilitários ──────────────────────────────────────────────────────────

    private double haversineMetros(double lat1, double lng1, double lat2, double lng2) {
        final double R = 6_371_000.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        try {
            return LocalDateTime.ofInstant(Instant.parse(timestamp), ZoneId.of("America/Rio_Branco"));
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
