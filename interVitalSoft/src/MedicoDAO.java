import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data Access Object para la entidad Medico
 * Maneja todas las operaciones de base de datos para médicos
 */
public class MedicoDAO {
    private Connection connection;

    public MedicoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Busca un médico por DNI y contraseña (login)
     */
    public Medico login(String dni, String password) {
        String sql = "SELECT * FROM medicos WHERE dni = ? AND password = ? AND activo = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearMedico(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al buscar médico: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Inserta un nuevo médico
     */
    public boolean insertar(Medico medico) {
        String sql = "INSERT INTO medicos (nombre, apellidos, especialidad, dni, precio_consulta, " +
                "dias_atencion, horario_hora, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellidos());
            stmt.setString(3, medico.getEspecialidad());
            stmt.setString(4, medico.getDni());
            stmt.setDouble(5, medico.getPrecioConsulta());
            stmt.setString(6, medico.getDiasAtencion());
            stmt.setString(7, medico.getHorarioHora());
            stmt.setString(8, medico.getPsw());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println("✅ Médico registrado con ID: " + rs.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar médico: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Actualiza los datos de un médico
     */
    public boolean actualizar(Medico medico) {
        String sql = "UPDATE medicos SET nombre = ?, apellidos = ?, especialidad = ?, dni = ?, " +
                "precio_consulta = ?, dias_atencion = ?, horario_hora = ?, password = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellidos());
            stmt.setString(3, medico.getEspecialidad());
            stmt.setString(4, medico.getDni());
            stmt.setDouble(5, medico.getPrecioConsulta());
            stmt.setString(6, medico.getDiasAtencion());
            stmt.setString(7, medico.getHorarioHora());
            stmt.setString(8, medico.getPsw());
            stmt.setInt(9, medico.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Elimina (desactiva) un médico
     */
    public boolean eliminar(int id) {
        String sql = "UPDATE medicos SET activo = FALSE WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al eliminar médico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene todos los médicos activos
     */
    public List<Medico> obtenerTodos() {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE activo = TRUE ORDER BY apellidos, nombre";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearMedico(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener médicos: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Busca un médico por ID
     */
    public Medico obtenerPorId(int id) {
        String sql = "SELECT * FROM medicos WHERE id = ? AND activo = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearMedico(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener médico: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene todas las especialidades únicas
     */
    public Set<String> obtenerEspecialidades() {
        Set<String> especialidades = new HashSet<>();
        String sql = "SELECT DISTINCT especialidad FROM medicos WHERE activo = TRUE ORDER BY especialidad";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                especialidades.add(rs.getString("especialidad"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener especialidades: " + e.getMessage());
            e.printStackTrace();
        }

        return especialidades;
    }

    /**
     * Obtiene médicos por especialidad
     */
    public List<Medico> obtenerPorEspecialidad(String especialidad) {
        List<Medico> lista = new ArrayList<>();
        String sql = "SELECT * FROM medicos WHERE especialidad = ? AND activo = TRUE ORDER BY apellidos, nombre";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, especialidad);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearMedico(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener médicos por especialidad: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Mapea un ResultSet a un objeto Medico
     */
    private Medico mapearMedico(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String apellidos = rs.getString("apellidos");
        String especialidad = rs.getString("especialidad");
        String dni = rs.getString("dni");
        double precioConsulta = rs.getDouble("precio_consulta");
        String diasAtencion = rs.getString("dias_atencion");
        String horarioHora = rs.getString("horario_hora");
        String password = rs.getString("password");

        Medico medico = new Medico(nombre, apellidos, especialidad, dni, precioConsulta, diasAtencion, horarioHora,
                password);
        medico.setId(id); // IMPORTANTE: Establecer el ID de la base de datos

        return medico;
    }
}
