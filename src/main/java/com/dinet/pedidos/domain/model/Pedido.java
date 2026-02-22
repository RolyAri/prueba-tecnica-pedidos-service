package com.dinet.pedidos.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public class Pedido {
    private UUID id;
    private String numeroPedido;
    private String clienteId;
    private String zonaId;
    private LocalDate fechaEntrega;
    private EstadoPedido estado;
    private boolean requiereRefrigeracion;

    public Pedido(UUID id, String numeroPedido, String clienteId, String zonaId, LocalDate fechaEntrega, EstadoPedido estado, boolean requiereRefrigeracion) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.clienteId = clienteId;
        this.zonaId = zonaId;
        this.fechaEntrega = fechaEntrega;
        this.estado = estado;
        this.requiereRefrigeracion = requiereRefrigeracion;
    }

    public UUID getId() {
        return id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public String getClienteId() {
        return clienteId;
    }

    public String getZonaId() {
        return zonaId;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public boolean isRequiereRefrigeracion() {
        return requiereRefrigeracion;
    }
}
