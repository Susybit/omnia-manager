# Business Core: Spring Boot REST API

API REST del **Future Space Manager** construida con Java 17 y Spring Boot.
Gestiona empleados, proyectos y asignaciones con integridad referencial,
bajas lógicas y validación profesional de datos.

---

## Características Técnicas

* **Arquitectura de 3 Capas:** Implementación del patrón
  Controller → Service → Repository, con DTOs de entrada y salida
  desacoplados de las entidades JPA para proteger el modelo de persistencia.
* **Bajas Lógicas:** Los empleados y proyectos nunca se eliminan
  físicamente. Se marcan con fecha de baja (`terminationDate`) para
  mantener el histórico intacto y permitir auditorías.
* **Integridad Referencial:** No se permite el borrado de un proyecto si
  tiene asignaciones activas. La lógica está centralizada en los services.
* **Validación Profesional:** Jakarta Bean Validation en DTOs y entidades
  (`@NotBlank`, `@Past`, `@Pattern` para NIF, `@Email`) con respuestas
  HTTP semánticas.
* **Manejo Global de Excepciones:**
  [GlobalExceptionHandler](src/main/java/com/futurespace/backend/exception/GlobalExceptionHandler.java)
  centraliza las respuestas 400 y 404 con mensajes estructurados.
* **Autenticación:** Módulo completo con login, registro y reset de
  contraseña en
  [AuthController](src/main/java/com/futurespace/backend/controller/AuthController.java).
* **Testing:** Cobertura con JUnit 5 y Mockito en controllers y services.

---

## Stack Tecnológico

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3.x
* **Acceso a Datos:** Spring Data JPA / Hibernate
* **Base de Datos:** MySQL 8.0
* **Testing:** JUnit 5, Mockito, Spring Boot Test
* **Documentación:** Swagger / OpenAPI (`/swagger-ui.html`)

---

## Configuración y Ejecución

### Requisitos
* Java JDK 17
* Maven 3.8+
* MySQL Server activo

### Instalación
1. Configurar las credenciales en `src/main/resources/application.properties`.
2. Ejecutar:
```bash
./mvnw spring-boot:run
# API disponible en http://localhost:8080
```

### Tests
```bash
./mvnw test
```

---

## Diseño de Entidades

* **Employee:** Datos personales (NIF, nombre, email), académicos
  (titulación universitaria) y laborales (fecha alta/baja, estado).
* **Project:** Descripción, sede, fechas de inicio/fin y estado operativo.
* **EmployeeProject:** Entidad relacional con clave compuesta para
  gestionar las asignaciones históricas y vigentes entre empleados y
  proyectos.
* **User:** Credenciales de acceso al sistema (login, contraseña, perfil).

---

## Arquitectura del Módulo

```
src/main/java/com/futurespace/backend/
├── controller/          Endpoints REST
│   ├── EmployeeController.java
│   ├── ProjectController.java
│   ├── AssignmentController.java
│   ├── EmployeeProjectController.java
│   └── AuthController.java
├── service/             Lógica de negocio
│   ├── EmployeeService.java
│   ├── ProjectService.java
│   ├── EmployeeProjectService.java
│   └── AuthService.java
├── repository/          Acceso a datos (JPA)
│   ├── EmployeeRepository.java
│   ├── ProjectRepository.java
│   ├── EmployeeProjectRepository.java
│   └── UserRepository.java
├── model/
│   ├── entities/        Entidades JPA (Employee, Project, EmployeeProject, User)
│   └── dto/             DTOs de entrada y salida
├── exception/           GlobalExceptionHandler, BusinessException
└── config/              SecurityConfig (CORS, autenticación)
```

---

**Desarrollado por:** Susana Bitar
