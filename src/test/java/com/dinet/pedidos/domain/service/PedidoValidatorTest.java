package com.dinet.pedidos.domain.service;

import com.dinet.pedidos.domain.exception.BusinessException;
import com.dinet.pedidos.domain.model.Cliente;
import com.dinet.pedidos.domain.model.EstadoPedido;
import com.dinet.pedidos.domain.model.Pedido;
import com.dinet.pedidos.domain.model.Zona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PedidoValidatorTest {
    private PedidoValidator validator;
    private Cliente clienteActivo;
    private Zona zonaValida;
    @BeforeEach
    void setUp() {
        validator = new PedidoValidator();
        clienteActivo = new Cliente("CLI-123", true);
        zonaValida = new Zona("ZONA1", true);
    }

    private Pedido pedidoBase() {
        return Pedido.builder()
                .numeroPedido("P001")
                .clienteId("CLI-123")
                .zonaId("ZONA1")
                .fechaEntrega(LocalDate.now().plusDays(1))
                .estado(EstadoPedido.PENDIENTE)
                .requiereRefrigeracion(false)
                .build();
    }

    @Test
    void debeValidarPedidoCorrecto() {
        assertDoesNotThrow(() ->
                validator.validar(
                        pedidoBase(),
                        clienteActivo,
                        zonaValida,
                        Set.of()
                )
        );
    }

    @Test
    void debeFallarSiNumeroInvalido() {
        //Pedido pedido = pedidoBase();
        Pedido pedido = Pedido.builder()
                .numeroPedido("###")
                .clienteId("CLI-123")
                .zonaId("ZONA1")
                .fechaEntrega(LocalDate.now().plusDays(1))
                .estado(EstadoPedido.PENDIENTE)
                .requiereRefrigeracion(false)
                .build();

        assertThrows(BusinessException.class, () ->
                validator.validar(pedido, clienteActivo, zonaValida, Set.of())
        );
    }

    @Test
    void debeFallarSiDuplicado() {
        assertThrows(BusinessException.class, () ->
                validator.validar(
                        pedidoBase(),
                        clienteActivo,
                        zonaValida,
                        Set.of("P001")
                )
        );
    }

    @Test
    void debeFallarSiClienteNoExiste() {
        assertThrows(BusinessException.class, () ->
                validator.validar(
                        pedidoBase(),
                        null,
                        zonaValida,
                        Set.of()
                )
        );
    }

    @Test
    void debeFallarSiClienteInactivo() {
        Cliente inactivo = new Cliente("CLI-123", false);

        assertThrows(BusinessException.class, () ->
                validator.validar(
                        pedidoBase(),
                        inactivo,
                        zonaValida,
                        Set.of()
                )
        );
    }

    @Test
    void debeFallarSiZonaNoExiste() {
        assertThrows(BusinessException.class, () ->
                validator.validar(
                        pedidoBase(),
                        clienteActivo,
                        null,
                        Set.of()
                )
        );
    }

    @Test
    void debeFallarSiFechaEsPasada() {
        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .clienteId("CLI-123")
                .zonaId("ZONA1")
                .fechaEntrega(LocalDate.now().minusDays(1))
                .estado(EstadoPedido.PENDIENTE)
                .requiereRefrigeracion(false)
                .build();

        assertThrows(BusinessException.class, () ->
                validator.validar(pedido, clienteActivo, zonaValida, Set.of())
        );
    }

    @Test
    void debeFallarSiRefrigeracionNoSoportada() {
        Zona zonaSinFrio = new Zona("ZONA1", false);

        Pedido pedido = Pedido.builder()
                .numeroPedido("P001")
                .clienteId("CLI-123")
                .zonaId("ZONA1")
                .fechaEntrega(LocalDate.now().plusDays(1))
                .estado(EstadoPedido.PENDIENTE)
                .requiereRefrigeracion(true)
                .build();

        assertThrows(BusinessException.class, () ->
                validator.validar(pedido, clienteActivo, zonaSinFrio, Set.of())
        );
    }
}
