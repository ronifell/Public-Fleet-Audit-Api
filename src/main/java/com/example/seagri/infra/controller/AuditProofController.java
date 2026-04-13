package com.example.seagri.infra.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.dto.AuditProofDTO;
import com.example.seagri.infra.service.AuditProofService;

/**
 * Item 15 — Endpoint "Prova de Fogo" (AP 04).
 *
 * Retorna um relatório consolidado de auditoria com os 4 pilares:
 *   1. Dado inserido (campos do registro)
 *   2. Foto/documento anexado (referência)
 *   3. Localização GPS (coordenadas verificadas)
 *   4. Hash SHA-256 de autenticidade (com validação da cadeia)
 *
 * Se TODOS os 4 pilares estiverem presentes, o registro é considerado
 * conforme o protocolo AP 04 de Fé Pública. Caso contrário, é AP 03
 * (cadastro comum).
 *
 * Endpoints:
 *   GET /audit/proof/glosa/{id}         — prova de fogo de um registro de glosa
 *   GET /audit/proof/patrimonio/{tombo} — prova de fogo de um bem patrimonial
 */
@RestController
@RequestMapping("/audit/proof")
public class AuditProofController {

    @Autowired
    private AuditProofService service;

    @GetMapping("/glosa/{id}")
    public ResponseEntity<?> proofForGlosa(@PathVariable Long id) {
        try {
            AuditProofDTO proof = service.proofForGlosa(id);
            return ResponseEntity.ok(Map.of(
                    "proof", proof,
                    "ap04_compliant", isAp04Compliant(proof),
                    "classification", isAp04Compliant(proof)
                            ? "FE_PUBLICA_AP04" : "CADASTRO_COMUM_AP03"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/patrimonio/{tombo}")
    public ResponseEntity<?> proofForPatrimonio(@PathVariable String tombo) {
        try {
            AuditProofDTO proof = service.proofForAsset(tombo);
            return ResponseEntity.ok(Map.of(
                    "proof", proof,
                    "ap04_compliant", isAp04Compliant(proof),
                    "classification", isAp04Compliant(proof)
                            ? "FE_PUBLICA_AP04" : "CADASTRO_COMUM_AP03"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    /**
     * A record is AP 04 compliant only when ALL four pillars are present:
     *   1. Data — always present if the record exists
     *   2. GPS — verified coordinates present
     *   3. Attachment — photo or document present
     *   4. Hash — SHA-256 present and chain valid
     */
    private boolean isAp04Compliant(AuditProofDTO proof) {
        return proof.gps().verified()
                && proof.attachment().present()
                && proof.integrity().hash() != null
                && proof.integrity().chainValid();
    }
}
