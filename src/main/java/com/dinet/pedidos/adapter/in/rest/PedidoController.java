package com.dinet.pedidos.adapter.in.rest;

import com.dinet.pedidos.application.dto.ResumenCargaResponse;
import com.dinet.pedidos.domain.port.in.CargarPedidosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final CargarPedidosUseCase cargarPedidosUseCase;
    @PostMapping("/cargar")
    public ResponseEntity<ResumenCargaResponse> cargar(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestParam("file") MultipartFile file
    ){
        return ResponseEntity.ok(
                cargarPedidosUseCase.cargar(file, idempotencyKey)
        );
    }
}
