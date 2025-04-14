# 🛣️ Route Control System - Microservicios

Sistema distribuido construido con Spring Boot, que permite gestionar rutas, usuarios, dispositivos IoT, tracking, pagos y autenticación centralizada con Keycloak.

## 🚀 Servicios principales

| Servicio        | Puerto | Descripción                          |
|-----------------|--------|--------------------------------------|
| Eureka Server   | 8761   | Registro y descubrimiento de servicios |
| API Gateway     | 8080   | Entrada unificada a los servicios    |
| User Service    | 8081   | Gestión de usuarios (MongoDB + Kafka) |
| IoT Service     | 8082   | Datos de sensores IoT                |
| Tracking Service| 8083   | Ubicaciones en tiempo real           |
| Payment Service | 8084   | Transacciones y pagos                |
| IAM Service     | 8085   | Login, registro, JWT, sesiones (PostgreSQL) |
| Audit Service   | 8086   | Registro de auditoría vía Kafka (PostgreSQL) |
| Spring Admin    | 9090   | Dashboard de servicios Spring Boot   |
| Keycloak        | 8081   | Gestión de identidad y seguridad     |
| PostgreSQL      | 5432   | Base de datos relacional             |
| Kafka           | 9092   | Broker de eventos asincrónicos       |
| MongoDB         | 27017  | Base de datos para User Service      |

## 🐳 Cómo levantar el proyecto

```bash
git clone https://github.com/tu-usuario/route-control-system.git
cd route-control-system
docker-compose up --build
```

## 🗂️ Arquitectura y tecnologías

- Spring Boot 3.x
- Eureka + Gateway
- MongoDB (user-service)
- PostgreSQL (IAM y audit)
- Kafka (mensajería de eventos)
- Keycloak (auth con JWT)
- Spring Security + OAuth2
- Docker multietapa con Maven & OpenJDK 21

## 🔐 Seguridad

- IAM gestiona login, logout y JWT
- Auditoría de sesiones y eventos
- Logout invalida token vía blacklist
- Integración con Keycloak posible para OAuth2/OIDC

## 🧠 Auditoría

El servicio `audit-service` guarda eventos como:
- `user-created` desde `user-service`
- `user-logged-in` desde `iam-service`

## 📡 Comunicación

- `user-service` emite eventos a Kafka
- `iam-service` y `audit-service` consumen eventos
- API Gateway enruta todas las solicitudes entrantes

## ✅ Endpoints útiles

- `POST /auth/register` - Registro de usuario
- `POST /auth/login` - Login con JWT
- `POST /auth/logout` - Logout e invalida token
- `POST /users` - Crear usuario (emite evento)
- `GET /actuator` - Monitoreo de cada microservicio

---

## 🧑‍💻 Autor

Desarrollado por [Llantatech](https://llantatech.org.pe)
