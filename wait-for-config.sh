#!/bin/sh
echo "Esperando a que config-server esté UP en http://config-server:8888/actuator/health ..."

until curl -s http://config-server:8888/actuator/health | grep -q '"status":"UP"'; do
  echo "Aún no está listo..."
  sleep 3
done

echo "Config server está UP! Iniciando el servicio..."
exec java -jar /app.jar