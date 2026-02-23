# 📦 Microservicio de Carga de Pedidos

Microservicio desarrollado en **Java 17 + Spring Boot 3** que permite
cargar pedidos desde un archivo CSV, validarlos y persistirlos
utilizando Arquitectura Hexagonal, procesamiento eficiente en batch e
idempotencia.

------------------------------------------------------------------------

## 🏗 Arquitectura

El proyecto sigue el patrón **Arquitectura Hexagonal (Ports &
Adapters)** para desacoplar la lógica de negocio de la infraestructura.

Estructura:

-   domain → Entidades, reglas de negocio, puertos
-   application → Casos de uso
-   adapter.in → REST Controllers, filtros, manejo de errores
-   adapter.out → Persistencia JPA
-   config → Seguridad y configuración

Beneficios:

-   Separación clara de responsabilidades
-   Dominio independiente de frameworks
-   Fácil testeo unitario
-   Bajo acoplamiento

------------------------------------------------------------------------

## ⚙ Tecnologías

-   Java 17
-   Spring Boot 3
-   Spring Data JPA
-   PostgreSQL
-   Flyway
-   Apache Commons CSV
-   Spring Security (OAuth2 Resource Server)
-   JWT
-   Logback + logstash encoder (logs JSON)
-   JUnit 5 + Mockito

------------------------------------------------------------------------

## 🚀 Ejecución Local

### 1️⃣ Levantar base de datos

docker-compose up -d

Base de datos: - DB: pedidosdb - Usuario: pedidos - Password: pedidos -
Puerto: 5431

### 2️⃣ Ejecutar aplicación

mvn clean install\
mvn spring-boot:run

------------------------------------------------------------------------

## 📌 Endpoint Principal

### POST /pedidos/cargar

Carga pedidos desde un archivo CSV.

Headers requeridos:

Authorization: Bearer `<JWT>`\
Idempotency-Key: `<valor-unico>`\
X-Correlation-Id: `<opcional>`

Body:

multipart/form-data\
file: sample2.csv (ubicado en src/main/resources/samples/)

Archivos de ejemplo disponibles:

- sample1.csv → 100 registros
- sample2.csv → 1000 registros

------------------------------------------------------------------------

## 📘 Documentación API (OpenAPI / Swagger)

El microservicio expone documentación automática mediante **OpenAPI 3**
utilizando **springdoc-openapi**.

------------------------------------------------------------------------

### 🔎 Swagger UI

Interfaz gráfica interactiva disponible en:

http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------

### 📄 Especificación OpenAPI (JSON)

http://localhost:8080/v3/api-docs

------------------------------------------------------------------------

## 📄 Formato CSV

numeroPedido,clienteId,fechaEntrega,estado,zonaEntrega,requiereRefrigeracion\
P001,CLI-123,2026-08-10,PENDIENTE,ZONA1,true

Validaciones:

-   numeroPedido → alfanumérico y único
-   clienteId → debe existir y estar activo
-   fechaEntrega → no puede ser pasada (America/Lima)
-   estado → PENDIENTE \| CONFIRMADO \| ENTREGADO
-   zonaEntrega → debe existir
-   Si requiereRefrigeracion = true → zona debe soportar refrigeración

------------------------------------------------------------------------

## 📊 Respuesta

{ "totalProcesados": 100, "guardados": 95, "conError": 5, "errores": \[
{ "linea": 10, "codigo": "CLIENTE_NO_ENCONTRADO" } \],
"erroresAgrupados": { "CLIENTE_NO_ENCONTRADO": 3, "ZONA_INVALIDA": 2 } }

------------------------------------------------------------------------

## 🔁 Estrategia de Batch

-   Procesamiento streaming con Apache Commons CSV
-   Persistencia por lotes configurables
-   Tamaño configurable: app.batch-size: 500
-   Hibernate batch_size: 500

------------------------------------------------------------------------

## 🔐 Seguridad

El microservicio actúa como OAuth2 Resource Server.

-   No emite tokens.
-   Valida JWT firmados externamente.
-   Todas las rutas están protegidas.

------------------------------------------------------------------------

## 🔐 Generación de JWT para Pruebas (Postman)

👉 https://jwt.io

### Configuración

- Algoritmo: `HS256`
- Secret (solo para entorno de desarrollo):

```
mi-clave-super-secreta-para-dev-que-tenga-32-bytes
```

> ⚠ **Importante:**  
> Este secret es únicamente para pruebas en entorno local.  
> **Nunca debe exponerse públicamente ni versionarse en repositorios.**
>
> 🔐 Buenas prácticas recomiendan:
> - Usar variables de entorno  
> - No compartir secrets en documentación pública  

---

### Payload de ejemplo

```json
{
  "sub": "usuario-test",
  "scope": "ROLE_USER",
  "iat": 1735689600,
  "exp": 1893456000
}
```

> Asegúrate de que `exp` sea mayor al timestamp actual.

---

### Usar en Postman

En la request:

- Ir a **Authorization**
- Tipo: **Bearer Token**
- Pegar el token generado

O agregar manualmente el header:

```
Authorization: Bearer TU_TOKEN_GENERADO
```

---

------------------------------------------------------------------------

## 📦 Colección Postman

También se incluye la colección:

```
Pedidos Service.postman_collection.json
```

Puedes importarla directamente en Postman:

1. Abrir Postman  
2. Clic en **Import**  
3. Seleccionar el archivo `Pedidos Service.postman_collection.json`

> ℹ La colección ya incluye un **Bearer Token por defecto** para facilitar las pruebas iniciales.  
> Si el token expira, puedes generar uno nuevo siguiendo la sección anterior y reemplazarlo en la configuración de Authorization.

------------------------------------------------------------------------

## 🔁 Idempotencia

Implementación:

-   Header obligatorio: Idempotency-Key
-   Cálculo SHA-256 del archivo recibido
-   Tabla cargas_idempotencia
-   Restricción UNIQUE (idempotency_key, archivo_hash)
-   Manejo de concurrencia delegada a la base de datos

------------------------------------------------------------------------

## 📈 Observabilidad

-   Logs estructurados en formato JSON
-   CorrelationId propagado en request/response
-   Manejo global de errores estandarizado

------------------------------------------------------------------------

## 🧪 Testing

-   Unit tests para dominio
-   Unit tests para aplicación con Mockito
-   Cobertura superior al 80%

Ejecutar:

mvn test
