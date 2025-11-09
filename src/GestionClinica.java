import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GestionClinica {
    private static final Random rand = new Random();
    private static final Scanner sc = new Scanner(System.in);

    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Cita> citas;
    private ArrayList<Administrador> administradores;

    public GestionClinica() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.administradores = new ArrayList<>();
        administradores.add(new Administrador("Admin", "111A", "admin123", "admin"));
    }

    public Paciente autenticarPaciente(String user, String psw) {
        for (Paciente p : pacientes) {
            if (String.valueOf(p.getUserID()).equals(user) && p.getPsw().equals(psw)) {
                return p;
            }
        }
        return null;
    }

    public Medico autenticarMedico(String user, String psw){
        for(Medico m : medicos){
            if(String.valueOf(m.getUserID()).equals(user) && m.getPsw().equals(psw)){
                return m;
            }
        }
        return null;
    }

    public Administrador autenticarAdministrador(String user, String psw){
        for(Administrador a : administradores){
            if(String.valueOf(a.getDni()).equals(user) && a.getPsw().equals(psw)){
                return a;
            }
        }
        return null;
    }

    private Paciente findPacienteByDni(String dni) {
        for (Paciente p : pacientes) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }
        return null;
    }

    private Medico findMedicoByDni(String dni) {
        for (Medico m : medicos) {
            if (m.getDni().equals(dni)) {
                return m;
            }
        }
        return null;
    }

    public void registrarPaciente(String nombre, String dni, int edad, String psw) {
        int userID = rand.nextInt(10000) + 10000;
        Paciente nuevoPaciente = new Paciente(nombre, dni, edad, userID);
        nuevoPaciente.setPsw(psw); // Asumiendo que tienes un setter
        this.pacientes.add(nuevoPaciente);
        System.out.println("✅ Paciente registrado con éxito. Su ID es: " + userID);
    }

    public void registrarMedico(String nombre, String especialidad, String dni, String psw) {
        int userID = rand.nextInt(10000) + 20000;
        Medico nuevoMedico = new Medico(nombre, especialidad, dni, userID);
        nuevoMedico.setPsw(psw);
        this.medicos.add(nuevoMedico);
        System.out.println("✅ Médico registrado con éxito. Su ID es: " + userID);
    }

    public void verPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("--- Lista de Pacientes ---");
        for (Paciente p : pacientes) {
            System.out.println(p);
        }
    }

    public void verMedicos(){
        if(medicos.isEmpty()){
            System.out.println("No hay médicos registrados.");
        }
        System.out.println("Lista de médicos: ");
        for(Medico m : medicos){
            System.out.println(m);
        }
    }

    public void eliminarMedico(int idBuscado){
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

    public void crearCita(String motivo, String dniPacienteLogueado, String dniMedico, String fecha) {
        Paciente paciente = findPacienteByDni(dniPacienteLogueado);
        Medico medico = findMedicoByDni(dniMedico);

        if (paciente == null) {
            System.out.println("Error: El paciente logueado no fue encontrado en el sistema.");
            return;
        }

        if (medico == null) {
            System.out.println("Error: Médico con DNI " + dniMedico + " no encontrado.");
            return;
        }

        int idCita = rand.nextInt(1000000) + 1000000;
        Cita nuevaCita = new Cita(motivo, paciente, medico, fecha, idCita);
        this.citas.add(nuevaCita);
        System.out.println("✅ Cita creada con éxito para " + paciente.getNombre() + " con el Dr./Dra. " + medico.getNombre());
    }
}