# Parent Management Service (Quarkus)

Este microservicio permite la gestión de padres/tutores en el sistema. Desarrollado con Quarkus y PostgreSQL.

## Tecnologías
- Quarkus (RESTEasy, Panache)
- PostgreSQL
- Maven
- Kafka (opcional)

## Ejecución local

```bash
./mvnw quarkus:dev
```

## Dockerfile multietapa

```Dockerfile
FROM quay.io/quarkus/ubi-quarkus-mandrel:23.1-java17 AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ./mvnw package -DskipTests

FROM quay.io/quarkus/quarkus-micro-image:1.0
COPY --from=build /usr/src/app/target/*-runner.jar /app/app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```
