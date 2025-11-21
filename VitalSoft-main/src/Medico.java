import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Medico extends Usuario {
    private int idMedico;
    private String especialidad;
    private String dni;
    private double precioConsulta;

    private static final Scanner sc = ScannerUtil.getScanner();

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    public Medico(int idMedico, int idUsuario, String nombre, String apellido, String userID, String psw, String rol, boolean activo,
                  String especialidad, String dni, double precioConsulta) {
        super(idUsuario, nombre, apellido, userID, psw, rol, activo);
        this.idMedico = idMedico;
        this.especialidad = especialidad;
        this.dni = dni;
        this.precioConsulta = precioConsulta;
    }

    // ✅ Getters
    public int getIdMedico() { return idMedico; }
    public String getEspecialidad() { return especialidad; }
    public String getDni() { return dni; }
    public double getPrecioConsulta() { return precioConsulta; }

    // ✅ Setters
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public void setDni(String dni) { this.dni = dni; }
    public void setPrecioConsulta(double precioConsulta) { this.precioConsulta = precioConsulta; }

    protected static void gestionarMedicos() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- GESTIÓN DE MÉDICOS ---");
            System.out.println("1. Registrar médico");
            System.out.println("2. Ver médicos");
            System.out.println("3. Eliminar médico");
            System.out.println("0. Salir");
            System.out.print("-> Ingrese una opción: ");

            int opc = Menu.leerOpcion("", 0, 3);
            switch (opc) {
                case 1 -> menuRegistrarMedico();
                case 2 -> verMedicos();
                case 3 -> menuEliminarMedico();
                case 0 -> running = false;
            }
        }
    }

    public static void menuRegistrarMedico(){
        System.out.println("\n--- REGISTRAR NUEVO MÉDICO ---");
        System.out.print("Ingrese Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Ingrese DNI: ");
        String dni = sc.nextLine();
        System.out.print("Ingrese Especialidad: ");
        String especialidad = sc.nextLine();
        System.out.print("Ingrese UsuarioID: ");
        String userID = sc.nextLine();
        System.out.print("Ingrese Contraseña inicial: ");
        String psw = sc.nextLine();
        System.out.print("Ingrese Precio de Consulta: ");
        double precioConsulta = sc.nextDouble();
        sc.nextLine(); // limpiar buffer

        // Llamamos al método de GestionClinica
        registrarMedico(nombre, apellido, userID, psw, especialidad, dni, precioConsulta);
    }

    public static void registrarMedico(String nombre, String apellido, String userID, 
                                    String psw, String especialidad, String dni, 
                                    double precioConsulta) {
        try {
            int idUsuario = Utilidades.registrarUsuarioBase(nombre, apellido, 
                                                        userID, psw, "Medico");
            
            // Insertar en tabla medicos
            String sqlMedico = "INSERT INTO medicos (id_usuario, especialidad, dni, precio_consulta) " +
                            "VALUES (?, ?, ?, ?)";
            try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sqlMedico)) {
                p.setInt(1, idUsuario);
                p.setString(2, especialidad);
                p.setString(3, dni);
                p.setDouble(4, precioConsulta);
                p.executeUpdate();
                System.out.println("✅ Médico registrado: " + nombre + " (" + userID + ")");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al registrar médico: " + e.getMessage());
        }
    }

    private static void menuEliminarMedico() {
        System.out.print("Ingrese la ID del médico que desea eliminar: ");
        int idBuscado = sc.nextInt();
        sc.nextLine();
        eliminarMedico(idBuscado);
    }

    public static void eliminarMedico(int idMedico) {
        String sqlFind = "SELECT id_usuario FROM medicos WHERE id_medico = ?";
        String sqlDeleteUsuario = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection c = conn();
             PreparedStatement pf = c.prepareStatement(sqlFind)) {
            pf.setInt(1, idMedico);
            try (ResultSet rs = pf.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("⚠️ No se encontró médico con id_medico = " + idMedico);
                    return;
                }
                int idUsuario = rs.getInt("id_usuario");

                try (PreparedStatement pd = c.prepareStatement(sqlDeleteUsuario)) {
                    pd.setInt(1, idUsuario);
                    int filas = pd.executeUpdate();
                    if (filas > 0) {
                        System.out.println("✅ Médico (y usuario) eliminado correctamente (id_usuario=" + idUsuario + ")");
                    } else {
                        System.out.println("⚠️ No se pudo eliminar el usuario asociado.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al eliminar médico: " + e.getMessage());
        }
    }

    protected static Medico obtenerMedico(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM medicos m JOIN usuarios u ON m.id_usuario = u.id_usuario WHERE m.id_usuario = ?";
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idUsuario);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Medico(
                            rs.getInt("id_medico"),
                            idUsuario,
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("userID"),
                            rs.getString("psw"),
                            "MEDICO",
                            true,
                            rs.getString("especialidad"),
                            rs.getString("DNI"),
                            rs.getDouble("precio_consulta")
                    );
                }
            }
        }
        return null;
    }

    public static void verMedicos() {
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.userID, m.id_medico, m.especialidad, m.DNI, m.precio_consulta " +
                "FROM usuarios u JOIN medicos m ON u.id_usuario = m.id_usuario WHERE u.rol = 'medico'";
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
            System.out.println("--- Lista de Médicos ---");
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("id_medico=%d | id_usuario=%d | %s %s | userID=%s | DNI=%s | esp=%s | precio=%.2f%n",
                        rs.getInt("id_medico"),
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("userID"),
                        rs.getString("DNI"),
                        rs.getString("especialidad"),
                        rs.getDouble("precio_consulta"));
            }
            if (!any) System.out.println("No hay médicos registrados.");
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar médicos: " + e.getMessage());
        }
    }

    public static void verAgendaMedico(Medico medicoLogueado) {
        int idMedico = Utilidades.obtenerIdMedicoSeguro(medicoLogueado);
        if (idMedico == -1) return;

        String sql = "SELECT c.id_cita, c.fecha, c.motivo, " +
                    "u.nombre AS paciente_nombre, u.apellido AS paciente_apellido, " +
                    "p.DNI AS paciente_dni " +
                    "FROM citas c " +
                    "JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                    "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                    "WHERE c.id_medico = ? ORDER BY c.fecha";
        
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idMedico);
            try (ResultSet rs = p.executeQuery()) {
                boolean any = false;
                System.out.println("--- Agenda de Citas ---");
                while (rs.next()) {
                    any = true;
                    System.out.printf("ID cita=%d | Fecha=%s | Paciente=%s %s (DNI=%s) | Motivo=%s%n",
                            rs.getInt("id_cita"),
                            rs.getString("fecha"),
                            rs.getString("paciente_nombre"),
                            rs.getString("paciente_apellido"),
                            rs.getString("paciente_dni"),
                            rs.getString("motivo"));
                }
                if (!any) System.out.println("No tienes citas programadas.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al ver agenda: " + e.getMessage());
        }
    }

    protected static int getIdMedicoByIdUsuario(int idUsuario) throws SQLException {
        return Utilidades.obtenerIdMedicoPorIdUsuario(idUsuario);
    }

    public static void mostrarMenuMedico(Medico medicoLogueado) {
        boolean running = true;
        while (running) {
            System.out.println("\n===== MENU MÉDICO =====");
            System.out.println("Bienvenido, " + medicoLogueado.getNombre());
            System.out.println("1. Ver citas programadas");
            System.out.println("2. Registrar diagnóstico");
            System.out.println("3. Ver historial médico");
            System.out.println("0. Cerrar sesión");
            System.out.print("-> Ingrese una opción: ");

            int opc = Menu.leerOpcion("", 0, 3);
            switch (opc) {
                case 1 -> verAgendaMedico(medicoLogueado);
                case 2 -> Diagnostico.menuAgregarDiagnostico(medicoLogueado);
                case 3 -> Paciente.menuVerHistorialPaciente(medicoLogueado);
                case 0 -> running = false;
            }
        }
    }
}
