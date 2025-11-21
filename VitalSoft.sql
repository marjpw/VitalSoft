-- ================================================
-- VITALSOFT - SISTEMA HOSPITALARIO
-- Script de Creación de Base de Datos OPTIMIZADO
-- ================================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS sistema_hospitalario 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE sistema_hospitalario;

-- ================================================
-- ELIMINAR TABLAS EN ORDEN CORRECTO (si existen)
-- ================================================
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS facturas;
DROP TABLE IF EXISTS citas;
DROP TABLE IF EXISTS administradores;
DROP TABLE IF EXISTS medicos;
DROP TABLE IF EXISTS pacientes;
DROP TABLE IF EXISTS usuarios;

SET FOREIGN_KEY_CHECKS = 1;

-- ================================================
-- TABLA: usuarios (TABLA PADRE)
-- ================================================
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    userID VARCHAR(50) NOT NULL UNIQUE,
    psw VARCHAR(255) NOT NULL COMMENT 'Hash BCrypt de la contraseña',
    rol ENUM('Paciente', 'Medico', 'Administrador') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Índices para optimización
    INDEX idx_userID (userID),
    INDEX idx_rol (rol),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: administradores
-- ================================================
CREATE TABLE administradores (
    id_administrador INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_asignacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    -- Un usuario solo puede ser administrador una vez
    UNIQUE KEY unique_admin (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: medicos
-- ================================================
CREATE TABLE medicos (
    id_medico INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    DNI VARCHAR(20) NOT NULL UNIQUE,
    especialidad VARCHAR(100) NOT NULL,
    precio_consulta DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    -- Índices
    INDEX idx_dni (DNI),
    INDEX idx_especialidad (especialidad),
    
    -- Un usuario solo puede ser médico una vez
    UNIQUE KEY unique_medico (id_usuario),
    
    -- Validaciones
    CHECK (precio_consulta >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: pacientes
-- ================================================
CREATE TABLE pacientes (
    id_paciente INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    DNI VARCHAR(20) NOT NULL UNIQUE,
    edad INT NOT NULL,
    historiaClinica TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    -- Índices
    INDEX idx_dni (DNI),
    INDEX idx_edad (edad),
    
    -- Un usuario solo puede ser paciente una vez
    UNIQUE KEY unique_paciente (id_usuario),
    
    -- Validaciones
    CHECK (edad > 0 AND edad < 150)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: citas
-- ================================================
CREATE TABLE citas (
    id_cita INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    fecha DATETIME NOT NULL,
    motivo TEXT NOT NULL,
    diagnostico TEXT,
    estado ENUM('Programada', 'Completada', 'Cancelada') DEFAULT 'Programada',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    FOREIGN KEY (id_medico) REFERENCES medicos(id_medico)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    -- Índices para optimización de consultas
    INDEX idx_paciente (id_paciente),
    INDEX idx_medico (id_medico),
    INDEX idx_fecha (fecha),
    INDEX idx_estado (estado),
    
    -- Índice compuesto para búsquedas complejas
    INDEX idx_medico_fecha (id_medico, fecha),
    INDEX idx_paciente_fecha (id_paciente, fecha)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- TABLA: facturas
-- ================================================
CREATE TABLE facturas (
    id_factura INT AUTO_INCREMENT PRIMARY KEY,
    id_cita INT NOT NULL UNIQUE,
    importe DECIMAL(10, 2) NOT NULL,
    pagada BOOLEAN DEFAULT FALSE,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_pago DATETIME,
    metodo_pago ENUM('Efectivo', 'Tarjeta', 'Transferencia', 'Seguro') DEFAULT NULL,
    
    FOREIGN KEY (id_cita) REFERENCES citas(id_cita)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    
    -- Índices
    INDEX idx_pagada (pagada),
    INDEX idx_fecha_emision (fecha_emision),
    
    -- Validaciones
    CHECK (importe >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ================================================
-- DATOS INICIALES - ADMINISTRADOR POR DEFECTO
-- ================================================

-- Insertar usuario administrador
-- Password: admin123 (hash BCrypt)
INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo)
VALUES (
    'Admin', 
    'Sistema', 
    'admin', 
    '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 
    'Administrador', 
    TRUE
);

-- Asignar rol de administrador
INSERT INTO administradores (id_usuario)
VALUES (LAST_INSERT_ID());

-- ================================================
-- DATOS DE PRUEBA (OPCIONAL - COMENTADO)
-- ================================================

/*
-- Médico de prueba
INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo)
VALUES ('Juan', 'Pérez', 'jperez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Medico', TRUE);

INSERT INTO medicos (id_usuario, DNI, especialidad, precio_consulta)
VALUES (LAST_INSERT_ID(), '12345678', 'Cardiología', 150.00);

-- Paciente de prueba
INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo)
VALUES ('María', 'González', 'mgonzalez', '$2a$10$R4gJfT1dqO4XUWJubfRz7ucAYFVloRQWCQ6Hpp0Wx9t65znq.rHA2', 'Paciente', TRUE);

INSERT INTO pacientes (id_usuario, DNI, edad, historiaClinica)
VALUES (LAST_INSERT_ID(), '87654321', 35, 'Sin antecedentes médicos relevantes');
*/

-- ================================================
-- VISTAS ÚTILES PARA CONSULTAS
-- ================================================

-- Vista: Usuarios completos con sus roles específicos
CREATE OR REPLACE VIEW vista_usuarios_completos AS
SELECT 
    u.id_usuario,
    u.nombre,
    u.apellido,
    u.userID,
    u.rol,
    u.activo,
    CASE 
        WHEN u.rol = 'Medico' THEN m.id_medico
        WHEN u.rol = 'Paciente' THEN p.id_paciente
        WHEN u.rol = 'Administrador' THEN a.id_administrador
    END AS id_rol_especifico,
    CASE 
        WHEN u.rol = 'Medico' THEN m.DNI
        WHEN u.rol = 'Paciente' THEN p.DNI
    END AS dni,
    m.especialidad,
    m.precio_consulta,
    p.edad,
    u.fecha_registro
FROM usuarios u
LEFT JOIN medicos m ON u.id_usuario = m.id_usuario
LEFT JOIN pacientes p ON u.id_usuario = p.id_usuario
LEFT JOIN administradores a ON u.id_usuario = a.id_usuario;

-- Vista: Citas con información completa
CREATE OR REPLACE VIEW vista_citas_completas AS
SELECT 
    c.id_cita,
    c.fecha,
    c.motivo,
    c.diagnostico,
    c.estado,
    -- Información del paciente
    p.id_paciente,
    p.DNI AS paciente_dni,
    up.nombre AS paciente_nombre,
    up.apellido AS paciente_apellido,
    -- Información del médico
    m.id_medico,
    m.DNI AS medico_dni,
    m.especialidad,
    um.nombre AS medico_nombre,
    um.apellido AS medico_apellido,
    m.precio_consulta,
    -- Información de factura
    f.id_factura,
    f.importe,
    f.pagada,
    f.fecha_emision AS fecha_factura
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN usuarios up ON p.id_usuario = up.id_usuario
JOIN medicos m ON c.id_medico = m.id_medico
JOIN usuarios um ON m.id_usuario = um.id_usuario
LEFT JOIN facturas f ON c.id_cita = f.id_cita;

-- ================================================
-- PROCEDIMIENTOS ALMACENADOS ÚTILES
-- ================================================

DELIMITER //

-- Procedimiento: Crear cita y factura automáticamente
CREATE PROCEDURE sp_crear_cita_con_factura(
    IN p_id_paciente INT,
    IN p_id_medico INT,
    IN p_fecha DATETIME,
    IN p_motivo TEXT
)
BEGIN
    DECLARE v_id_cita INT;
    DECLARE v_precio DECIMAL(10,2);
    
    -- Iniciar transacción
    START TRANSACTION;
    
    -- Insertar cita
    INSERT INTO citas (id_paciente, id_medico, fecha, motivo)
    VALUES (p_id_paciente, p_id_medico, p_fecha, p_motivo);
    
    SET v_id_cita = LAST_INSERT_ID();
    
    -- Obtener precio de consulta del médico
    SELECT precio_consulta INTO v_precio
    FROM medicos
    WHERE id_medico = p_id_medico;
    
    -- Crear factura automáticamente
    INSERT INTO facturas (id_cita, importe)
    VALUES (v_id_cita, v_precio);
    
    COMMIT;
    
    SELECT v_id_cita AS id_cita_creada;
END //

DELIMITER ;

-- ================================================
-- TRIGGERS PARA AUDITORÍA Y VALIDACIONES
-- ================================================

DELIMITER //

-- Trigger: Validar que la fecha de cita no sea en el pasado
CREATE TRIGGER trg_validar_fecha_cita
BEFORE INSERT ON citas
FOR EACH ROW
BEGIN
    IF NEW.fecha < NOW() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La fecha de la cita es demasiado antigua';
    END IF;
END //

-- Trigger: Actualizar estado de cita a Completada cuando se añade diagnóstico
CREATE TRIGGER trg_actualizar_estado_cita
BEFORE UPDATE ON citas
FOR EACH ROW
BEGIN
    IF NEW.diagnostico IS NOT NULL AND OLD.diagnostico IS NULL THEN
        SET NEW.estado = 'Completada';
    END IF;
END //

DELIMITER ;

-- ================================================
-- INFORMACIÓN DEL SISTEMA
-- ================================================
SELECT 'Base de datos VITALSOFT creada exitosamente' AS mensaje;
SELECT 'Usuario admin creado con contraseña: admin123' AS credenciales;
SELECT 'Ejecute queries de prueba para verificar' AS siguiente_paso;