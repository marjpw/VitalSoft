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
            System.out.println("0. Volver al menú principal");
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

    }

    public static void mostrarMenuMedico(Medico medicoLogueado, GestionClinica gestion){

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