package com.dinet.pedidos.domain.model;

import java.util.UUID;

public class Idempotencia {
    private UUID id;
    private String idempotencyKey;
    private String archivoHash;
    private java.time.LocalDateTime createdAt;

    public Idempotencia(java.util.UUID id, String idempotencyKey, String archivoHash, java.time.LocalDateTime createdAt) {
        this.id = id;
        this.idempotencyKey = idempotencyKey;
        this.archivoHash = archivoHash;
        this.createdAt = createdAt;
    }

    public java.util.UUID getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public String getArchivoHash() {
        return archivoHash;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
