package com.dinet.pedidos.domain.port.out;

import com.dinet.pedidos.domain.model.Zona;

import java.util.Map;

public interface ZonaRepositoryPort {
    Map<String, Zona> findAllToMap();
}
