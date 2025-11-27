import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Administrador
 * Maneja todas las operaciones de base de datos para administradores
 */
public class AdministradorDAO {
    private Connection connection;

    public AdministradorDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Busca un administrador por usuario y contraseña (login)
     */
    public Administrador login(String usuario, String password) {
        String sql = "SELECT * FROM administradores WHERE usuario = ? AND password = ? AND activo = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearAdministrador(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar administrador: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene todos los administradores activos
     */
    public List<Administrador> obtenerTodos() {
        List<Administrador> lista = new ArrayList<>();
        String sql = "SELECT * FROM administradores WHERE activo = TRUE";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearAdministrador(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener administradores: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Inserta un nuevo administrador
     */
    public boolean insertar(Administrador admin) {
        String sql = "INSERT INTO administradores (nombre, dni, usuario, password) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getNombre());
            stmt.setString(2, admin.getDni());
            stmt.setString(3, admin.getUser());
            stmt.setString(4, admin.getPsw());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar administrador: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Administrador
     */
    private Administrador mapearAdministrador(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre");
        String dni = rs.getString("dni");
        String password = rs.getString("password");
        String usuario = rs.getString("usuario");

        return new Administrador(nombre, dni, password, usuario);
    }
}
