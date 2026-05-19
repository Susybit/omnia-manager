<div align="center">
  <img src="frontend/src/assets/img/logos/svg/omnia_logo.svg" alt="Omnia Manager Logo" width="200"/>
  <h1>Omnia Manager</h1>
  <p><strong>Enterprise Management System (EMS)</strong></p>

  <p>
    <img src="https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D" alt="Vue.js" />
    <img src="https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
    <img src="https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  </p>
</div>

---

**Omnia Manager** es una plataforma corporativa *Full-Stack* (SPA) diseñada para la gestión integral de recursos humanos, seguimiento de proyectos e inteligencia de negocio. Destaca por su arquitectura limpia, analítica en tiempo real integrada y manejo avanzado de la integridad de los datos.

## ✨ Características Principales

* 📊 **Dashboard Analítico Nativo:** KPIs en tiempo real (carga operativa, proyectos próximos a vencer, alertas de personal inactivo) generados desde el backend y renderizados interactivamente.
* 🛡️ **Seguridad Corporativa:** Autenticación JWT y BCrypt, con validaciones restrictivas (e ej., bloqueo de registros fuera del dominio `@omnia.com`).
* 🔒 **Bajas Lógicas (Soft Deletes):** Preservación absoluta del historial de negocio. Los empleados y proyectos nunca se borran físicamente.
* ⚡ **Arquitectura Desacoplada:** Backend robusto en Java 17 y frontend reactivo en Vue 3, comunicados a través de una API RESTful completamente documentada.

---

## 🛠️ Stack Tecnológico

| Capa | Tecnologías Principales |
| :--- | :--- |
| **Frontend** | Vue 3, Vite, Pinia, Vuetify 3, Axios, CSS Grid |
| **Backend** | Java 17, Spring Boot 3.x, Spring Data JPA, Hibernate |
| **Base de Datos**| MySQL 8.0 |
| **Infraestructura**| Docker, Docker Compose, Nginx |
| **Calidad / Testing**| JUnit 5, Mockito, Swagger / OpenAPI 3.0 |

---

## 🚀 Despliegue Rápido (Quick Start)

La aplicación está diseñada para desplegarse en segundos mediante contenedores, garantizando la misma experiencia en cualquier sistema operativo.

### Requisitos Previos
* **Docker Desktop** (o Docker Engine + Docker Compose)

### Instrucciones

1. Clona el repositorio y sitúate en la raíz.
2. Ejecuta el entorno completo:
   ```bash
   docker-compose up --build -d
   ```
3. Accede a los servicios:
   * **Frontend UI**: [http://localhost:5173](http://localhost:5173)
   * **Backend API / Swagger**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

*(Para detener y destruir la base de datos local, utiliza `docker-compose down -v`)*

---

## 📂 Estructura del Repositorio

El proyecto se divide en módulos independientes para garantizar la escalabilidad:

```text
omnia-gestor-empresarial/
├── frontend/                   # Cliente SPA (Vue 3 + Vuetify)
├── backend/                    # API REST Core (Spring Boot 17)
├── postman/                    # Colecciones de prueba de API
├── docs/                       # Documentación técnica y presentaciones
├── docker-compose.yml          # Orquestación de contenedores
└── .env                        # Variables de entorno y credenciales
```

---

> **Desarrollado por:** Susana Bitar  
> *Proyecto Final - 2026*
