# 🎨 Omnia Design System & Brand Book

El **Omnia Design System** (basado en la estética *Fine-Line Minimalist*) es la fuente única de verdad para el diseño visual de la plataforma. Su objetivo es garantizar coherencia, reducir la fatiga visual de los usuarios (operadores de recursos humanos) y mantener un código CSS altamente reutilizable.

---

## 1. 🌈 Paleta de Colores Corporativa

La paleta de colores está diseñada para transmitir confianza corporativa (azules/índigos) manteniendo un alto contraste para la accesibilidad.

| Color | Hexadecimal | Uso Principal | Muestra |
| :--- | :--- | :--- | :--- |
| **Primario (Navbar)** | `#312E81` | Barra superior, elementos principales de navegación. | 🟦 Índigo Oscuro |
| **Acento (Botones)** | `#1E1B4B` | Botones de acción principal (CTAs), estados `hover`. | 🌌 Morado Noche |
| **Secundario (Texto/Iconos)**| `#4F46E5` | Enlaces, iconos activos, textos de acento. | 🔷 Índigo Medio |
| **Fondo Base** | `#F4F4F5` | Color de fondo de la aplicación (evita el blanco puro para reducir fatiga visual).| ⚪ Gris Perla |
| **Superficies** | `#FFFFFF` | Tarjetas (`CrystalCard`), modales y formularios. | ⬜ Blanco Puro |

---

## 2. 🔤 Tipografía Dual (Científica)

Utilizamos un sistema de tipografía dual para separar visualmente la "marca" de los "datos duros", mejorando la legibilidad técnica.

* **Outfit (Google Fonts):** 
  * *Uso:* Cabeceras (H1, H2, H3), logotipo, y títulos de tarjetas.
  * *Por qué:* Su geometría moderna y limpia aporta un carácter premium y tecnológico a la interfaz.
* **Inter (Google Fonts):** 
  * *Uso:* Cuerpos de texto, tablas de datos, inputs y números.
  * *Por qué:* Diseñada específicamente para interfaces de ordenador. Sus números tabulares evitan que las columnas de datos bailen, siendo ideal para dashboards y analíticas.

---

## 3. 🧩 Componentes Base (Crystal Design)

Hemos abstraído los elementos repetitivos de Vuetify en nuestros propios componentes encapsulados en Vue 3 para mantener el control total del diseño:

* `CrystalCard`: Tarjetas de superficie blanca con esquinas redondeadas y sombras muy sutiles (`box-shadow: 0 4px 6px rgba(0,0,0,0.05)`).
* `FButton`: Botón corporativo. Utiliza el color **Acento (#1E1B4B)** para acciones afirmativas. Carece de sombras duras, prefiriendo un diseño flat moderno.
* `EliteSearch`: Barra de búsqueda minimalista integrada directamente en las cabeceras de las tablas para no romper el flujo visual de la página.
* `CrystalInput`: Campos de formulario con bordes muy finos y etiquetas flotantes suaves, eliminando el ruido visual de los formularios tradicionales.

---

## 4. 📐 Principios de Espaciado y Layout

* **CSS Grid / Bento Grid:** Los dashboards utilizan cuadrículas asimétricas (estilo Bento) para agrupar visualmente la información de manera natural.
* **Respiración Visual (White Space):** No se comprime la información. Se utiliza `gap` nativo de CSS y paddings generosos (`20px - 24px`) en los contenedores interiores para que la aplicación "respire".
