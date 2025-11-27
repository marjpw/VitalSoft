# VitalSoft - Conexión a Base de Datos

## 📁 Archivos Creados

### Capa de Conexión
- **DatabaseConnection.java** - Singleton para gestionar conexiones MySQL

### Capa DAO (Data Access Objects)
- **AdministradorDAO.java** - Operaciones de BD para administradores
- **PacienteDAO.java** - Operaciones de BD para pacientes
- **MedicoDAO.java** - Operaciones de BD para médicos
- **CitaDAO.java** - Operaciones de BD para citas
- **FacturaDAO.java** - Operaciones de BD para facturas

### Servicios Actualizados
Todos los servicios ahora usan la capa DAO:
- ✅ AdministradorService
- ✅ PacienteService
- ✅ MedicoService
- ✅ CitaService
- ✅ FacturaService

## 🔧 Configuración Requerida

### 1. Descargar MySQL Connector/J

Descarga el driver JDBC de MySQL:
- URL: https://dev.mysql.com/downloads/connector/j/
- Versión recomendada: 8.0 o superior

### 2. Agregar al Classpath

Copia el archivo `.jar` descargado a una carpeta `libs` en tu proyecto y agrégalo al classpath:

```
VitalSoft/
├── interVitalSoft/
│   ├── src/
│   └── libs/
│       └── mysql-connector-j-8.0.33.jar
```

### 3. Configurar db.properties

Edita el archivo `database/db.properties` con tus credenciales:

```properties
db.url=jdbc:mysql://localhost:3306/vitalsoft_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=TU_PASSWORD_AQUI
db.driver=com.mysql.cj.jdbc.Driver
```

### 4. Implementar Base de Datos

Ejecuta el script `database/schema.sql` en MySQL Workbench o desde la línea de comandos.

## 🚀 Cómo Funciona

### Patrón de Diseño

El proyecto ahora usa el patrón **DAO (Data Access Object)**:

```
UI Panels → Services → DAOs → Database
```

### Ejemplo de Flujo

1. **Login de Paciente**:
   ```
   LoginDialog → PacienteService.loginPaciente() 
   → PacienteDAO.login() → MySQL Query
   ```

2. **Crear Cita**:
   ```
   PanelCrearCita → CitaService.crearCita() 
   → CitaDAO.insertar() → Stored Procedure sp_crear_cita
   ```

### Ventajas

- ✅ **Persistencia**: Los datos se guardan en MySQL
- ✅ **Separación de responsabilidades**: Lógica de negocio separada de acceso a datos
- ✅ **Reutilizable**: Los DAOs pueden usarse desde cualquier servicio
- ✅ **Mantenible**: Cambios en la BD solo afectan a los DAOs

## 📝 Notas Importantes

1. **Primera Ejecución**: 
   - Asegúrate de que MySQL esté ejecutándose
   - Verifica que la base de datos `vitalsoft_db` exista
   - Confirma que el archivo `db.properties` tenga las credenciales correctas

2. **Datos de Prueba**:
   - El script `schema.sql` incluye datos de prueba
   - Admin: usuario=`admin`, password=`admin123`
   - Médicos: password=`medico123`
   - Pacientes: password=`paciente123`

3. **IDs Autoincrementales**:
   - Los IDs ahora los genera MySQL (AUTO_INCREMENT)
   - No es necesario gestionar contadores en Java

4. **Transacciones**:
   - Los procedimientos almacenados manejan transacciones automáticamente
   - Para operaciones complejas, considera usar transacciones explícitas

## 🐛 Solución de Problemas

### Error: "No suitable driver found"
- Verifica que `mysql-connector-j.jar` esté en el classpath
- Confirma que el driver esté cargado: `Class.forName("com.mysql.cj.jdbc.Driver")`

### Error: "Access denied for user"
- Verifica usuario y contraseña en `db.properties`
- Asegúrate de que el usuario tenga permisos en la base de datos

### Error: "Unknown database 'vitalsoft_db'"
- Ejecuta el script `schema.sql` para crear la base de datos
- Verifica que MySQL esté ejecutándose

### La aplicación no guarda datos
- Verifica la conexión a la base de datos
- Revisa los logs de consola para errores SQL
- Confirma que los servicios estén usando los DAOs correctamente

## 📚 Próximos Pasos

1. ✅ Base de datos implementada
2. ✅ Capa de conexión creada
3. ✅ DAOs implementados
4. ✅ Servicios actualizados
5. ⏳ Probar la aplicación con la base de datos
6. ⏳ Ajustar UI si es necesario

---

**¿Listo para probar?** Ejecuta la aplicación y verifica que los datos se guarden correctamente en MySQL.
