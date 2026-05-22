# 💻 Enterprise UI: Vue 3 Minimalist SPA

<p>
  <img src="https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D" alt="Vue.js" />
  <img src="https://img.shields.io/badge/Vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=AEDDFF" alt="Vuetify" />
  <img src="https://img.shields.io/badge/Vite-B73BFE?style=for-the-badge&logo=vite&logoColor=FFD62E" alt="Vite" />
  <img src="https://img.shields.io/badge/Pinia-F6C239?style=for-the-badge&logo=vue.js&logoColor=black" alt="Pinia" />
  <img src="https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge&logo=axios&logoColor=white" alt="Axios" />
</p>

El módulo **Frontend** de Omnia Manager es una *Single Page Application (SPA)* construida sobre principios de **minimalismo científico**, diseño reactivo de alta velocidad y una experiencia de usuario (UX) inmersiva.

---

## 🎨 Características de Diseño (UX/UI)

* **Sistema de Diseño *Fine-Line Minimalist*:** Uso estratégico de los espacios en blanco, bordes sutiles y micro-interacciones para reducir la fatiga visual.
* **Componentes Reutilizables (*Crystal Design*):** Librería propia de componentes UI (`CrystalInput`, `CrystalCard`, `EliteSearch`) que unifica la experiencia en toda la plataforma.
* **Tipografía Dual Científica:**
  * `Outfit`: Utilizada en cabeceras y elementos de marca para ofrecer legibilidad premium.
  * `Inter`: Diseñada para tablas de datos de alta densidad y números, evitando confusiones visuales.
* **Validaciones Reactivas:** Feedback instantáneo en los formularios antes de realizar peticiones al servidor, optimizando la latencia y reduciendo los errores de los operadores.

---

## ⚡ Arquitectura Frontend

* ⚙️ **Composition API:** Lógica completamente desacoplada con `<script setup>` de Vue 3, logrando componentes limpios y fáciles de mantener.
* 📦 **Estado Global (Pinia):** Manejo centralizado de sesiones de usuario, roles y estados visuales (por ejemplo, notificaciones `CrystalToast`).
* 🔌 **Capa HTTP Abstraída (Axios):** Interceptores que gestionan automáticamente las cabeceras de sesión, re-enrutamientos de seguridad y manejo global de respuestas de error.
* 📊 **Analítica Integrada (CSS Grid / Vue):** El dashboard renderiza componentes gráficos complejos y dinámicos construidos de forma nativa para presentar los KPIs operacionales de negocio (`AnalyticsView`).

---

## 📂 Estructura del Código

```text
src/
├── assets/          # Estilos globales, variables CSS y recursos estáticos
├── components/
│   ├── common/      # Botones, Toasts, Inputs genéricos
│   ├── dashboard/   # Tarjetas estadísticas, Gráficos y Feeds
│   └── layout/      # Menú lateral (Sidebar) y barra superior (Topbar)
├── router/          # Reglas de enrutamiento y guardias de seguridad (Guards)
├── services/        # Archivos que concentran las llamadas Axios a la API
├── stores/          # Estado de aplicación (Pinia)
└── views/           # Vistas inyectadas por el Router (Páginas)
```

---

## 🚀 Entorno de Desarrollo Local

Si deseas correr la aplicación sin contenedores:

### Requisitos

* Node.js (v18 o superior)
* NPM / Yarn

### Instrucciones

1. Instala las dependencias del proyecto:

   ```bash
   npm install
   ```

2. Inicializa el entorno de desarrollo ultrarrápido (Vite):

   ```bash
   npm run dev
   ```

3. (Opcional) Compilación para producción:

   ```bash
   npm run build
   ```

<br>

> **Desarrollado por:** Susana Bitar  
> *Proyecto Intermodular - 2026*
