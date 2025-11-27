-- ============================================
-- VITALSOFT - MYSQL DATABASE SCHEMA
-- Sistema de Gestión Clínica
-- ============================================

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS vitalsoft_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE vitalsoft_db;

-- ============================================
-- TABLA: administradores
-- Almacena información de los administradores del sistema
-- ============================================
CREATE TABLE administradores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_usuario (usuario),
    INDEX idx_dni (dni)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: medicos
-- Almacena información de los médicos
-- ============================================
CREATE TABLE medicos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    precio_consulta DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    dias_atencion VARCHAR(255),
    horario_hora VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_dni (dni),
    INDEX idx_especialidad (especialidad),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: pacientes
-- Almacena información de los pacientes
-- ============================================
CREATE TABLE pacientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    celular VARCHAR(20),
    fecha_nacimiento DATE NOT NULL,
    alergias TEXT,
    password VARCHAR(255) NOT NULL,
    imc VARCHAR(10),
    categoria_imc VARCHAR(50),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    INDEX idx_dni (dni),
    INDEX idx_apellidos (apellidos),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: citas
-- Almacena información de las citas médicas
-- ============================================
CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha DATE NOT NULL,
    motivo TEXT NOT NULL,
    numero_turno INT NOT NULL,
    estado ENUM('PENDIENTE', 'ATENDIDA', 'CANCELADA') DEFAULT 'PENDIENTE',
    
    -- Datos clínicos
    diagnostico TEXT,
    receta TEXT,
    
    -- Datos de triaje (signos vitales)
    peso DECIMAL(5, 2) DEFAULT 0.00,
    talla DECIMAL(4, 2) DEFAULT 0.00,
    temperatura DECIMAL(4, 2) DEFAULT 0.00,
    presion_arterial VARCHAR(20),
    triaje_realizado BOOLEAN DEFAULT FALSE,
    
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (paciente_id) REFERENCES pacientes(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES medicos(id) ON DELETE CASCADE,
    
    INDEX idx_paciente (paciente_id),
    INDEX idx_medico (medico_id),
    INDEX idx_fecha (fecha),
    INDEX idx_estado (estado),
    INDEX idx_triaje (triaje_realizado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- TABLA: facturas
-- Almacena información de las facturas generadas
-- ============================================
CREATE TABLE facturas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cita_id INT NOT NULL UNIQUE,
    subtotal DECIMAL(10, 2) NOT NULL,
    igv DECIMAL(10, 2) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (cita_id) REFERENCES citas(id) ON DELETE CASCADE,
    
    INDEX idx_cita (cita_id),
    INDEX idx_fecha_emision (fecha_emision)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- DATOS INICIALES DE PRUEBA
-- ============================================

-- Insertar administrador por defecto
-- Usuario: admin, Password: admin123
INSERT INTO administradores (nombre, dni, usuario, password) 
VALUES ('Administrador Principal', '12345678', 'admin', 'admin123');

-- Insertar médicos de ejemplo
INSERT INTO medicos (nombre, apellidos, especialidad, dni, precio_consulta, dias_atencion, horario_hora, password) 
VALUES 
    ('Juan', 'Pérez García', 'Cardiología', '20111222', 150.00, 'Lunes, Miércoles, Viernes', '09:00 - 13:00', 'medico123'),
    ('María', 'López Sánchez', 'Pediatría', '20333444', 120.00, 'Martes, Jueves', '14:00 - 18:00', 'medico123'),
    ('Carlos', 'Rodríguez Torres', 'Medicina General', '20555666', 100.00, 'Lunes a Viernes', '08:00 - 12:00', 'medico123');

-- Insertar pacientes de ejemplo
INSERT INTO pacientes (nombre, apellidos, dni, celular, fecha_nacimiento, alergias, password) 
VALUES 
    ('Ana', 'Martínez Flores', '30111222', '987654321', '1990-05-15', 'Penicilina', 'paciente123'),
    ('Pedro', 'González Ruiz', '30333444', '987654322', '1985-08-20', 'Ninguna', 'paciente123'),
    ('Lucía', 'Fernández Castro', '30555666', '987654323', '2000-12-10', 'Polen', 'paciente123');

-- ============================================
-- VISTAS ÚTILES
-- ============================================

-- Vista para obtener información completa de citas
CREATE OR REPLACE VIEW vista_citas_completas AS
SELECT 
    c.id AS cita_id,
    c.fecha,
    c.numero_turno,
    c.motivo,
    c.estado,
    c.diagnostico,
    c.receta,
    c.triaje_realizado,
    c.peso,
    c.talla,
    c.temperatura,
    c.presion_arterial,
    CASE 
        WHEN c.talla > 0 AND c.peso > 0 
        THEN ROUND(c.peso / (c.talla * c.talla), 2)
        ELSE 0
    END AS imc,
    p.id AS paciente_id,
    p.nombre AS paciente_nombre,
    p.apellidos AS paciente_apellidos,
    p.dni AS paciente_dni,
    p.celular AS paciente_celular,
    p.fecha_nacimiento AS paciente_fecha_nacimiento,
    TIMESTAMPDIFF(YEAR, p.fecha_nacimiento, CURDATE()) AS paciente_edad,
    p.alergias AS paciente_alergias,
    m.id AS medico_id,
    m.nombre AS medico_nombre,
    m.apellidos AS medico_apellidos,
    m.especialidad AS medico_especialidad,
    m.precio_consulta AS medico_precio
FROM citas c
INNER JOIN pacientes p ON c.paciente_id = p.id
INNER JOIN medicos m ON c.medico_id = m.id;

-- Vista para obtener información completa de facturas
CREATE OR REPLACE VIEW vista_facturas_completas AS
SELECT 
    f.id AS factura_id,
    f.subtotal,
    f.igv,
    f.total,
    f.fecha_emision,
    c.id AS cita_id,
    c.fecha AS cita_fecha,
    c.numero_turno,
    p.nombre AS paciente_nombre,
    p.apellidos AS paciente_apellidos,
    p.dni AS paciente_dni,
    m.nombre AS medico_nombre,
    m.apellidos AS medico_apellidos,
    m.especialidad AS medico_especialidad
FROM facturas f
INNER JOIN citas c ON f.cita_id = c.id
INNER JOIN pacientes p ON c.paciente_id = p.id
INNER JOIN medicos m ON c.medico_id = m.id;

-- ============================================
-- PROCEDIMIENTOS ALMACENADOS
-- ============================================

-- Procedimiento para crear una nueva cita
DELIMITER //
CREATE PROCEDURE sp_crear_cita(
    IN p_paciente_id INT,
    IN p_medico_id INT,
    IN p_fecha DATE,
    IN p_motivo TEXT,
    OUT p_cita_id INT,
    OUT p_numero_turno INT
)
BEGIN
    -- Obtener el siguiente número de turno para ese médico en esa fecha
    SELECT COALESCE(MAX(numero_turno), 0) + 1 INTO p_numero_turno
    FROM citas
    WHERE medico_id = p_medico_id AND fecha = p_fecha;
    
    -- Insertar la cita
    INSERT INTO citas (paciente_id, medico_id, fecha, motivo, numero_turno)
    VALUES (p_paciente_id, p_medico_id, p_fecha, p_motivo, p_numero_turno);
    
    SET p_cita_id = LAST_INSERT_ID();
END //
DELIMITER ;

-- Procedimiento para generar factura
DELIMITER //
CREATE PROCEDURE sp_generar_factura(
    IN p_cita_id INT,
    OUT p_factura_id INT
)
BEGIN
    DECLARE v_precio_consulta DECIMAL(10, 2);
    DECLARE v_subtotal DECIMAL(10, 2);
    DECLARE v_igv DECIMAL(10, 2);
    DECLARE v_total DECIMAL(10, 2);
    
    -- Obtener precio de consulta
    SELECT m.precio_consulta INTO v_precio_consulta
    FROM citas c
    INNER JOIN medicos m ON c.medico_id = m.id
    WHERE c.id = p_cita_id;
    
    -- Calcular montos
    SET v_total = v_precio_consulta;
    SET v_subtotal = v_total / 1.18;
    SET v_igv = v_total - v_subtotal;
    
    -- Insertar factura
    INSERT INTO facturas (cita_id, subtotal, igv, total)
    VALUES (p_cita_id, v_subtotal, v_igv, v_total);
    
    SET p_factura_id = LAST_INSERT_ID();
END //
DELIMITER ;

-- ============================================
-- TRIGGERS
-- ============================================

-- Trigger para actualizar IMC del paciente cuando se registra triaje
DELIMITER //
CREATE TRIGGER trg_actualizar_imc_paciente
AFTER UPDATE ON citas
FOR EACH ROW
BEGIN
    DECLARE v_imc DECIMAL(5, 2);
    DECLARE v_categoria VARCHAR(50);
    
    IF NEW.triaje_realizado = TRUE AND NEW.talla > 0 AND NEW.peso > 0 THEN
        SET v_imc = NEW.peso / (NEW.talla * NEW.talla);
        
        -- Determinar categoría
        IF v_imc < 18.5 THEN
            SET v_categoria = 'Bajo peso';
        ELSEIF v_imc < 25.0 THEN
            SET v_categoria = 'Normal';
        ELSEIF v_imc < 30.0 THEN
            SET v_categoria = 'Sobrepeso';
        ELSEIF v_imc < 35.0 THEN
            SET v_categoria = 'Obesidad I';
        ELSEIF v_imc < 40.0 THEN
            SET v_categoria = 'Obesidad II';
        ELSE
            SET v_categoria = 'Obesidad III';
        END IF;
        
        -- Actualizar paciente
        UPDATE pacientes 
        SET imc = ROUND(v_imc, 2),
            categoria_imc = v_categoria
        WHERE id = NEW.paciente_id;
    END IF;
END //
DELIMITER ;

-- ============================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- ============================================

-- Índice compuesto para búsquedas de citas por médico y fecha
CREATE INDEX idx_medico_fecha ON citas(medico_id, fecha);

-- Índice compuesto para búsquedas de citas por paciente y estado
CREATE INDEX idx_paciente_estado ON citas(paciente_id, estado);

-- ============================================
-- COMENTARIOS FINALES
-- ============================================
-- Este esquema incluye:
-- 1. Todas las tablas necesarias para el sistema VitalSoft
-- 2. Relaciones con claves foráneas y restricciones
-- 3. Índices para optimizar consultas frecuentes
-- 4. Datos iniciales de prueba
-- 5. Vistas para consultas complejas
-- 6. Procedimientos almacenados para operaciones comunes
-- 7. Triggers para mantener integridad de datos
-- ============================================
