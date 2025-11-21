-- ================================================
-- VITALSOFT - SCRIPT DE PRUEBAS Y MANTENIMIENTO
-- ================================================

USE sistema_hospitalario;

-- ================================================
-- CONSULTAS DE VERIFICACIÓN
-- ================================================

-- Verificar estructura de tablas
SHOW TABLES;

-- Verificar columnas de cada tabla
DESCRIBE usuarios;
DESCRIBE administradores;
DESCRIBE medicos;
DESCRIBE pacientes;
DESCRIBE citas;
DESCRIBE facturas;

-- Verificar índices
SHOW INDEX FROM usuarios;
SHOW INDEX FROM medicos;
SHOW INDEX FROM pacientes;
SHOW INDEX FROM citas;

-- Verificar foreign keys
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'sistema_hospitalario'
AND REFERENCED_TABLE_NAME IS NOT NULL;

-- ================================================
-- INSERTAR DATOS DE PRUEBA COMPLETOS
-- ================================================

-- Limpiar datos de prueba anteriores (CUIDADO EN PRODUCCIÓN)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE facturas;
TRUNCATE TABLE citas;
TRUNCATE TABLE medicos;
TRUNCATE TABLE pacientes;
-- NO truncar usuarios ni administradores para mantener el admin
DELETE FROM usuarios WHERE id_usuario > 1;
SET FOREIGN_KEY_CHECKS = 1;

-- Insertar 3 médicos
INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo) VALUES
('Carlos', 'Ramírez', 'cramirez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Medico', TRUE),
('Ana', 'Torres', 'atorres', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Medico', TRUE),
('Luis', 'Mendoza', 'lmendoza', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Medico', TRUE);

INSERT INTO medicos (id_usuario, DNI, especialidad, precio_consulta) VALUES
(2, '12345678', 'Cardiología', 150.00),
(3, '23456789', 'Pediatría', 120.00),
(4, '34567890', 'Traumatología', 180.00);

-- Insertar 5 pacientes
INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo) VALUES
('María', 'González', 'mgonzalez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Paciente', TRUE),
('Pedro', 'Sánchez', 'psanchez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Paciente', TRUE),
('Laura', 'Martínez', 'lmartinez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Paciente', TRUE),
('José', 'López', 'jlopez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Paciente', TRUE),
('Carmen', 'Díaz', 'cdiaz', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Paciente', TRUE);

INSERT INTO pacientes (id_usuario, DNI, edad, historiaClinica) VALUES
(5, '45678901', 35, 'Hipertensión controlada'),
(6, '56789012', 42, 'Diabetes tipo 2'),
(7, '67890123', 28, 'Sin antecedentes'),
(8, '78901234', 55, 'Colesterol alto'),
(9, '89012345', 38, 'Alergia a penicilina');

-- Insertar citas (algunas en el futuro, otras pasadas)
INSERT INTO citas (id_paciente, id_medico, fecha, motivo, diagnostico, estado) VALUES
(1, 1, '2025-11-25 10:00:00', 'Control de presión arterial', 'Presión estable, continuar medicación', 'Completada'),
(2, 2, '2025-11-26 11:30:00', 'Revisión pediátrica', NULL, 'Programada'),
(3, 3, '2025-11-27 14:00:00', 'Dolor en rodilla', NULL, 'Programada'),
(1, 1, '2025-12-05 09:00:00', 'Seguimiento cardiológico', NULL, 'Programada'),
(4, 2, '2025-11-20 16:00:00', 'Chequeo general', 'Valores normales, dieta saludable recomendada', 'Completada'),
(5, 3, '2025-12-10 10:30:00', 'Dolor de espalda', NULL, 'Programada');

-- Insertar facturas para las citas completadas
INSERT INTO facturas (id_cita, importe, pagada, metodo_pago) VALUES
(1, 150.00, TRUE, 'Tarjeta'),
(5, 120.00, FALSE, NULL);

-- ================================================
-- CONSULTAS DE PRUEBA ÚTILES
-- ================================================

-- 1. Ver todos los usuarios con sus roles
SELECT * FROM vista_usuarios_completos;

-- 2. Ver todas las citas con información completa
SELECT * FROM vista_citas_completas;

-- 3. Médicos con más citas
SELECT 
    m.id_medico,
    u.nombre,
    u.apellido,
    m.especialidad,
    COUNT(c.id_cita) AS total_citas
FROM medicos m
JOIN usuarios u ON m.id_usuario = u.id_usuario
LEFT JOIN citas c ON m.id_medico = c.id_medico
GROUP BY m.id_medico, u.nombre, u.apellido, m.especialidad
ORDER BY total_citas DESC;

-- 4. Citas próximas (futuras)
SELECT 
    c.id_cita,
    c.fecha,
    c.motivo,
    CONCAT(up.nombre, ' ', up.apellido) AS paciente,
    CONCAT(um.nombre, ' ', um.apellido) AS medico,
    m.especialidad
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN usuarios up ON p.id_usuario = up.id_usuario
JOIN medicos m ON c.id_medico = m.id_medico
JOIN usuarios um ON m.id_usuario = um.id_usuario
WHERE c.fecha > NOW()
AND c.estado = 'Programada'
ORDER BY c.fecha;

-- 5. Facturas pendientes de pago
SELECT 
    f.id_factura,
    f.importe,
    f.fecha_emision,
    c.fecha AS fecha_cita,
    CONCAT(u.nombre, ' ', u.apellido) AS paciente
FROM facturas f
JOIN citas c ON f.id_cita = c.id_cita
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN usuarios u ON p.id_usuario = u.id_usuario
WHERE f.pagada = FALSE
ORDER BY f.fecha_emision DESC;

-- 6. Historial médico completo de un paciente (ejemplo: DNI 45678901)
SELECT 
    c.fecha,
    c.motivo,
    c.diagnostico,
    CONCAT(um.nombre, ' ', um.apellido) AS medico,
    m.especialidad
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN medicos m ON c.id_medico = m.id_medico
JOIN usuarios um ON m.id_usuario = um.id_usuario
WHERE p.DNI = '45678901'
ORDER BY c.fecha DESC;

-- 7. Estadísticas generales del sistema
SELECT 
    (SELECT COUNT(*) FROM usuarios WHERE rol = 'Paciente') AS total_pacientes,
    (SELECT COUNT(*) FROM usuarios WHERE rol = 'Medico') AS total_medicos,
    (SELECT COUNT(*) FROM usuarios WHERE rol = 'Administrador') AS total_admins,
    (SELECT COUNT(*) FROM citas) AS total_citas,
    (SELECT COUNT(*) FROM citas WHERE estado = 'Programada') AS citas_programadas,
    (SELECT COUNT(*) FROM citas WHERE estado = 'Completada') AS citas_completadas,
    (SELECT COUNT(*) FROM facturas WHERE pagada = FALSE) AS facturas_pendientes,
    (SELECT SUM(importe) FROM facturas WHERE pagada = FALSE) AS monto_pendiente;

-- ================================================
-- PRUEBAS DE INTEGRIDAD REFERENCIAL
-- ================================================

-- Intentar insertar cita con id_paciente inexistente (debe fallar)
-- INSERT INTO citas (id_paciente, id_medico, fecha, motivo) 
-- VALUES (999, 1, '2024-12-01 10:00:00', 'Test');

-- Intentar insertar factura con id_cita inexistente (debe fallar)
-- INSERT INTO facturas (id_cita, importe) VALUES (999, 100.00);

-- ================================================
-- PRUEBAS DE TRIGGERS
-- ================================================

-- Intentar crear cita en el pasado (debe fallar por trigger)
-- INSERT INTO citas (id_paciente, id_medico, fecha, motivo)
-- VALUES (1, 1, '2020-01-01 10:00:00', 'Test fecha pasada');

-- ================================================
-- LIMPIEZA DE DATOS DE PRUEBA (EJECUTAR CON CUIDADO)
-- ================================================

/*
-- Para limpiar TODOS los datos (excepto el administrador)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE facturas;
TRUNCATE TABLE citas;
TRUNCATE TABLE medicos;
TRUNCATE TABLE pacientes;
DELETE FROM usuarios WHERE id_usuario > 1;
SET FOREIGN_KEY_CHECKS = 1;
*/

-- ================================================
-- CONSULTAS DE MANTENIMIENTO
-- ================================================

-- Verificar tamaño de las tablas
SELECT 
    table_name AS 'Tabla',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Tamaño (MB)'
FROM information_schema.TABLES 
WHERE table_schema = 'sistema_hospitalario'
ORDER BY (data_length + index_length) DESC;

-- Optimizar todas las tablas
OPTIMIZE TABLE usuarios, administradores, medicos, pacientes, citas, facturas;

-- Analizar tablas para estadísticas del optimizador
ANALYZE TABLE usuarios, administradores, medicos, pacientes, citas, facturas;

SELECT '✅ Script de pruebas ejecutado exitosamente' AS resultado;