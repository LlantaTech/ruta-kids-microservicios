# Documentación de Arquitectura de Microservicios

## Resumen General
Este proyecto define una arquitectura de microservicios moderna basada en Spring Boot, utilizando PostgreSQL, MongoDB, Kafka, Eureka, Config Server, API Gateway y Keycloak.

Cada microservicio está diseñado bajo una estructura profesional, separando responsabilidades en entidades, DTOs, repositorios, servicios, controladores y consumidores Kafka.

---

## Microservicios

### 1. Parent Service (PostgreSQL)
- **Entidades**: `Parent`, `Child`, `User`
- **DTOs**: `ParentDTO`, `ChildDTO`, `UserDTO`
- **Repositorios**: `ParentRepository`, `ChildRepository`, `UserRepository`
- **Servicios**: `ParentService`, `ChildService`, `UserService`
- **Controladores**: `ParentController`, `ChildController`, `UserController`
- **Kafka**: `ParentKafkaConsumer`
- **Base de datos**: PostgreSQL

### 2. IoT Service (MongoDB)
- **Entidades**: `Device`, `Sensor`, `SensorData`
- **DTOs**: `DeviceDTO`, `SensorDTO`, `SensorDataDTO`
- **Repositorios**: `DeviceRepository`, `SensorRepository`, `SensorDataRepository`
- **Servicios**: `DeviceService`, `SensorService`, `SensorDataService`
- **Controladores**: `DeviceController`, `SensorController`, `SensorDataController`
- **Kafka**: `IoTKafkaConsumer`
- **Base de datos**: MongoDB

### 3. Routes Service (PostgreSQL)
- **Entidades**: `Route`, `RoutePoint`, `TravelHistory`
- **DTOs**: `RouteDTO`, `RoutePointDTO`, `TravelHistoryDTO`
- **Repositorios**: `RouteRepository`, `TravelHistoryRepository`
- **Servicios**: `RouteService`, `TravelHistoryService`
- **Controladores**: `RouteController`, `TravelHistoryController`
- **Kafka**: `RoutesKafkaConsumer`
- **Base de datos**: PostgreSQL

### 4. IoT Interceptor (WebSocket + Kafka)
- **Componentes**: `IoTConsumer`, `WebSocketController`
- **Función**: Consumir eventos Kafka y transmitirlos por WebSocket.

---

## Infraestructura

### Servicios Principales
- **API Gateway**: Gestiona el enrutamiento de peticiones REST.
- **Eureka Server**: Discovery service para registros dinámicos de servicios.
- **Config Server**: Proporciona configuraciones centralizadas a todos los microservicios.
- **Keycloak**: Sistema de identidad y acceso (SSO, OAuth2, OpenID Connect).
- **Kafka**: Broker de mensajería para eventos IoT.

### Flujo Principal
1. El usuario ingresa por el **API Gateway**.
2. **Keycloak** gestiona la autenticación.
3. **API Gateway** enruta hacia los servicios.
4. Los servicios se descubren mediante **Eureka**.
5. La configuración inicial de cada servicio se obtiene del **Config Server**.
6. Eventos de dispositivos IoT son producidos a **Kafka** por IoT Service.
7. **IoT Interceptor** escucha esos eventos y los transmite en tiempo real por WebSocket.
8. **Parent Service** y **Routes Service** pueden también reaccionar a eventos de Kafka.

---

## Tecnologías Principales
- **Spring Boot** (Microservicios)
- **Spring Cloud** (Config, Discovery, Gateway)
- **Keycloak** (Autenticación y autorización)
- **Apache Kafka** (Mensajería de eventos)
- **MongoDB** (Base de datos documental para IoT)
- **PostgreSQL** (Base de datos relacional para Parent/Routes)
- **WebSocket (STOMP)** (Comunicación en tiempo real)

---

## Diagramas Incluidos
- **Diagramas de Clases (Mermaid)** de cada microservicio:
    - Parent Service
    - IoT Service
    - Routes Service
    - IoT Interceptor
- **Diagrama de Infraestructura**
- **Relaciones detalladas** entre componentes (routes, registers, fetches config, validates tokens, produces events, consumes events).

---