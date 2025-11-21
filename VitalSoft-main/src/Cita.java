import java.sql.*;
import java.util.Scanner;

public class Cita {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private String fecha;
    private String motivo;
    private String diagnostico;

    private static final Scanner sc = ScannerUtil.getScanner();

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    public Cita(String motivo, Paciente paciente, Medico medico, String fecha, int id) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    // Getters y Setters
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public void setMotivo(String motivo){ this.motivo = motivo; }
    public String getMotivo() { return motivo; }
    public void setId(int id) { this.id = id; }
    public int getId() { return id; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Paciente getPaciente() { return paciente; }
    public void setMedico(Medico medico) { this.medico = medico; }
    public Medico getMedico() { return medico; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha){ this.fecha = fecha; }

    @Override
    public String toString() {
        String pacienteNombre = (paciente != null) ? paciente.getNombre() : "No asignado";
        String medicoNombre = (medico != null) ? medico.getNombre() : "No asignado";
        String diagnosticoStr = (diagnostico == null || diagnostico.isEmpty())
                ? "Pendiente" : diagnostico;
        return "Cita [ID=" + id + ", Paciente=" + pacienteNombre +
                ", Medico=" + medicoNombre + ", Fecha=" + fecha +
                ", Diagnóstico=" + diagnosticoStr + "]";
    }

    
    protected static void verHistorialCitasPaciente(Paciente pacienteLogueado) {
        int idPaciente = Utilidades.obtenerIdPacienteSeguro(pacienteLogueado);
        if (idPaciente == -1) return;

        String sql = "SELECT c.*, m.DNI AS dni_medico, m.especialidad, " +
                    "u.nombre AS medico_nombre, u.apellido AS medico_apellido " +
                    "FROM citas c " +
                    "JOIN medicos m ON c.id_medico = m.id_medico " +
                    "JOIN usuarios u ON m.id_usuario = u.id_usuario " +
                    "WHERE c.id_paciente = ? ORDER BY c.fecha DESC";
        
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idPaciente);
            try (ResultSet rs = p.executeQuery()) {
                boolean any = false;
                System.out.println("--- Historial de Citas ---");
                while (rs.next()) {
                    any = true;
                    imprimirDetalleCita(rs);
                }
                if (!any) System.out.println("No tienes citas registradas.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al obtener historial: " + e.getMessage());
        }
    }

   
    private static void imprimirDetalleCita(ResultSet rs) throws SQLException {
        System.out.println(rs.getString("fecha") + " | ID cita: " + 
                         rs.getInt("id_cita"));
        System.out.println("Médico: " + rs.getString("medico_nombre") + " " + 
                         rs.getString("medico_apellido") + " (DNI: " + 
                         rs.getString("dni_medico") + ", " + 
                         rs.getString("especialidad") + ")");
        System.out.println("Motivo: " + rs.getString("motivo"));
        String diag = rs.getString("diagnostico");
        System.out.println("Diagnóstico: " + (diag == null ? "Pendiente" : diag));
        System.out.println("--------------------------------------------------");
    }

    protected static void menuCrearCita(Paciente pacienteLogueado) {
        System.out.println("\n--- CREAR CITA ---");
        System.out.print("Ingrese motivo de la cita: ");
        String motivo = sc.nextLine();
        System.out.print("Ingrese DNI del Médico: ");
        String dniMedico = sc.nextLine();
        String fecha = null;
        crearCita(motivo, pacienteLogueado.getDni(), dniMedico, fecha);
    }

    public static void crearCita(String motivo, String dniPacienteLogueado, 
                                String dniMedico, String fechaStr) {
        String sqlPaciente = "SELECT id_paciente FROM pacientes WHERE DNI = ?";
        String sqlMedico = "SELECT id_medico FROM medicos WHERE DNI = ?";
        String sqlInsert = "INSERT INTO citas (motivo, id_paciente, id_medico, fecha) " +
                          "VALUES (?, ?, ?, ?)";

        try (Connection c = conn();
             PreparedStatement sp = c.prepareStatement(sqlPaciente);
             PreparedStatement sm = c.prepareStatement(sqlMedico);
             PreparedStatement si = c.prepareStatement(sqlInsert)) {

            // Buscar ID del paciente
            sp.setString(1, dniPacienteLogueado);
            try (ResultSet rsP = sp.executeQuery()) {
                if (!rsP.next()) {
                    System.out.println("❌ Error: Paciente no encontrado.");
                    return;
                }
                int idPaciente = rsP.getInt("id_paciente");

                // Buscar ID del médico
                sm.setString(1, dniMedico);
                try (ResultSet rsM = sm.executeQuery()) {
                    if (!rsM.next()) {
                        System.out.println("❌ Error: Médico no encontrado.");
                        return;
                    }
                    int idMedico = rsM.getInt("id_medico");

                    Timestamp fecha = Utilidades.convertirATimestamp(fechaStr);
                    
                    if (fecha == null) {
                        System.out.print("Ingrese fecha (YYYY-MM-DD HH:MM:SS o YYYY-MM-DD): ");
                        String fechaInput = sc.nextLine();
                        fecha = Utilidades.convertirATimestamp(fechaInput);
                        if (fecha == null) return;
                    }

                    // Insertar la cita
                    si.setString(1, motivo);
                    si.setInt(2, idPaciente);
                    si.setInt(3, idMedico);
                    si.setTimestamp(4, fecha);
                    si.executeUpdate();
                    
                    System.out.println("✅ Cita creada con éxito para id_paciente=" +
                                     idPaciente + " con id_medico=" + idMedico);
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al crear cita: " + e.getMessage());
        }
    }

    protected static void menuCancelarCita(Paciente pacienteLogueado) {
        System.out.println("\n--- CANCELAR CITA ---");
        System.out.print("Ingrese el ID de la cita que desea cancelar: ");
        int idCita = sc.nextInt();
        sc.nextLine();
        cancelarCita(idCita, pacienteLogueado);
    }

    public static void cancelarCita(int idCita, Paciente pacienteLogueado) {
        String sqlCheck = "SELECT id_paciente FROM citas WHERE id_cita = ?";
        String sqlDelete = "DELETE FROM citas WHERE id_cita = ?";

        try (Connection c = conn(); PreparedStatement pc = c.prepareStatement(sqlCheck)) {
            pc.setInt(1, idCita);
            try (ResultSet rs = pc.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ Error: Cita no encontrada con ID " + idCita);
                    return;
                }
                int idPacienteEnCita = rs.getInt("id_paciente");

                // ✅ OPTIMIZADO: Una sola línea en lugar de múltiples try-catch
                int idPacienteLogueado = Utilidades.obtenerIdPacienteSeguro(pacienteLogueado);
                if (idPacienteLogueado == -1) return;

                if (idPacienteEnCita != idPacienteLogueado) {
                    System.out.println("❌ Error: No puedes cancelar esta cita.");
                    return;
                }

                try (PreparedStatement pd = c.prepareStatement(sqlDelete)) {
                    pd.setInt(1, idCita);
                    pd.executeUpdate();
                    System.out.println("✅ Cita " + idCita + " cancelada exitosamente.");
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al cancelar cita: " + e.getMessage());
        }
    }

}