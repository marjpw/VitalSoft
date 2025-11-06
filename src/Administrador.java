import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Administrador {

    public static final Scanner sc = new Scanner(System.in);
    public static final Random rand = new Random();

    private String nombre;
    private String dni;
    private String psw;
    private String user;

    public ArrayList<Paciente> paciente;
    public ArrayList<Medico> medico;

    public Administrador(){
        paciente = new ArrayList<>();
        medico = new ArrayList<>();
    }

    public boolean menuAdministrador(String user, String psw){
        boolean running = true;

        while(running){
            int opc = showMenuAdministrador();
            if(opc == 0){
                System.out.println("Cerrando VITALSOFT. ¡Hasta pronto!");
                running = false;
            }
            else{
                switch (opc){
                    case 1: registrarPaciente(); break;
                    case 2: registrarMedico(); break;
                    case 3: verPacientes(); break;
                    case 4: verMedicos(); break;
                }
            }
        }
        return running;
    }

    public int showMenuAdministrador(){
        boolean valido = false;
        int opc = -1;
        do{
            try{
                System.out.println("\n=====nMENU ADMINISTRADOR=====");
                System.out.println("1. Registrar Paciente ");
                System.out.println("2. Registrar Medico");
                System.out.println("3. Ver pacientes");
                System.out.println("4. ver medicos ");
                System.out.println("0. Salir");
                System.out.println("Ingrese una opcion: ");
                opc= sc.nextInt();
                sc.nextLine();
                if (opc >= 0 || opc < 5) {
                    valido = true;
                }
                else {
                    System.out.println("\n❌ Ingrese una opción válida (0-5).\n");
                }

            } catch (InputMismatchException e){
                System.out.println("\n⚠️ Error: Solo se permiten números enteros.\n");
                sc.nextLine();
            }
        }while(!valido);
        return opc;
    }

    public void registrarPaciente(){
        System.out.println("Ingrese Nombre: ");
        String nombreP= sc.nextLine();
        System.out.println("Ingrese DNI: ");
        String dniP= sc.nextLine();
        System.out.println("Ingrese edad: ");
        int edadP= sc.nextInt();

        int userID = rand.nextInt(10000) + 10000;

        Paciente nuevoPaciente= new Paciente(dniP,nombreP,edadP, userID);
        paciente.add(nuevoPaciente);
    }

    public void registrarMedico(){
        System.out.println("Ingrese Nombre: ");
        String nombreM= sc.nextLine();
        System.out.println("Ingrese Especialidad: ");
        String especialidad= sc.nextLine();
        System.out.println("Ingrese DNI: ");
        String dniM= sc.nextLine();

        int userID = rand.nextInt(10000) + 20000;

        Medico nuevoMedico = new Medico(nombreM,especialidad,dniM, userID);
        medico.add(nuevoMedico);
    }

    public void verPacientes(){
        System.out.println("Lista de pacientes: ");
        for(Paciente p : paciente){
            System.out.println(p);
        }
    }

    public void verMedicos(){
        System.out.println("Lista de médicos: ");
        for(Medico m : medico){
            System.out.println(m);
        }
    }
}
