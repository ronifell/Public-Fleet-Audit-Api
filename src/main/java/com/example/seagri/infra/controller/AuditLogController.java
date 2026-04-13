package com.example.seagri.infra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.seagri.infra.audit.AuditLog;
import com.example.seagri.infra.audit.AuditLogService;

/**
 * Endpoints de consulta à trilha de auditoria.
 *
 * Não existe endpoint de escrita, atualização ou deleção —
 * a escrita é feita de forma transparente pelo AuditAspect.
 */
@RestController
@RequestMapping("/audit")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    /** Lista todos os logs, paginados e ordenados do mais recente para o mais antigo. */
    @GetMapping
    public ResponseEntity<Page<AuditLog>> getAll(
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAll(pageable));
    }

    /** Lista os logs de uma entidade específica (ex: Supply, Maintenance, VehicleTravel). */
    @GetMapping("/entity/{type}/{id}")
    public ResponseEntity<List<AuditLog>> getByEntity(
            @PathVariable String type,
            @PathVariable Long id) {
        return ResponseEntity.ok(auditLogService.getByEntity(type, id));
    }

    /** Lista os logs de todos os registros de um tipo de entidade. */
    @GetMapping("/entity/{type}")
    public ResponseEntity<Page<AuditLog>> getByEntityType(
            @PathVariable String type,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getByEntityType(type, pageable));
    }

    /** Lista os logs de um usuário específico. */
    @GetMapping("/user")
    public ResponseEntity<Page<AuditLog>> getByUser(
            @RequestParam String login,
            @PageableDefault(size = 50) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getByUser(login, pageable));
    }
}
