# VitalSoft - Guía de Implementación de Base de Datos MySQL

## 📋 Descripción General

Este documento proporciona instrucciones detalladas para implementar la base de datos MySQL del sistema VitalSoft en tu PC.

## 🗄️ Estructura de la Base de Datos

La base de datos `vitalsoft_db` incluye las siguientes tablas:

### Tablas Principales

1. **administradores** - Gestión de usuarios administradores
2. **medicos** - Información de médicos y especialidades
3. **pacientes** - Datos de pacientes registrados
4. **citas** - Registro de citas médicas con triaje y diagnósticos
5. **facturas** - Facturas generadas por consultas

### Características Adicionales

- ✅ **Vistas**: `vista_citas_completas`, `vista_facturas_completas`
- ✅ **Procedimientos Almacenados**: `sp_crear_cita`, `sp_generar_factura`
- ✅ **Triggers**: Actualización automática de IMC del paciente
- ✅ **Índices**: Optimización de consultas frecuentes
- ✅ **Datos de Prueba**: Usuarios, médicos y pacientes de ejemplo

## 🔧 Requisitos Previos

1. **MySQL Server** instalado (versión 5.7 o superior)
   - Descargar desde: https://dev.mysql.com/downloads/mysql/
   
2. **MySQL Workbench** (opcional, pero recomendado)
   - Descargar desde: https://dev.mysql.com/downloads/workbench/

3. **Credenciales de acceso** a MySQL con permisos para crear bases de datos

## 📝 Pasos de Implementación

### Opción 1: Usando MySQL Workbench (Recomendado)

1. **Abrir MySQL Workbench**
   - Conectarse a tu servidor MySQL local

2. **Ejecutar el Script**
   - File → Open SQL Script
   - Seleccionar el archivo `schema.sql`
   - Hacer clic en el ícono del rayo ⚡ (Execute)

3. **Verificar la Creación**
   - Refrescar el panel de Schemas
   - Deberías ver `vitalsoft_db` con todas las tablas

### Opción 2: Usando Línea de Comandos

```bash
# 1. Abrir terminal/CMD y conectarse a MySQL
mysql -u root -p

# 2. Ejecutar el script desde MySQL
source C:\Users\Intel\source\repos\VitalSoft\database\schema.sql

# O alternativamente, desde la línea de comandos directamente:
mysql -u root -p < C:\Users\Intel\source\repos\VitalSoft\database\schema.sql
```

### Opción 3: Usando phpMyAdmin

1. Acceder a phpMyAdmin (generalmente http://localhost/phpmyadmin)
2. Ir a la pestaña "SQL"
3. Copiar y pegar el contenido de `schema.sql`
4. Hacer clic en "Continuar"

## 🔐 Credenciales por Defecto

El script crea los siguientes usuarios de prueba:

### Administrador
- **Usuario**: `admin`
- **Contraseña**: `admin123`
- **DNI**: `12345678`

### Médicos
| Nombre | DNI | Especialidad | Contraseña |
|--------|-----|--------------|------------|
| Juan Pérez García | 20111222 | Cardiología | medico123 |
| María López Sánchez | 20333444 | Pediatría | medico123 |
| Carlos Rodríguez Torres | 20555666 | Medicina General | medico123 |

### Pacientes
| Nombre | DNI | Contraseña |
|--------|-----|------------|
| Ana Martínez Flores | 30111222 | paciente123 |
| Pedro González Ruiz | 30333444 | paciente123 |
| Lucía Fernández Castro | 30555666 | paciente123 |

## ⚙️ Configuración de Conexión

Deberás configurar la conexión en tu aplicación Java con los siguientes parámetros:

```properties
# Archivo: db.properties
db.url=jdbc:mysql://localhost:3306/vitalsoft_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=tu_password_mysql
db.driver=com.mysql.cj.jdbc.Driver
```

**Importante**: Reemplaza `tu_password_mysql` con la contraseña de tu usuario MySQL.

## 📊 Diagrama de Relaciones

```
administradores (independiente)

medicos (independiente)
    ↓
pacientes (independiente)
    ↓
citas (FK: paciente_id, medico_id)
    ↓
facturas (FK: cita_id)
```

## 🧪 Verificación de la Instalación

Ejecuta las siguientes consultas para verificar que todo esté correcto:

```sql
-- Verificar tablas creadas
SHOW TABLES;

-- Verificar datos de prueba
SELECT COUNT(*) FROM administradores;  -- Debe retornar 1
SELECT COUNT(*) FROM medicos;          -- Debe retornar 3
SELECT COUNT(*) FROM pacientes;        -- Debe retornar 3

-- Verificar vistas
SELECT * FROM vista_citas_completas LIMIT 1;

-- Verificar procedimientos
SHOW PROCEDURE STATUS WHERE Db = 'vitalsoft_db';
```

## 🔄 Actualización del Schema

Si necesitas actualizar el schema en el futuro:

```sql
-- Hacer backup primero
mysqldump -u root -p vitalsoft_db > backup_vitalsoft.sql

-- Luego ejecutar el nuevo schema
-- (El script usa IF NOT EXISTS para evitar duplicados)
```

## 🚨 Solución de Problemas

### Error: "Access denied for user"
- Verifica que tu usuario MySQL tenga permisos para crear bases de datos
- Intenta con: `GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost';`

### Error: "Unknown database"
- Asegúrate de que el script se ejecute completo
- Verifica que la línea `CREATE DATABASE` se haya ejecutado

### Error: "Table already exists"
- La base de datos ya existe
- Puedes eliminarla con: `DROP DATABASE vitalsoft_db;` y volver a ejecutar el script

## 📞 Próximos Pasos

Una vez implementada la base de datos:

1. ✅ Crear clase `DatabaseConnection.java` para gestionar conexiones
2. ✅ Crear DAOs (Data Access Objects) para cada entidad
3. ✅ Integrar con `ServiceManager.java`
4. ✅ Actualizar los paneles de UI para usar la base de datos

## 📝 Notas Importantes

- **Seguridad**: Las contraseñas están en texto plano para desarrollo. En producción, usa hashing (BCrypt, SHA-256, etc.)
- **Backup**: Realiza backups regulares de tu base de datos
- **Índices**: Los índices están optimizados para las consultas más frecuentes
- **Charset**: Se usa `utf8mb4` para soportar caracteres especiales y emojis

---

**¿Necesitas ayuda?** Consulta la documentación de MySQL: https://dev.mysql.com/doc/
