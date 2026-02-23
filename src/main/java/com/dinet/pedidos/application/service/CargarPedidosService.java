package com.dinet.pedidos.application.service;

import com.dinet.pedidos.application.dto.ErrorDetalle;
import com.dinet.pedidos.application.dto.ResumenCargaResponse;
import com.dinet.pedidos.domain.exception.BusinessException;
import com.dinet.pedidos.domain.model.Cliente;
import com.dinet.pedidos.domain.model.EstadoPedido;
import com.dinet.pedidos.domain.model.Pedido;
import com.dinet.pedidos.domain.model.Zona;
import com.dinet.pedidos.domain.port.in.CargarPedidosUseCase;
import com.dinet.pedidos.domain.port.out.ClienteRepositoryPort;
import com.dinet.pedidos.domain.port.out.IdempotenciaRepositoryPort;
import com.dinet.pedidos.domain.port.out.PedidoRepositoryPort;
import com.dinet.pedidos.domain.port.out.ZonaRepositoryPort;
import com.dinet.pedidos.domain.service.PedidoValidator;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CargarPedidosService implements CargarPedidosUseCase {
    private final PedidoRepositoryPort pedidoRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final ZonaRepositoryPort zonaRepository;
    private final IdempotenciaRepositoryPort idempotenciaRepository;
    private final PedidoValidator validator;

    @Value("${app.batch-size}")
    @Setter
    private int batchSize;

    @Override
    public ResumenCargaResponse cargar(MultipartFile file, String idempotencyKey) {
        log.info("Iniciando carga de pedidos. idempotencyKey={}", idempotencyKey);
        try {
            byte[] bytes = file.getBytes();
            String hash = calcularHash(bytes);

            if (idempotenciaRepository.exists(idempotencyKey, hash)) {
                log.info("Idempotencia detectada, no se reprocesa");
                return ResumenCargaResponse.builder()
                        .totalProcesados(0)
                        .guardados(0)
                        .conError(0)
                        .errores(List.of())
                        .erroresAgrupados(Map.of())
                        .build();
            }

            Map<String, Cliente> clientes = clienteRepository.findAllToMap();
            Map<String, Zona> zonas = zonaRepository.findAllToMap();

            List<Pedido> buffer = new ArrayList<>();
            List<ErrorDetalle> errores = new ArrayList<>();
            Set<String> pedidosProcesados = new HashSet<>();

            int total = 0;
            int guardados = 0;

            try (CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new InputStreamReader(file.getInputStream()))) {

                for (CSVRecord record : parser) {

                    total++;

                    try {
                        Pedido pedido = mapear(record);

                        if (pedidoRepository.existsByNumeroPedido(pedido.getNumeroPedido())) {
                            throw new BusinessException("PEDIDO_YA_EXISTE");
                        }

                        validator.validar(
                                pedido,
                                clientes.get(pedido.getClienteId()),
                                zonas.get(pedido.getZonaId()),
                                pedidosProcesados
                        );

                        pedidosProcesados.add(pedido.getNumeroPedido());

                        buffer.add(pedido);

                        if (buffer.size() >= batchSize) {
                            log.info("Persistiendo batch de {} registros", buffer.size());
                            pedidoRepository.saveAll(buffer);
                            guardados += buffer.size();
                            buffer.clear();
                        }

                    } catch (Exception e) {
                        errores.add(
                                ErrorDetalle.builder()
                                        .linea((int) record.getRecordNumber())
                                        .codigo(e.getMessage())
                                        .build()
                        );
                    }
                }
            }

            if (!buffer.isEmpty()) {
                log.info("Persistiendo batch de {} registros", buffer.size());
                pedidoRepository.saveAll(buffer);
                guardados += buffer.size();
            }

            idempotenciaRepository.save(idempotencyKey, hash);

            Map<String, Long> agrupados = errores.stream()
                    .collect(Collectors.groupingBy(
                            ErrorDetalle::getCodigo,
                            Collectors.counting()
                    ));

            return ResumenCargaResponse.builder()
                    .totalProcesados(total)
                    .guardados(guardados)
                    .conError(errores.size())
                    .errores(errores)
                    .erroresAgrupados(agrupados)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("ERROR_PROCESANDO_ARCHIVO", e);
        }
    }

    private Pedido mapear(CSVRecord record) {

        EstadoPedido estado;

        try {
            estado = EstadoPedido.valueOf(record.get("estado"));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new BusinessException(
                    "ESTADO_INVALIDO"
            );
        }

        return Pedido.builder()
                .numeroPedido(record.get("numeroPedido"))
                .clienteId(record.get("clienteId"))
                .zonaId(record.get("zonaEntrega"))
                .fechaEntrega(LocalDate.parse(record.get("fechaEntrega")))
                .estado(estado)
                .requiereRefrigeracion(Boolean.parseBoolean(record.get("requiereRefrigeracion")))
                .build();
    }

    private String calcularHash(byte[] fileBytes) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(fileBytes);
        return HexFormat.of().formatHex(hash);
    }

}
