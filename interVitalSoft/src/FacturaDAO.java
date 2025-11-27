import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad Factura
 * Maneja todas las operaciones de base de datos para facturas
 */
public class FacturaDAO {
    private Connection connection;
    private CitaDAO citaDAO;

    public FacturaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.citaDAO = new CitaDAO();
    }

    /**
     * Genera una factura usando el procedimiento almacenado
     */
    public int generarFactura(int idCita) {
        // Verificar que no exista factura ya
        if (citaYaFacturada(idCita)) {
            System.out.println("⚠️ Ya existe factura.");
            return -1;
        }

        String sql = "{CALL sp_generar_factura(?, ?)}";

        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, idCita);
            stmt.registerOutParameter(2, Types.INTEGER); // factura_id

            stmt.execute();

            int facturaId = stmt.getInt(2);
            System.out.println("✅ Factura generada con ID: " + facturaId);

            // Mostrar la factura
            Factura factura = obtenerPorId(facturaId);
            if (factura != null) {
                System.out.println(factura);
            }

            return facturaId;

        } catch (SQLException e) {
            System.err.println("❌ Error al generar factura: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Verifica si una cita ya tiene factura
     */
    public boolean citaYaFacturada(int idCita) {
        String sql = "SELECT COUNT(*) FROM facturas WHERE cita_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idCita);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar factura: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Obtiene una factura por ID
     */
    public Factura obtenerPorId(int id) {
        String sql = "SELECT * FROM vista_facturas_completas WHERE factura_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapearFacturaCompleta(rs);
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener factura: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene todas las facturas de un paciente
     */
    public List<Factura> obtenerPorPaciente(Paciente paciente) {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM vista_facturas_completas WHERE paciente_dni = ? ORDER BY fecha_emision DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paciente.getDni());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(mapearFacturaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener facturas del paciente: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Obtiene todas las facturas
     */
    public List<Factura> obtenerTodas() {
        List<Factura> lista = new ArrayList<>();
        String sql = "SELECT * FROM vista_facturas_completas ORDER BY fecha_emision DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearFacturaCompleta(rs));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener facturas: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Calcula el total recaudado de todas las facturas
     */
    public double calcularTotalRecaudado() {
        String sql = "SELECT SUM(total) as total_recaudado FROM facturas";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble("total_recaudado");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al calcular total recaudado: " + e.getMessage());
            e.printStackTrace();
        }

        return 0.0;
    }

    /**
     * Muestra las facturas de un paciente
     */
    public void verFacturasDePaciente(Paciente paciente) {
        List<Factura> facturas = obtenerPorPaciente(paciente);

        if (facturas.isEmpty()) {
            System.out.println("Sin facturas.");
        } else {
            for (Factura f : facturas) {
                System.out.println(f);
            }
        }
    }

    /**
     * Genera reporte financiero
     */
    public void reporteFinanciero() {
        double total = calcularTotalRecaudado();
        System.out.println("\n--- CAJA DEL DÍA ---");
        System.out.println("Total Recaudado: S/ " + String.format("%.2f", total));
    }

    /**
     * Mapea un ResultSet de la vista completa a un objeto Factura
     */
    private Factura mapearFacturaCompleta(ResultSet rs) throws SQLException {
        // Obtener la cita asociada
        int citaId = rs.getInt("cita_id");
        Cita cita = citaDAO.obtenerPorId(citaId);

        if (cita != null) {
            return new Factura(cita);
        }

        return null;
    }
}
