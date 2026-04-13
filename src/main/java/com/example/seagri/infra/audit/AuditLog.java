package com.example.seagri.infra.audit;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Registro imutável de auditoria.
 *
 * Todos os campos são updatable = false — uma vez gravado,
 * nenhum campo pode ser alterado pelo JPA. Não expõe endpoint
 * de deleção, garantindo a imutabilidade da trilha.
 */
@Entity
@Table(name = "AUDIT_LOG")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome da entidade afetada, ex: "Supply", "VehicleTravel". */
    @Column(nullable = false, updatable = false, length = 60)
    private String entityType;

    /** ID do registro afetado (null para ações de login/logout). */
    @Column(updatable = false)
    private Long entityId;

    /** Ação executada: CREATE, UPDATE, DELETE, LOGIN. */
    @Column(nullable = false, updatable = false, length = 10)
    private String action;

    /** ID do usuário autenticado no momento da ação. */
    @Column(updatable = false)
    private Long userId;

    /** Login do usuário autenticado. */
    @Column(updatable = false, length = 100)
    private String userLogin;

    /** Descrição legível do que foi feito. */
    @Column(columnDefinition = "TEXT", updatable = false)
    private String description;

    /** Momento exato da ação (gerado no momento da criação). */
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    /** Hash SHA-256 deste próprio registro — garante a integridade do log. */
    @Column(columnDefinition = "CHAR(64)", updatable = false)
    private String integrityHash;

    public AuditLog() {}

    public AuditLog(String entityType, Long entityId, String action,
                    Long userId, String userLogin, String description) {
        this.entityType  = entityType;
        this.entityId    = entityId;
        this.action      = action;
        this.userId      = userId;
        this.userLogin   = userLogin;
        this.description = description;
        this.timestamp   = LocalDateTime.now();
    }

    // ── getters ─────────────────────────────────────────────────────────────

    public Long getId()            { return id; }
    public String getEntityType()  { return entityType; }
    public Long getEntityId()      { return entityId; }
    public String getAction()      { return action; }
    public Long getUserId()        { return userId; }
    public String getUserLogin()   { return userLogin; }
    public String getDescription() { return description; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getIntegrityHash()    { return integrityHash; }

    public void setIntegrityHash(String integrityHash) {
        this.integrityHash = integrityHash;
    }
}
