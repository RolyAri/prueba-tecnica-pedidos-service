package com.dinet.pedidos.domain.port.out;

import com.dinet.pedidos.domain.model.Pedido;

import java.util.List;

public interface PedidoRepositoryPort {
    void saveAll(List<Pedido> pedidos);
    boolean existsByNumeroPedido(String numeroPedido);
}
