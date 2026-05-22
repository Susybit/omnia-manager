# 📖 Omnia Manager: El Documento Maestro (La Biblia del Proyecto)

**Proyecto:** Omnia Manager (Sistema de Gestión Empresarial)  
**Naturaleza:** Proyecto Intermodular (Fin de FP)  

Este documento constituye la guía definitiva de Omnia Manager. Se divide en dos grandes bloques: el **Manual Funcional** (descripción de la aplicación, público objetivo y funcionamiento) y la **Memoria Técnica** (arquitectura interna y justificación de las decisiones de diseño).

---

# PARTE 1: FUNCIONALIDAD Y NEGOCIO

## 1.1. ¿Qué es Omnia Manager?

Omnia Manager es una aplicación web corporativa (un ERP ligero) diseñada para resolver un problema frecuente en las medianas empresas: la desorganización y fragmentación de los datos internos.

Habitualmente, las empresas mantienen la lista de sus empleados en hojas de cálculo aisladas y los proyectos en sistemas paralelos, lo que dificulta conocer con exactitud la carga de trabajo del personal. Omnia Manager centraliza **Recursos Humanos, Proyectos y Asignaciones** en una única plataforma digital, segura y en tiempo real.

## 1.2. ¿Para quién está diseñada?

La aplicación **no** es de acceso público. Es una herramienta de uso interno (Intranet corporativa). Su público objetivo principal incluye:

* **Departamentos de Recursos Humanos:** Para gestionar altas, bajas y el histórico del personal.
* **Project Managers (Jefes de Proyecto):** Para consultar el personal disponible y sus asignaciones a diferentes sedes o tareas.
* **Gerencia:** Para visualizar gráficas en tiempo real sobre la rotación de la plantilla o el estado general de la empresa.

## 1.3. ¿Cómo funciona? (Flujo de la Aplicación)

La navegación del sistema es intuitiva y se estructura en 6 pantallas clave:

1. **Login (Autenticación):** El acceso está restringido. Un operador solo puede acceder si un administrador le ha provisto de una cuenta. Al autenticarse correctamente, el sistema da paso a la zona privada.
2. **Dashboard (Panel de Control):** Es la pantalla principal. Muestra de un vistazo los indicadores clave (KPIs): número de empleados en activo, proyectos en marcha, distribución de la empresa mediante gráficas (anillos y barras) y estadísticas generales de productividad.
3. **Módulo de Empleados:** Un listado completo del personal. Permite dar de alta nuevos trabajadores rellenando su ficha corporativa (DNI, nombre, titulación, teléfono).
4. **Módulo de Proyectos:** El espacio destinado a registrar los trabajos o servicios que ofrece la empresa, indicando su ubicación geográfica y las fechas estimadas de ejecución.
5. **Módulo de Asignaciones (El Corazón de la App):** Es la pantalla más crítica. Aquí se cruza la información: se selecciona un proyecto y se marca qué empleados van a participar en él. El sistema controla y actualiza en tiempo real los contadores de personal disponible.
6. **Mi Perfil:** Sección donde el operador puede modificar su fotografía de avatar y revisar los datos de su cuenta.

---

# PARTE 2: MEMORIA TÉCNICA Y DECISIONES DE DISEÑO

En esta sección se justifican las decisiones de arquitectura adoptadas durante el ciclo de desarrollo del software.

## 2.1. Arquitectura General del Proyecto

### ¿Por qué separar Backend y Frontend?

Para el desarrollo del proyecto se ha descartado el modelo monolítico clásico (donde el servidor genera directamente el código HTML mediante plantillas). En su lugar, el sistema se ha estructurado en dos bloques completamente independientes:

* **Backend (API REST):** Desarrollado en Java 17 con Spring Boot. Su única función es procesar datos, aplicar las reglas de negocio y comunicarse con la base de datos.
* **Frontend (SPA):** Desarrollado en Vue 3. Se encarga exclusivamente de la interfaz visual y de solicitar los datos al servidor mediante peticiones HTTP.

**Justificación:** Se ha optado por este modelo por ser el estándar actual en la industria del software. Además, esta arquitectura garantiza la escalabilidad futura: si la empresa necesitara desarrollar una aplicación móvil, esta podría conectarse directamente a la API actual sin necesidad de reprogramar la lógica de negocio, ya que el servidor únicamente devuelve datos en formato JSON puro.

## 2.2. Base de Datos y Persistencia

### Uso de base de datos relacional (MySQL)

Aunque las bases de datos NoSQL presentan ciertas ventajas de agilidad, se ha elegido MySQL porque Omnia Manager gestiona datos altamente estructurados y relacionados (empleados adscritos a proyectos). Resultaba fundamental contar con la seguridad y el control de integridad que ofrece el modelo relacional (transacciones ACID) para evitar inconsistencias en la información empresarial.

### Separación entre "Usuarios" y "Empleados"

Una de las decisiones de diseño arquitectónico más importantes ha sido mantener la tabla de `usuarios` aislada de la tabla de `empleados`.
Aunque fusionarlas bajo un mismo registro añadiendo un campo "rol" podría parecer la solución más rápida, se separaron por tres motivos técnicos de peso:

1. **Lógica empresarial:** En una compañía puede haber 500 empleados (operarios de fábrica, transportistas, etc.), pero solo 3 trabajadores del área de Recursos Humanos necesitan acceso a la aplicación. No es eficiente ni lógico obligar a que 497 empleados inactivos digitalmente posean campos de "contraseña" en la base de datos.
2. **Accesos externos:** Si la empresa contrata a un auditor externo, este necesita un "Usuario" para acceder al sistema, pero no puede ser dado de alta como "Empleado" ya que esto alteraría los registros contables y nóminas de la compañía.
3. **Seguridad:** Aislar las credenciales de acceso del sistema frente a los datos personales (DNI, teléfonos, historial) es una medida básica de seguridad para proteger la información sensible.

### Bajas Lógicas (Soft-Deletes)

En todo el sistema, cuando se elimina un empleado o un proyecto, la base de datos no ejecuta un comando `DELETE`. En su lugar, se actualiza un campo denominado `F_BAJA` (`terminationDate`).
**Justificación:** En un entorno corporativo, eliminar físicamente a un trabajador destruiría el registro histórico. Si se requiere auditar en qué proyectos participó un exempleado hace un año, la información debe persistir en el sistema, a pesar de que el individuo ya no figure como "activo" en los listados principales.

### Protección contra datos huérfanos

El sistema bloquea la opción de dar de baja un proyecto si este tiene empleados asignados actualmente. Esto garantiza la integridad referencial y evita que un trabajador quede vinculado a un "proyecto fantasma" inexistente en el sistema.

## 2.3. Desarrollo del Backend (Spring Boot)

### Estructura en Capas

El código fuente en Java se ha dividido estrictamente en capas lógicas para mantener el orden y cumplir con el principio de responsabilidad única:

* `Controllers`: Encargados de recibir las peticiones web y devolver las respuestas HTTP correspondientes.
* `Services`: Contienen el núcleo lógico de la aplicación. En ellos se validan las reglas de negocio.
* `Repositories`: Interfaces que gestionan la persistencia y comunicación con MySQL mediante Spring Data JPA.

### Uso de DTOs (Data Transfer Objects)

El servidor nunca expone los objetos directos de la base de datos (`Entities`) hacia el cliente web. Estos se transforman sistemáticamente en `DTOs`.
**Justificación:** Esta medida previene vulnerabilidades de asignación masiva (*Mass Assignment*). Si se enviara la entidad `User` de forma íntegra, se podría exponer accidentalmente la contraseña cifrada a la pantalla del operador. Los DTOs actúan como una barrera de seguridad donde solo se empaqueta la información estrictamente necesaria.

### Manejo Global de Errores

Se ha implementado una clase centralizada anotada con `@RestControllerAdvice`. Si ocurre una excepción en la aplicación (por ejemplo, intentar asignar a un empleado que no existe), el servidor no colapsa ni devuelve la clásica traza de error de Java. Este interceptor captura el fallo y devuelve al frontend un mensaje de error limpio y estructurado en formato JSON.

## 2.4. Desarrollo del Frontend (Vue 3)

### Navegación sin recargas (SPA)

El frontend opera como una Single Page Application (SPA). Al transitar entre secciones (del menú "Empleados" al menú "Proyectos"), la página web no requiere una recarga completa del navegador; únicamente se sustituye el contenido del área central. Esto proporciona una experiencia de usuario muy fluida, similar a la de un programa de escritorio tradicional.

### Componentes Personalizados

Aunque se ha empleado el *framework* Vuetify para facilitar la estructura visual base, se han diseñado componentes propios envolventes (como `CrystalInput` o `CrystalCard`).
**Justificación:** Esta abstracción evita el acoplamiento extremo (Vendor Lock-in). Si en el futuro se decidiera migrar de Vuetify a otra librería visual, los cambios estructurales se limitarían a modificar los componentes base en un único punto del proyecto, en lugar de alterar todo el código HTML de las vistas individuales.

### Gestión de Estado y Seguridad (Pinia y Axios)

* **Pinia:** Se encarga de almacenar los datos de la sesión de manera temporal en el cliente. Así, todos los componentes conocen la identidad del usuario conectado sin requerir constantes llamadas de comprobación al servidor.
* **Axios:** Las peticiones de red se gestionan mediante Axios. Se han configurado interceptores centrales para asegurar que todas las solicitudes lleven incrustadas las credenciales y cabeceras de seguridad requeridas.

## 2.5. El Panel de Analítica (Dashboard)

En la sección de estadísticas (Dashboard), se ha optado por procesar los cálculos matemáticos íntegramente en el **backend**.
En lugar de forzar al frontend a realizar múltiples consultas SQL independientes (una para contabilizar empleados, otra para proyectos, otra para la rotación), el servidor extrae la información y ejecuta los cálculos de agrupación mediante *Java Streams*. Finalmente, retorna al cliente un único objeto estructurado con los resultados listos para mostrar.
**Justificación:** Este enfoque reduce drásticamente la latencia de red y la carga del cliente, consiguiendo que el renderizado de las gráficas sea inmediato al delegar el esfuerzo computacional al servidor.

## 2.6. Despliegue e Instalación (Docker)

El ecosistema completo (Frontend, Backend y Base de Datos) se ha contenedorizado utilizando Docker y orquestado mediante un archivo `docker-compose.yml`.
**Justificación:** Esta decisión mitiga los problemas de compatibilidad entre diferentes entornos de desarrollo. Cualquier usuario puede inicializar el sistema completo con un único comando (`docker-compose up -d`), independientemente de su sistema operativo o de las versiones locales de Java/Node. Además, se ha configurado un volumen persistente para MySQL, asegurando que los datos corporativos no se destruyan al detener los contenedores.

---

<br>

> **Desarrollado por:** Susana Bitar  
> *Proyecto Intermodular - 2026*
