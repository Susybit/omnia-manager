# Informe de Decisiones Técnicas: Omnia Manager
## Memoria de Arquitectura e Implementación
**Proyecto:** Gestor de Empleados y Proyectos

---

## 1. Introducción y Estructura del Proyecto
El sistema **Omnia Manager** se ha diseñado como una aplicación orientada a la gestión de recursos humanos y proyectos operativos. La arquitectura se basa en una separación de responsabilidades entre el backend (Java), el frontend (Vue 3) y un módulo de analítica (Python), con el objetivo de crear un sistema coherente y mantenible.

---

## 2. Desarrollo del Backend
Para el servidor se ha utilizado **Java 17** con **Spring Boot 3.x**, priorizando la seguridad y la integridad de los datos en toda la aplicación.

### 2.1. Persistencia y Gestión de Datos
*   **Integridad con Claves Compuestas:** En la relación entre empleados y proyectos (`EmployeeProject`), se ha implementado una clave compuesta mediante `@EmbeddedId`. Esto asegura que las asignaciones sean únicas y evita registros duplicados en la base de datos.
*   **Mapeo de Entidades:** Se han utilizado nombres descriptivos en el código fuente, mapeándolos mediante `@Column` a la estructura física de la base de datos original para mantener la compatibilidad.
*   **Optimización de Consultas:** Se utiliza carga perezosa (`FetchType.LAZY`) en las relaciones para evitar sobrecargar las respuestas de la API y mejorar el rendimiento general.

### 2.2. Lógica de Negocio y Seguridad
*   **Bajas Lógicas (Soft-Delete):** Se ha implementado un sistema de bajas mediante el campo `F_BAJA`. Esto permite desactivar registros sin eliminarlos físicamente, preservando el histórico de datos para futuras consultas.
*   **Validación de Asignaciones:** Se ha incluido lógica en la capa de servicios para impedir la baja de empleados que tengan proyectos activos asignados, informando al usuario mediante mensajes claros sobre el motivo.
*   **Seguridad de Credenciales:** Las contraseñas se almacenan cifradas mediante el algoritmo **BCrypt**, asegurando que la información sensible esté protegida en la base de datos.
*   **Validación de Datos:** Uso de anotaciones estándar (`@NotBlank`, `@Email`, `@Pattern`) para asegurar que la información de entrada cumple con los formatos requeridos antes de su persistencia.
*   **Gestión de Errores:** Se implementó un manejador global de excepciones que estandariza las respuestas de la API, facilitando la comunicación con el frontend.

---

## 3. Desarrollo del Frontend
El frontend se ha construido con **Vue 3** y **Vite**, buscando ofrecer una interfaz fluida y una estructura de componentes modular.

### 3.1. Interfaz y Componentes Reutilizables
*   **Coherencia Visual:** Se ha definido una paleta de colores profesional y tipografías legibles (**Outfit** para títulos e **Inter** para contenido), manteniendo un diseño limpio en todas las secciones.
*   **Componentes Personalizados:**
    *   `CrystalCard`: Contenedores para organizar la información de forma estructurada.
    *   `CrystalToast`: Sistema de notificaciones para dar feedback sobre las acciones realizadas (éxito o fallo).
    *   `FButton`: Botones con estados visuales claros para mejorar la interactividad.
*   **Diseño Adaptativo:** Sidebar con diferentes estados de visualización que se ajusta según el tamaño de la pantalla para optimizar el espacio de trabajo.

### 3.2. Gestión de Estado y Comunicación
*   **Estado con Pinia:** Se centralizó la información de sesión y autenticación en un store con persistencia, lo que permite mantener la sesión activa incluso al recargar la página.
*   **Interceptores de Axios:** Capa que gestiona automáticamente los encabezados de seguridad y el manejo de errores HTTP de forma centralizada.
*   **Filtros de Búsqueda:** Implementación de filtrado en las tablas para agilizar la localización de empleados y proyectos de manera intuitiva.

---

## 4. Módulo de Analítica
El módulo de analítica procesa los datos almacenados para generar visualizaciones que ayuden a entender el estado de la plantilla y los proyectos.

### 4.1. Procesamiento con Python
*   **Acceso a Datos (DataEngine):** Se desarrolló una capa encargada de normalizar la información extraída. Incluye una opción de "modo demo" para generar datos de prueba si la base de datos no está accesible.
*   **Uso de Pandas:** Se utiliza esta librería para el procesamiento de los dataframes y el cálculo de métricas como la desviación típica, medias de edad y el análisis de crecimiento anual.

### 4.2. Visualización
*   **Gráficos Interactivos:** Implementación con **Plotly**, permitiendo interactuar con las gráficas mediante zoom y tooltips informativos.
*   **Identificación de Inconsistencias:** Lógica para detectar proyectos activos que no tienen personal asignado, facilitando una gestión más eficiente de los recursos.
*   **Compatibilidad Visual:** Configuración del reporte para asegurar que los gráficos sean legibles tanto en temas claros como oscuros del editor.

---

## 5. Matriz de Cumplimiento de Requisitos

| Bloque             | Requisito PDF           | Implementación y Mejoras                                                                  |
| :----------------- | :---------------------- | :---------------------------------------------------------------------------------------- |
| **G. Empleados**   | Consulta y Alta         | Listado con búsqueda, validación de NIF y feedback visual.                                |
| **Baja Empleados** | Validación de proyectos | Control en el Service que impide la baja si hay tareas activas.                           |
| **G. Proyectos**   | CRUD y Sedes            | Gestión de estados y control de ubicaciones geográficas.                                  |
| **Asignaciones**   | Selección multiregistro | Interfaz con checkboxes y actualización de datos vinculados.                              |
| **Analítica**      | 12 Indicadores base     | 15 Indicadores, incluyendo rankings y detección de falta de recursos.                     |
| **Arquitectura**   | Java, Vue, Python       | Implementación sólida con buenas prácticas, BCrypt y Soft-Delete.                         |
| **Interfaz**       | Diseño funcional        | Interfaz consistente y navegación adaptativa, tipografía legible y navegación adaptativa. |

---

## 6. Conclusión
El sistema resultante integra una arquitectura de backend estable con una interfaz de usuario clara y funcional. Cada decisión técnica se ha tomado buscando un equilibrio entre la robustez del código y la facilidad de uso, logrando un resultado coherente y listo para su presentación final.

---
**Desarrollado por:** Susana Bitar  
*Beca Omnia 2026*
