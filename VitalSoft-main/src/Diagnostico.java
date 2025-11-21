import java.sql.*;
import java.util.Scanner;

public class Diagnostico {

    private static final Scanner sc = ScannerUtil.getScanner();

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    protected static void consultarDiagnostico(Paciente pacienteLogueado) {
        int idPaciente = Utilidades.obtenerIdPacienteSeguro(pacienteLogueado);
        if (idPaciente == -1) return;

        String sql = "SELECT id_cita, fecha, diagnostico FROM citas " +
                    "WHERE id_paciente = ? AND diagnostico IS NOT NULL AND diagnostico <> ''";
        
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idPaciente);
            try (ResultSet rs = p.executeQuery()) {
                boolean any = false;
                while (rs.next()) {
                    any = true;
                    System.out.println("-------------------------------------");
                    System.out.println("ID Cita: " + rs.getInt("id_cita"));
                    System.out.println("Fecha: " + rs.getString("fecha"));
                    System.out.println("Diagnóstico: " + rs.getString("diagnostico"));
                    System.out.println("-------------------------------------");
                }
                if (!any) System.out.println("No tienes diagnósticos disponibles.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al consultar diagnósticos: " + e.getMessage());
        }
    }

    protected static void menuAgregarDiagnostico(Medico medicoLogueado) {
        System.out.println("\n--- AÑADIR DIAGNÓSTICO ---");
        System.out.print("Ingrese el ID de la cita: ");
        int idCita = sc.nextInt();
        sc.nextLine();
        System.out.print("Ingrese el diagnóstico: ");
        String diagnostico = sc.nextLine();
        agregarDiagnostico(idCita, diagnostico, medicoLogueado);
    }

    public static void agregarDiagnostico(int idCita, String diagnostico, 
                                         Medico medicoLogueado) {
        String sqlCheck = "SELECT id_medico FROM citas WHERE id_cita = ?";
        String sqlUpdate = "UPDATE citas SET diagnostico = ? WHERE id_cita = ?";

        try (Connection c = conn(); PreparedStatement pc = c.prepareStatement(sqlCheck)) {
            pc.setInt(1, idCita);
            try (ResultSet rs = pc.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ Error: Cita no encontrada con ID " + idCita);
                    return;
                }
                int idMedicoEnCita = rs.getInt("id_medico");
                
                // ✅ OPTIMIZADO: Una línea en lugar de try-catch anidados
                int idMedicoObj = Utilidades.obtenerIdMedicoSeguro(medicoLogueado);
                if (idMedicoObj == -1) return;
                
                if (idMedicoEnCita != idMedicoObj) {
                    System.out.println("❌ Error: No puedes añadir diagnóstico a esta cita.");
                    return;
                }

                try (PreparedStatement pu = c.prepareStatement(sqlUpdate)) {
                    pu.setString(1, diagnostico);
                    pu.setInt(2, idCita);
                    pu.executeUpdate();
                    System.out.println("✅ Diagnóstico añadido a la cita ID " + idCita);
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al añadir diagnóstico: " + e.getMessage());
        }
    }
}