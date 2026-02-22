package com.dinet.pedidos.adapter.out.persistence.adapter;

import com.dinet.pedidos.adapter.out.persistence.entity.IdempotenciaEntity;
import com.dinet.pedidos.adapter.out.persistence.repository.IdempotenciaJpaRepository;
import com.dinet.pedidos.domain.port.out.IdempotenciaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdempotenciaPersistenceAdapter implements IdempotenciaRepositoryPort {
    private final IdempotenciaJpaRepository repository;

    @Override
    public boolean exists(String key, String hash) {
        return repository.existsByIdempotencyKeyAndArchivoHash(key, hash);
    }

    @Override
    public void save(String key, String hash) {
        IdempotenciaEntity entity = IdempotenciaEntity.builder()
                .id(UUID.randomUUID())
                .idempotencyKey(key)
                .archivoHash(hash)
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(entity);
    }
}
