package com.dinet.pedidos.adapter.in.rest.error;

import com.dinet.pedidos.domain.exception.BusinessException;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        String correlationId = MDC.get("correlationId");

        ErrorResponse error = ErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .message(ex.getMessage())
                .details(List.of())
                .correlationId(correlationId)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
