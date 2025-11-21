import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administrador extends Usuario {
    private int idAdministrador;

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    public Administrador(int idAdministrador, int idUsuario, String nombre, String apellido, String userID, String psw, String rol, boolean activo) {
        super(idUsuario, nombre, apellido, userID, psw, rol, activo);
        this.idAdministrador = idAdministrador;
    }

    public int getIdAdministrador() { return idAdministrador; }

    protected static Administrador obtenerAdministrador(int idUsuario) throws SQLException {
        String sql = "SELECT a.id_administrador, u.* " +
                "FROM administradores a " +
                "JOIN usuarios u ON a.id_usuario = u.id_usuario " +
                "WHERE a.id_usuario = ?";

        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idUsuario);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Administrador(
                            rs.getInt("id_administrador"),
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("userID"),
                            rs.getString("psw"),
                            rs.getString("rol"),
                            rs.getBoolean("activo")
                    );
                }
            }
        }
        return null;
    }

    public static void mostrarMenuAdministrador(Administrador adminLogueado) {
        boolean running = true;
        while (running) {
            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("Bienvenido, " + adminLogueado.getNombre());
            System.out.println("1. Gestionar Pacientes");
            System.out.println("2. Gestionar Médicos");
            System.out.println("0. Cerrar sesión");
            System.out.print("-> Ingrese una opción: ");

            int opc = Menu.leerOpcion("", 0, 2);

            switch (opc) {
                case 1 -> Paciente.gestionarPacientes();
                case 2 -> Medico.gestionarMedicos();
                case 0 -> {
                    System.out.println("Cerrando sesión de administrador. ¡Hasta pronto!");
                    running = false;
                }
            }
        }
    }
}
