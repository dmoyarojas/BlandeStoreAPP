# 1. Fase de Construcción (Build Stage): Construye la aplicación
# Usa una imagen que ya incluye Java y Maven para construir el proyecto.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Maven (para usar el caché)
COPY pom.xml .

# Copia el código fuente
COPY src /app/src

# Construye la aplicación. El flag -DskipTests omite las pruebas
RUN mvn clean package -DskipTests

# -----------------------------------------------------------

# 2. Fase de Ejecución (Run Stage): Ejecuta el JAR final
# Usa una imagen más ligera (solo Java) para la producción
FROM eclipse-temurin:21-jre-alpine

# Expone el puerto donde se ejecuta Spring Boot (generalmente 8080)
EXPOSE 8080

# Copia el JAR construido desde la fase 'build' al contenedor de ejecución
# Reemplaza 'nombre-de-tu-artefacto' con el nombre de tu proyecto en tu pom.xml
COPY --from=build /app/target/*.jar app.jar

# Comando de inicio: Ejecuta la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]