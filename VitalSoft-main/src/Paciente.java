import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Scanner;

public class Paciente extends Usuario {
    private int idPaciente;
    private int edad;
    private String dni;
    private String historiaClinica;

    private static final Scanner sc = ScannerUtil.getScanner();

    private static Connection conn() throws SQLException {
        Connection c = Conexion.conectar();
        if (c == null) {
            throw new SQLException("❌ No se pudo establecer conexión con la base de datos");
        }
        return c;
    }

    public Paciente(int idPaciente, int idUsuario, String nombre, String apellido, String userID, String psw, String rol, boolean activo,
                    int edad, String dni, String historiaClinica) {
        super(idUsuario, nombre, apellido, userID, psw, rol, activo);
        this.idPaciente = idPaciente;
        this.edad = edad;
        this.dni = dni;
        this.historiaClinica = historiaClinica;
    }

    //  Getters
    public int getIdPaciente() { return idPaciente; }
    public int getEdad() { return edad; }
    public String getDni() { return dni; }
    public String getHistoriaClinica() { return historiaClinica; }

    //  Setters
    public void setEdad(int edad) { this.edad = edad; }
    public void setDni(String dni) { this.dni = dni; }
    public void setHistoriaClinica(String historiaClinica) { this.historiaClinica = historiaClinica; }

    protected static void gestionarPacientes() {
        boolean running = true;
        while (running) {
            System.out.println("\n--- GESTIÓN DE PACIENTES ---");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Ver lista de pacientes");
            System.out.println("0. Salir");
            System.out.print("-> Ingrese una opción: ");

            int opc = Menu.leerOpcion("", 0, 2);
            switch (opc) {
                case 1 -> menuRegistrarPaciente();
                case 2 -> verPacientes();
                case 0 -> running = false;
            }
        }
    }

    private static void menuRegistrarPaciente() {
        System.out.println("\n--- REGISTRAR NUEVO PACIENTE ---");
        System.out.print("Ingrese Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese Apellido: ");
        String apellido = sc.nextLine();
        System.out.print("Ingrese DNI: ");
        String dni = sc.nextLine();
        System.out.print("Ingrese Edad: ");
        int edad = sc.nextInt();
        sc.nextLine(); // limpiar buffer
        System.out.print("Ingrese UsuarioID: ");
        String userID = sc.nextLine();
        System.out.print("Ingrese Contraseña inicial: ");
        String psw = sc.nextLine();

        // Llamamos al método de GestionClinica
        registrarPaciente(nombre, apellido, userID, psw, edad, dni);
    }

    public static void registrarPaciente(String nombre, String apellido, String userID, 
                                    String psw, int edad, String dni) {
    try {
        int idUsuario = Utilidades.registrarUsuarioBase(nombre, apellido, 
                                                       userID, psw, "Paciente");
        
        // Insertar en tabla pacientes
        String sqlPaciente = "INSERT INTO pacientes (id_usuario, edad, dni, historiaClinica) " +
                            "VALUES (?, ?, ?, '')";
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sqlPaciente)) {
            p.setInt(1, idUsuario);
            p.setInt(2, edad);
            p.setString(3, dni);
            p.executeUpdate();
            System.out.println("✅ Paciente registrado: " + nombre + " (" + userID + ")");
        }
    } catch (SQLException e) {
        System.out.println("⚠️ Error al registrar paciente: " + e.getMessage());
    }
}

    public static void verPacientes() {
        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, u.userID, p.edad, p.DNI " +
                "FROM usuarios u JOIN pacientes p ON u.id_usuario = p.id_usuario WHERE u.rol = 'paciente'";
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
            System.out.println("--- Lista de Pacientes ---");
            boolean any = false;
            while (rs.next()) {
                any = true;
                System.out.printf("id_usuario=%d | %s %s | userID=%s | DNI=%s | edad=%d%n",
                        rs.getInt("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("userID"),
                        rs.getString("DNI"),
                        rs.getInt("edad"));
            }
            if (!any) System.out.println("No hay pacientes registrados.");
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar pacientes: " + e.getMessage());
        }
    }

    protected static Paciente obtenerPaciente(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM pacientes p " +
                "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                "WHERE p.id_usuario = ?";

        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {

            p.setInt(1, idUsuario);

            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getInt("id_paciente"),    // 1. idPaciente
                            idUsuario,                   // 2. idUsuario
                            rs.getString("nombre"),      // 3. nombre
                            rs.getString("apellido"),    // 4. apellido
                            rs.getString("userID"),      // 5. userID
                            rs.getString("psw"),         // 6. psw
                            "PACIENTE",                  // 7. rol
                            rs.getBoolean("activo"),     // 8. activo
                            rs.getInt("edad"),           // 9. edad
                            rs.getString("dni"),         // 10. dni
                            rs.getString("historiaClinica") // 11. historiaClinica
                    );
                }
            }
        }
        return null;
    }


    protected static int getIdPacienteByIdUsuario(int idUsuario) throws SQLException {
        return Utilidades.obtenerIdPacientePorIdUsuario(idUsuario);
    }

    protected static void mostrarMenuPaciente(Paciente pacienteLogueado) {
        boolean running = true;
        while (running) {
            System.out.println("\n===== MENU PACIENTE =====");
            System.out.println("Bienvenido, " + pacienteLogueado.getNombre());
            System.out.println("1. Crear cita");
            System.out.println("2. Ver historial de citas");
            System.out.println("3. Cancelar cita");
            System.out.println("4. Consultar diagnóstico");
            System.out.println("5. Consultar factura");
            System.out.println("0. Cerrar sesión");
            System.out.print("-> Ingrese una opción: ");

            int opc = Menu.leerOpcion("", 0, 5);

            switch (opc) {
                case 1 -> Cita.menuCrearCita(pacienteLogueado);
                case 2 -> Cita.verHistorialCitasPaciente(pacienteLogueado);
                case 3 -> Cita.menuCancelarCita(pacienteLogueado);
                case 4 -> Diagnostico.consultarDiagnostico(pacienteLogueado);
                case 5 -> Factura.verFacturasDePaciente(pacienteLogueado);
                case 0 -> running = false;
            }
        }
    }

    protected static void menuVerHistorialPaciente(Medico medicoLogueado) {
        System.out.println("\n--- VER HISTORIAL DE UN PACIENTE ---");
        verListaDePacientesAtendidos(medicoLogueado);
        System.out.print("\nIngrese el DNI del paciente cuyo historial desea ver: ");
        String dniPaciente = sc.nextLine();
        verHistorialMedicoDePaciente(dniPaciente, medicoLogueado);
    }

    public static void verListaDePacientesAtendidos(Medico medicoLogueado) {
        int idMedico = Utilidades.obtenerIdMedicoSeguro(medicoLogueado);
        if (idMedico == -1) return;

        String sql = "SELECT DISTINCT u.nombre, u.apellido, p.DNI " +
                    "FROM citas c " +
                    "JOIN pacientes p ON c.id_paciente = p.id_paciente " +
                    "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                    "WHERE c.id_medico = ?";
        
        try (Connection c = conn(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idMedico);
            try (ResultSet rs = p.executeQuery()) {
                HashSet<String> set = new HashSet<>();
                while (rs.next()) {
                    String s = rs.getString("nombre") + " " + 
                            rs.getString("apellido") + " (DNI: " + 
                            rs.getString("DNI") + ")";
                    set.add(s);
                }
                if (set.isEmpty()) {
                    System.out.println("No ha atendido a ningún paciente aún.");
                } else {
                    System.out.println("--- Pacientes atendidos ---");
                    for (String x : set) System.out.println("- " + x);
                }
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al listar pacientes atendidos: " + e.getMessage());
        }
    }
    

    public static void verHistorialMedicoDePaciente(String dniPaciente, Medico medicoLogueado) {
        String sqlFindPaciente = "SELECT id_paciente FROM pacientes WHERE DNI = ?";
        
        try (Connection c = conn(); 
            PreparedStatement pf = c.prepareStatement(sqlFindPaciente)) {
            
            pf.setString(1, dniPaciente);
            try (ResultSet rsP = pf.executeQuery()) {
                if (!rsP.next()) {
                    System.out.println("❌ Error: Paciente no encontrado con DNI " + dniPaciente);
                    return;
                }
                int idPaciente = rsP.getInt("id_paciente");

                int idMedico = Utilidades.obtenerIdMedicoSeguro(medicoLogueado);
                if (idMedico == -1) return;

                // Verificar que el médico ha atendido al paciente
                String sqlCheck = "SELECT COUNT(*) FROM citas WHERE id_medico = ? AND id_paciente = ?";
                try (PreparedStatement pc = c.prepareStatement(sqlCheck)) {
                    pc.setInt(1, idMedico);
                    pc.setInt(2, idPaciente);
                    try (ResultSet rsCheck = pc.executeQuery()) {
                        rsCheck.next();
                        if (rsCheck.getInt(1) == 0) {
                            System.out.println("❌ Error: No tiene permiso para ver este historial.");
                            return;
                        }
                    }
                }

                // Mostrar historial
                mostrarHistorialPaciente(idPaciente, dniPaciente, c);
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error al obtener historial médico: " + e.getMessage());
        }
    }

    private static void mostrarHistorialPaciente(int idPaciente, String dniPaciente, 
                                                Connection c) throws SQLException {
        String sqlHist = "SELECT c.fecha, c.motivo, c.diagnostico " +
                        "FROM citas c WHERE c.id_paciente = ? ORDER BY c.fecha DESC";
        
        try (PreparedStatement ph = c.prepareStatement(sqlHist)) {
            ph.setInt(1, idPaciente);
            try (ResultSet rsH = ph.executeQuery()) {
                boolean any = false;
                System.out.println("\n--- HISTORIAL MÉDICO (DNI: " + dniPaciente + ") ---");
                while (rsH.next()) {
                    any = true;
                    System.out.println("Fecha: " + rsH.getString("fecha"));
                    System.out.println("Motivo: " + rsH.getString("motivo"));
                    String diag = rsH.getString("diagnostico");
                    System.out.println("Diagnóstico: " + (diag == null ? "Pendiente" : diag));
                    System.out.println("------------------------------------------------");
                }
                if (!any) System.out.println("Este paciente no tiene citas registradas.");
            }
        }
    }
}
