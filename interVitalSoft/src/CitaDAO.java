import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Cita
 * Maneja todas las operaciones de base de datos para citas médicas
 */
public class CitaDAO {
    private Connection connection;

    public CitaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    /**
     * Inserta una nueva cita usando el procedimiento almacenado
     */
    public int insertar(Cita cita) {
        String sql = "{CALL sp_crear_cita(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, cita.getPaciente().getId());
            stmt.setInt(2, cita.getMedico().getId());
            stmt.setDate(3, Date.valueOf(cita.getFecha()));
            stmt.setString(4, cita.getMotivo());
            stmt.registerOutParameter(5, Types.INTEGER); // cita_id
            stmt.registerOutParameter(6, Types.INTEGER); // numero_turno

            stmt.execute();

            int citaId = stmt.getInt(5);
            int numeroTurno = stmt.getInt(6);

            System.out.println("✅ CITA CREADA. Turno #" + numeroTurno + " | ID Cita: " + citaId);
            return citaId;

        } catch (SQLException e) {
            System.err.println("❌ Error al insertar cita: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Actualiza el estado de una cita
     */
    public boolean actualizarEstado(int idCita, EstadoCita nuevoEstado) {
        String sql = "UPDATE citas SET estado = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado.name());
            stmt.setInt(2, idCita);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error al actualizar estado de cita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra los signos vitales (triaje) de una cita
     */
    public boolean registrarTriaje(int idCita, double peso, double talla, double temperatura, String presionArterial) {
        String sql = "UPDATE citas SET peso = ?, talla = ?, temperatura = ?, presion_arterial = ?, " +
                "triaje_realizado = TRUE WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, peso);
            stmt.setDouble(2, talla);
            stmt.setDouble(3, temperatura);
            stmt.setString(4, presionArterial);
            stmt.setInt(5, idCita);

            boolean actualizado = stmt.executeUpdate() > 0;

            if (actualizado) {
                System.out.println("✅ Triaje registrado. El paciente está listo para el médico.");
            }

            return actualizado;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar triaje: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registra el diagnóstico y receta de una cita
     */
    public boolean registrarDiagnostico(int idCita, String diagnostico, String receta) {
        String sql = "UPDATE citas SET diagnostico = ?, receta = ?, estado = 'ATENDIDA' WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, diagnostico);
            stmt.setString(2, receta);
            stmt.setInt(3, idCita);

            boolean actualizado = stmt.executeUpdate() > 0;

            if (actualizado) {
                System.out.println("✅ Atención médica guardada.");
            }

            return actualizado;

        } catch (SQLException e) {
            System.err.println("❌ Error al registrar diagnóstico: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene una cita por ID
     */
    public Cita obtenerPorId(int id) {
        String sql = "SELECT * FROM vista_citas_completas WHERE cita_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearCitaCompleta(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener cita: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene todas las citas pendientes de triaje
     */
    public List<Cita> obtenerPendientesTriaje() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM vista_citas_completas WHERE estado = 'PENDIENTE' AND triaje_realizado = FALSE";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearCitaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener citas pendientes de triaje: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene las citas de un médico específico (pendientes)
     */
    public List<Cita> obtenerPorMedico(int idMedico) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM vista_citas_completas WHERE medico_id = ? AND estado = 'PENDIENTE' ORDER BY fecha, numero_turno";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearCitaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener citas del médico: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene el historial de citas de un paciente
     */
    public List<Cita> obtenerHistorialPaciente(int idPaciente) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM vista_citas_completas WHERE paciente_id = ? ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearCitaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener historial del paciente: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene el historial de citas de un médico
     */
    public List<Cita> obtenerHistorialMedico(int idMedico) {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT * FROM vista_citas_completas WHERE medico_id = ? ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearCitaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener historial del médico: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene citas atendidas pendientes de facturación
     */
    public List<Cita> obtenerPendientesPago() {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.* FROM vista_citas_completas c " +
                "LEFT JOIN facturas f ON c.cita_id = f.cita_id " +
                "WHERE c.estado = 'ATENDIDA' AND f.id IS NULL";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearCitaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener citas pendientes de pago: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Verifica si un médico tiene citas pendientes
     */
    public boolean tieneCitasPendientes(int idMedico) {
        String sql = "SELECT COUNT(*) FROM citas WHERE medico_id = ? AND estado = 'PENDIENTE'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar citas pendientes: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene los pacientes únicos de un médico
     */
    public List<Paciente> obtenerPacientesDeMedico(int idMedico) {
        List<Paciente> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.* FROM pacientes p " +
                "INNER JOIN citas c ON p.id = c.paciente_id " +
                "WHERE c.medico_id = ? AND p.activo = TRUE " +
                "ORDER BY p.apellidos, p.nombre";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearPacienteSimple(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener pacientes del médico: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Mapea un ResultSet de la vista completa a un objeto Cita
     */
    private Cita mapearCitaCompleta(ResultSet rs) throws SQLException {
        // Obtener IDs de la base de datos
        int citaId = rs.getInt("cita_id");
        int pacienteId = rs.getInt("paciente_id");
        int medicoId = rs.getInt("medico_id");

        // Crear paciente
        Paciente paciente = new Paciente(
                rs.getString("paciente_nombre"),
                rs.getString("paciente_apellidos"),
                rs.getString("paciente_dni"),
                rs.getString("paciente_celular"),
                rs.getDate("paciente_fecha_nacimiento").toLocalDate(),
                rs.getString("paciente_alergias"),
                "" // password no se necesita aquí
        );
        paciente.setId(pacienteId); // IMPORTANTE: Establecer ID de BD

        // Crear médico
        Medico medico = new Medico(
                rs.getString("medico_nombre"),
                rs.getString("medico_apellidos"),
                rs.getString("medico_especialidad"),
                "", // dni no se necesita aquí
                rs.getDouble("medico_precio"),
                "", // dias_atencion
                "", // horario_hora
                "" // password
        );
        medico.setId(medicoId); // IMPORTANTE: Establecer ID de BD

        // Crear cita
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        String motivo = rs.getString("motivo");
        int numeroTurno = rs.getInt("numero_turno");

        Cita cita = new Cita(paciente, medico, fecha, motivo, numeroTurno);
        cita.setId(citaId); // IMPORTANTE: Establecer ID de BD

        // Cargar estado
        String estadoStr = rs.getString("estado");
        if ("ATENDIDA".equals(estadoStr)) {
            cita.setEstado(EstadoCita.ATENDIDO);
        } else if ("CANCELADA".equals(estadoStr)) {
            cita.setEstado(EstadoCita.CANCELADO);
        }

        // Cargar diagnóstico y receta
        String diagnostico = rs.getString("diagnostico");
        String receta = rs.getString("receta");
        if (diagnostico != null) {
            cita.setDiagnostico(diagnostico);
        }
        if (receta != null) {
            cita.setReceta(receta);
        }

        // Cargar triaje
        if (rs.getBoolean("triaje_realizado")) {
            cita.registrarSignosVitales(
                    rs.getDouble("peso"),
                    rs.getDouble("talla"),
                    rs.getDouble("temperatura"),
                    rs.getString("presion_arterial"));
        }

        return cita;
    }

    /**
     * Mapea un ResultSet simple a un objeto Paciente
     */
    private Paciente mapearPacienteSimple(ResultSet rs) throws SQLException {
        return new Paciente(
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("dni"),
                rs.getString("celular"),
                rs.getDate("fecha_nacimiento").toLocalDate(),
                rs.getString("alergias"),
                rs.getString("password"));
    }
}
