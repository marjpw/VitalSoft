import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);

    public static void mostrarMenuAdministrador(Administrador adminLogueado, GestionClinica gestion) {
        boolean running = true;
        while (running) {
            System.out.println("\n===== MENU ADMINISTRADOR =====");
            System.out.println("Bienvenido, " + adminLogueado.getNombre());
            System.out.println("1. Gestionar Pacientes");
            System.out.println("2. Gestionar Médicos");
            System.out.println("0. Cerrar sesión");
            System.out.print("-> Ingrese una opción: ");

            int opc = leerOpcion("", 0, 2); // Usamos un método utilitario

            switch (opc) {
                case 1:
                    gestionarPacientes(gestion); // Delegamos al submenú
                    break;
                case 2:
                    gestionarMedicos(gestion);   // Delegamos al submenú
                    break;
                case 0:
                    System.out.println("Cerrando sesión de administrador. ¡Hasta pronto!");
                    running = false;
                    break;
            }
        }
    }

    private static void gestionarPacientes(GestionClinica gestion) {
        boolean running = true;
        while (running) {
            System.out.println("\n--- GESTIÓN DE PACIENTES ---");
            System.out.println("1. Registrar paciente");
            System.out.println("2. Ver lista de pacientes");
            System.out.println("0. Salir");
            System.out.print("-> Ingrese una opción: ");

            int opc = leerOpcion("", 0, 2);

            switch (opc) {
                case 1:
                    registrarPaciente(gestion); // Delegamos la acción específica
                    break;
                case 2:
                    gestion.verPacientes();      // Llamada directa a GestionClinica
                    break;
                case 0:
                    running = false; // Sale de este submenú
                    break;
            }
        }
    }

    private static void registrarPaciente(GestionClinica gestion) {
        System.out.println("\n--- REGISTRAR NUEVO PACIENTE ---");
        System.out.print("Ingrese Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese DNI: ");
        String dni = sc.nextLine();
        System.out.print("Ingrese Edad: ");
        int edad = sc.nextInt();
        sc.nextLine(); // Limpiar buffer
        System.out.print("Ingrese Contraseña inicial: ");
        String psw = sc.nextLine();

        gestion.registrarPaciente(nombre, dni, edad, psw);
    }

    private static void gestionarMedicos(GestionClinica gestion) {
        boolean running = true;
        while (running) {
            System.out.println("--- GESTION DE MEDICOS ---");
            System.out.println("1. Registrar médico");
            System.out.println("2. Ver médicos");
            System.out.println("3. Eliminar médico");
            System.out.println("0. Salir");
            System.out.print("-> Ingrese una opción: ");

            int opc = leerOpcion("", 0, 2);

            switch (opc) {
                case 1:
                    registrarMedico(gestion); // Delegamos la acción específica
                    break;
                case 2:
                    gestion.verMedicos();      // Llamada directa a GestionClinica
                    break;
                case 3:
                    eliminarMedico(gestion);
                case 0:
                    running = false; // Sale de este submenú
                    break;
            }
        }
    }

    public static void registrarMedico(GestionClinica gestion){
        System.out.println("\n--- REGISTRAR NUEVO MÉDICO ---");
        System.out.print("Ingrese Nombre: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese DNI: ");
        String dni = sc.nextLine();
        System.out.print("Ingrese Especialidad: ");
        String especialidad = sc.nextLine();
        sc.nextLine(); // Limpiar buffer
        System.out.print("Ingrese Contraseña inicial: ");
        String psw = sc.nextLine();

        gestion.registrarMedico(nombre, especialidad, dni, psw);
    }

    public static void eliminarMedico(GestionClinica gestion){
        System.out.println("Ingrese la ID del médico que desea eliminar: ");
        int idBuscado = sc.nextInt();
        sc.nextLine();

        gestion.eliminarMedico(idBuscado);
    }

    public static void mostrarMenuPaciente(Paciente pacienteLogueado, GestionClinica gestion){
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

            int opc = leerOpcion("", 0, 5);

            switch(opc){
                case 1:
                    crearCita(pacienteLogueado.getDni(), gestion);
                    break;
                case 2:
                    gestion.verHistorialCitasPaciente(pacienteLogueado);
                    break;
                case 3:
                    cancelarCita(pacienteLogueado, gestion);
                    break;
                case 4:
                    gestion.consultarDiagnostico(pacienteLogueado);
                    break;
                case 5:
                    gestion.verFacturasDePaciente(pacienteLogueado);
                    break;
                case 0:
                    running = false;
                    break;
            }
        }
    }

    public static void crearCita(String pacienteDNI, GestionClinica gestion){
        System.out.println("\n--- CREAR CITA ---");
        System.out.print("Ingrese motivo de la cita: ");
        String nombre = sc.nextLine();
        System.out.print("Ingrese DNI del Médico: ");
        String dniMedico = sc.nextLine();
        sc.nextLine(); // Limpiar buffer
        String disponibilidad = horarioDisponible();
        if(disponibilidad != null){
            gestion.crearCita(nombre, pacienteDNI, dniMedico, disponibilidad);
        }
        else{
            System.out.println("No es posible crear la cita.");
        }
    }

    public static String horarioDisponible(){
        return "fecha";     // Falta añadir esta función
    }

    public static void cancelarCita(Paciente pacienteLogueado, GestionClinica gestion){
        System.out.println("\n--- CANCELAR CITA ---");
        System.out.print("Ingrese el ID de la cita que desea cancelar: ");      // Previamente ha tenido que revisar las citas que posee
        int idCita = sc.nextInt();
        sc.nextLine();

        gestion.cancelarCita(idCita, pacienteLogueado);
    }

    public static void mostrarMenuMedico(Medico medicoLogueado, GestionClinica gestion){
            boolean running = true;
            while (running) {
                System.out.println("\n===== MENU MEDICO =====");
                System.out.println("Bienvenido, " + medicoLogueado.getNombre());
                System.out.println("1. Ver citas programadas");
                System.out.println("2. Registrar diagnóstico");
                System.out.println("3. Ver historial médico");
                System.out.println("0. Cerrar sesión");
                System.out.print("-> Ingrese una opción: ");

                int opc = leerOpcion("", 0, 3);

                switch (opc){
                    case 1:
                        gestion.verAgendaMedico(medicoLogueado);
                        break;
                    case 2:
                        agregarDiagnostico(medicoLogueado, gestion);
                        break;
                    case 3:
                        verHistorialPaciente(medicoLogueado, gestion);
                        break;
                    case 0:
                        running = false;
                        break;
                }
            }
    }

    private static void agregarDiagnostico(Medico medicoLogueado, GestionClinica gestion) {
        System.out.println("\n--- AÑADIR DIAGNÓSTICO ---");
        System.out.print("Ingrese el ID de la cita: ");
        int idCita = sc.nextInt();
        sc.nextLine(); // Limpiar buffer
        System.out.print("Ingrese el diagnóstico: ");
        String diagnostico = sc.nextLine();

        gestion.agregarDiagnostico(idCita, diagnostico, medicoLogueado);
    }

    private static void verHistorialPaciente(Medico medicoLogueado, GestionClinica gestion) {
        System.out.println("\n--- VER HISTORIAL DE UN PACIENTE ---");
        // Primero, mostramos la lista de pacientes para que el médico sepa cuáles elegir
        gestion.verListaDePacientesAtendidos(medicoLogueado);

        System.out.print("\nIngrese el DNI del paciente cuyo historial desea ver: ");
        String dniPaciente = sc.nextLine();

        gestion.verHistorialMedicoDePaciente(dniPaciente, medicoLogueado);
    }

    private static int leerOpcion(String mensaje, int min, int max) {
        int opc = -1;
        do {
            try {
                System.out.print(mensaje);
                opc = sc.nextInt();
                sc.nextLine();
                if (opc >= min && opc <= max) {
                    return opc;
                } else {
                    System.out.println("❌ Opción inválida. Ingrese un número entre " + min + " y " + max + ".");
                }
            } catch (InputMismatchException e) {
                System.out.println("⚠️ Error: Solo se permiten números.");
                sc.nextLine();
            }
        } while (true);
    }
}