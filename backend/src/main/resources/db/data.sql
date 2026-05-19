SET NAMES utf8mb4;
-- Dataset estratégico enriquecido - Gestor Empresarial 2026 çç
-- 2 admins · 55 empleados (45 activos, 10 cesados) · 20 proyectos · 96 asignaciones
-- Incluye proyectos sin empleados asignados, empleados activos sin asignar y proyectos que vencen pronto (< 30 días) desde el 19-May-2026.

SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM PR_EMPLEADOS_PROYECTO;
DELETE FROM PR_PROYECTOS;
DELETE FROM EM_EMPLEADOS;
DELETE FROM US_USUARIOS;
SET FOREIGN_KEY_CHECKS = 1;

-- ─── 1. USUARIOS ADMINISTRADORES (2 Perfiles Exclusivos) ─────────────────────
-- Contraseña para ambos: S12345678* (Hash BCrypt)
INSERT INTO US_USUARIOS (TX_EMAIL, TX_PASSWORD, TX_ROLE, F_ALTA, TX_NOMBRE) VALUES
('admin@omnia.com',   '$2a$10$MDCyccybDV93IJm8NUTjFujRl6gPX4Be0nTIPMVTBJ9z9cerMAv76', 'ADMIN', '2024-01-01', 'Admin Sistema'),
('susana@omnia.com',  '$2a$10$MDCyccybDV93IJm8NUTjFujRl6gPX4Be0nTIPMVTBJ9z9cerMAv76', 'ADMIN', '2024-01-01', 'Susana Bitar Azevedo');

-- ─── 2. EMPLEADOS (55 total: 45 activos, 10 cesados) ────────────────────────
-- Empleados Activos: IDs 1 a 45
-- Empleados Cesados: IDs 46 a 55 (con F_BAJA establecida)
INSERT INTO EM_EMPLEADOS (ID_EMPLEADO, TX_NIF, TX_NOMBRE, TX_APELLIDO1, TX_APELLIDO2, F_NACIMIENTO, N_TELEFONO1, N_TELEFONO2, TX_EMAIL, F_ALTA, F_BAJA, CX_EDOCIVIL, B_FORMACIONU) VALUES
(1,  '12345678A', 'Susana',    'Bitar',     'Azevedo',   '1995-05-20', '600111222', '911222333', 'susana.bitar@omnia.com',     '2023-01-10', NULL,         'S', 'S'),
(2,  '23456789B', 'Carlos',    'García',    'Ruiz',      '1988-03-15', '600222333', '911333444', 'carlos.garcia@omnia.com',    '2022-05-20', NULL,         'C', 'S'),
(3,  '45678901D', 'Javier',    'López',     'Mora',      '1990-07-22', '600444555', '911555666', 'javier.lopez@omnia.com',     '2021-09-01', NULL,         'C', 'N'),
(4,  '56789012E', 'Beatriz',   'Núñez',     'Vera',      '1997-12-12', '600555666', '911666777', 'beatriz.nunez@omnia.com',    '2024-01-15', NULL,         'S', 'S'),
(5,  '67890123F', 'Ricardo',   'Soto',      'Blanco',    '1985-06-30', '600666777', '911777888', 'ricardo.soto@omnia.com',     '2020-02-10', NULL,         'C', 'S'),
(6,  '89012345H', 'Marcos',    'Pérez',     'Gómez',     '1991-04-05', '600888999', '911999000', 'marcos.perez@omnia.com',     '2022-11-01', NULL,         'C', 'N'),
(7,  '90123456I', 'Laura',     'Díaz',      'Ríos',      '1996-02-28', '600999000', '911000111', 'laura.diaz@omnia.com',       '2024-02-01', NULL,         'S', 'S'),
(8,  '01234567J', 'Alberto',   'Cano',      'Ruiz',      '1987-08-14', '600000111', '911111222', 'alberto.cano@omnia.com',     '2021-01-15', NULL,         'C', 'S'),
(9,  '11223344K', 'Marta',     'Gil',       'Lara',      '1994-10-10', '600112233', '911223344', 'marta.gil@omnia.com',        '2023-07-01', NULL,         'S', 'S'),
(10, '22334455L', 'Víctor',    'Ramos',     'Ponce',     '1989-12-05', '600223344', '911334455', 'victor.ramos@omnia.com',     '2022-03-20', NULL,         'C', 'S'),
(11, '33445566M', 'Paula',     'Sánchez',   'Torres',    '1992-05-15', '600334455', '911445566', 'paula.sanchez@omnia.com',    '2023-10-10', NULL,         'S', 'N'),
(12, '44556677N', 'Raúl',      'Castro',    'Vidal',     '1990-01-20', '600445566', '911556677', 'raul.castro@omnia.com',      '2021-06-15', NULL,         'C', 'S'),
(13, '55667788O', 'Isabel',    'Luna',      'Ferrer',    '1998-03-03', '600556677', '911667788', 'isabel.luna@omnia.com',      '2024-03-01', NULL,         'S', 'S'),
(14, '66778899P', 'Hugo',      'Mora',      'Reyes',     '1986-11-11', '600667788', '911778899', 'hugo.mora@omnia.com',        '2020-09-01', NULL,         'C', 'S'),
(15, '77889900Q', 'Sara',      'Vila',      'Ortega',    '1993-07-07', '600778899', '911889900', 'sara.vila@omnia.com',        '2023-12-01', NULL,         'S', 'S'),
(16, '88990011R', 'Daniel',    'Pascual',   'Cid',       '1989-05-25', '600889900', '911990011', 'daniel.pascual@omnia.com',   '2022-04-01', NULL,         'C', 'S'),
(17, '10112233T', 'Jorge',     'Lorenzo',   'Soto',      '1992-03-18', '600001122', '911112233', 'jorge.lorenzo@omnia.com',    '2024-01-10', NULL,         'C', 'S'),
(18, '20304050U', 'Alejandro', 'Cruz',      'Herrero',   '1988-02-14', '601111222', '912222333', 'alejandro.cruz@omnia.com',   '2018-03-01', NULL,         'C', 'S'),
(19, '30405060V', 'Patricia',  'Molina',    'Ríos',      '1993-06-22', '601222333', '912333444', 'patricia.molina@omnia.com',  '2020-04-01', NULL,         'S', 'S'),
(20, '40506070W', 'Miguel',    'Serrano',   'Gallego',   '1985-09-08', '601333444', '912444555', 'miguel.serrano@omnia.com',   '2015-06-15', NULL,         'C', 'S'),
(21, '50607080X', 'Natalia',   'Jiménez',   'Castro',    '1997-04-17', '601444555', '912555666', 'natalia.jimenez@omnia.com',  '2022-09-01', NULL,         'S', 'S'),
(22, '70809000Z', 'Cristina',  'Blanco',    'Prieto',    '1994-11-30', '601666777', '912777888', 'cristina.blanco@omnia.com',  '2021-03-15', NULL,         'S', 'S'),
(23, '80900011A', 'Andrés',    'Campos',    'Delgado',   '1986-07-04', '601777888', '912888999', 'andres.campos@omnia.com',    '2016-02-01', NULL,         'C', 'S'),
(24, '90011122B', 'Lucía',     'Guerrero',  'Reyes',     '1999-08-19', '601888999', '912999000', 'lucia.guerrero@omnia.com',   '2023-06-01', NULL,         'S', 'S'),
(25, '11213141C', 'Álvaro',    'Fuentes',   'Ibáñez',    '1991-10-12', '601999000', '913000111', 'alvaro.fuentes@omnia.com',   '2020-01-10', NULL,         'C', 'N'),
(26, '21314151D', 'Inés',      'Ortega',    'Garrido',   '1995-03-28', '602000111', '913111222', 'ines.ortega@omnia.com',      '2022-07-01', NULL,         'S', 'S'),
(27, '31415161E', 'Pablo',     'Aguilar',   'Pardo',     '1987-12-16', '602111222', '913222333', 'pablo.aguilar@omnia.com',    '2017-05-15', NULL,         'C', 'S'),
(28, '51617181G', 'Roberto',   'Medina',    'Aranda',    '1984-05-22', '602333444', '913444555', 'roberto.medina@omnia.com',   '2015-09-01', NULL,         'C', 'S'),
(29, '61718191H', 'Ana',       'Reyes',     'Peñalver',  '1996-08-11', '602444555', '913555666', 'ana.reyes@omnia.com',        '2023-04-01', NULL,         'S', 'S'),
(30, '81920211J', 'Verónica',  'Mendoza',   'Gallego',   '1994-04-05', '602666777', '913777888', 'veronica.mendoza@omnia.com', '2021-10-01', NULL,         'D', 'S'),
(31, '92021221K', 'David',     'Ferreiro',  'Valero',    '1990-06-18', '602777888', '913888999', 'david.ferreiro@omnia.com',   '2019-03-01', NULL,         'C', 'N'),
(32, '12223242M', 'Manuel',    'Palacios',  'Cabrera',   '1989-11-14', '602999000', '914000111', 'manuel.palacios@omnia.com',  '2018-06-01', NULL,         'C', 'S'),
(33, '42526272P', 'Adriana',   'Costa',     'Flores',    '1998-10-25', '603222333', '914333444', 'adriana.costa@omnia.com',    '2024-04-01', NULL,         'S', 'S'),
(34, '62728292R', 'Olga',      'Mendoza',   'Pérez',     '1991-04-30', '603444555', '914555666', 'olga.mendoza@omnia.com',     '2019-11-01', NULL,         'S', 'S'),
(35, '72829302S', 'Samuel',    'Ortiz',     'Ferreiro',  '1995-12-08', '603555666', '914666777', 'samuel.ortiz@omnia.com',     '2022-01-10', NULL,         'S', 'S'),
(36, '82930312T', 'Diana',     'Ruiz',      'Gallego',   '1988-06-22', '603666777', '914777888', 'diana.ruiz@omnia.com',       '2018-10-01', NULL,         'C', 'S'),
(37, '03132332V', 'Míriam',    'Bernal',    'Salvado',   '1997-07-11', '603888999', '914999000', 'miriam.bernal@omnia.com',    '2023-08-01', NULL,         'S', 'S'),
(38, '23334352X', 'Lorena',    'Soto',      'Bravo',     '1996-01-14', '604000111', '915111222', 'lorena.soto@omnia.com',      '2023-11-01', NULL,         'S', 'S'),
(39, '50403020Y', 'Fernando',  'Rueda',     'Navarro',   '1990-01-25', '601555777', '912666888', 'fernando.rueda@omnia.com',   '2019-07-01', NULL,         'C', 'N'),
(40, '60504030Z', 'Cristina',  'Pérez',     'Prieto',    '1994-11-30', '601666888', '912777999', 'cristina.perez@omnia.com',   '2021-03-15', NULL,         'S', 'S'),
-- Empleados Activos que quedarán COMPLETAMENTE SIN ASIGNAR (IDs 41 a 45)
(41, '34567890C', 'Elena',     'Martín',    'Sanz',      '1992-11-02', '600333444', '911444555', 'elena.martin@omnia.com',     '2023-03-15', NULL,         'S', 'S'),
(42, '70707070M', 'Julián',    'Torres',    'Gil',       '1991-09-09', '605888999', '915222333', 'julian.torres@omnia.com',    '2024-05-15', NULL,         'C', 'S'),
(43, '80808080N', 'Sofía',     'Vega',      'Luna',      '1993-09-18', '600777888', '911888999', 'sofia.vega@omnia.com',       '2023-05-20', NULL,         'S', 'S'),
(44, '90909090P', 'Marta',     'Gil',       'Lara',      '1994-10-10', '600112233', '911223344', 'marta.gil@omnia.com',        '2023-07-01', NULL,         'S', 'S'),
(45, '10101010Q', 'Víctor',    'Ramos',     'Ponce',     '1989-12-05', '600223344', '911334455', 'victor.ramos@omnia.com',     '2022-03-20', NULL,         'C', 'S'),
-- Empleados Cesados (Baja histórica: IDs 46 a 55)
(46, '33333333X', 'Alberto',   'Núñez',     'Feijoo',    '1970-04-10', '602123456', '911998877', 'alberto.nunez@omnia.com',    '2018-01-15', '2025-12-31', 'C', 'S'),
(47, '44444444Y', 'Yolanda',   'Díaz',      'Pérez',     '1975-08-20', '602654321', '911776655', 'yolanda.diaz@omnia.com',     '2019-03-01', '2025-06-30', 'C', 'S'),
(48, '55555555Z', 'Irene',     'Montero',   'Gil',       '1983-02-15', '603112233', '912445566', 'irene.montero@omnia.com',    '2020-05-20', '2024-11-30', 'S', 'S'),
(49, '66666666W', 'Santiago',  'Abascal',   'Conde',     '1976-06-05', '604223344', '913556677', 'santiago.abascal@omnia.com', '2017-09-01', '2024-03-31', 'C', 'N'),
(50, '77777777V', 'Gabriel',   'Rufián',    'Romero',    '1982-12-10', '605334455', '914667788', 'gabriel.rufian@omnia.com',   '2021-02-01', '2025-01-15', 'C', 'S'),
(51, '88888888T', 'Arnaldo',   'Otegi',     'Mondragón', '1965-07-25', '606445566', '915778899', 'arnaldo.otegi@omnia.com',    '2016-10-10', '2023-12-31', 'C', 'N'),
(52, '99999999R', 'Isabel',    'Díaz',      'Ayuso',     '1978-10-17', '607556677', '916889900', 'isabel.ayuso@omnia.com',     '2020-01-01', '2025-04-30', 'S', 'S'),
(53, '11111111K', 'Ada',       'Colau',     'Ballano',   '1974-03-03', '608667788', '917990011', 'ada.colau@omnia.com',        '2018-06-15', '2023-06-30', 'S', 'S'),
(54, '22222222L', 'José Luis', 'Martínez',  'Almeida',   '1975-04-17', '609778899', '918001122', 'joseluis.almeida@omnia.com', '2019-11-01', '2024-06-30', 'S', 'S'),
(55, '99001122S', 'Carmen',    'Marín',     'Ramos',     '1995-07-14', '600990011', '911001122', 'carmen.marin@omnia.com',     '2023-09-01', '2025-10-31', 'S', 'N');

-- ─── 3. PROYECTOS (20 total: 16 activos, 4 históricos) ──────────────────────
-- Proyectos que expiran en < 30 días respecto al 19-May-2026: IDs 1 a 4
-- Proyectos activos largo plazo / continuados: IDs 5 a 13
-- Proyectos activos SIN personal asignado (Detección Analítica): IDs 14 a 16
-- Proyectos cesados históricamente: IDs 17 a 20
INSERT INTO PR_PROYECTOS (ID_PROYECTO, TX_DESCRIPCION, F_INICIO, F_FIN, F_BAJA, TX_LUGAR, TX_OBSERVACIONES) VALUES
-- Proyectos que vencen pronto (menor a 30 días desde 19-May-2026)
(1,  'Migración Cloud Azure 2026',          '2024-01-01', '2026-05-23', NULL,         'Madrid - Central',    'Migración crítica de la arquitectura legacy a Azure Cloud. Fase final de cierre.'),
(2,  'Portal Inteligencia Prescriptiva',    '2024-02-15', '2026-05-28', NULL,         'Remoto',              'Motor de analítica prescriptiva con IA generativa para modelos de riesgos.'),
(3,  'Sistema Gestión Logística v2',        '2023-11-01', '2026-06-08', NULL,         'Barcelona',           'Optimización de la cadena de suministro internacional con módulo BI.'),
(4,  'Auditoría Ciberseguridad Global',     '2024-03-01', '2026-06-15', NULL,         'Valencia',            'Evaluación integral de seguridad de perímetros y protocolos de cifrado.'),
-- Proyectos activos normales
(5,  'Modernización Core Bancario',         '2024-04-01', '2027-06-30', NULL,         'Sevilla',             'Reingeniería de los servicios bancarios legacy a microservicios Spring Boot.'),
(6,  'Rediseño UX/UI Plataformas',          '2024-05-01', '2026-12-31', NULL,         'Málaga',              'Modernización de interfaces de usuario para incrementar la usabilidad un 40%.'),
(7,  'Integración Pasarela de Pagos',       '2024-01-15', '2026-10-31', NULL,         'Bilbao',              'Implementación de nuevos protocolos PSD2 y tokenización de pagos web.'),
(8,  'Expansión Infraestructura de Red',    '2024-06-01', '2027-02-28', NULL,         'Zaragoza',            'Despliegue de red de alta velocidad y nodos de computación de borde.'),
(9,  'Digitalización Procesos RRHH',        '2024-07-01', '2026-08-31', NULL,         'Remoto',              'Automatización de onboarding, flujos de nómina y portal del empleado.'),
(10, 'Plataforma e-Learning Corporativo',   '2024-08-01', '2026-12-31', NULL,         'Madrid - Norte',      'Suite corporativa de formación con gamificación para ingenieros de software.'),
(11, 'Smart Office Madrid HQ',              '2025-01-01', '2027-12-31', NULL,         'Madrid - Central',    'Integración de tecnología IoT para eficiencia energética y gestión de aforo.'),
(12, 'Análisis Big Data Clientes',          '2024-09-01', '2026-09-30', NULL,         'Barcelona',           'Segmentación avanzada de clientes mediante machine learning y clustering.'),
(13, 'Sistema CRM Integrado',               '2024-10-01', '2027-03-31', NULL,         'Sevilla',             'Despliegue y personalización de CRM con analítica integrada en tiempo real.'),
-- Proyectos activos SIN EMPLEADOS ASIGNADOS 
(14, 'IA Generativa y Copilotos I+D',       '2026-01-15', '2027-06-30', NULL,         'Remoto',              'Proyecto de I+D sin personal asignado para evaluar capacidades de LLMs locales.'),
(15, 'Plataforma IoT Eficiencia Agraria',   '2026-02-01', '2027-03-31', NULL,         'Málaga',              'Proyecto medioambiental para monitorización hídrica, a la espera de ingenieros.'),
(16, 'Optimización Algorítmica Verde',      '2026-03-01', '2026-12-31', NULL,         'Barcelona',           'Desarrollo de optimizadores de código de bajo consumo, pendiente de asignaciones.'),
-- Proyectos históricos cesados / terminados
(17, 'App Móvil Fidelización Plus (Hist)',   '2022-05-10', '2023-12-20', '2023-12-31', 'Madrid - Norte',      'App móvil de fidelización con gamificación. Concluido con éxito.'),
(18, 'Mantenimiento Servidores 2023 (Hist)', '2023-01-01', '2023-12-31', '2023-12-31', 'Madrid - DataCenter', 'Ciclo preventivo anual de actualización de cabinas de almacenamiento.'),
(19, 'Implantación SAP S/4HANA (Hist)',      '2021-01-01', '2023-06-30', '2023-06-30', 'A Coruña',            'Migración ERP corporativo a versión HANA con módulos de finanzas.'),
(20, 'Certificación ISO 27001 Fase 1 (Hist)','2022-06-01', '2023-03-31', '2023-03-31', 'Valencia',            'Diseño e implantación inicial del SGSI corporativo bajo norma internacional.');

-- ─── 4. ASIGNACIONES (96 registros) ──────────────────────────────────────────
-- Asignación de empleados activos a proyectos activos (IDs 1 a 13)
-- Asignaciones históricas a proyectos cesados (IDs 17 a 20)
-- IMPORTANTE: Los empleados del 41 al 45 no se asignan a NADA activo.
-- Los proyectos 14, 15 y 16 NO reciben ninguna asignación para quedar con 0 empleados.
INSERT INTO PR_EMPLEADOS_PROYECTO (ID_PROYECTO, ID_EMPLEADO, F_ALTA) VALUES
-- Proyecto 1 (vence pronto): 6 empleados
(1,  1,  '2024-01-01'), (1,  2,  '2024-01-01'), (1,  3,  '2024-01-15'),
(1,  4,  '2024-01-20'), (1,  5,  '2024-02-01'), (1,  6,  '2024-02-15'),
-- Proyecto 2 (vence pronto): 5 empleados
(2,  7,  '2024-02-15'), (2,  8,  '2024-03-01'), (2,  9,  '2024-03-05'),
(2,  10, '2024-03-10'), (2,  11, '2024-03-15'),
-- Proyecto 3 (vence pronto): 5 empleados
(3,  12, '2023-11-01'), (3,  13, '2023-11-15'), (3,  14, '2023-12-01'),
(3,  15, '2024-01-10'), (3,  16, '2024-01-20'),
-- Proyecto 4 (vence pronto): 4 empleados
(4,  17, '2024-03-01'), (4,  18, '2024-03-15'), (4,  19, '2024-03-20'),
(4,  20, '2024-04-01'),
-- Proyecto 5 (Core Bancario - Proyecto Estrella con más carga: 9 empleados)
(5,  1,  '2024-04-01'), (5,  2,  '2024-04-01'), (5,  7,  '2024-04-10'),
(5,  8,  '2024-04-15'), (5,  12, '2024-04-20'), (5,  21, '2024-05-01'),
(5,  22, '2024-05-05'), (5,  23, '2024-05-10'), (5,  24, '2024-05-15'),
-- Proyecto 6 (Rediseño UX/UI): 7 empleados
(6,  3,  '2024-05-01'), (6,  9,  '2024-05-05'), (6,  13, '2024-05-10'),
(6,  25, '2024-05-15'), (6,  26, '2024-05-20'), (6,  27, '2024-05-25'),
(6,  28, '2024-06-01'),
-- Proyecto 7 (Pasarela Pagos): 6 empleados
(7,  4,  '2024-01-15'), (7,  10, '2024-02-01'), (7,  14, '2024-02-15'),
(7,  29, '2024-03-01'), (7,  30, '2024-03-10'), (7,  31, '2024-03-15'),
-- Proyecto 8 (Infraestructura Red): 5 empleados
(8,  5,  '2024-06-01'), (8,  11, '2024-06-10'), (8,  15, '2024-06-15'),
(8,  32, '2024-07-01'), (8,  33, '2024-07-05'),
-- Proyecto 9 (Digitalización RRHH): 4 empleados
(9,  6,  '2024-07-01'), (9,  16, '2024-07-15'), (9,  34, '2024-07-20'),
(9,  35, '2024-08-01'),
-- Proyecto 10 (e-Learning): 4 empleados
(10, 17, '2024-08-01'), (10, 21, '2024-08-10'), (10, 36, '2024-08-15'),
(10, 37, '2024-09-01'),
-- Proyecto 11 (Smart Office): 4 empleados
(11, 18, '2025-01-01'), (11, 22, '2025-01-10'), (11, 38, '2025-01-15'),
(11, 39, '2025-02-01'),
-- Proyecto 12 (Big Data Clientes): 3 empleados
(12, 19, '2024-09-01'), (12, 23, '2024-09-15'), (12, 40, '2024-10-01'),
-- Proyecto 13 (CRM): 3 empleados
(13, 20, '2024-10-01'), (13, 24, '2024-10-15'), (13, 25, '2024-11-01'),

-- ─── HISTÓRICOS ───
-- Proyecto 17 (histórico): Asignaciones pasadas (incluye empleados cesados que estaban activos entonces)
(17, 1,  '2022-05-10'), (17, 13, '2022-05-20'), (17, 46, '2022-06-01'), (17, 47, '2022-06-15'),
-- Proyecto 18 (histórico)
(18, 5,  '2023-01-01'), (18, 14, '2023-01-15'), (18, 48, '2023-02-01'), (18, 49, '2023-02-15'), (18, 50, '2023-03-01'),
-- Proyecto 19 (histórico)
(19, 6,  '2021-01-01'), (19, 23, '2021-01-15'), (19, 51, '2021-02-01'), (19, 52, '2021-02-15'), (19, 53, '2021-03-01'),
-- Proyecto 20 (histórico)
(20, 8,  '2022-06-01'), (20, 15, '2022-06-15'), (20, 54, '2022-07-01'), (20, 55, '2022-07-15');
