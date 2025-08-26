## restaurants-service (Spring Boot, Hexagonal)

-   Java 17 · Spring Boot 3 · Gradle 8
-   Arquitectura Hexagonal (domain, application, infrastructure)
-   OpenAPI 3.0 (springdoc)
-   Seguridad: JWT validado en el Gateway; autorización por roles con headers `X-User-*`
-   Testing: JUnit 5, WebMvc slice, ArchUnit, JaCoCo (≥80%)

### Funcionalidad cubierta (Historias de Usuario)

-   HU-002: Crear Restaurante (ADMIN)
-   HU-003: Crear Plato (OWNER)
-   HU-004: Modificar Plato (precio y descripción) (OWNER)
-   HU-007: Habilitar/Deshabilitar Plato (OWNER)
-   HU-009: Listar Restaurantes (público, paginado, alfabético)
-   HU-010: Listar Platos por Restaurante (público, solo activos, filtro categoría, paginado)

### Endpoints principales

-   Restaurants
    -   POST `/api/v1/restaurants` — Crear restaurante (rol: ADMIN)
    -   GET `/api/v1/restaurants?page=0&size=10` — Listar restaurantes (público; devuelve `{ name, logoUrl }`)
-   Plates
    -   POST `/api/v1/plates` — Crear plato (rol: OWNER del restaurante)
    -   PUT `/api/v1/plates/{id}` — Modificar precio y descripción (rol: OWNER)
    -   PATCH `/api/v1/plates/{id}/status` — Habilitar/Deshabilitar (rol: OWNER)
    -   GET `/api/v1/plates/restaurant/{id}?category=PRINCIPAL&page=0&size=10` — Listar platos activos (público)

Documentación OpenAPI: ver `docs/openapi/plazoleta.yaml`.

### Seguridad

-   El API Gateway valida el JWT y propaga `X-User-Id`, `X-User-Email`, `X-User-Role`.
-   Este servicio usa `HeaderAuthenticationFilter` y `@PreAuthorize`:
    -   `ADMIN` para `POST /restaurants`
    -   `OWNER` para `POST /plates`, `PUT /plates/{id}`, `PATCH /plates/{id}/status`
    -   Público: `GET /restaurants` y `GET /plates/restaurant/**`

### Ejecución local

```powershell
.\gradlew.bat spotlessApply
.\gradlew.bat test jacocoTestReport
.\gradlew.bat bootRun
# Swagger UI: http://localhost:8082/swagger-ui/index.html
```

Variables relevantes (application.yml/env)

-   `SPRING_APPLICATION_NAME=restaurants-service`
-   `PORT=8082`
-   `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

### Arquitectura (resumen)

-   Domain
    -   Modelos: `RestaurantModel`, `PlateModel`, `PlateCategory`
    -   Casos de uso: `RestaurantUseCase` (crear restaurante), `PlateUseCase` (crear/actualizar/estado), `RestaurantQueryUseCase` (listar restaurantes), `PlateQueryUseCase` (listar platos)
    -   Puertos IN: `IRestaurantServicePort`, `IPlateServicePort`, `IRestaurantQueryServicePort`, `IPlateQueryServicePort`
    -   Puertos OUT: `IRestaurantPersistencePort`, `IPlatePersistencePort`, `IRestaurantQueryPort`, `IPlateQueryPort`, `IUserServicePort`
-   Application
    -   Handlers: `RestaurantHandler`, `PlateHandler`
    -   DTOs con Bean Validation; mappers MapStruct (DTO↔Model)
-   Infrastructure
    -   REST: `RestaurantRestController`, `PlateRestController`
    -   JPA: `RestaurantJpaAdapter`, `RestaurantQueryJpaAdapter`, `PlateJpaAdapter`, `PlateQueryJpaAdapter`
    -   Security: `SecurityConfiguration`, `HeaderAuthenticationFilter`, `RoleConstants`
    -   Beans: `BeanConfiguration`

### Tests

-   Dominio: `CreateRestaurantUseCaseTest`, `CreatePlateUseCaseTest`, `ListRestaurantsUseCaseTest`, `ListPlatesByRestaurantUseCaseTest`
-   WebMvc: `RestaurantRestControllerWebMvcTest`, `PlateRestControllerWebMvcTest`
-   Arquitectura: `HexagonalArchitectureTest`

### Convenciones y CI

-   Formato: Spotless (Google Java Format)
-   Cobertura: JaCoCo ≥ 80% (`.\gradlew.bat check`)
-   Validación OpenAPI: `.\gradlew.bat openApiValidateAll`
-   Ramas sugeridas: `feature/HU-xxx-descripcion` y tag al merge de cada HU

### Referencias

-   Diagramas HU: `docs/diagrams/HU/` (HU002, HU003, HU004, HU007, HU009, HU010)
-   Requisitos: `docs/Requirements.md`
-   Guía ampliada: `docs/README.microservicios.md`

### Integraciones con otros microservicios

-   Dependencias síncronas (salientes)

    -   users-service (requerido): validaciones de usuarios/roles para operaciones de restaurantes
        -   ¿Para qué? Validar que `ownerId` tenga rol OWNER; validar usuarios activos si se requiere.
        -   Implementación pendiente en este servicio: reemplazar el stub `UsersServiceAdapter` por un cliente Feign.
        -   Requisitos:
            -   Dependencia: `org.springframework.cloud:spring-cloud-starter-openfeign`
            -   Configuración `application.yml` (timeouts y URL):
                ```yaml
                feign:
                    client:
                        config:
                            default:
                                connectTimeout: 3000
                                readTimeout: 5000
                                loggerLevel: basic
                microservices:
                    users:
                        url: ${MICROSERVICES_USERS_URL:http://localhost:8081}
                ```
            -   Variables de entorno: `MICROSERVICES_USERS_URL=http://localhost:8081`
            -   Interfaz Feign sugerida:
                ```java
                @FeignClient(name = "users-service", url = "${microservices.users.url}")
                interface UsersServiceClient {
                  @GetMapping("/api/v1/usuarios/{id}")
                  UsuarioResponseDto getUser(@PathVariable Long id);
                  @GetMapping("/api/v1/usuarios/{id}/activo")
                  Boolean isActive(@PathVariable Long id);
                }
                ```

-   Endpoints expuestos para otros servicios

    -   orders-service: usa `GET /api/v1/plates/restaurant/{id}` para obtener menú activo por restaurante.
    -   Frontends/Gateway: `GET /api/v1/restaurants` para catálogo de restaurantes.

-   Seguridad y Gateway

    -   Requiere que el Gateway valide JWT y propague `X-User-*`.
    -   Este servicio asume esos headers para autorización (`@PreAuthorize`).

-   Asíncrono (mensajería)

    -   No aplica en este servicio (no publica ni consume eventos actualmente).

-   Opcional (si usas Service Discovery/Config Server)

    -   Service Discovery (Eureka): registrar el servicio y usar `lb://users-service` en lugar de URL fija.
    -   Config Server: externalizar propiedades y rutas Feign.

-   Faltante para producción (checklist)
    -   [ ] Agregar `spring-cloud-starter-openfeign` y el cliente a users-service
    -   [ ] Manejo de errores/resiliencia (fallbacks o circuit breaker si aplica)
    -   [ ] Métricas y logs de integraciones (timings/errores Feign)
