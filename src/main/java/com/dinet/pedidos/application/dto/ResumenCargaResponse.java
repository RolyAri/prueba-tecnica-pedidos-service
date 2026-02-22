package com.dinet.pedidos.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ResumenCargaResponse {
    private int totalProcesados;
    private int guardados;
    private int conError;

    private List<ErrorDetalle> errores;

    private Map<String, Long> erroresAgrupados;
}
