package com.dinet.pedidos.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cargas_idempotencia",
        uniqueConstraints = @UniqueConstraint(columnNames = {"idempotency_key", "archivo_hash"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdempotenciaEntity {
    @Id
    private UUID id;

    @Column(name = "idempotency_key")
    private String idempotencyKey;

    @Column(name = "archivo_hash")
    private String archivoHash;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
