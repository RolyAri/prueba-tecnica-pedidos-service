package com.dinet.pedidos.application.service;

import com.dinet.pedidos.domain.model.Cliente;
import com.dinet.pedidos.domain.model.Zona;
import com.dinet.pedidos.domain.port.out.ClienteRepositoryPort;
import com.dinet.pedidos.domain.port.out.IdempotenciaRepositoryPort;
import com.dinet.pedidos.domain.port.out.PedidoRepositoryPort;
import com.dinet.pedidos.domain.port.out.ZonaRepositoryPort;
import com.dinet.pedidos.domain.service.PedidoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CargarPedidosServiceTest {
    @Mock
    private PedidoRepositoryPort pedidoRepository;

    @Mock
    private ClienteRepositoryPort clienteRepository;

    @Mock
    private ZonaRepositoryPort zonaRepository;

    @Mock
    private IdempotenciaRepositoryPort idempotenciaRepository;

    private PedidoValidator validator;

    private CargarPedidosService service;

    private int batchSize;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        validator = new PedidoValidator();

        service = new CargarPedidosService(
                pedidoRepository,
                clienteRepository,
                zonaRepository,
                idempotenciaRepository,
                validator
        );

        service.setBatchSize(2);
    }

    @Test
    void noDebeProcesarSiIdempotenciaExiste() throws Exception {

        when(idempotenciaRepository.exists(any(), any()))
                .thenReturn(true);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                "numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion\n"
                        .getBytes()
        );

        var response = service.cargar(file, "key-123");

        assertEquals(0, response.getTotalProcesados());
        verify(pedidoRepository, never()).saveAll(any());
    }

    @Test
    void debeGuardarPedidoValido() throws Exception {

        when(idempotenciaRepository.exists(any(), any()))
                .thenReturn(false);

        when(clienteRepository.findAllToMap())
                .thenReturn(Map.of("CLI-123", new Cliente("CLI-123", true)));

        when(zonaRepository.findAllToMap())
                .thenReturn(Map.of("ZONA1", new Zona("ZONA1", true)));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                ("numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion\n" +
                        "P001,CLI-123," + LocalDate.now().plusDays(1) +
                        ",PENDIENTE,ZONA1,false\n").getBytes()
        );

        var response = service.cargar(file, "key-123");

        assertEquals(1, response.getGuardados());

        verify(pedidoRepository, atLeastOnce()).saveAll(any());
        verify(idempotenciaRepository, times(1)).save(any(), any());
    }

    @Test
    void noDebeGuardarSiClienteNoExiste() throws Exception {

        when(idempotenciaRepository.exists(any(), any()))
                .thenReturn(false);

        when(clienteRepository.findAllToMap())
                .thenReturn(Map.of()); // Cliente no existe

        when(zonaRepository.findAllToMap())
                .thenReturn(Map.of("ZONA1", new Zona("ZONA1", true)));

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.csv",
                "text/csv",
                ("numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion\n" +
                        "P001,CLI-123," + LocalDate.now().plusDays(1) +
                        ",PENDIENTE,ZONA1,false\n").getBytes()
        );

        var response = service.cargar(file, "key-123");

        assertEquals(1, response.getConError());
        assertEquals(0, response.getGuardados());

        verify(pedidoRepository, never()).saveAll(any());
    }
}

