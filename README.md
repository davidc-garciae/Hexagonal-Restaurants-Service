# 🍽️ Restaurants Service - Microservicio de Gestión de Restaurantes

[![Java 17](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![OpenFeign](https://img.shields.io/badge/OpenFeign-HTTP%20Client-6DB33F?logo=spring&logoColor=white)](https://spring.io/projects/spring-cloud-openfeign)

## 🎯 Descripción

El **Restaurants Service** es el microservicio responsable de la gestión completa de restaurantes y sus menús en el sistema de Plazoleta de Comidas. Maneja la creación, modificación y consulta de restaurantes y platos con categorías predefinidas mediante enum.

## 🚀 Estado del Proyecto

✅ **COMPLETAMENTE FUNCIONAL**  
✅ **5 Endpoints** operativos  
✅ **Arquitectura Hexagonal** implementada  
✅ **OpenFeign Integration** con Users Service  
✅ **PostgreSQL** configurado  
✅ **JWT Authentication** distribuido

## 🏗️ Arquitectura

### Patrón Hexagonal (Ports & Adapters)

```
📦 src/main/java/com/pragma/powerup/
├── 🎯 domain/
│   ├── model/          # Entidades (Restaurant, Plate, PlateCategory)
│   ├── api/            # Puertos (interfaces)
│   └── usecase/        # Casos de uso de negocio
├── 🔌 infrastructure/
│   ├── input/          # Adaptadores REST Controllers
│   ├── output/         # Adaptadores JPA Repositories
│   ├── feign/          # Clientes OpenFeign
│   ├── configuration/  # Configuración Spring
│   └── security/       # Seguridad JWT
└── 🚀 application/     # DTOs, Handlers, Mappers, Utils
```

## 📊 Entidades del Dominio

### 🏪 Restaurante

```java
@Entity
public class Restaurant {
    private Long id;
    private String name;        // Nombre del restaurante
    private String address;     // Dirección física
    private String phone;       // Teléfono internacional
    private String logoUrl;     // URL del logo
    private String nit;         // NIT único
    private Long ownerId;       // ID del propietario
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 🍽️ Plato

```java
@Entity
public class Plate {
    private Long id;
    private String name;            // Nombre del plato
    private String description;     // Descripción (min 10 chars)
    private BigDecimal price;       // Precio (> 0)
    private String imageUrl;        // URL de la imagen
    private Boolean active;         // Estado activo/inactivo
    private Long restaurantId;      // ID del restaurante
    private PlateCategory category; // Categoría del plato (ENUM)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### 🏷️ PlateCategory (Enum)

```java
public enum PlateCategory {
    ENTRADA,    // Entradas y aperitivos
    PRINCIPAL,  // Platos principales
    POSTRE,     // Postres
    BEBIDA      // Bebidas
}
```

## 🌐 API Endpoints

**Base URL**: `http://localhost:8084/api/v1`

### 🏪 Gestión de Restaurantes

#### POST `/restaurants`

**Descripción**: Crear nuevo restaurante (HU-002)  
**Acceso**: 👑 Solo ADMIN

```bash
POST http://localhost:8084/api/v1/restaurants
Authorization: Bearer <ADMIN_JWT_TOKEN>
Content-Type: application/json

{
  "name": "Restaurante Gourmet",
  "address": "Calle 123 #45-67",
  "phone": "+573001234567",
  "logoUrl": "https://example.com/logo.png",
  "nit": "900123456-7",
  "ownerId": 2
}
```

**Response**:

```json
{
    "id": 3,
    "name": "Restaurante Gourmet",
    "ownerId": 2,
    "message": "Restaurante creado exitosamente"
}
```

#### GET `/restaurants`

**Descripción**: Listar restaurantes con paginación (HU-009)  
**Acceso**: 🌐 Público

```bash
GET http://localhost:8084/api/v1/restaurants?page=0&size=10&sortBy=name&sortDirection=asc
```

**Response**:

```json
{
    "content": [
        {
            "id": 1,
            "name": "El Buen Sabor",
            "logoUrl": "https://example.com/logo1.png"
        }
    ],
    "totalPages": 1,
    "totalElements": 1,
    "number": 0,
    "size": 10
}
```

#### GET `/restaurants/{id}`

**Descripción**: Obtener restaurante por ID  
**Acceso**: 🌐 Público

```bash
GET http://localhost:8084/api/v1/restaurants/1
```

### 🍽️ Gestión de Platos

#### POST `/plates`

**Descripción**: Crear nuevo plato (HU-003)  
**Acceso**: 🏪 Solo OWNER (del restaurante)

```bash
POST http://localhost:8084/api/v1/plates
Authorization: Bearer <OWNER_JWT_TOKEN>
Content-Type: application/json

{
  "name": "Hamburguesa Gourmet",
  "description": "Deliciosa hamburguesa con ingredientes premium",
  "price": 25000,
  "imageUrl": "https://example.com/hamburguesa.jpg",
  "restaurantId": 1,
  "category": "PRINCIPAL"
}
```

**Response**:

```json
{
    "id": 15,
    "name": "Hamburguesa Gourmet",
    "active": true,
    "message": "Plato creado exitosamente"
}
```

#### PUT `/plates/{id}`

**Descripción**: Modificar plato existente (HU-004)  
**Acceso**: 🏪 Solo OWNER (del restaurante)

```bash
PUT http://localhost:8084/api/v1/plates/15
Authorization: Bearer <OWNER_JWT_TOKEN>
Content-Type: application/json

{
  "description": "Nueva descripción del plato mejorada",
  "price": 28000
}
```

#### PATCH `/plates/{id}/status`

**Descripción**: Habilitar/Deshabilitar plato (HU-007)  
**Acceso**: 🏪 Solo OWNER (del restaurante)

```bash
PATCH http://localhost:8084/api/v1/plates/15/status
Authorization: Bearer <OWNER_JWT_TOKEN>
Content-Type: application/json

{
  "active": false
}
```

**Response**:

```json
{
    "id": 15,
    "active": false,
    "message": "Estado del plato actualizado"
}
```

#### GET `/plates/restaurant/{id}`

**Descripción**: Listar platos de restaurante con filtros (HU-010)  
**Acceso**: 🌐 Público

```bash
GET http://localhost:8084/api/v1/plates/restaurant/1?category=PRINCIPAL&page=0&size=10
```

**Response**:

```json
{
    "content": [
        {
            "id": 1,
            "name": "Hamburguesa Clásica",
            "description": "Hamburguesa tradicional",
            "price": 20000,
            "imageUrl": "https://example.com/hamburguesa.jpg",
            "active": true,
            "category": "PRINCIPAL"
        }
    ],
    "totalPages": 1,
    "totalElements": 1
}
```

## ✅ Validaciones Implementadas

### 🏪 Restaurantes

-   ✅ **Solo ADMIN** puede crear restaurantes
-   ✅ **NIT único** en el sistema
-   ✅ **Owner válido** (existe en Users Service)
-   ✅ **Formato teléfono** internacional
-   ✅ **Campos obligatorios** completos

### 🍽️ Platos

-   ✅ **Solo OWNER** del restaurante puede crear/modificar
-   ✅ **Precio positivo** (> 0)
-   ✅ **Descripción mínima** (10 caracteres)
-   ✅ **Categoría válida** (enum: ENTRADA, PRINCIPAL, POSTRE, BEBIDA)
-   ✅ **Restaurante válido** (pertenece al owner)

## 🔧 Configuración del Servicio

### Variables de Entorno (.env)

```properties
# Aplicación
SPRING_APPLICATION_NAME=restaurants-service
PORT=8084

# Base de Datos PostgreSQL
DB_URL=jdbc:postgresql://localhost:5432/restaurants_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_SCHEMA=public

# Microservicios
MICROSERVICES_USERS_URL=http://localhost:8081

# JWT Configuration (COMPARTIDO)
JWT_SECRET=change-me-change-me-change-me-change-me-change-me-change-me
JWT_EXPIRATION=86400000

# Logging
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_COM_PRAGMA=DEBUG
```

### Base de Datos

**PostgreSQL Database**: `restaurants_db`

```sql
-- Tabla de restaurantes
CREATE TABLE restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    logo_url VARCHAR(500),
    nit VARCHAR(20) UNIQUE NOT NULL,
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de platos
CREATE TABLE plates (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    image_url VARCHAR(500),
    active BOOLEAN DEFAULT true,
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id),
    category VARCHAR(20) NOT NULL CHECK (category IN ('ENTRADA', 'PRINCIPAL', 'POSTRE', 'BEBIDA')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para optimización
CREATE INDEX idx_restaurants_owner_id ON restaurants(owner_id);
CREATE INDEX idx_restaurants_nit ON restaurants(nit);
CREATE INDEX idx_plates_restaurant_id ON plates(restaurant_id);
CREATE INDEX idx_plates_category ON plates(category);
CREATE INDEX idx_plates_active ON plates(active);
```

CREATE INDEX idx_plates_active ON plates(active);

````

## 🔗 Integración con Otros Servicios

### OpenFeign Clients

#### Users Service Client

```java
@FeignClient(name = "users-service", url = "${microservices.users.url}")
public interface UserServiceClient {
    @GetMapping("/api/v1/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}
````

**Uso**: Validar que el propietario existe al crear restaurantes

## 🧪 Testing

### Datos de Prueba

**Restaurante de Prueba**:

-   **ID**: 1
-   **Nombre**: "El Buen Sabor"
-   **Owner ID**: 2 (owner@plazoleta.com)

**Platos de Prueba**:

-   **Hamburguesa Clásica** - $20,000 (Categoría: Hamburguesas)
-   **Pizza Margherita** - $35,000 (Categoría: Pizzas)

### Ejecutar Tests

```bash
# Tests unitarios
./gradlew test

# Tests de integración
./gradlew integrationTest

# Cobertura de código
./gradlew jacocoTestReport
```

### Casos de Prueba Críticos

```bash
# 1. Crear restaurante (solo ADMIN)
curl -X POST http://localhost:8084/api/v1/restaurants \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Nuevo Restaurante","ownerId":2}'

# 2. Crear plato (solo OWNER del restaurante)
curl -X POST http://localhost:8084/api/v1/plates \
  -H "Authorization: Bearer <OWNER_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Nuevo Plato","restaurantId":1}'

# 3. Listar restaurantes (público)
curl http://localhost:8084/api/v1/restaurants?page=0&size=5
```

## 🚀 Ejecución del Servicio

### Desarrollo Local

```bash
# 1. Clonar el repositorio
git clone <repository-url>
cd Hexagonal-Restaurants-Service

# 2. Configurar variables de entorno
cp .env.example .env
# Editar .env con los valores correctos

# 3. Iniciar PostgreSQL
docker run -d \
  --name postgres-restaurants \
  -e POSTGRES_DB=restaurants_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5433:5432 \
  postgres:15

# 4. Ejecutar el servicio
./gradlew bootRun
```

### Verificación

```bash
# Health check
curl http://localhost:8084/actuator/health

# Swagger UI
http://localhost:8084/swagger-ui/index.html

# Test endpoint
curl http://localhost:8084/api/v1/categories
```

## 📚 Documentación Adicional

### OpenAPI/Swagger

-   **Swagger UI**: http://localhost:8084/swagger-ui/index.html
-   **OpenAPI Spec**: [docs/openapi/restaurants.yaml](./docs/openapi/restaurants.yaml)

### Diagramas

-   [Arquitectura del Servicio](./docs/diagrams/04.Architecture.mmd)
-   [Flujo de Creación de Platos](./docs/HU/)

## 🎯 Casos de Uso Implementados

### Para ADMIN

-   ✅ **Crear restaurantes** y asignar propietarios
-   ✅ **Consultar todos los restaurantes** del sistema

### Para OWNER

-   ✅ **Gestionar menú** de su restaurante
-   ✅ **Crear/modificar platos** de su propiedad
-   ✅ **Habilitar/deshabilitar platos** según disponibilidad

### Para Público

-   ✅ **Consultar restaurantes** disponibles
-   ✅ **Ver menús** y platos por categoría
-   ✅ **Filtrar platos** por restaurante y categoría

## 🏆 Historias de Usuario Implementadas

-   ✅ **HU-002**: Crear Restaurante (ADMIN)
-   ✅ **HU-003**: Crear Plato (OWNER)
-   ✅ **HU-004**: Modificar Plato (OWNER)
-   ✅ **HU-007**: Habilitar/Deshabilitar Plato (OWNER)
-   ✅ **HU-009**: Listar Restaurantes (PÚBLICO)
-   ✅ **HU-010**: Listar Platos (PÚBLICO)
