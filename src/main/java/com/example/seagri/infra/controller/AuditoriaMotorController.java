package com.example.seagri.infra.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.service.AuditoriaMotorService;

/**
 * REST endpoints for Milestone 1 & 2 (Motor de Auditoria SEAGRI).
 * Serves data from DB.json located in the Backend project root.
 *
 * All endpoints are publicly accessible (no authentication required)
 * to match the frontend milestone-demo and inova-thec pages.
 */
@RestController
@RequestMapping("/auditoria/motor")
public class AuditoriaMotorController {

    @Autowired
    private AuditoriaMotorService service;

    /**
     * Returns the full audit data set:
     * abastecimentos + resultados_motor_glosa + resumo_dashboard.
     * This is the primary endpoint consumed by the milestone1-demo
     * and inova-thec-governanca frontend components.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDadosCompletos() {
        Map<String, Object> result = new HashMap<>();
        result.put("abastecimentos", service.getAbastecimentos());
        result.put("resultados_motor_glosa", service.getResultadosMotorGlosa());
        result.put("resumo_dashboard", service.getResumoDashboard());
        result.put("bens_patrimonio", service.getBensPatrimonio());
        result.put("transferencias_patrimonio", service.getTransferenciasPatrimonio());
        result.put("cadeia_custodia_patrimonio", service.getCadeiaCustodiaPatrimonio());
        result.put("resumo_patrimonio", service.getResumoPatrimonio());
        return ResponseEntity.ok(result);
    }

    /**
     * Returns only the abastecimentos (fuel supply transactions).
     */
    @GetMapping("/abastecimentos")
    public ResponseEntity<List<Map<String, Object>>> getAbastecimentos() {
        return ResponseEntity.ok(service.getAbastecimentos());
    }

    /**
     * Returns only the glosa engine results.
     */
    @GetMapping("/glosa-resultados")
    public ResponseEntity<List<Map<String, Object>>> getGlosaResultados() {
        return ResponseEntity.ok(service.getResultadosMotorGlosa());
    }

    /**
     * Returns glosa results filtered by plate (placa).
     */
    @GetMapping("/glosa-resultados/{placa}")
    public ResponseEntity<List<Map<String, Object>>> getGlosaResultadosPorPlaca(
            @PathVariable String placa) {
        List<Map<String, Object>> filtered = service.getResultadosMotorGlosa().stream()
                .filter(r -> placa.equalsIgnoreCase((String) r.get("placa")))
                .toList();
        return ResponseEntity.ok(filtered);
    }

    /**
     * Returns the dashboard summary (ROI, totals, savings).
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboard() {
        return ResponseEntity.ok(service.getResumoDashboard());
    }

    /**
     * Returns the audit trail records.
     */
    @GetMapping("/trilha-auditoria")
    public ResponseEntity<List<Map<String, Object>>> getTrilhaAuditoria() {
        return ResponseEntity.ok(service.getTrilhaAuditoria());
    }

    @GetMapping("/patrimonio/bens")
    public ResponseEntity<List<Map<String, Object>>> getBensPatrimonio() {
        return ResponseEntity.ok(service.getBensPatrimonio());
    }

    @GetMapping("/patrimonio/transferencias")
    public ResponseEntity<List<Map<String, Object>>> getTransferencias() {
        return ResponseEntity.ok(service.getTransferenciasPatrimonio());
    }

    @GetMapping("/patrimonio/custodia")
    public ResponseEntity<List<Map<String, Object>>> getCadeiaCustodia() {
        return ResponseEntity.ok(service.getCadeiaCustodiaPatrimonio());
    }

    @GetMapping("/patrimonio/resumo")
    public ResponseEntity<Map<String, Object>> getResumoPatrimonio() {
        return ResponseEntity.ok(service.getResumoPatrimonio());
    }

    @PostMapping("/reload")
    public ResponseEntity<Map<String, String>> reload() {
        service.reload();
        return ResponseEntity.ok(Map.of("status", "DB.json reloaded successfully"));
    }
}
