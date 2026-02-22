package com.dinet.pedidos.adapter.out.persistence.adapter;

import com.dinet.pedidos.adapter.out.persistence.entity.ClienteEntity;
import com.dinet.pedidos.adapter.out.persistence.repository.ClienteJpaRepository;
import com.dinet.pedidos.domain.model.Cliente;
import com.dinet.pedidos.domain.port.out.ClienteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientePersistenceAdapter implements ClienteRepositoryPort {

    private final ClienteJpaRepository repository;

    @Override
    public Map<String, Cliente> findAllToMap() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(
                        ClienteEntity::getId,
                        e -> new Cliente(e.getId(), e.getActivo())
                ));
    }
}
