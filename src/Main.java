import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    private static Administrador admin = new Administrador();
    private static Paciente patient = new Paciente();
    private static Medico medic = new Medico();

    public static void main(String[] args){
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
                if (opc > 0 || opc < 5) {
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

    public static void iniciarSesion(int opc){
        String[] tipo = {"Iniciar sesión: Paciente", "Iniciar sesión: Médico", "Iniciar sesión: Administrador"};
        System.out.println(tipo[opc-1]);

        System.out.print("Usuario ID: ");
        String user = sc.nextLine();
        System.out.print("Contraseña: ");
        String psw = sc.nextLine();

        switch (opc){
            case 1: if(patient.menuPaciente(user, psw)) { break; }
            case 2: if(!medic.menuMedico(user, psw)) { break; }
            case 3: if(!admin.menuAdministrador(user, psw)) { break; }
        }
    }
}
