package com.dinet.pedidos.adapter.out.persistence.adapter;

import com.dinet.pedidos.adapter.out.persistence.entity.PedidoEntity;
import com.dinet.pedidos.adapter.out.persistence.repository.PedidoJpaRepository;
import com.dinet.pedidos.domain.model.Pedido;
import com.dinet.pedidos.domain.port.out.PedidoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PedidoPersistenceAdapter implements PedidoRepositoryPort {
    private final PedidoJpaRepository repository;

    @Override
    public void saveAll(List<Pedido> pedidos) {

        List<PedidoEntity> entities = pedidos.stream()
                .map(this::toEntity)
                .toList();

        repository.saveAll(entities);
    }

    @Override
    public boolean existsByNumeroPedido(String numeroPedido) {
        return repository.existsByNumeroPedido(numeroPedido);
    }

    private PedidoEntity toEntity(Pedido pedido) {
        return PedidoEntity.builder()
                .id(UUID.randomUUID())
                .numeroPedido(pedido.getNumeroPedido())
                .clienteId(pedido.getClienteId())
                .zonaId(pedido.getZonaId())
                .fechaEntrega(pedido.getFechaEntrega())
                .estado(pedido.getEstado().name())
                .requiereRefrigeracion(pedido.isRequiereRefrigeracion())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
