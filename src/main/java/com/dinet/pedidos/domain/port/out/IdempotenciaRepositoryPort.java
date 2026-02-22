package com.dinet.pedidos.domain.port.out;

public interface IdempotenciaRepositoryPort {
    boolean exists(String key, String hash);

    void save(String key, String hash);
}
