package com.example.seagri.infra.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.dto.PatrimonyAssetInputDTO;
import com.example.seagri.infra.dto.PatrimonyTransferInputDTO;
import com.example.seagri.infra.model.PatrimonyAsset;
import com.example.seagri.infra.model.PatrimonyTransfer;
import com.example.seagri.infra.service.PatrimonyService;

/**
 * API do módulo de Patrimônio (AP 04).
 *
 * Endpoints:
 *   POST /patrimonio/bens                     — cadastra novo bem patrimonial
 *   GET  /patrimonio/bens                     — lista todos os bens
 *   GET  /patrimonio/bens/{tombo}             — busca bem pelo tombo
 *   PUT  /patrimonio/bens/{tombo}/situacao     — atualiza situação (inclui regra de baixa com doc obrigatório)
 *   POST /patrimonio/transferencias            — registra transferência de custódia
 *   GET  /patrimonio/transferencias            — lista todas as transferências
 *   GET  /patrimonio/transferencias/{tombo}    — transferências de um bem específico
 *   GET  /patrimonio/dashboard                 — resumo do módulo
 *   POST /patrimonio/reconciliacao             — reconcilia inventário teórico vs real
 */
@RestController
@RequestMapping("/patrimonio")
public class PatrimonyController {

    @Autowired
    private PatrimonyService service;

    // ── Bens patrimoniais ────────────────────────────────────────────────────

    @PostMapping("/bens")
    public ResponseEntity<PatrimonyAsset> createAsset(@RequestBody PatrimonyAssetInputDTO input) {
        return ResponseEntity.ok(service.saveAsset(input));
    }

    @GetMapping("/bens")
    public ResponseEntity<List<PatrimonyAsset>> listAssets() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/bens/{tombo}")
    public ResponseEntity<PatrimonyAsset> getByTombo(@PathVariable String tombo) {
        return service.findByTombo(tombo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Item 7 — Atualiza a situação de um bem.
     * Se a nova situação for "BAIXA", o campo documento_anexo é OBRIGATÓRIO.
     *
     * Body esperado: { "situacao": "BAIXA", "documento_anexo": "/uploads/laudo_123.pdf" }
     */
    @PutMapping("/bens/{tombo}/situacao")
    public ResponseEntity<?> updateSituacao(
            @PathVariable String tombo,
            @RequestBody Map<String, String> body) {
        try {
            String novaSituacao = body.get("situacao");
            String documentoAnexo = body.get("documento_anexo");
            PatrimonyAsset updated = service.updateSituacao(tombo, novaSituacao, documentoAnexo);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // ── Transferências ───────────────────────────────────────────────────────

    @PostMapping("/transferencias")
    public ResponseEntity<PatrimonyTransfer> createTransfer(@RequestBody PatrimonyTransferInputDTO input) {
        try {
            return ResponseEntity.ok(service.saveTransfer(input));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/transferencias")
    public ResponseEntity<List<PatrimonyTransfer>> listTransfers() {
        return ResponseEntity.ok(service.listTransfers());
    }

    @GetMapping("/transferencias/{tombo}")
    public ResponseEntity<List<PatrimonyTransfer>> listTransfersByTombo(@PathVariable String tombo) {
        return ResponseEntity.ok(service.listTransfersByTombo(tombo));
    }

    // ── Dashboard ────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> dashboard() {
        return ResponseEntity.ok(service.dashboard());
    }

    // ── Item 8 — Reconciliação de inventário ─────────────────────────────────

    /**
     * Recebe uma lista de tombos encontrados durante a vistoria (inventário real)
     * e compara com o cadastro teórico (bens ATIVO).
     *
     * Body esperado: ["TOMBO-001", "TOMBO-002", "TOMBO-999"]
     */
    @PostMapping("/reconciliacao")
    public ResponseEntity<Map<String, Object>> reconciliar(@RequestBody List<String> tombosVistoriados) {
        return ResponseEntity.ok(service.reconciliarInventario(tombosVistoriados));
    }
}
