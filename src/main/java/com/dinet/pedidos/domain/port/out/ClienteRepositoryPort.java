package com.dinet.pedidos.domain.port.out;

import com.dinet.pedidos.domain.model.Cliente;

import java.util.Map;

public interface ClienteRepositoryPort {
    Map<String, Cliente> findAllToMap();
}
