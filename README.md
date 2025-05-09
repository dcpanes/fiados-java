# 🚀 Guía para Crear un Proyecto desde Cero 🛠️

Esta guía te proporcionará los pasos esenciales para iniciar cualquier proyecto de desarrollo, desde la configuración del entorno hasta la gestión del versionado.

## ⚙️ 1. Configurar el Entorno 🛠️

Antes de comenzar a codificar, asegúrate de tener tu entorno de desarrollo listo.

1.  **💾 Instalar Java JDK**:
    * Descarga e instala una versión compatible de Java JDK (por ejemplo, JDK 17). Puedes encontrarla en el sitio web de Oracle o a través de un gestor de paquetes.
    * Configura la variable de entorno `JAVA_HOME` para que apunte al directorio de instalación del JDK. Esto es crucial para que otras herramientas Java funcionen correctamente.

2.  **🏗️ Instalar Maven**:
    * Descarga e instala Apache Maven, una poderosa herramienta de gestión de proyectos Java.
    * Configura la variable de entorno `MAVEN_HOME` para que apunte al directorio de instalación de Maven.
    * Añade el directorio `bin` de Maven (`$MAVEN_HOME/bin` o `%MAVEN_HOME%\bin`) a tu variable de entorno `PATH` para poder ejecutar los comandos de Maven desde cualquier lugar en tu terminal.

3.  **🐬 Instalar MySQL**:
    * Descarga e instala MySQL Server, un popular sistema de gestión de bases de datos relacional.
    * Una vez instalado, conéctate al servidor y crea una base de datos específica para tu proyecto. ¡Dale un nombre descriptivo!

4.  **🔑 Configurar Variables de Entorno para la Base de Datos**:
    * Para mantener la seguridad y flexibilidad, define variables de entorno para las credenciales de conexión a tu base de datos. Esto evita codificar información sensible directamente en tu aplicación.
        * `DB_URL`: La URL de conexión a tu base de datos MySQL (ejemplo: `jdbc:mysql://localhost:3306/nombre_de_tu_base_de_datos?serverTimezone=America/Santiago`). ¡Recuerda ajustar la zona horaria!
        * `DB_USERNAME`: El nombre de usuario para acceder a la base de datos.
        * `DB_PASSWORD`: La contraseña de la base de datos.

5.  **💻 Instalar un IDE**:
    * Elige un Entorno de Desarrollo Integrado (IDE) que se adapte a tus preferencias y que ofrezca buen soporte para Java y Spring Boot. IntelliJ IDEA (Community o Ultimate) y Visual Studio Code (con extensiones Java) son excelentes opciones.

---

## 🛠️ 2. Crear el Proyecto ⚙️

Con tu entorno listo, es hora de dar vida a tu proyecto.

1.  **🌱 Generar un Proyecto Spring Boot**:
    * Dirígete a [Spring Initializr](https://start.spring.io/), una herramienta web que te permite generar la estructura básica de un proyecto Spring Boot con las dependencias que necesites.
    * Selecciona las dependencias esenciales para tu proyecto, como:
        * `Spring Web` para construir aplicaciones web y APIs RESTful.
        * `Spring Data JPA` para facilitar la interacción con bases de datos relacionales utilizando JPA y Hibernate.
        * El `MySQL Driver` específico para conectar con tu base de datos MySQL.
    * Descarga el archivo ZIP generado y ábrelo en tu IDE.

2.  **⚙️ Configurar el Archivo de Propiedades**:
    * Localiza el archivo de propiedades de tu proyecto (`application.properties` o `application.yml` dentro de la carpeta `src/main/resources`).
    * Configura la conexión a tu base de datos utilizando las variables de entorno que definiste anteriormente. Spring Boot puede acceder a estas variables utilizando la sintaxis `${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`.

    ```properties
    spring.datasource.url=${DB_URL}
    spring.datasource.username=${DB_USERNAME}
    spring.datasource.password=${DB_PASSWORD}
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    spring.jpa.show-sql=true
    spring.jpa.format_sql=true
    ```

---

## 🧱 3. Diseñar la Estructura del Proyecto 📂

Una estructura bien organizada facilita el desarrollo y mantenimiento. Aquí tienes una estructura común para proyectos Spring Boot:

1.  **Model 📊**:
    * Crea clases Java (generalmente anotadas con `@Entity`) que representen las tablas de tu base de datos. Estas clases definirán los atributos y las relaciones de tus datos.

2.  **Repositorio 💾**:
    * Define interfaces que extienden `JpaRepository` de Spring Data JPA. Estas interfaces proporcionan métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en tus entidades sin escribir código SQL explícitamente.

3.  **Servicio ⚙️**:
    * Implementa la lógica de negocio de tu aplicación en clases de servicio (anotadas con `@Service`). Estas clases suelen interactuar con los repositorios para acceder a los datos y realizar las operaciones necesarias.

4.  **Controlador 🕹️**:
    * Define clases (anotadas con `@RestController`) que actúan como los puntos de entrada de tu API REST. Estos controladores reciben las peticiones HTTP y delegan el trabajo a los servicios, devolviendo las respuestas a los clientes.

---

## 🗄️ 4. Configurar la Base de Datos 🐬

Asegúrate de que tu base de datos esté lista para almacenar la información de tu aplicación.

1.  **🛠️ Crear las Tablas Necesarias**:
    * Utiliza un cliente MySQL (como la línea de comandos `mysql` o una interfaz gráfica como MySQL Workbench) para conectarte a tu servidor MySQL y crear las tablas que corresponden a tus entidades definidas en el modelo. Asegúrate de que los nombres de las columnas y los tipos de datos coincidan con los definidos en tus clases `@Entity`.

2.  **🌱 Configurar la Inicialización de Datos** (opcional):
    * Si tu aplicación requiere datos iniciales al arrancar (por ejemplo, un conjunto de usuarios predefinidos o configuraciones iniciales), puedes configurar scripts SQL o utilizar funcionalidades de Spring Boot para poblar la base de datos automáticamente al inicio de la aplicación.

---

## 🧪 5. Probar el Proyecto ✅

La prueba es una parte fundamental del ciclo de desarrollo.

1.  **🚀 Ejecutar la Aplicación**:
    * Utiliza el comando de Maven `mvn spring-boot:run` en tu terminal (dentro del directorio del proyecto) o utiliza la funcionalidad de ejecución de tu IDE para iniciar la aplicación Spring Boot.

2.  **🧪 Probar los Endpoints**:
    * Emplea herramientas como Postman, Insomnia o `curl` para enviar peticiones HTTP a los endpoints REST que definiste en tus controladores. Verifica que las respuestas sean las esperadas y que los datos se guarden y recuperen correctamente de la base de datos.

---

## 📦 6. Versionar el Proyecto 🌳

El control de versiones es esencial para la colaboración y el seguimiento de los cambios.

1.  **📝 Configurar `.gitignore`**:
    * Crea un archivo llamado `.gitignore` en la raíz de tu proyecto. Este archivo listará los archivos y directorios que Git debe ignorar y no incluir en el control de versiones (por ejemplo, carpetas `target`, archivos `.iml` de IntelliJ, archivos `.log`, etc.). Puedes encontrar plantillas `.gitignore` para proyectos Java y Spring Boot en línea.

2.  **🌱 Inicializar un Repositorio Git**:
    * Abre tu terminal, navega al directorio raíz de tu proyecto y ejecuta el comando `git init`. Esto inicializará un nuevo repositorio Git local en tu proyecto.
    * Luego, agrega todos los archivos a la zona de preparación con `git add .` y crea tu primer commit con `git commit -m "Initial commit"`.
    * Finalmente, crea un repositorio remoto en plataformas como GitHub, GitLab o Bitbucket y vincula tu repositorio local con el remoto utilizando `git remote add origin <URL_del_repositorio_remoto>`. Luego, sube tu código con `git push -u origin main` (o `git push -u origin master` si tu rama principal se llama así).

---

¡Con esta guía detallada y bien estructurada, estás listo para embarcarte en la creación de cualquier proyecto desde cero! ¡Mucha suerte en tu viaje de desarrollo! 🚀