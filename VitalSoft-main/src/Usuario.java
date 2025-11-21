import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Statement;

public class Usuario {
    protected int idUsuario;
    protected String nombre;
    protected String apellido;
    protected String userID;
    protected String psw;
    protected String rol;
    protected boolean activo;

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    public Usuario(int idUsuario, String nombre, String apellido, String userID, String psw, String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.userID = userID;
        this.psw = psw;
        this.rol = rol;
        this.activo = activo;
    }

    // ✅ Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getUserID() { return userID; }
    public String getPsw() { return psw; }
    public String getRol() { return rol; }
    public boolean isActivo() { return activo; }

    // ✅ Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setActivo(boolean activo) { this.activo = activo; }

    //    protected static void registrarUsuario(String nombre, String apellido, String userID, String psw, String rol) {
    //        String sql = "INSERT INTO usuarios (nombre, apellido, userID, psw, rol, activo) VALUES (?, ?, ?, ?, ?, TRUE)";
    //        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    //            String hashed = BCrypt.hashpw(psw, BCrypt.gensalt());
    //            p.setString(1, nombre);
    //            p.setString(2, apellido);
    //            p.setString(3, userID);
    //            p.setString(4, hashed);
    //            p.setString(5, rol);
    //            p.executeUpdate();
    //            System.out.println("✅ Usuario registrado: " + userID + " (" + rol + ")");
    //        } catch (SQLException e) {
    //            System.out.println("⚠️ Error al registrar usuario: " + e.getMessage());
    //        }
    //    }


    protected static Usuario autenticarUsuario(String userID, String psw) {
            String sql = "SELECT * FROM usuarios WHERE userID = ? AND activo = TRUE";

            try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {

                p.setString(1, userID);

                try (ResultSet rs = p.executeQuery()) {
                    if (rs.next()) {

                        String hash = rs.getString("psw");

                        if (!BCrypt.checkpw(psw, hash)) {
                            return null; // contraseña incorrecta
                        }

                        String rol = rs.getString("rol").trim().toUpperCase();
                        int idUsuario = rs.getInt("id_usuario");

                        switch (rol) {

                            case "ADMINISTRADOR":
                                return Administrador.obtenerAdministrador(idUsuario);

                            case "MEDICO":
                                return Medico.obtenerMedico(idUsuario);

                            case "PACIENTE":
                                return Paciente.obtenerPaciente(idUsuario);

                            default:
                                System.out.println("⚠️ Rol desconocido en la BD: " + rol);
                                return null;
                        }
                    }
                }

            } catch (SQLException e) {
                System.out.println("⚠️ Error en autenticación: " + e.getMessage());
            }

            return null;
        }
}   
