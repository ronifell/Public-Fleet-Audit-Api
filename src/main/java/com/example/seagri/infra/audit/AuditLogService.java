package com.example.seagri.infra.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.seagri.infra.integrity.HashService;

/**
 * Serviço responsável por persistir e consultar a trilha de auditoria.
 *
 * Registros de auditoria são INSERT-only: não existe método de
 * atualização ou deleção exposto, garantindo imutabilidade.
 */
@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository repository;

    @Autowired
    private HashService hashService;

    /**
     * Grava um registro de auditoria com seu próprio hash de integridade.
     */
    public AuditLog record(String entityType, Long entityId, String action,
                           Long userId, String userLogin, String description) {
        AuditLog log = new AuditLog(entityType, entityId, action, userId, userLogin, description);

        String previousHash = repository.findTopByOrderByIdDesc()
                .map(AuditLog::getIntegrityHash)
                .orElse("GENESIS");

        String hashInput = String.join("|",
                previousHash,
                entityType,
                entityId    != null ? entityId.toString()  : "",
                action,
                userId      != null ? userId.toString()     : "",
                userLogin   != null ? userLogin             : "",
                description != null ? description           : "",
                log.getTimestamp().toString()
        );
        log.setIntegrityHash(hashService.generate(hashInput));

        return repository.save(log);
    }

    /** Retorna todos os registros de uma entidade específica, ordenados por data. */
    public List<AuditLog> getByEntity(String entityType, Long entityId) {
        return repository.findByEntityTypeAndEntityIdOrderByTimestampDesc(entityType, entityId);
    }

    /** Retorna todos os registros de um usuário específico, paginados. */
    public Page<AuditLog> getByUser(String userLogin, Pageable pageable) {
        return repository.findByUserLoginOrderByTimestampDesc(userLogin, pageable);
    }

    /** Retorna todos os registros de um tipo de entidade, paginados. */
    public Page<AuditLog> getByEntityType(String entityType, Pageable pageable) {
        return repository.findByEntityTypeOrderByTimestampDesc(entityType, pageable);
    }

    /** Retorna todos os registros, paginados. */
    public Page<AuditLog> getAll(Pageable pageable) {
        return repository.findAllByOrderByTimestampDesc(pageable);
    }
}
