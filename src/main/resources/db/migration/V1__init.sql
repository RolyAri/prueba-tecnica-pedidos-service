CREATE TABLE pedidos (
    id UUID PRIMARY KEY,
    numero_pedido VARCHAR(50) UNIQUE,
    cliente_id VARCHAR(50),
    zona_id VARCHAR(50),
    fecha_entrega DATE,
    estado VARCHAR(20) CHECK (estado IN ('PENDIENTE','CONFIRMADO','ENTREGADO')),
    requiere_refrigeracion BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_estado_fecha
ON pedidos (estado, fecha_entrega);

CREATE TABLE clientes (
    id VARCHAR(50) PRIMARY KEY,
    activo BOOLEAN
);

CREATE TABLE zonas (
    id VARCHAR(50) PRIMARY KEY,
    soporte_refrigeracion BOOLEAN
);

CREATE TABLE cargas_idempotencia (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(100),
    archivo_hash VARCHAR(255),
    created_at TIMESTAMP,
    UNIQUE (idempotency_key, archivo_hash)
);
