import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = ScannerUtil.getScanner();

    public static void main(String[] args) {

        System.out.println("=== PROBANDO CONEXIÓN A BD ===");
        if (Conexion.probarConexion()) {
            System.out.println("✅ Conexión exitosa a MySQL");
        } else {
            System.out.println("❌ Error de conexión - Verifica MySQL y credenciales");
            return; // No continuar si no hay conexión
        }

        boolean running = true;

        while (running) {
            int opc = menuPrincipal();
            if (opc == 4) {
                System.out.println("Cerrando VITALSOFT. ¡Hasta pronto!");
                running = false;
            } else {
                iniciarSesion();
            }
        }
        sc.close();
    }

    public static int menuPrincipal() {
        boolean valido = false;
        int opc = -1;
        do {
            try {
                System.out.print("\t\tVITALSOFT\n\n");
                System.out.println("Iniciar Sesión - tipo de usuario");
                System.out.println("1. Paciente");
                System.out.println("2. Médico");
                System.out.println("3. Administrador");
                System.out.println("4. Salir");
                System.out.print("-> Ingrese una opción: ");
                opc = sc.nextInt();
                sc.nextLine(); // limpiar buffer
                if (opc > 0 && opc < 5) {
                    valido = true;
                } else {
                    System.out.println("\n❌ Ingrese una opción válida (1-4).\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("\n⚠️ Error: Solo se permiten números enteros.\n");
                sc.nextLine(); // limpiar buffer
            }
        } while (!valido);
        return opc;
    }

    public static void iniciarSesion() {
        System.out.print("\nUsuario ID: ");
        String userID = sc.nextLine();
        System.out.print("Contraseña: ");
        String psw = sc.nextLine();

        // autenticar devuelve un Usuario, que puede ser instancia de un rol
        Usuario usuario = Usuario.autenticarUsuario(userID, psw);

        if (usuario != null) {
            switch (usuario.getRol().toUpperCase()) {
                case "PACIENTE":
                    if (usuario instanceof Paciente paciente) {
                        System.out.println("\n✅ Bienvenido paciente " + paciente.getNombre());
                        Paciente.mostrarMenuPaciente(paciente);
                    }
                    break;

                case "MEDICO":
                    if (usuario instanceof Medico medico) {
                        System.out.println("\n✅ Bienvenido Dr. " + medico.getNombre() + " (" + medico.getEspecialidad() + ")");
                        Medico.mostrarMenuMedico(medico);
                    }
                    break;

                case "ADMINISTRADOR":
                    if (usuario instanceof Administrador admin) {
                        System.out.println("\n✅ Bienvenido Administrador " + admin.getNombre());
                        Administrador.mostrarMenuAdministrador(admin);
                    }
                    break;

                default:
                    System.out.println("⚠️ Rol no reconocido.");
            }
        } else {
            System.out.println("\n❌ Usuario o contraseña incorrectos.");
        }
    }
}

