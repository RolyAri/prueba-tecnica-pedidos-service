package com.dinet.pedidos.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDetalle {
    private int linea;
    private String codigo;
}
