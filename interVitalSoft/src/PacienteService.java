import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteService {
    private PacienteDAO pacienteDAO;
    private CitaService citaService;
    private FacturaService facturaService;

    public PacienteService() {
        this.pacienteDAO = new PacienteDAO();
    }

    // Métodos para inyectar dependencias
    public void setCitaService(CitaService citaService) {
        this.citaService = citaService;
    }

    public void setFacturaService(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public Paciente loginPaciente(String dni, String psw) {
        return pacienteDAO.login(dni, psw);
    }

    public void registrarPaciente(String nombre, String apellido, String dni, String cel,
            LocalDate nac, String alergias, String psw) {
        Paciente nuevo = new Paciente(nombre, apellido, dni, cel, nac, alergias, psw);
        pacienteDAO.insertar(nuevo);
    }

    public void verPacientes() {
        List<Paciente> pacientes = pacienteDAO.obtenerTodos();
        for (Paciente p : pacientes) {
            System.out.println(p);
        }
    }

    public ArrayList<Paciente> getListaPacientes() {
        return new ArrayList<>(pacienteDAO.obtenerTodos());
    }

    public List<Cita> getHistorialPaciente(int idPaciente) {
        if (citaService != null) {
            return citaService.getHistorialPaciente(idPaciente);
        }
        return new ArrayList<>();
    }

    public void consultarDiagnostico(Paciente p) {
        List<Cita> hist = getHistorialPaciente(p.getId());
        boolean found = false;
        System.out.println("\n--- HISTORIAL CLÍNICO ---");

        for (Cita c : hist) {
            if (c.getEstado() == EstadoCita.ATENDIDA) {
                System.out.println("FECHA: " + c.getFecha() + " | DR: " + c.getMedico().getApellidos());
                System.out.println(">> Signos Vitales: T " + c.getTemperatura() + "°C | P " + c.getPeso() + "kg");
                System.out.println(">> Diagnóstico: " + c.getDiagnostico());
                System.out.println(">> Receta: " + c.getReceta());
                System.out.println("----------------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No hay registros.");
        }
    }

    public void verFacturasDePaciente(Paciente p) {
        if (facturaService != null) {
            facturaService.verFacturasDePaciente(p);
        }
    }
}
