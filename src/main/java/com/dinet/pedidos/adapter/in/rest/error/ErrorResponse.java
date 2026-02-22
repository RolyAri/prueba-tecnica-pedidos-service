package com.dinet.pedidos.adapter.in.rest.error;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private List<String> details;
    private String correlationId;
}
