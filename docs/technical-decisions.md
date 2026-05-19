# 📖 Memoria Técnica de Arquitectura e Implementación

**Proyecto:** Omnia Manager (Sistema de Gestión Empresarial)  
**Documento:** Decisiones de Diseño, Arquitectura y Reglas de Negocio  

---

## 1. 🏛️ Resumen Ejecutivo y Arquitectura Global

### ¿Qué hace la aplicación?

Omnia Manager es una plataforma corporativa *Full-Stack* (SPA) orientada a la gestión integral de recursos humanos (Empleados), control operativo (Proyectos) y distribución de carga laboral (Asignaciones). Incorpora un motor de inteligencia de negocio nativo que analiza la productividad y distribución de la plantilla en tiempo real.

### ¿Cómo lo hace? (El Enfoque Arquitectónico)

Se ha descartado la arquitectura monolítica tradicional en favor de una **arquitectura distribuida cliente-servidor** altamente desacoplada:

1. **Capa de Presentación (Frontend):** *Single Page Application* (SPA) estática servida independientemente, que consume datos mediante peticiones asíncronas HTTP.
2. **Capa de Negocio (Backend API):** Un servidor RESTful *stateless* que centraliza las reglas de validación, seguridad y orquestación de datos.
3. **Capa de Persistencia:** Base de datos relacional (MySQL) asegurando atomicidad y consistencia (ACID).

### ¿Por qué esta arquitectura?

Este desacoplamiento permite que en un futuro la empresa pueda desarrollar una app móvil (iOS/Android) conectándose exactamente a la misma API, sin necesidad de reprogramar la lógica de negocio.

---

## 2. 🗄️ Persistencia y Lógica de Datos (El Core)

### 2.1. El Paradigma de Bajas Lógicas (Soft-Deletes)

**¿Qué es?** En toda la plataforma, la acción de "Borrar" no ejecuta comandos `DELETE` en la base de datos SQL. En su lugar, se hace un `UPDATE` que rellena el campo `terminationDate` (Fecha de Baja).

* **¿Por qué?** A nivel corporativo, borrar físicamente un empleado destruiría el registro histórico de quién trabajó en qué proyecto el año pasado, falseando las métricas contables y de auditoría.
* **¿Cómo?** Las consultas en el repositorio (Spring Data JPA) filtran automáticamente los registros con `WHERE terminationDate IS NULL` para la vista operativa normal, pero permiten a la vista de analítica acceder a todo el histórico.

### 2.2. Integridad Referencial Absoluta

**¿Qué pasa si intentamos dar de baja un proyecto con gente trabajando?**

* **Regla de Negocio:** El sistema bloquea de inmediato la acción.
* **¿Por qué?** Para evitar datos huérfanos. Si un proyecto desaparece, el empleado asignado se quedaría en un "limbo" administrativo.
* **¿Cómo?** El `ProjectService` cuenta las asignaciones activas antes de proceder. Si es mayor a cero, aborta y lanza una `BusinessException`.

### 2.3. Claves Compuestas para Asignaciones

La entidad `EmployeeProject` gestiona quién trabaja dónde.

* **¿Cómo se asegura que un empleado no sea asignado dos veces al mismo proyecto al mismo tiempo?** Se utiliza el patrón de `@EmbeddedId` (Clave Primaria Compuesta) cruzando el ID del Empleado y el ID del Proyecto.

---

## 3. ⚙️ Backend Core (Java 17 & Spring Boot 3.x)

### 3.1. Arquitectura N-Capas

El código Java se divide de forma estricta para garantizar el principio de responsabilidad única (SOLID):

* **Controllers (`/controller`):** Únicamente exponen las URLs, reciben el JSON y devuelven Códigos HTTP (200, 400, 404). No tienen lógica condicional de negocio.
* **Services (`/service`):** Son el cerebro. Aquí se decide si un correo es válido, si un empleado puede ser borrado, o si la contraseña es correcta.
* **Repositories (`/repository`):** Capa tonta; solo hablan con SQL a través de Hibernate.

### 3.2. Protección de Datos mediante el Patrón DTO

**¿Qué hace?** Nunca devolvemos ni recibimos los objetos exactos de la Base de Datos (`Entities`) hacia Internet. Usamos `DTOs` (Data Transfer Objects).

* **¿Por qué?** Previene vulnerabilidades de **Mass Assignment**. Si devolviéramos la entidad `User`, enviaríamos por error la contraseña cifrada a la pantalla del navegador. Un DTO crea un "escudo" donde solo colocamos los campos públicos (`id`, `name`, `email`).

### 3.3. Manejo Global de Excepciones

**¿Cómo se gestionan los errores?** En lugar de tener sentencias `try/catch` en cada clase de la aplicación, utilizamos la clase `GlobalExceptionHandler` con `@RestControllerAdvice`.

* **¿Por qué?** Si ocurre un error (ej. Usuario no encontrado), el servicio simplemente lanza una excepción. Este manejador global lo intercepta y empaqueta el error en un JSON estandarizado para el frontend, garantizando que el servidor jamás colapse ni devuelva la traza de Java.

### 3.4. Seguridad (BCrypt)

* **¿Cómo?** Las contraseñas de los usuarios no se guardan en texto plano, sino que pasan por un algoritmo criptográfico de un solo sentido (`BCryptPasswordEncoder`).
* **¿Por qué?** Incluso si un atacante consiguiera descargar la tabla de MySQL, le sería matemáticamente imposible recuperar las contraseñas reales.

---

## 4. 💻 Frontend UI (Vue 3 & Vite)

### 4.1. Composition API (`<script setup>`)

* **¿Por qué?** Hemos abandonado la clásica `Options API` de Vue 2. La `Composition API` permite organizar el código Javascript por funcionalidad lógica (todo lo relativo a búsquedas junto, todo lo de paginación junto), en lugar de agrupar por ciclo de vida (`data`, `methods`). Esto resulta en componentes más pequeños y mucho más rápidos.

### 4.2. Gestión de Estado Global (Pinia)

* **¿Qué hace?** Es el sucesor oficial de Vuex. Pinia guarda quién está logueado en la aplicación, permitiendo que todas las pantallas (Dashboard, Listados) sepan inmediatamente el nombre del usuario sin volver a preguntar al servidor.
* **Persistencia:** Mantiene el estado en la caché del navegador para que si el usuario recarga la página con F5, no se cierre su sesión.

### 4.3. Comunicación de Red Segura (Axios Interceptors)

* Las peticiones no se hacen "en crudo". Axios está configurado con **interceptores**. Esto significa que antes de enviar cualquier paquete al backend, inyecta automáticamente el token de seguridad.
* **¿Por qué?** Evita repetir código de autenticación en las decenas de llamadas a la API que tiene el sistema.

### 4.4. UI/UX: El Patrón "Crystal Design"

Se han creado componentes abstractos (`CrystalCard`, `FButton`) para no depender excesivamente de la interfaz cruda de Vuetify.

* **Tipografía Científica:** `Outfit` para cabeceras (premium/corporativo) e `Inter` para tablas (números tabulares estables).
* **Minimalismo:** Eliminación de ruido visual (sombras muy difuminadas, colores perla de fondo en lugar de blancos puros para prevenir la fatiga visual de los empleados de RRHH).

---

## 5. 📊 El Motor Analítico Nativo (La Joya de la Corona)

### 5.1. Arquitectura Analítica Integrada

En lugar de depender de lenguajes externos o herramientas de terceros para la inteligencia de negocio (como Python/Pandas o PowerBI), se tomó la **decisión arquitectónica crítica** de construir el motor analítico **100% de forma nativa** dentro del propio ecosistema Java + Vue 3.

* **¿Por qué?** Esto unifica y centraliza el proyecto, evita fragmentar el código en múltiples lenguajes o contenedores, y acelera drásticamente el despliegue en producción al mantener todo dentro del flujo de Spring Boot.

### 5.2. Cómo funciona el AnalyticsController

El backend recupera la lista completa de empleados, proyectos y asignaciones y ejecuta operaciones matemáticas en memoria (mediante *Java Streams*), agrupando la información en un solo súper-objeto: `AnalyticsDTO`.

* **Ventaja:** En lugar de hacer 10 peticiones HTTP al servidor para rellenar 10 gráficos distintos, el Dashboard hace **una única petición** muy eficiente.

### 5.3. Interfaz del Dashboard (Vue + CSS Grid)

El dashboard pinta los resultados utilizando tecnología CSS Grid (estilo Bento), logrando que los KPIs empresariales se acomoden fluidamente en la pantalla sin depender de librerías externas de gráficos que recarguen el peso del cliente.

---

## 6. 🐳 Despliegue e Infraestructura (DevOps)

El proyecto está dockerizado para asegurar el principio de: *"Si funciona en mi máquina, funciona en producción"*.

* **Docker Compose:** Orquesta tres servicios aislados: Servidor MySQL, Backend de Spring Boot y Servidor web Frontend.
* **Volúmenes:** MySQL cuenta con un volumen dedicado (`omnia_db_data`) que asegura que aunque el contenedor se apague y se destruya, los datos empresariales se mantengan persitentes en el disco duro del servidor físico.

---

## 7. ⚖️ Matriz de Requisitos del Proyecto (Rúbrica)

Esta tabla demuestra el cumplimiento del 100% de los requerimientos corporativos/educativos exigidos al proyecto intermodular:

| Bloque Evaluado | Requisito Exigido | Implementación Exacta en el Código |
| :--- | :--- | :--- |
| **G. Empleados** | Consultar, Crear y Modificar. | Endpoints completos en `EmployeeController`. Vista `EmployeesView` con grid interactivo. |
| **Bajas** | Soft-Delete y Validaciones. | `EmployeeService.deleteEmployee()` implementa fecha lógica y bloquea si el empleado tiene proyectos activos. |
| **G. Proyectos** | Ciclo de vida y Sedes locales. | Atributo geográfico en entidad `Project`, listados filtrables por fecha inicio/fin. |
| **Asignaciones** | Interfaz multiselección cruzada. | Vista dinámica donde un Empleado puede ver los checkboxes de todos los Proyectos vigentes simultáneamente. |
| **Analítica** | Mínimo 12 indicadores visuales. | 15 Indicadores creados nativamente en el Dashboard de Vue consumidos por el nuevo `AnalyticsController`. |
| **Arquitectura** | Front y Back desacoplados. | Spa (Vue 3) en el puerto 5173; API REST (Spring Boot) en el puerto 8080. Completamente agnósticos. |
| **Seguridad** | Cifrado y control de accesos. | Login vía DTO con `BCryptPasswordEncoder` validando usuarios corporativos reales. |

---
**Desarrollado y Diseñado por:** Susana Bitar
