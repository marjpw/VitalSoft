import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Menu {
    private static Scanner sc = new Scanner(System.in);
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // 1. UTILIDADES
    private static String pedirCelular() {
        while (true) {
            System.out.print("Celular (9 dígitos): ");
            String cel = sc.nextLine();
            if (cel.matches("^9\\d{8}$"))
                return cel;
            System.out.println("❌ Formato incorrecto.");
        }
    }

    private static LocalDate pedirFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (dd/MM/yyyy): ");
            String input = sc.nextLine();
            try {
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("❌ Fecha inválida.");
            }
        }
    }

    // 2. MENÚ ADMINISTRADOR
    public static void mostrarMenuAdministrador(Administrador admin, GestionClinica gestion) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n===== PANEL ADMINISTRADOR =====");
            System.out.println("1. Registrar Nuevo Médico");
            System.out.println("2. Registrar Nuevo Paciente");
            System.out.println("3. Eliminar Médico");
            System.out.println("4. Ver Médicos / Pacientes");
            System.out.println("5. TRIAJE (Registrar Signos Vitales)"); // <--- NUEVO
            System.out.println("6. CAJA (Cobrar Atenciones)"); // <--- NUEVO
            System.out.println("7. Reporte Financiero");
            System.out.println("0. Cerrar Sesión");
            System.out.print("-> Opción: ");

            try {
                int op = Integer.parseInt(sc.nextLine());
                switch (op) {
                    case 1:
                        flujoRegistroMedico(gestion);
                        break;
                    case 2:
                        flujoRegistroPaciente(gestion);
                        break;
                    case 3:
                        System.out.print("ID Médico a eliminar: ");
                        gestion.eliminarMedico(Integer.parseInt(sc.nextLine()));
                        break;
                    case 4:
                        System.out.println("-- Médicos --");
                        gestion.verMedicos();
                        System.out.println("-- Pacientes --");
                        gestion.verPacientes();
                        break;

                    case 5: // FLUJO TRIAJE
                        System.out.println("\n--- MÓDULO DE TRIAJE ---");
                        List<Cita> pendientesTriaje = gestion.getCitasPendientesTriaje();
                        if (pendientesTriaje.isEmpty()) {
                            System.out.println("✅ No hay pacientes esperando triaje.");
                        } else {
                            for (Cita c : pendientesTriaje)
                                System.out.println(c);
                            System.out.print("ID Cita a procesar: ");
                            int idT = Integer.parseInt(sc.nextLine());

                            System.out.print("Peso (kg): ");
                            double peso = Double.parseDouble(sc.nextLine());
                            System.out.print("Talla (m): ");
                            double talla = Double.parseDouble(sc.nextLine());
                            System.out.print("Temperatura (°C): ");
                            double temp = Double.parseDouble(sc.nextLine());
                            System.out.print("Presión Arterial (ej 120/80): ");
                            String pres = sc.nextLine();

                            gestion.registrarTriaje(idT, peso, talla, temp, pres);
                        }
                        break;

                    case 6: // FLUJO CAJA
                        System.out.println("\n--- MÓDULO DE CAJA ---");
                        List<Cita> porCobrar = gestion.getCitasPendientesDePago();
                        if (porCobrar.isEmpty()) {
                            System.out.println("✅ No hay pacientes pendientes de cobro.");
                        } else {
                            for (Cita c : porCobrar)
                                System.out.println("ID: " + c.getId() + " | Paciente: " + c.getPaciente().getApellidos()
                                        + " | Monto: S/" + c.getMedico().getPrecioConsulta());

                            System.out.print("ID Cita a cobrar: ");
                            gestion.generarFactura(Integer.parseInt(sc.nextLine()));
                        }
                        break;

                    case 7:
                        gestion.reporteFinanciero();
                        break;
                    case 0:
                        salir = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void flujoRegistroMedico(GestionClinica gestion) {
        System.out.println("Nombre, Apellido, Especialidad, DNI:");
        String n = sc.nextLine();
        String a = sc.nextLine();
        String e = sc.nextLine();
        String d = sc.nextLine();
        System.out.print("Precio: ");
        double p = Double.parseDouble(sc.nextLine());
        System.out.println("Días, Horario, Password:");
        String di = sc.nextLine();
        String h = sc.nextLine();
        String pass = sc.nextLine();
        gestion.registrarMedico(n, a, e, d, p, di, h, pass);
    }

    // 3. FLUJOS PACIENTE Y MÉDICO
    public static void flujoRegistroPaciente(GestionClinica gestion) {
        System.out.println("Nombre, Apellido, DNI:");
        String n = sc.nextLine();
        String a = sc.nextLine();
        String d = sc.nextLine();
        String c = pedirCelular();
        LocalDate nac = pedirFecha("Nacimiento");
        System.out.print("Alergias: ");
        String al = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        gestion.registrarPaciente(n, a, d, c, nac, al, pass);
    }

    public static void flujoCrearCita(Paciente p, GestionClinica gestion) {
        Set<String> esps = gestion.obtenerEspecialidades();
        if (esps.isEmpty()) {
            System.out.println("Sin médicos.");
            return;
        }
        for (String e : esps)
            System.out.println("- " + e);
        System.out.print("Especialidad: ");
        String esp = sc.nextLine();
        List<Medico> meds = gestion.obtenerMedicosPorEspecialidad(esp);
        if (meds.isEmpty()) {
            System.out.println("No hay médicos.");
            return;
        }
        for (int i = 0; i < meds.size(); i++)
            System.out.println((i + 1) + ". " + meds.get(i));

        try {
            int idx = Integer.parseInt(sc.nextLine()) - 1;
            Medico m = meds.get(idx);
            System.out.println("Horarios: " + m.getDiasAtencion());
            LocalDate f = pedirFecha("Fecha Cita");
            if (f.isBefore(LocalDate.now())) {
                System.out.println("Fecha pasada.");
                return;
            }
            System.out.print("Motivo: ");
            String mot = sc.nextLine();
            gestion.crearCita(p, m, f, mot);
        } catch (Exception e) {
            System.out.println("Error.");
        }
    }

    public static void menuPaciente(Paciente p, GestionClinica gestion) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== PACIENTE: " + p.getNombre() + " ===");
            System.out.println("1. Cita nueva | 2. Historial | 3. Facturas | 4. Diagnósticos | 5. Cancelar | 0. Salir");
            try {
                int op = Integer.parseInt(sc.nextLine());
                switch (op) {
                    case 1:
                        flujoCrearCita(p, gestion);
                        break;
                    case 2:
                        for (Cita c : gestion.getHistorialPaciente(p.getId()))
                            System.out.println(c);
                        break;
                    case 3:
                        gestion.verFacturasDePaciente(p);
                        break;
                    case 4:
                        gestion.consultarDiagnostico(p);
                        break;
                    case 5:
                        List<Cita> l = gestion.getHistorialPaciente(p.getId());
                        for (Cita c : l)
                            if (c.getEstado() == EstadoCita.PENDIENTE)
                                System.out.println(c);
                        System.out.print("ID a cancelar: ");
                        gestion.cancelarCita(Integer.parseInt(sc.nextLine()), p);
                        break;
                    case 0:
                        salir = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error.");
            }
        }
    }

    public static void menuMedico(Medico m, GestionClinica gestion) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== DR. " + m.getApellidos() + " ===");
            System.out.println("1. Agenda | 2. Atender | 3. No Asistió | 0. Salir");
            try {
                int op = Integer.parseInt(sc.nextLine());
                switch (op) {
                    case 1:
                        List<Cita> l = gestion.getCitasMedico(m.getId());
                        if (l.isEmpty())
                            System.out.println("Agenda vacía.");
                        for (Cita c : l) {
                            System.out.println(c); // Muestra si tiene triaje o no
                            if (c.isTriajeRealizado()) {
                                System.out.println("   -> SIGNOS: T: " + c.getTemperatura() + " | P: " + c.getPeso()
                                        + "kg | PA: " + c.getPresionArterial());
                            } else {
                                System.out.println("   ⚠️ FALTA PASAR POR TRIAJE");
                            }
                            if (!c.getPaciente().getAlergias().equalsIgnoreCase("Ninguna"))
                                System.out.println("   ⚠️ ALERGIAS: " + c.getPaciente().getAlergias());
                        }
                        break;
                    case 2:
                        System.out.print("ID Cita: ");
                        int id = Integer.parseInt(sc.nextLine());
                        System.out.print("Diagnóstico: ");
                        String d = sc.nextLine();
                        System.out.print("Receta: ");
                        String r = sc.nextLine();
                        gestion.registrarDiagnostico(id, d, r);
                        System.out.println("Paciente enviado a Caja.");
                        break;
                    case 3:
                        System.out.print("ID Cita: ");
                        gestion.marcarInasistencia(Integer.parseInt(sc.nextLine()));
                        break;
                    case 0:
                        salir = true;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error.");
            }
        }
    }
}