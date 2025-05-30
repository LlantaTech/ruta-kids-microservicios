FROM jelastic/maven:3.9.5-openjdk-21 AS build

# Set the module name to build
ARG MODULE_NAME=config-server

RUN test -n "${MODULE_NAME}" || (echo "MODULE_NAME not set" && exit 1)

WORKDIR /app
COPY . .

RUN mvn clean install -pl ${MODULE_NAME} -am -DskipTests

# ---- Runtime stage ----
FROM openjdk:21-jdk-slim

ARG MODULE_NAME=config-server

COPY --from=build /app/${MODULE_NAME}/target/*.jar app.jar

RUN apt-get update && apt-get install -y curl

COPY wait-for-config.sh /wait-for-config.sh

RUN chmod +x /wait-for-config.sh

ENTRYPOINT ["/wait-for-config.sh"]

