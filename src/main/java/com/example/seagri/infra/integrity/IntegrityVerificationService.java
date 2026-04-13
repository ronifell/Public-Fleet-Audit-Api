package com.example.seagri.infra.integrity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.audit.AuditLog;
import com.example.seagri.infra.audit.AuditLogRepository;
import com.example.seagri.infra.model.GlosaRecord;
import com.example.seagri.infra.model.PatrimonyAsset;
import com.example.seagri.infra.model.PatrimonyTransfer;
import com.example.seagri.infra.model.Supply;
import com.example.seagri.infra.model.VehicleTravel;
import com.example.seagri.infra.repository.GlosaRepository;
import com.example.seagri.infra.repository.PatrimonyAssetRepository;
import com.example.seagri.infra.repository.PatrimonyTransferRepository;
import com.example.seagri.infra.repository.SupplyRepository;
import com.example.seagri.infra.repository.VehicleTravelRepository;

/**
 * Item 11 — Serviço de verificação proativa de integridade.
 *
 * Percorre as tabelas que possuem integrityHash e recalcula cada hash
 * para detectar adulterações diretas no banco de dados.
 * Também verifica a cadeia de hashes (encadeamento) das tabelas append-only
 * (GlosaRecord e AuditLog).
 */
@Service
public class IntegrityVerificationService {

    @Autowired private HashService hashService;
    @Autowired private SupplyRepository supplyRepository;
    @Autowired private VehicleTravelRepository vehicleTravelRepository;
    @Autowired private GlosaRepository glosaRepository;
    @Autowired private AuditLogRepository auditLogRepository;
    @Autowired private PatrimonyAssetRepository patrimonyAssetRepository;
    @Autowired private PatrimonyTransferRepository patrimonyTransferRepository;

    /**
     * Executa verificação completa de todas as entidades com integridade hash.
     * Retorna um relatório por tabela com contagens e detalhes de registros adulterados.
     */
    public Map<String, Object> verifyAll() {
        Map<String, Object> report = new HashMap<>();
        report.put("timestamp", java.time.LocalDateTime.now().toString());

        report.put("supplies", verifyHashableEntities("SUPPLIES",
                supplyRepository.findAll()));
        report.put("vehicle_travels", verifyHashableEntities("VEHICLE_TRAVELS",
                vehicleTravelRepository.findAll()));
        report.put("patrimony_assets", verifyHashableEntities("PATRIMONY_ASSETS",
                patrimonyAssetRepository.findAll()));
        report.put("patrimony_transfers", verifyHashableEntities("PATRIMONY_TRANSFERS",
                patrimonyTransferRepository.findAll()));
        report.put("glosa_records", verifyGlosaChain());
        report.put("audit_logs", verifyAuditLogChain());

        return report;
    }

    /**
     * Verifies entities that implement Hashable by recalculating each hash.
     */
    private <T extends Hashable> Map<String, Object> verifyHashableEntities(
            String tableName, List<T> entities) {
        int total = entities.size();
        int valid = 0;
        int tampered = 0;
        int noHash = 0;
        List<Map<String, Object>> tamperedRecords = new ArrayList<>();

        for (T entity : entities) {
            String storedHash = extractHash(entity);
            if (storedHash == null || storedHash.isBlank()) {
                noHash++;
                continue;
            }
            if (hashService.verify(entity, storedHash)) {
                valid++;
            } else {
                tampered++;
                Map<String, Object> detail = new HashMap<>();
                detail.put("id", extractId(entity));
                detail.put("stored_hash", storedHash);
                detail.put("expected_hash", hashService.generate(entity));
                tamperedRecords.add(detail);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("table", tableName);
        result.put("total", total);
        result.put("valid", valid);
        result.put("tampered", tampered);
        result.put("no_hash", noHash);
        result.put("integrity_ok", tampered == 0);
        if (!tamperedRecords.isEmpty()) {
            result.put("tampered_records", tamperedRecords);
        }
        return result;
    }

    /**
     * Verifica a cadeia encadeada de GlosaRecord.
     * Recalcula o hash de cada registro usando o hash do registro anterior.
     */
    private Map<String, Object> verifyGlosaChain() {
        List<GlosaRecord> records = glosaRepository.findAll()
                .stream().sorted((a, b) -> Long.compare(a.getId(), b.getId())).toList();

        int total = records.size();
        int valid = 0;
        int tampered = 0;
        List<Map<String, Object>> brokenLinks = new ArrayList<>();

        String previousHash = "GENESIS";
        for (GlosaRecord r : records) {
            String hashInput = previousHash + "|" + r.getTransacaoId() + "|" + r.getPlaca()
                    + "|" + (r.getTransacaoTimestamp() != null ? r.getTransacaoTimestamp().toString() : "")
                    + "|" + r.getGlosaStatus();
            String expectedHash = hashService.generate(hashInput);

            if (expectedHash.equals(r.getIntegrityHash())) {
                valid++;
            } else {
                tampered++;
                Map<String, Object> detail = new HashMap<>();
                detail.put("id", r.getId());
                detail.put("transacao_id", r.getTransacaoId());
                detail.put("stored_hash", r.getIntegrityHash());
                detail.put("expected_hash", expectedHash);
                detail.put("chain_broken_at", r.getId());
                brokenLinks.add(detail);
            }
            previousHash = r.getIntegrityHash();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("table", "GLOSA_RECORDS");
        result.put("total", total);
        result.put("valid", valid);
        result.put("chain_breaks", tampered);
        result.put("integrity_ok", tampered == 0);
        if (!brokenLinks.isEmpty()) {
            result.put("broken_links", brokenLinks);
        }
        return result;
    }

    /**
     * Verifica a cadeia encadeada de AuditLog.
     */
    private Map<String, Object> verifyAuditLogChain() {
        List<AuditLog> logs = auditLogRepository.findAll()
                .stream().sorted((a, b) -> Long.compare(a.getId(), b.getId())).toList();

        int total = logs.size();
        int valid = 0;
        int tampered = 0;
        List<Map<String, Object>> brokenLinks = new ArrayList<>();

        String previousHash = "GENESIS";
        for (AuditLog log : logs) {
            String hashInput = String.join("|",
                    previousHash,
                    log.getEntityType(),
                    log.getEntityId() != null ? log.getEntityId().toString() : "",
                    log.getAction(),
                    log.getUserId() != null ? log.getUserId().toString() : "",
                    log.getUserLogin() != null ? log.getUserLogin() : "",
                    log.getDescription() != null ? log.getDescription() : "",
                    log.getTimestamp().toString());
            String expectedHash = hashService.generate(hashInput);

            if (expectedHash.equals(log.getIntegrityHash())) {
                valid++;
            } else {
                tampered++;
                Map<String, Object> detail = new HashMap<>();
                detail.put("id", log.getId());
                detail.put("entity_type", log.getEntityType());
                detail.put("stored_hash", log.getIntegrityHash());
                detail.put("expected_hash", expectedHash);
                brokenLinks.add(detail);
            }
            previousHash = log.getIntegrityHash();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("table", "AUDIT_LOG");
        result.put("total", total);
        result.put("valid", valid);
        result.put("chain_breaks", tampered);
        result.put("integrity_ok", tampered == 0);
        if (!brokenLinks.isEmpty()) {
            result.put("broken_links", brokenLinks);
        }
        return result;
    }

    // ── Reflection helpers ───────────────────────────────────────────────────

    private String extractHash(Object entity) {
        try {
            return (String) entity.getClass().getMethod("getIntegrityHash").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private Object extractId(Object entity) {
        try {
            return entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return "?";
        }
    }
}
