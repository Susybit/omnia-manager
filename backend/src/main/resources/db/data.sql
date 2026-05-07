SET NAMES utf8mb4;
-- Dataset estratégico - Gestor Empresarial 2026
-- 4 admins · 50 empleados (40 activos, 10 cesados) · 20 proyectos · 100 asignaciones

SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM PR_EMPLEADOS_PROYECTO;
DELETE FROM PR_PROYECTOS;
DELETE FROM EM_EMPLEADOS;
DELETE FROM US_USUARIOS;
SET FOREIGN_KEY_CHECKS = 1;

-- ─── 1. USUARIOS ADMINISTRADORES ────────────────────────────────────────────
-- Password para todos: 12345678S* (hash BCrypt)
INSERT INTO US_USUARIOS (TX_EMAIL, TX_PASSWORD, TX_ROLE, F_ALTA, TX_NOMBRE) VALUES
('admin@futurespace.com',        '$2a$10$6l8lrZLFcDdU7Kxo4y9Zb.bCrKm9XPN9i5hVpDmdYCYLlIRrmT6LK', 'ADMIN', '2024-01-01', 'Admin Sistema'),
('susana@futurespace.com',       '$2a$10$6l8lrZLFcDdU7Kxo4y9Zb.bCrKm9XPN9i5hVpDmdYCYLlIRrmT6LK', 'ADMIN', '2024-01-01', 'Susana Bitar'),
('rrhh@futurespace.com',         '$2a$10$6l8lrZLFcDdU7Kxo4y9Zb.bCrKm9XPN9i5hVpDmdYCYLlIRrmT6LK', 'ADMIN', '2024-02-01', 'Director RRHH'),
('operaciones@futurespace.com',  '$2a$10$6l8lrZLFcDdU7Kxo4y9Zb.bCrKm9XPN9i5hVpDmdYCYLlIRrmT6LK', 'ADMIN', '2024-02-01', 'Director Operaciones');

-- ─── 2. EMPLEADOS (50 total: 40 activos, 10 cesados) ────────────────────────
-- Cesados: IDs 7, 19, 33, 35, 38, 40, 41, 43, 47, 49
INSERT INTO EM_EMPLEADOS (TX_NIF, TX_NOMBRE, TX_APELLIDO1, TX_APELLIDO2, F_NACIMIENTO, N_TELEFONO1, N_TELEFONO2, TX_EMAIL, F_ALTA, F_BAJA, CX_EDOCIVIL, B_FORMACIONU) VALUES
('12345678A', 'Susana',    'Bitar',     'Azevedo',   '1995-05-20', '600111222', '911222333', 'susana.bitar@futurespace.com',     '2023-01-10', NULL,         'S', 'S'),
('23456789B', 'Carlos',    'García',    'Ruiz',      '1988-03-15', '600222333', '911333444', 'carlos.garcia@futurespace.com',    '2022-05-20', NULL,         'C', 'S'),
('34567890C', 'Elena',     'Martín',    'Sanz',      '1992-11-02', '600333444', '911444555', 'elena.martin@futurespace.com',     '2023-03-15', NULL,         'S', 'S'),
('45678901D', 'Javier',    'López',     'Mora',      '1990-07-22', '600444555', '911555666', 'javier.lopez@futurespace.com',     '2021-09-01', NULL,         'C', 'N'),
('56789012E', 'Beatriz',   'Núñez',     'Vera',      '1997-12-12', '600555666', '911666777', 'beatriz.nunez@futurespace.com',    '2024-01-15', NULL,         'S', 'S'),
('67890123F', 'Ricardo',   'Soto',      'Blanco',    '1985-06-30', '600666777', '911777888', 'ricardo.soto@futurespace.com',     '2020-02-10', NULL,         'C', 'S'),
('78901234G', 'Sofía',     'Vega',      'Luna',      '1993-09-18', '600777888', '911888999', 'sofia.vega@futurespace.com',       '2023-05-20', '2024-06-30', 'S', 'S'),
('89012345H', 'Marcos',    'Pérez',     'Gómez',     '1991-04-05', '600888999', '911999000', 'marcos.perez@futurespace.com',     '2022-11-01', NULL,         'C', 'N'),
('90123456I', 'Laura',     'Díaz',      'Ríos',      '1996-02-28', '600999000', '911000111', 'laura.diaz@futurespace.com',       '2024-02-01', NULL,         'S', 'S'),
('01234567J', 'Alberto',   'Cano',      'Ruiz',      '1987-08-14', '600000111', '911111222', 'alberto.cano@futurespace.com',     '2021-01-15', NULL,         'C', 'S'),
('11223344K', 'Marta',     'Gil',       'Lara',      '1994-10-10', '600112233', '911223344', 'marta.gil@futurespace.com',        '2023-07-01', NULL,         'S', 'S'),
('22334455L', 'Víctor',    'Ramos',     'Ponce',     '1989-12-05', '600223344', '911334455', 'victor.ramos@futurespace.com',     '2022-03-20', NULL,         'C', 'S'),
('33445566M', 'Paula',     'Sánchez',   'Torres',    '1992-05-15', '600334455', '911445566', 'paula.sanchez@futurespace.com',    '2023-10-10', NULL,         'S', 'N'),
('44556677N', 'Raúl',      'Castro',    'Vidal',     '1990-01-20', '600445566', '911556677', 'raul.castro@futurespace.com',      '2021-06-15', NULL,         'C', 'S'),
('55667788O', 'Isabel',    'Luna',      'Ferrer',    '1998-03-03', '600556677', '911667788', 'isabel.luna@futurespace.com',      '2024-03-01', NULL,         'S', 'S'),
('66778899P', 'Hugo',      'Mora',      'Reyes',     '1986-11-11', '600667788', '911778899', 'hugo.mora@futurespace.com',        '2020-09-01', NULL,         'C', 'S'),
('77889900Q', 'Sara',      'Vila',      'Ortega',    '1993-07-07', '600778899', '911889900', 'sara.vila@futurespace.com',        '2023-12-01', NULL,         'S', 'S'),
('88990011R', 'Daniel',    'Pascual',   'Cid',       '1989-05-25', '600889900', '911990011', 'daniel.pascual@futurespace.com',   '2022-04-01', NULL,         'C', 'S'),
('99001122S', 'Carmen',    'Marín',     'Ramos',     '1995-07-14', '600990011', '911001122', 'carmen.marin@futurespace.com',     '2023-09-01', '2024-09-30', 'S', 'N'),
('10112233T', 'Jorge',     'Lorenzo',   'Soto',      '1992-03-18', '600001122', '911112233', 'jorge.lorenzo@futurespace.com',    '2024-01-10', NULL,         'C', 'S'),
('20304050U', 'Alejandro', 'Cruz',      'Herrero',   '1988-02-14', '601111222', '912222333', 'alejandro.cruz@futurespace.com',   '2018-03-01', NULL,         'C', 'S'),
('30405060V', 'Patricia',  'Molina',    'Ríos',      '1993-06-22', '601222333', '912333444', 'patricia.molina@futurespace.com',  '2020-04-01', NULL,         'S', 'S'),
('40506070W', 'Miguel',    'Serrano',   'Gallego',   '1985-09-08', '601333444', '912444555', 'miguel.serrano@futurespace.com',   '2015-06-15', NULL,         'C', 'S'),
('50607080X', 'Natalia',   'Jiménez',   'Castro',    '1997-04-17', '601444555', '912555666', 'natalia.jimenez@futurespace.com',  '2022-09-01', NULL,         'S', 'S'),
('60708090Y', 'Fernando',  'Rueda',     'Navarro',   '1990-01-25', '601555666', '912666777', 'fernando.rueda@futurespace.com',   '2019-07-01', NULL,         'C', 'N'),
('70809000Z', 'Cristina',  'Blanco',    'Prieto',    '1994-11-30', '601666777', '912777888', 'cristina.blanco@futurespace.com',  '2021-03-15', NULL,         'S', 'S'),
('80900011A', 'Andrés',    'Campos',    'Delgado',   '1986-07-04', '601777888', '912888999', 'andres.campos@futurespace.com',    '2016-02-01', NULL,         'C', 'S'),
('90011122B', 'Lucía',     'Guerrero',  'Reyes',     '1999-08-19', '601888999', '912999000', 'lucia.guerrero@futurespace.com',   '2023-06-01', NULL,         'S', 'S'),
('11213141C', 'Álvaro',    'Fuentes',   'Ibáñez',    '1991-10-12', '601999000', '913000111', 'alvaro.fuentes@futurespace.com',   '2020-01-10', NULL,         'C', 'N'),
('21314151D', 'Inés',      'Ortega',    'Garrido',   '1995-03-28', '602000111', '913111222', 'ines.ortega@futurespace.com',      '2022-07-01', NULL,         'S', 'S'),
('31415161E', 'Pablo',     'Aguilar',   'Pardo',     '1987-12-16', '602111222', '913222333', 'pablo.aguilar@futurespace.com',    '2017-05-15', NULL,         'C', 'S'),
('41516171F', 'Silvia',    'Expósito',  'Montoya',   '1993-02-07', '602222333', '913333444', 'silvia.exposito@futurespace.com',  '2021-08-01', NULL,         'S', 'N'),
('51617181G', 'Roberto',   'Medina',    'Aranda',    '1984-05-22', '602333444', '913444555', 'roberto.medina@futurespace.com',   '2015-09-01', '2023-06-30', 'C', 'S'),
('61718191H', 'Ana',       'Reyes',     'Peñalver',  '1996-08-11', '602444555', '913555666', 'ana.reyes@futurespace.com',        '2023-04-01', NULL,         'S', 'S'),
('71819201I', 'José Luis', 'Vargas',    'Costa',     '1982-01-30', '602555666', '913666777', 'joseluis.vargas@futurespace.com',  '2015-11-01', '2024-03-31', 'C', 'S'),
('81920211J', 'Verónica',  'Mendoza',   'Gallego',   '1994-04-05', '602666777', '913777888', 'veronica.mendoza@futurespace.com', '2021-10-01', NULL,         'D', 'S'),
('92021221K', 'David',     'Ferreiro',  'Valero',    '1990-06-18', '602777888', '913888999', 'david.ferreiro@futurespace.com',   '2019-03-01', NULL,         'C', 'N'),
('02122232L', 'Yolanda',   'Bravo',     'Salvado',   '1978-09-03', '602888999', '913999000', 'yolanda.bravo@futurespace.com',    '2015-04-01', '2022-12-31', 'C', 'S'),
('12223242M', 'Manuel',    'Palacios',  'Cabrera',   '1989-11-14', '602999000', '914000111', 'manuel.palacios@futurespace.com',  '2018-06-01', NULL,         'C', 'S'),
('22324252N', 'Teresa',    'García',    'Miriam',    '1992-07-20', '603000111', '914111222', 'teresa.garciamt@futurespace.com',  '2020-08-01', '2023-06-30', 'S', 'S'),
('32425262O', 'Francisco', 'Santos',    'Lorena',    '1983-03-09', '603111222', '914222333', 'francisco.santos@futurespace.com', '2016-07-01', '2023-12-31', 'C', 'N'),
('42526272P', 'Adriana',   'Costa',     'Flores',    '1998-10-25', '603222333', '914333444', 'adriana.costa@futurespace.com',    '2024-04-01', NULL,         'S', 'S'),
('52627282Q', 'Héctor',    'Vargas',    'López',     '1986-01-15', '603333444', '914444555', 'hector.vargas@futurespace.com',    '2017-09-01', '2021-12-31', 'C', 'S'),
('62728292R', 'Olga',      'Mendoza',   'Pérez',     '1991-04-30', '603444555', '914555666', 'olga.mendoza@futurespace.com',     '2019-11-01', NULL,         'S', 'S'),
('72829302S', 'Samuel',    'Ortiz',     'Ferreiro',  '1995-12-08', '603555666', '914666777', 'samuel.ortiz@futurespace.com',     '2022-01-10', NULL,         'S', 'S'),
('82930312T', 'Diana',     'Ruiz',      'Gallego',   '1988-06-22', '603666777', '914777888', 'diana.ruiz@futurespace.com',       '2018-10-01', NULL,         'C', 'S'),
('93031322U', 'Gonzalo',   'Luna',      'Cabrera',   '1979-02-17', '603777888', '914888999', 'gonzalo.luna@futurespace.com',     '2015-12-01', '2020-06-30', 'C', 'N'),
('03132332V', 'Míriam',    'Bernal',    'Salvado',   '1997-07-11', '603888999', '914999000', 'miriam.bernal@futurespace.com',    '2023-08-01', NULL,         'S', 'S'),
('13233342W', 'Ernesto',   'Cid',       'Valero',    '1984-10-28', '603999000', '915000111', 'ernesto.cid@futurespace.com',      '2016-05-01', '2022-06-30', 'C', 'S'),
('23334352X', 'Lorena',    'Soto',      'Bravo',     '1996-01-14', '604000111', '915111222', 'lorena.soto@futurespace.com',      '2023-11-01', NULL,         'S', 'S');

-- ─── 3. PROYECTOS (20 total: 15 activos, 5 históricos) ──────────────────────
INSERT INTO PR_PROYECTOS (TX_DESCRIPCION, F_INICIO, F_FIN, F_BAJA, TX_LUGAR, TX_OBSERVACIONES) VALUES
('Migración Cloud Azure 2026',          '2024-01-01', '2026-12-31', NULL,         'Madrid - Central',    'Modernización de infraestructura crítica hacia arquitectura cloud híbrida.'),
('Portal Inteligencia Prescriptiva',    '2024-02-15', '2025-06-30', NULL,         'Remoto',              'Motor de analítica avanzada con modelos predictivos e IA generativa.'),
('Sistema Gestión Logística v2',        '2023-11-01', '2025-08-30', NULL,         'Barcelona',           'Optimización de flujos y cadena de suministro con BI integrado.'),
('Auditoría Ciberseguridad Global',     '2024-03-01', '2025-05-31', NULL,         'Valencia',            'Evaluación de perímetros, protocolos y respuesta a incidentes.'),
('Modernización Core Bancario',         '2024-04-01', '2026-06-30', NULL,         'Sevilla',             'Migración a microservicios sobre sistemas legados mainframe.'),
('Rediseño UX/UI Plataformas',          '2024-05-01', '2025-04-30', NULL,         'Málaga',              'Rediseño integral de experiencia de usuario en productos digitales.'),
('Integración Pasarela de Pagos',       '2024-01-15', '2025-03-31', NULL,         'Bilbao',              'Implementación de métodos de pago PSD2 y wallets digitales.'),
('Expansión Infraestructura de Red',    '2024-06-01', '2025-12-31', NULL,         'Zaragoza',            'Despliegue de fibra óptica y modernización de nodos regionales.'),
('Digitalización Procesos RRHH',        '2024-07-01', '2025-06-30', NULL,         'Remoto',              'Automatización de onboarding, nómina y control horario digital.'),
('Plataforma e-Learning Corporativo',   '2024-08-01', '2025-12-31', NULL,         'Madrid - Norte',      'LMS corporativo con itinerarios formativos y certificación interna.'),
('Smart Office Madrid HQ',              '2025-01-01', '2026-06-30', NULL,         'Madrid - Central',    'Transformación del espacio de trabajo con IoT y automatización.'),
('Análisis Big Data Clientes',          '2024-09-01', '2025-12-31', NULL,         'Barcelona',           'Plataforma de datos unificada para segmentación y comportamiento.'),
('Sistema CRM Integrado',               '2024-10-01', '2026-03-31', NULL,         'Sevilla',             'Implantación de CRM corporativo con integraciones ERP y BI.'),
('Automatización Procesos Financieros', '2025-02-01', '2026-06-30', NULL,         'Madrid - Central',    'RPA y automatización de cierres contables y reporting regulatorio.'),
('Plataforma Colaboración Global',      '2025-01-01', '2025-12-31', NULL,         'Remoto',              'Suite unificada con videoconferencia y gestión documental.'),
('App Móvil Fidelización Plus',         '2023-05-10', '2023-12-20', '2023-12-31', 'Madrid - Norte',      'Proyecto concluido. App de fidelización con gamificación y recompensas.'),
('Mantenimiento Servidores 2023',       '2023-01-01', '2023-12-31', '2023-12-31', 'Madrid - DataCenter', 'Ciclo anual de mantenimiento preventivo y actualización de parque.'),
('Implantación SAP S/4HANA',            '2022-01-01', '2023-06-30', '2023-06-30', 'A Coruña',            'Migración ERP corporativo a SAP S/4HANA con módulos FI, CO, MM.'),
('Certificación ISO 27001 Fase 1',      '2022-06-01', '2023-03-31', '2023-03-31', 'Valencia',            'Implementación SGSI y obtención de certificación ISO 27001.'),
('Renovación Parque Tecnológico',       '2022-03-01', '2023-01-31', '2023-01-31', 'Madrid - DataCenter', 'Sustitución de equipos end-of-life y despliegue de nuevos servidores.');

-- ─── 4. ASIGNACIONES (100 registros) ────────────────────────────────────────
-- Activos en proyectos 1-15 (solo empleados activos)
-- Históricos en proyectos 16-20 (incluye empleados ya cesados que estaban activos entonces)
INSERT INTO PR_EMPLEADOS_PROYECTO (ID_PROYECTO, ID_EMPLEADO, F_ALTA) VALUES
-- Proyecto 1: Migración Cloud Azure (7 personas)
(1,  1,  '2024-01-01'), (1,  2,  '2024-01-01'), (1,  3,  '2024-01-15'),
(1,  14, '2024-01-20'), (1,  21, '2024-01-01'), (1,  27, '2024-01-10'), (1,  31, '2024-01-15'),
-- Proyecto 2: Portal Inteligencia Prescriptiva (6 personas)
(2,  1,  '2024-02-15'), (2,  4,  '2024-03-01'), (2,  13, '2024-03-05'),
(2,  22, '2024-02-15'), (2,  28, '2024-03-01'), (2,  36, '2024-02-20'),
-- Proyecto 3: Sistema Gestión Logística v2 (6 personas)
(3,  3,  '2023-11-01'), (3,  6,  '2023-11-01'), (3,  15, '2024-01-10'),
(3,  23, '2023-11-15'), (3,  31, '2023-12-01'), (3,  46, '2023-11-20'),
-- Proyecto 4: Auditoría Ciberseguridad (6 personas)
(4,  8,  '2024-03-01'), (4,  9,  '2024-03-15'), (4,  17, '2024-03-20'),
(4,  24, '2024-03-01'), (4,  29, '2024-03-10'), (4,  45, '2024-03-15'),
-- Proyecto 5: Modernización Core Bancario (6 personas)
(5,  11, '2024-04-01'), (5,  12, '2024-04-01'), (5,  18, '2024-04-05'),
(5,  25, '2024-04-10'), (5,  32, '2024-04-01'), (5,  42, '2024-04-15'),
-- Proyecto 6: Rediseño UX/UI (5 personas — 5 y 16 sin proyecto activo)
(6,  13, '2024-05-01'), (6,  26, '2024-05-10'), (6,  30, '2024-05-01'),
(6,  34, '2024-05-15'), (6,  48, '2024-05-01'),
-- Proyecto 7: Integración Pasarela de Pagos (3 personas — 16 y 37 sin proyecto activo)
(7,  2,  '2024-01-15'), (7,  9,  '2024-02-01'), (7,  44, '2024-01-20'),
-- Proyecto 8: Expansión Infraestructura de Red (4 personas — 20 sin proyecto activo)
(8,  39, '2024-06-01'), (8,  45, '2024-06-15'), (8,  50, '2024-06-01'), (8,  10, '2024-06-10'),
-- Proyecto 9: Digitalización RRHH (5 personas)
(9,  22, '2024-07-01'), (9,  28, '2024-07-01'), (9,  36, '2024-07-15'),
(9,  48, '2024-07-10'), (9,  11, '2024-07-01'),
-- Proyecto 10: e-Learning Corporativo (5 personas)
(10, 4,  '2024-08-01'), (10, 30, '2024-08-15'), (10, 34, '2024-08-01'),
(10, 42, '2024-08-10'), (10, 26, '2024-08-01'),
-- Proyecto 11: Smart Office Madrid HQ (4 personas)
(11, 21, '2025-01-01'), (11, 27, '2025-01-15'), (11, 31, '2025-01-01'), (11, 46, '2025-01-10'),
-- Proyecto 12: Análisis Big Data Clientes (6 personas)
(12, 2,  '2024-09-01'), (12, 13, '2024-09-15'), (12, 15, '2024-09-01'),
(12, 23, '2024-09-10'), (12, 36, '2024-09-01'), (12, 44, '2024-09-15'),
-- Proyecto 13: Sistema CRM Integrado (5 personas)
(13, 6,  '2024-10-01'), (13, 18, '2024-10-15'), (13, 25, '2024-10-01'),
(13, 32, '2024-10-10'), (13, 39, '2024-10-01'),
-- Proyecto 14: Automatización Procesos Financieros (5 personas)
(14, 1,  '2025-02-01'), (14, 10, '2025-02-15'), (14, 14, '2025-02-01'),
(14, 29, '2025-02-10'), (14, 46, '2025-02-01'),
-- Proyecto 15: Plataforma Colaboración Global (5 personas)
(15, 4,  '2025-01-01'), (15, 24, '2025-01-15'), (15, 26, '2025-01-01'),
(15, 48, '2025-01-10'), (15, 50, '2025-01-01'),
-- Proyecto 16: App Móvil Fidelización (histórico, 4 personas)
(16, 5,  '2023-05-10'), (16, 13, '2023-05-10'), (16, 16, '2023-06-01'), (16, 41, '2023-05-20'),
-- Proyecto 17: Mantenimiento Servidores 2023 (histórico, 5 personas)
(17, 8,  '2023-01-01'), (17, 16, '2023-01-01'), (17, 17, '2023-01-15'),
(17, 33, '2023-01-01'), (17, 41, '2023-01-20'),
-- Proyecto 18: Implantación SAP S/4HANA (histórico, 5 personas)
(18, 6,  '2022-01-01'), (18, 23, '2022-01-15'), (18, 35, '2022-01-15'),
(18, 38, '2022-02-01'), (18, 49, '2022-02-01'),
-- Proyecto 19: Certificación ISO 27001 (histórico, 5 personas)
(19, 8,  '2022-06-01'), (19, 12, '2022-06-01'), (19, 17, '2022-06-15'),
(19, 33, '2022-07-01'), (19, 40, '2022-07-01'),
-- Proyecto 20: Renovación Parque Tecnológico (histórico, 4 personas)
(20, 6,  '2022-03-01'), (20, 16, '2022-03-01'), (20, 35, '2022-03-15'), (20, 38, '2022-03-15');
