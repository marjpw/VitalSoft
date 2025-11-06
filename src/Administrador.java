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

    public ArrayList<Paciente> pacientes;
    public ArrayList<Medico> medicos;

    public Administrador(){
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
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
                System.out.println("2. Registrar Médico");
                System.out.println("3. Ver pacientes");
                System.out.println("4. Ver médicos ");
                System.out.println("5. Eliminar médicos");
                System.out.println("0. Salir");
                System.out.println("Ingrese una opcion: ");
                opc= sc.nextInt();
                sc.nextLine();
                if (opc >= 0 || opc < 6) {
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
        pacientes.add(nuevoPaciente);
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
        medicos.add(nuevoMedico);
    }

    public void verPacientes(){
        System.out.println("Lista de pacientes: ");
        for(Paciente p : pacientes){
            System.out.println(p);
        }
    }

    public void verMedicos(){
        System.out.println("Lista de médicos: ");
        for(Medico m : medicos){
            System.out.println(m);
        }
    }

    public void eliminarMedico(){
        System.out.println("Ingrese la ID del médico que desea eliminar: ");
        int idBuscado = sc.nextInt();
        sc.nextLine();

        Medico medicoEncontrado = null;
        for(Medico m : medicos){
            if(m.getUserID() == idBuscado){
                medicoEncontrado = m;
                break;
            }
        }
        if(medicoEncontrado == null){
            System.out.println("Error: No se encontró ningún médico con el id: " + idBuscado);
            return;
        }
        System.out.print("Se encontró al siguiente médico: ");
        System.out.println(medicoEncontrado);
        System.out.println("¿Está seguro que desea eliminarlo? (s/n)");
        String confirmacion = sc.nextLine().toUpperCase();

        if(confirmacion.equals("S")){
            medicos.remove(medicoEncontrado);
            System.out.println("Médico eliminado exitosamente.");
        }
        else{
            System.out.println("Operación cancelada.");
        }
    }
}
