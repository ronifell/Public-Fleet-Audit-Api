package com.example.seagri.infra.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class BaseEntity implements Serializable {
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    
    // Fui aplicar com User mas deu merda

    // @ManyToOne()
    // @JoinColumn(name = "created_by_user_id", nullable = false, updatable = false)
    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Long createdBy;

    // @ManyToOne()
    // @JoinColumn(name = "updated_by_user_id", nullable = false)
    @LastModifiedBy
    @Column(nullable = false)
    private Long updatedBy;
}
