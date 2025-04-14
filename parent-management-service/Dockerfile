FROM quay.io/quarkus/ubi-quarkus-mandrel:23.1-java17 AS build
COPY . /usr/src/app
WORKDIR /usr/src/app
RUN ./mvnw package -DskipTests

FROM quay.io/quarkus/quarkus-micro-image:1.0
COPY --from=build /usr/src/app/target/*-runner.jar /app/app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
