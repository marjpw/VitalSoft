import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GestionClinica gestion = new GestionClinica();
        Scanner sc = new Scanner(System.in);
        boolean appRunning = true;

        while (appRunning) {
            // Menú principal limpio y profesional
            System.out.println("\n=================================");
            System.out.println("    SISTEMA CLÍNICO VITALSOFT    ");
            System.out.println("=================================");
            System.out.println("SELECCIONE SU ROL PARA INGRESAR:");
            System.out.println("1. Ingresar como PACIENTE");
            System.out.println("2. Ingresar como MÉDICO");
            System.out.println("3. Ingresar como ADMINISTRADOR");
            System.out.println("0. Salir del sistema");
            System.out.println("---------------------------------");
            System.out.print("-> Ingrese una opción: ");

            try {
                int op = Integer.parseInt(sc.nextLine());
                switch (op) {
                    case 1:
                        // LOGIN PACIENTE
                        System.out.println("\n--- ACCESO PACIENTES ---");
                        System.out.print("Ingrese DNI: ");
                        String uP = sc.nextLine();
                        System.out.print("Ingrese Contraseña: ");
                        String pP = sc.nextLine();

                        Paciente pac = gestion.loginPaciente(uP, pP);
                        if (pac != null) {
                            System.out.println("Bienvenido/a " + pac.getNombre());
                            Menu.menuPaciente(pac, gestion);
                        } else
                            System.out.println("❌ Credenciales incorrectas.");
                        break;

                    case 2:
                        // LOGIN MÉDICO
                        System.out.println("\n--- ACCESO MÉDICOS ---");
                        System.out.print("Ingrese DNI: ");
                        String uM = sc.nextLine();
                        System.out.print("Ingrese Contraseña: ");
                        String pM = sc.nextLine();

                        Medico med = gestion.loginMedico(uM, pM);
                        if (med != null) {
                            System.out.println("Bienvenido Dr/a. " + med.getApellidos());
                            Menu.menuMedico(med, gestion);
                        } else
                            System.out.println("❌ Credenciales incorrectas.");
                        break;

                    case 3:
                        // LOGIN ADMINISTRADOR
                        System.out.println("\n--- ACCESO ADMINISTRADOR ---");
                        System.out.print("Usuario: ");
                        String uA = sc.nextLine();
                        System.out.print("Contraseña: ");
                        String pA = sc.nextLine();

                        // Aquí valida user: 111A y pass: admin123
                        Administrador admin = gestion.loginAdmin(uA, pA);

                        if (admin != null) {

                            Menu.mostrarMenuAdministrador(admin, gestion);
                        } else
                            System.out.println("❌ Acceso denegado.");
                        break;

                    case 0:
                        System.out.println("Cerrando VITALSOFT. ¡Hasta luego!");
                        appRunning = false;
                        break;

                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Por favor ingrese un número.");
            }
        }
        sc.close();
    }
}