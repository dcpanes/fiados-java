# --- Etapa 1: Compilación con Maven ---
# Usamos una imagen de Maven con JDK 17 para compilar el proyecto.
# El alias 'build' nos permitirá referenciar esta etapa más adelante.
FROM maven:3.9-eclipse-temurin-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copiamos primero el pom.xml y el wrapper de Maven.
# Esto aprovecha la caché de Docker: si estos archivos no cambian,
# no se volverán a descargar las dependencias.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Descargamos todas las dependencias del proyecto.
RUN ./mvnw dependency:go-offline

# Copiamos el resto del código fuente de la aplicación.
COPY src ./src

# Compilamos la aplicación y empaquetamos en un JAR, omitiendo los tests.
# El JAR se generará en el directorio /app/target/
RUN ./mvnw package -DskipTests

# --- Etapa 2: Creación de la imagen final de ejecución ---
# Usamos una imagen de JRE (Java Runtime Environment) que es más ligera
# que la de JDK, ya que solo necesitamos ejecutar la aplicación.
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo.
WORKDIR /app

# Copiamos el archivo JAR compilado desde la etapa 'build' a la imagen final.
# El nombre del JAR se basa en el artifactId y version de tu pom.xml.
COPY --from=build /app/target/fiados-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8083, que es el que tu aplicación utiliza
# según el archivo application.properties.
EXPOSE 8083

# --- Variables de Entorno para Render ---
# En la configuración de tu servicio en Render, deberás establecer
# las siguientes variables de entorno para conectar con tu base de datos.
# Spring Boot las detectará automáticamente.
#
# SPRING_DATASOURCE_URL=jdbc:mysql://<tu_host_db>:<tu_puerto_db>/<tu_db_name>
# SPRING_DATASOURCE_USERNAME=<tu_usuario_db>
# SPRING_DATASOURCE_PASSWORD=<tu_contraseña_db>

# Comando para ejecutar la aplicación cuando se inicie el contenedor.
ENTRYPOINT ["java", "-jar", "app.jar"]