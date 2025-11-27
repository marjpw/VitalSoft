-- ============================================
-- MIGRATION SCRIPT: Fix estado ENUM column
-- ============================================
-- This script updates the estado column in the citas table
-- to match the Java enum values and add the missing NO_ASISTIO state

USE vitalsoft_db;

-- Modify the estado column to include all valid states
ALTER TABLE citas 
MODIFY COLUMN estado ENUM('PENDIENTE', 'ATENDIDA', 'CANCELADA', 'NO_ASISTIO') DEFAULT 'PENDIENTE';

-- Verify the change
SHOW COLUMNS FROM citas LIKE 'estado';
