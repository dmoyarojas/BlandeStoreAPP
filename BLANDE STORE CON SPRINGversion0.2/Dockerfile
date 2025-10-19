# =================================================================
# 1. FASE DE CONSTRUCCIÓN (BUILD)
# Usa una imagen con Java y Maven para compilar el proyecto
# =================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia pom.xml y el código fuente para la construcción
# Esto asume que pom.xml y la carpeta src están en la raíz del repositorio.
COPY pom.xml .
COPY src ./src

# Construye la aplicación. El flag -DskipTests omite las pruebas
RUN mvn clean package -DskipTests

# -----------------------------------------------------------

# =================================================================
# 2. FASE DE EJECUCIÓN (RUNTIME)
# Usa una imagen más ligera (solo Java Runtime) para producción
# =================================================================
FROM eclipse-temurin:21-jre-alpine

# Expone el puerto que usa Spring Boot (Render usa la variable PORT)
EXPOSE 8080

# Copia el JAR construido desde la fase 'build' al contenedor de ejecución.
# El asterisco (*) asegura que el nombre completo del JAR sea capturado.
COPY --from=build /app/target/*.jar app.jar

# Comando de inicio: Ejecuta la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "/app.jar"]