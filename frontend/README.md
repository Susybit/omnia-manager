# Enterprise UI: Minimalist SPA & Vue 3 Engine

Interfaz de usuario del **Omnia Manager**, una SPA (Single Page Application)
diseñada bajo principios de minimalismo científico, alta reactividad y una
experiencia de usuario fluida.

---

## Características Técnicas

* **Arquitectura Reactiva:** Implementación basada en **Vue 3** con Composition
  API (`<script setup>`) para un código desacoplado y modular.
* **Sistema de Diseño Propio:** Estética **Fine-Line Minimalist** con una
  librería de componentes personalizados (`CrystalInput`, `CrystalCard`,
  `FButton`, `EliteSearch`) que garantizan coherencia visual en toda la
  plataforma.
* **Tipografía Dual:** Uso estratégico de **Outfit** para elementos de marca y
  cabeceras, combinada con **Inter** para la visualización de datos y lectura
  técnica.
* **Gestión de Estado Centralizada:** Implementación de **Pinia** para la
  persistencia de la sesión de usuario y estados globales de la aplicación.
* **Dashboard Analítico:** Panel de control interactivo con KPIs dinámicos,
  seguimiento de flujo de proyectos y reparto geográfico de sedes.
* **Validación Reactiva:** Feedback instantáneo en formularios (NIF, email,
  fechas) para reducir la tasa de error en la entrada de datos.

---

## Stack Tecnológico

* **Framework:** Vue 3 (Vite)
* **UI Library:** Vuetify 3
* **State Management:** Pinia
* **Iconografía:** Lucide Vue Next
* **Comunicación:** Axios (con servicios desacoplados por entidad)
* **Estilos:** CSS3 nativo con variables globales y diseño adaptativo

---

## Guía de Desarrollo

### Requisitos
* Node.js 18.x o superior
* npm 9.x o superior

### Instalación de dependencias
```bash
npm install
```

### Servidor de desarrollo
```bash
npm run dev
# Acceso en: http://localhost:5173
```

### Build para producción
```bash
npm run build
```

---

## Arquitectura del Módulo

```
frontend/src/
├── views/          Vistas principales (Dashboard, Employees, Projects, Assignments)
├── components/
│   ├── common/     Componentes base (CrystalInput, FButton, EliteSearch, CrystalToast)
│   ├── dashboard/  Componentes específicos del panel (StatCard, ChartArea, DataFeed)
│   └── layout/     Estructura global (Sidebar, Topbar)
├── services/       Abstracción de API (employeeService, projectService, dashboardService)
├── stores/         Gestión de estado global con Pinia (auth, ui)
├── assets/         Estilos globales (main.css, forms.css) y recursos estáticos
└── router/         Configuración de navegación y guardias de acceso
```

---

**Desarrollado por:** Susana Bitar
