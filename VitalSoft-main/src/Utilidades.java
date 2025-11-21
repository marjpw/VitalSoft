import java.sql.*;

public class Utilidades {
    
    public static int obtenerIdPacientePorIdUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT id_paciente FROM pacientes WHERE id_usuario = ?";
        try (Connection c = Conexion.conectar(); 
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idUsuario);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_paciente");
                }
            }
        }
        throw new SQLException("No se encontró paciente con id_usuario: " + idUsuario);
    }
    
    public static int obtenerIdMedicoPorIdUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT id_medico FROM medicos WHERE id_usuario = ?";
        try (Connection c = Conexion.conectar(); 
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idUsuario);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_medico");
                }
            }
        }
        throw new SQLException("No se encontró médico con id_usuario: " + idUsuario);
    }
    
    public static int obtenerIdPacienteSeguro(Paciente paciente) {
        try {
            return paciente.getIdPaciente();
        } catch (Exception ex) {
            try {
                return obtenerIdPacientePorIdUsuario(paciente.getIdUsuario());
            } catch (Exception e) {
                System.out.println("⚠️ Imposible obtener id_paciente.");
                return -1;
            }
        }
    }
   
    public static int obtenerIdMedicoSeguro(Medico medico) {
        try {
            return medico.getIdMedico();
        } catch (Exception ex) {
            try {
                return obtenerIdMedicoPorIdUsuario(medico.getIdUsuario());
            } catch (Exception e) {
                System.out.println("⚠️ Imposible obtener id_medico.");
                return -1;
            }
        }
    }
    
    public static Timestamp convertirATimestamp(String fechaInput) {
        if (fechaInput == null || fechaInput.isBlank() || 
            fechaInput.equalsIgnoreCase("fecha")) {
            return null; // Indica que se debe pedir la fecha al usuario
        }
        
        fechaInput = fechaInput.trim();
        
        try {
            // Intento 1: formato completo YYYY-MM-DD HH:MM:SS
            return Timestamp.valueOf(fechaInput);
        } catch (IllegalArgumentException e1) {
            try {
                // Intento 2: solo fecha YYYY-MM-DD → agrego hora por defecto
                return Timestamp.valueOf(fechaInput + " 00:00:00");
            } catch (IllegalArgumentException e2) {
                System.out.println("❌ Fecha inválida. Formato: YYYY-MM-DD HH:MM:SS");
                return null;
            }
        }
    }
    
    public static int registrarUsuarioBase(String nombre, String apellido, 
                                          String userID, String psw, String rol) 
                                          throws SQLException {
        String sql = "INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo) " +
                    "VALUES (?, ?, ?, ?, ?, TRUE)";
        
        try (Connection c = Conexion.conectar(); 
             PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(psw, 
                           org.mindrot.jbcrypt.BCrypt.gensalt());
            
            p.setString(1, nombre);
            p.setString(2, apellido);
            p.setString(3, userID);
            p.setString(4, hashed);
            p.setString(5, rol);
            p.executeUpdate();
            
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Error al generar ID de usuario");
    }
    
    public static boolean existeRegistro(String tabla, String columna, Object valor) 
                                        throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + tabla + " WHERE " + columna + " = ?";
        try (Connection c = Conexion.conectar(); 
             PreparedStatement p = c.prepareStatement(sql)) {
            
            if (valor instanceof Integer) {
                p.setInt(1, (Integer) valor);
            } else if (valor instanceof String) {
                p.setString(1, (String) valor);
            }
            
            try (ResultSet rs = p.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public static boolean validarDNI(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            return false;
        }
        // DNI de 8 dígitos (estándar peruano)
        return dni.matches("\\d{8}");
    }
}
