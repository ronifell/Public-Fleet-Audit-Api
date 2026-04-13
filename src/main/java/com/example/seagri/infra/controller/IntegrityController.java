package com.example.seagri.infra.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.integrity.IntegrityVerificationService;

/**
 * Item 11 — Endpoint de verificação de integridade (AP 04).
 *
 * Percorre todas as tabelas com hash SHA-256 e detecta:
 *   - Registros com hash adulterado (dado alterado diretamente no banco)
 *   - Cadeias de hash quebradas (registro deletado ou reordenado)
 *
 * GET /integrity/verify — relatório completo de integridade
 */
@RestController
@RequestMapping("/integrity")
public class IntegrityController {

    @Autowired
    private IntegrityVerificationService verificationService;

    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyIntegrity() {
        return ResponseEntity.ok(verificationService.verifyAll());
    }
}
