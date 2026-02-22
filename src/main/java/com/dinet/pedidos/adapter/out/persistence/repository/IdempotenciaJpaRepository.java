package com.dinet.pedidos.adapter.out.persistence.repository;

import com.dinet.pedidos.adapter.out.persistence.entity.IdempotenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IdempotenciaJpaRepository extends JpaRepository<IdempotenciaEntity, UUID> {
    boolean existsByIdempotencyKeyAndArchivoHash(String idempotencyKey, String archivoHash);
}
