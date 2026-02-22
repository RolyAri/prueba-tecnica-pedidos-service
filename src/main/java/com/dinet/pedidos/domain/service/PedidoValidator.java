package com.dinet.pedidos.domain.service;

import com.dinet.pedidos.domain.exception.BusinessException;
import com.dinet.pedidos.domain.model.Cliente;
import com.dinet.pedidos.domain.model.Pedido;
import com.dinet.pedidos.domain.model.Zona;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
@Component
public class PedidoValidator {
    public void validar(Pedido pedido,
                        Cliente cliente,
                        Zona zona,
                        Set<String> pedidosExistentes) {

        if (!pedido.getNumeroPedido().matches("^[a-zA-Z0-9]+$")) {
            throw new BusinessException("NUMERO_INVALIDO");
        }

        if (pedidosExistentes.contains(pedido.getNumeroPedido())) {
            throw new BusinessException("DUPLICADO");
        }

        if (cliente == null || !cliente.isActivo()) {
            throw new BusinessException("CLIENTE_NO_ENCONTRADO");
        }

        if (zona == null) {
            throw new BusinessException("ZONA_INVALIDA");
        }

        if (pedido.getFechaEntrega()
                .isBefore(LocalDate.now(ZoneId.of("America/Lima")))) {
            throw new BusinessException("FECHA_INVALIDA");
        }

        if (pedido.isRequiereRefrigeracion() &&
                !zona.isSoporteRefrigeracion()) {
            throw new BusinessException("CADENA_FRIO_NO_SOPORTADA");
        }
    }

}
