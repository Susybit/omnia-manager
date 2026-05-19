# ☕ Business Core: Spring Boot REST API

<p>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java 17" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=black" alt="Swagger" />
</p>

El módulo **Backend** de Omnia Manager es una API RESTful corporativa, robusta y segura. Se encarga de procesar toda la lógica de negocio, garantizar la integridad de la base de datos y proveer los indicadores analíticos para la toma de decisiones en tiempo real.

---

## 🌟 Características de Arquitectura

* 🏛️ **Arquitectura de 3 Capas (N-Tier):** Implementación estricta del patrón `Controller → Service → Repository`.
* 🛡️ **Protección de Datos (DTOs):** Las entidades JPA (`Entities`) están completamente aisladas de la capa web utilizando Objetos de Transferencia de Datos (`DTOs`) para evitar vulnerabilidades de sobre-asignación (*Mass Assignment*).
* 🗑️ **Bajas Lógicas (Soft Deletes):** Los registros críticos (Empleados, Proyectos) jamás se eliminan de la base de datos físicamente; su estado es alterado (`terminationDate`) para preservar el histórico de la empresa y asegurar auditorías precisas.
* 🔗 **Integridad Referencial Estricta:** Lógica de validación previa que impide, por ejemplo, borrar un proyecto si actualmente tiene personal asignado a él.
* 🚨 **Manejo Global de Excepciones:** Respuestas de error estandarizadas (`@RestControllerAdvice`) que traducen las validaciones de negocio en formatos JSON legibles (400 Bad Request, 404 Not Found).
* ✅ **Validación de Jakarta:** Verificación reactiva de expresiones regulares para NIF español, dominios de correo corporativo y fortaleza de contraseñas.

---

## 🏗️ Estructura del Código

El código fuente está centralizado en el paquete principal `com.omnia.backend`:

```text
src/main/java/com/omnia/backend/
├── config/              # CORS, Seguridad, Password Encoding
├── controller/          # Endpoints REST y capa de exposición (Swagger)
├── exception/           # GlobalExceptionHandler y excepciones custom
├── model/
│   ├── dto/             # Objetos de Transferencia de Datos
│   └── entities/        # Entidades JPA (Persistencia)
├── repository/          # Interfaces Spring Data JPA
└── service/             # Lógica de negocio y reglas corporativas
```

---

## 🧪 Testing y Calidad

El proyecto incluye una suite de pruebas automatizadas que verifican el correcto comportamiento del Core de Negocio.

* **Herramientas:** JUnit 5, Mockito, Spring Boot Test.
* **Ejecución local:**

  ```bash
  ./mvnw clean test
  ```

---

## 📚 Documentación Interactiva (OpenAPI)

La API cuenta con documentación autogenerada y un cliente de pruebas integrado:

* **Swagger UI:** Accesible en `/swagger-ui/index.html` tras arrancar el proyecto.
* Permite interactuar visualmente con los controladores (`EmployeeController`, `AnalyticsController`, etc.) y probar las validaciones sin requerir herramientas externas.

**Desarrollado por:** Susana Bitar
*Proyecto Intermodular - 2026*
