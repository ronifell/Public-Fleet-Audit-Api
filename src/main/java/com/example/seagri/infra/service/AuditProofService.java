package com.example.seagri.infra.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.audit.AuditLog;
import com.example.seagri.infra.audit.AuditLogRepository;
import com.example.seagri.infra.dto.AuditProofDTO;
import com.example.seagri.infra.dto.AuditProofDTO.*;
import com.example.seagri.infra.integrity.HashService;
import com.example.seagri.infra.model.GlosaRecord;
import com.example.seagri.infra.model.PatrimonyAsset;
import com.example.seagri.infra.model.PatrimonyTransfer;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.repository.GlosaRepository;
import com.example.seagri.infra.repository.PatrimonyAssetRepository;
import com.example.seagri.infra.repository.PatrimonyTransferRepository;
import com.example.seagri.infra.repository.SupplyRepository;

/**
 * Item 15 — Serviço "Prova de Fogo" (AP 04).
 *
 * Gera um relatório consolidado de auditoria que reúne os 4 pilares exigidos:
 *   1. O dado inserido
 *   2. A foto/documento anexado
 *   3. A localização GPS
 *   4. O código hash SHA-256
 *
 * Suporta registros de Glosa (combustível) e Patrimônio.
 */
@Service
public class AuditProofService {

    @Autowired private GlosaRepository glosaRepository;
    @Autowired private SupplyRepository supplyRepository;
    @Autowired private PatrimonyAssetRepository patrimonyAssetRepository;
    @Autowired private PatrimonyTransferRepository patrimonyTransferRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private HashService hashService;

    /**
     * Generates the "Prova de Fogo" for a GlosaRecord (fuel module).
     */
    public AuditProofDTO proofForGlosa(Long glosaId) {
        GlosaRecord r = glosaRepository.findById(glosaId)
                .orElseThrow(() -> new IllegalArgumentException("GlosaRecord not found: " + glosaId));

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("transacao_id", r.getTransacaoId());
        fields.put("placa", r.getPlaca());
        fields.put("combustivel", r.getCombustivel());
        fields.put("volume_litros", r.getVolumeLitros());
        fields.put("valor_total", r.getValorTotal());
        fields.put("odometro", r.getOdomteroInformado());
        fields.put("posto_cnpj", r.getPostoCnpj());
        fields.put("glosa_status", r.getGlosaStatus());
        fields.put("observacao", r.getObservacao());
        fields.put("transacao_timestamp_cliente", r.getTransacaoTimestamp());
        fields.put("server_timestamp", r.getServerTimestamp());
        fields.put("processed_at", r.getProcessedAt());
        fields.put("processed_by", r.getProcessedBy());

        AuditProofData data = new AuditProofData("SIG-FROTA", "GlosaRecord", fields);

        boolean gpsPresent = r.getPostoLat() != null && r.getPostoLng() != null;
        AuditProofGps gps = new AuditProofGps(
                r.getPostoLat(), r.getPostoLng(),
                gpsPresent ? "POSTO_GPS" : "NAO_DISPONIVEL",
                gpsPresent);

        String attachmentPath = findSupplyPhoto(r.getTransacaoId());
        AuditProofAttachment attachment = new AuditProofAttachment(
                "foto_abastecimento",
                attachmentPath,
                attachmentPath != null && !attachmentPath.isBlank());

        String previousHash = findPreviousGlosaHash(r.getId());
        boolean chainOk = verifyGlosaHashAtPosition(r, previousHash);
        AuditProofIntegrity integrity = new AuditProofIntegrity(
                "SHA-256", r.getIntegrityHash(), previousHash, chainOk);

        List<Map<String, Object>> trail = buildAuditTrail("GlosaRecord", glosaId);

        return new AuditProofDTO(
                "PROVA_DE_FE_PUBLICA",
                "GLOSA-" + glosaId,
                r.getServerTimestamp() != null ? r.getServerTimestamp() : r.getProcessedAt(),
                data, gps, attachment, integrity, trail);
    }

    /**
     * Generates the "Prova de Fogo" for a PatrimonyAsset.
     */
    public AuditProofDTO proofForAsset(String tombo) {
        PatrimonyAsset a = patrimonyAssetRepository.findByTombo(tombo)
                .orElseThrow(() -> new IllegalArgumentException("Bem não encontrado: " + tombo));

        Map<String, Object> fields = new LinkedHashMap<>();
        fields.put("tombo", a.getTombo());
        fields.put("descricao", a.getDescricao());
        fields.put("categoria", a.getCategoria());
        fields.put("responsavel", a.getResponsavel());
        fields.put("situacao", a.getSituacao());
        fields.put("valor_patrimonial", a.getValorPatrimonial());
        fields.put("conservacao_percent", a.getConservacaoPercent());
        fields.put("data_registro", a.getDataRegistro());

        AuditProofData data = new AuditProofData("SIG-PATRIMONIO", "PatrimonyAsset", fields);

        boolean gpsPresent = a.getLat() != null && a.getLng() != null;
        AuditProofGps gps = new AuditProofGps(
                a.getLat(), a.getLng(),
                gpsPresent ? "GPS_VISTORIA" : "NAO_DISPONIVEL",
                gpsPresent);

        AuditProofAttachment attachment = new AuditProofAttachment(
                "documento_cautela",
                a.getDocumentoAnexo(),
                a.getDocumentoAnexo() != null && !a.getDocumentoAnexo().isBlank());

        String previousHash = findPreviousAssetHash(a.getId());
        boolean chainOk = verifyAssetHashAtPosition(a, previousHash);
        AuditProofIntegrity integrity = new AuditProofIntegrity(
                "SHA-256", a.getIntegrityHash(), previousHash, chainOk);

        List<Map<String, Object>> trail = buildAuditTrail("PatrimonyAsset", a.getId());

        List<PatrimonyTransfer> transfers = patrimonyTransferRepository
                .findByTomboOrderByDataTransferenciaDesc(tombo);
        List<Map<String, Object>> transferHistory = new ArrayList<>();
        for (PatrimonyTransfer t : transfers) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("data", t.getDataTransferencia());
            row.put("remetente", t.getRemetenteNome() + " (" + t.getRemetenteUnidade() + ")");
            row.put("destinatario", t.getDestinatarioNome() + " (" + t.getDestinatarioUnidade() + ")");
            row.put("motivo", t.getMotivo());
            row.put("hash", t.getIntegrityHash());
            transferHistory.add(row);
        }

        List<Map<String, Object>> fullTrail = new ArrayList<>(trail);
        if (!transferHistory.isEmpty()) {
            Map<String, Object> transferSection = new LinkedHashMap<>();
            transferSection.put("tipo", "HISTORICO_TRANSFERENCIAS");
            transferSection.put("registros", transferHistory);
            fullTrail.add(transferSection);
        }

        return new AuditProofDTO(
                "PROVA_DE_FE_PUBLICA",
                "PATRIMONIO-" + tombo,
                a.getDataRegistro(),
                data, gps, attachment, integrity, fullTrail);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String findSupplyPhoto(String transacaoId) {
        if (transacaoId == null) return null;
        // Supply entities may have a photo path; future enhancement can
        // store photo_path in Supply. For now, return the transacaoId as reference.
        return null;
    }

    private String findPreviousGlosaHash(Long currentId) {
        List<GlosaRecord> all = glosaRepository.findAll().stream()
                .filter(r -> r.getId() < currentId)
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .toList();
        return all.isEmpty() ? "GENESIS" : all.get(0).getIntegrityHash();
    }

    private boolean verifyGlosaHashAtPosition(GlosaRecord r, String previousHash) {
        String hashInput = previousHash + "|" + r.getTransacaoId() + "|" + r.getPlaca()
                + "|" + (r.getServerTimestamp() != null ? r.getServerTimestamp().toString() : "")
                + "|" + r.getGlosaStatus();
        String expected = hashService.generate(hashInput);
        return expected.equals(r.getIntegrityHash());
    }

    private String findPreviousAssetHash(Long currentId) {
        List<PatrimonyAsset> all = patrimonyAssetRepository.findAll().stream()
                .filter(a -> a.getId() < currentId)
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .toList();
        return all.isEmpty() ? "GENESIS" : all.get(0).getIntegrityHash();
    }

    private boolean verifyAssetHashAtPosition(PatrimonyAsset asset, String previousHash) {
        String expected = hashService.generate(previousHash + "|" + asset.toHashString());
        return expected.equals(asset.getIntegrityHash());
    }

    private List<Map<String, Object>> buildAuditTrail(String entityType, Long entityId) {
        List<AuditLog> logs = auditLogRepository
                .findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
        List<Map<String, Object>> trail = new ArrayList<>();
        for (AuditLog log : logs) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("timestamp", log.getTimestamp());
            entry.put("action", log.getAction());
            entry.put("user", log.getUserLogin());
            entry.put("description", log.getDescription());
            entry.put("hash", log.getIntegrityHash());
            trail.add(entry);
        }
        return trail;
    }
}
