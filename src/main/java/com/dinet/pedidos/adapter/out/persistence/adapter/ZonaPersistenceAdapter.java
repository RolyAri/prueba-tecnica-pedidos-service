package com.dinet.pedidos.adapter.out.persistence.adapter;

import com.dinet.pedidos.adapter.out.persistence.entity.ClienteEntity;
import com.dinet.pedidos.adapter.out.persistence.entity.ZonaEntity;
import com.dinet.pedidos.adapter.out.persistence.repository.ZonaJpaRepository;
import com.dinet.pedidos.domain.model.Cliente;
import com.dinet.pedidos.domain.model.Zona;
import com.dinet.pedidos.domain.port.out.ZonaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ZonaPersistenceAdapter implements ZonaRepositoryPort{

    private final ZonaJpaRepository repository;

    @Override
    public Map<String, Zona> findAllToMap() {
        return repository.findAll().stream()
                .collect(Collectors.toMap(
                        ZonaEntity::getId,
                        e -> new Zona(e.getId(), e.getSoporteRefrigeracion())
                ));
    }

}
