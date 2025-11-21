import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Factura {
    private int idFactura;
    private Cita citaAsociada;
    private double importe;
    private LocalDate fechaEmision;
    private boolean pagada;

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    public Factura(int idFactura, Cita citaAsociada, double importe) {
        this.idFactura = idFactura;
        this.citaAsociada = citaAsociada;
        this.importe = importe;
        this.fechaEmision = LocalDate.now(); // Se asigna la fecha actual automáticamente
        this.pagada = false; // Por defecto, toda factura nueva está pendiente
    }

    
    // Getters y Setters
    public int getIdFactura() { return idFactura; }
    public Cita getCitaAsociada() { return citaAsociada; }
    public double getImporte() { return importe; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public boolean isPagada() { return pagada; }
    public void setPagada(boolean pagada) { this.pagada = pagada; }

    @Override
    public String toString() {
        return "=====================================\n" +
                "          FACTURA CLÍNICA\n" +
                "=====================================\n" +
                "ID Factura: " + idFactura + "\n" +
                "Fecha Emisión: " + fechaEmision + "\n" +
                "-------------------------------------\n" +
                "Datos de la Cita:\n" +
                citaAsociada.toString() + "\n" +
                "-------------------------------------\n" +
                "Importe Total: $" + String.format("%.2f", importe) + "\n" +
                "Estado: " + (pagada ? "PAGADA" : "PENDIENTE DE PAGO") + "\n" +
                "=====================================";
    }

    public static void generarFactura(int idCita) {
        String sqlCheck = "SELECT COUNT(*) FROM facturas WHERE id_cita = ?";
        String sqlPrecio = "SELECT m.precio_consulta FROM citas c JOIN medicos m ON c.id_medico = m.id_medico WHERE c.id_cita = ?";
        String sqlInsert = "INSERT INTO facturas (id_cita, importe) VALUES (?, ?)";

        try (Connection c = conn();
             PreparedStatement pCheck = c.prepareStatement(sqlCheck);
             PreparedStatement pPrecio = c.prepareStatement(sqlPrecio);
             PreparedStatement pInsert = c.prepareStatement(sqlInsert)) {

            pCheck.setInt(1, idCita);
            try (ResultSet rsCheck = pCheck.executeQuery()) {
                rsCheck.next();
                if (rsCheck.getInt(1) > 0) {
                    System.out.println("⚠️ Ya existe una factura para esta cita.");
                    return;
                }
            }

            pPrecio.setInt(1, idCita);
            try (ResultSet rsPrecio = pPrecio.executeQuery()) {
                if (!rsPrecio.next()) {
                    System.out.println("❌ No se encontró la cita o el precio del médico.");
                    return;
                }
                double precio = rsPrecio.getDouble("precio_consulta");

                pInsert.setInt(1, idCita);
                pInsert.setDouble(2, precio);
                pInsert.executeUpdate();

                System.out.println("✅ Factura generada con éxito. Importe: " + precio);
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al generar factura: " + e.getMessage());
        }
    }

    protected static void verFacturasDePaciente(Paciente paciente) {
        // obtenemos id_paciente desde el objeto
        int idPaciente = -1;
        try {
            idPaciente = paciente.getIdPaciente();
        } catch (Exception ex) {
            try {
                idPaciente = Paciente.getIdPacienteByIdUsuario(paciente.getIdUsuario());
            } catch (Exception e) {
                System.out.println("⚠️ Imposible obtener id_paciente del objeto Paciente.");
                return;
            }
        }

        String sql = "SELECT f.id_factura, f.importe, f.id_cita FROM facturas f WHERE f.id_cita IN (SELECT id_cita FROM citas WHERE id_paciente = ?)";
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idPaciente);
            try (ResultSet rs = p.executeQuery()) {
                boolean any = false;
                System.out.println("--- Facturas del paciente ---");
                while (rs.next()) {
                    any = true;
                    System.out.printf("id_factura=%d | id_cita=%d | importe=%.2f%n",
                            rs.getInt("id_factura"),
                            rs.getInt("id_cita"),
                            rs.getDouble("importe"));
                }
                if (!any) System.out.println("No tienes facturas registradas.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar facturas: " + e.getMessage());
        }
    }
}
