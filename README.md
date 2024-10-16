# Proyecto de Gestión de Empleados
Este proyecto es una API REST para la gestión de empleados, desarrollada en Java utilizando **Spring Boot**, **Spring JPA** y **Swagger** para la documentación. 

## Funcionalidades principales
- Crear empleados.
- Consultar empleados por ID.
- Actualizar la información de empleados.
- Eliminar empleados.
- Listar todos los empleados.
- Documentación interactiva con Swagger (SpringDoc OpenAPI).

## Requisitos previos
Asegúrate de tener instalados los siguientes programas:
- [Java 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)
- [Maven 3.x](https://maven.apache.org/download.cgi)
- [MySQL 5.7+](https://dev.mysql.com/downloads/)
- [Git](https://git-scm.com/)

## Instalación y configuración

### 1. Clonar el repositorio en tu PC
```bash
git clone https://github.com/HeribertoReynaC/Invex.git

### 2. Configurar la base de datos
CREATE DATABASE invex;
CREATE TABLE empleado (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    PRIMER_NOMBRE VARCHAR(15) NOT NULL,
    SEGUNDO_NOMBRE VARCHAR(15),
    APELLIDO_PATERNO VARCHAR(15) NOT NULL,
    APELLIDO_MATERNO VARCHAR(15) NOT NULL,
    EDAD INT NOT NULL,
    SEXO VARCHAR(10) NOT NULL,
    FECHA_NACIMIENTO DATE NOT NULL,
    PUESTO VARCHAR(15) NOT NULL
);
INSERT INTO empleado 
(PRIMER_NOMBRE, SEGUNDO_NOMBRE, APELLIDO_PATERNO, APELLIDO_MATERNO, EDAD, SEXO, FECHA_NACIMIENTO, PUESTO)
VALUES
('Juan', 'Carlos', 'García', 'Lopez', 30, 'Masculino', '1994-05-10', 'Desarrollador'),
('Ana', NULL, 'Martinez', 'Gonzalez', 25, 'Femenino', '1999-02-20', 'Analista'),
('Luis', 'Alberto', 'Hernandez', 'Perez', 28, 'Masculino', '1996-12-15', 'Administrador');

### 3. Configura las credenciales en el archivo application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/invex
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
# Configuraciones adicionales
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

### 4. Instalar dependencia con Maven
mvn clean install

### 5. Ejecutar la aplicacion con Maven
mvn spring-boot:run

### 6. **Documentación de la API**
La documentación interactiva de la API se encuentra disponible en la siguiente URL una vez que la aplicación esté ejecutándose:
(http://localhost:8080/swagger-ui.html)
Aquí podrás visualizar y probar los diferentes endpoints disponibles en la API.

## 7. Pruebas Unitarias
Este proyecto incluye pruebas unitarias con **JUnit** y **Mockito**. Para ejecutar las pruebas, utiliza el siguiente comando:
mvn test

## Autor
Desarrollado por Heriberto Reyna Castro (https://github.com/HeribertoReynaC). Si tienes preguntas, no dudes en contactarme.





