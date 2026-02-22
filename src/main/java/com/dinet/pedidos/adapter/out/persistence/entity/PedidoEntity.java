package com.dinet.pedidos.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoEntity {
    @Id
    private UUID id;

    @Column(name = "numero_pedido", unique = true)
    private String numeroPedido;

    @Column(name = "cliente_id")
    private String clienteId;

    @Column(name = "zona_id")
    private String zonaId;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "estado")
    private String estado;

    @Column(name = "requiere_refrigeracion")
    private Boolean requiereRefrigeracion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
