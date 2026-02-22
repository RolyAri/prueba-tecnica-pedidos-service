package com.dinet.pedidos.domain.port.in;

import com.dinet.pedidos.application.dto.ResumenCargaResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CargarPedidosUseCase {
    ResumenCargaResponse cargar(MultipartFile file, String idempotencyKey);
}
