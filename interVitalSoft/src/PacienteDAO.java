import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Paciente
 * Maneja todas las operaciones de base de datos para pacientes
 */
public class PacienteDAO {
    private Connection connection;

    public PacienteDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Busca un paciente por DNI y contraseña (login)
     */
    public Paciente login(String dni, String password) {
        String sql = "SELECT * FROM pacientes WHERE dni = ? AND password = ? AND activo = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearPaciente(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar paciente: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserta un nuevo paciente
     */
    public boolean insertar(Paciente paciente) {
        String sql = "INSERT INTO pacientes (nombre, apellidos, dni, celular, fecha_nacimiento, alergias, password) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellidos());
            stmt.setString(3, paciente.getDni());
            stmt.setString(4, paciente.getCelular());
            stmt.setDate(5, Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setString(6, paciente.getAlergias());
            stmt.setString(7, paciente.getPsw());

            int filasAfectadas = stmt.executeUpdate();

            // Obtener el ID generado
            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("✅ Paciente registrado con ID: " + rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar paciente: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene todos los pacientes activos
     */
    public List<Paciente> obtenerTodos() {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM pacientes WHERE activo = TRUE ORDER BY apellidos, nombre";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPaciente(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pacientes: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Busca un paciente por ID
     */
    public Paciente obtenerPorId(int id) {
        String sql = "SELECT * FROM pacientes WHERE id = ? AND activo = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearPaciente(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener paciente: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Actualiza los datos de un paciente
     */
    public boolean actualizar(Paciente paciente) {
        String sql = "UPDATE pacientes SET nombre = ?, apellidos = ?, celular = ?, " +
                "fecha_nacimiento = ?, alergias = ?, password = ?, imc = ?, categoria_imc = ? " +
                "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellidos());
            stmt.setString(3, paciente.getCelular());
            stmt.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setString(5, paciente.getAlergias());
            stmt.setString(6, paciente.getPsw());
            stmt.setString(7, paciente.getImc());
            stmt.setString(8, paciente.getCategoriaIMC());
            stmt.setInt(9, paciente.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar paciente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Mapea un ResultSet a un objeto Paciente
     */
    private Paciente mapearPaciente(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String dni = rs.getString("dni");
        String celular = rs.getString("celular");
        LocalDate fechaNacimiento = rs.getDate("fecha_nacimiento").toLocalDate();
        String alergias = rs.getString("alergias");
        String password = rs.getString("password");

        Paciente paciente = new Paciente(nombre, apellidos, dni, celular, fechaNacimiento, alergias, password);
        paciente.setId(id); // IMPORTANTE: Establecer el ID de la base de datos

        // Cargar IMC si existe
        String imc = rs.getString("imc");
        String categoriaIMC = rs.getString("categoria_imc");
        if (imc != null) {
            paciente.setImc(imc);
        }
        if (categoriaIMC != null) {
            paciente.setCategoriaIMC(categoriaIMC);
        }

        return paciente;
    }
}
