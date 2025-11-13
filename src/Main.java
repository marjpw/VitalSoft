import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final GestionClinica gestion = new GestionClinica();

    public static void main(String[] args) {
        boolean running = true;

        while(running){
            int opc = menuPrincipal();
            if(opc == 4){
                System.out.println("Cerrando VITALSOFT. ¡Hasta pronto!");
                running = false;
            }
            else { iniciarSesion(opc); }
        }
        sc.close();
    }

    public static int menuPrincipal(){
        boolean valido = false;
        int opc = -1;
        do{
            try{
                System.out.print("\t\tVITALSOFT\n\n");
                System.out.println("Iniciar Sesión - tipo de usuario");
                System.out.println("1. Paciente");
                System.out.println("2. Médico");
                System.out.println("3. Administrador");
                System.out.println("4. Salir");
                System.out.print("-> Ingrese una opción: ");
                opc = sc.nextInt();
                sc.nextLine();
                if (opc > 0 && opc < 5) {
                    valido = true;
                }
                else {
                    System.out.println("\n❌ Ingrese una opción válida (1-4).\n");
                }

            } catch(InputMismatchException e){
                System.out.println("\n⚠️ Error: Solo se permiten números enteros.\n");
                sc.nextLine();
            }
        }while(!valido);
        return opc;
    }

    public static void iniciarSesion(int opc) {
        String[] tipo = {"Paciente", "Médico", "Administrador"};
        System.out.println("\n--- Iniciar sesión: " + tipo[opc - 1] + " ---");

        System.out.print("Usuario ID: ");
        String user = sc.nextLine();
        System.out.print("Contraseña: ");
        String psw = sc.nextLine();

        switch (opc) {
            case 1:
                Paciente pacienteLogueado = gestion.autenticarPaciente(user, psw);
                if (pacienteLogueado != null) {
                    // Pasamos el control a una clase de UI, pasando el paciente autenticado
                    Menu.mostrarMenuPaciente(pacienteLogueado, gestion);
                } else {
                    System.out.println("❌ Credenciales incorrectas.");
                }
                break;
            case 2:
                Medico medicoLogueado = gestion.autenticarMedico(user, psw);
                if (medicoLogueado != null) {
                    Menu.mostrarMenuMedico(medicoLogueado, gestion);
                } else {
                    System.out.println("❌ Credenciales incorrectas.");
                }
                break;
            case 3:
                Administrador adminLogueado = gestion.autenticarAdministrador(user, psw);
                if (adminLogueado != null) {
                    Menu.mostrarMenuAdministrador(adminLogueado, gestion);
                } else {
                    System.out.println("❌ Credenciales incorrectas.");
                }
                break;
        }
    }
}