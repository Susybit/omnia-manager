# Business Intelligence: Data Engine & Strategic Insights

El módulo de analítica de **Future Space Manager** proporciona la capa de
inteligencia necesaria para transformar los datos operativos en decisiones
estratégicas sobre el capital humano y la cartera de proyectos.

---

## Módulos de Análisis

### [data_analitycs.ipynb](data_analitycs.ipynb)

Notebook interactivo de alto nivel diseñado para la presentación de resultados.
Implementa visualizaciones avanzadas con Plotly siguiendo la línea estética
minimalista de la aplicación.

* **Análisis de Plantilla:** Estudio de veteranía, demografía (edad y estado
  civil) y evolución histórica de contrataciones.
* **Control Operativo:** Seguimiento de la duración de proyectos y
  concentración por sedes geográficas.
* **Carga de Trabajo:** Visualización de la distribución de asignaciones y
  detección de saturación en proyectos críticos.
* **Detección de Anomalías:** Identificación automática de proyectos activos
  sin personal asignado ("huérfanos").

---

## Características Técnicas

* **Data Engine Híbrido:** [data_engine.py](data_engine.py) — Sistema capaz de
  conectar con la base de datos MySQL de producción o activar un **modo demo**
  con datos sintéticos realistas para presentaciones fuera del entorno de red.
* **Visualización de Producto:** Gráficos configurados con los design tokens
  exactos de la app (colores `#4F46E5`, `#1E293B` y tipografía `Inter`).
* **Exportación de Reportes:** [generate_report.py](generate_report.py) — Script
  automatizado para la exportación masiva de visualizaciones a formato PNG
  listas para informes ejecutivos.

---

## Stack Tecnológico

* **Lenguaje:** Python 3.11+
* **Data Science:** Pandas, NumPy
* **Visualización:** Plotly, Seaborn, Matplotlib
* **Notebook:** Jupyter (ipykernel)

---

## Puesta en Marcha

### Configuración del entorno

```bash
cd analytics
python -m venv .venv
source .venv/bin/activate  # En Windows: .venv\Scripts\activate
pip install -r requirements.txt
```

### Ejecución de Análisis

1. **Interactivo:** Abrir `data_analitycs.ipynb` en VS Code o Jupyter Lab.
2. **Batch:** Ejecutar `python generate_report.py` para regenerar los activos
   de imagen en la carpeta de reportes.

---

## Estructura del Módulo

```
analytics/
├── .venv/                      Entorno virtual aislado
├── data_analitycs.ipynb     Notebook principal de analítica
├── data_engine.py              Motor de extracción y simulación de datos
├── generate_report.py          Script de exportación de visualizaciones
├── requirements.txt            Dependencias del módulo
└── README.md
```

---

**Desarrollado por:** Susana Bitar
