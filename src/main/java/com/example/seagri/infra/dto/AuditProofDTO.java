package com.example.seagri.infra.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Item 15 — "Prova de Fogo" (AP 04).
 *
 * Consolidates the four pillars required by the client:
 *   1. The data inserted (transaction/asset record)
 *   2. Photo/document reference (attachment path/URL)
 *   3. GPS coordinates (location on map)
 *   4. SHA-256 integrity hash
 *
 * Used by both fuel (Glosa) and patrimony modules.
 */
public record AuditProofDTO(
    String proofType,
    String recordId,
    LocalDateTime serverTimestamp,
    AuditProofData data,
    AuditProofGps gps,
    AuditProofAttachment attachment,
    AuditProofIntegrity integrity,
    List<Map<String, Object>> auditTrail
) {

    public record AuditProofData(
        String module,
        String entityType,
        Map<String, Object> fields
    ) {}

    public record AuditProofGps(
        BigDecimal latitude,
        BigDecimal longitude,
        String source,
        boolean verified
    ) {}

    public record AuditProofAttachment(
        String type,
        String path,
        boolean present
    ) {}

    public record AuditProofIntegrity(
        String algorithm,
        String hash,
        String previousHash,
        boolean chainValid
    ) {}
}
