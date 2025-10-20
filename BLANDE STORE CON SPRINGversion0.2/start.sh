#!/bin/bash

# 1. Navega al directorio donde se encuentra el JAR.
# Usa comillas dobles para manejar los espacios en los nombres de las carpetas.
cd "BLANDE STORE CON SPRINGversion0.2/demo"

# 2. Ejecuta el JAR desde esa nueva ubicación (la ruta ahora es simple: target/...).
# El nombre de tu JAR debe ser verificado.
java -jar target/demo-0.0.1-SNAPSHOT.jar