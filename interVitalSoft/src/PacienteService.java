import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PacienteService {
    private ArrayList<Paciente> listaPacientes;
    private CitaService citaService;
    private FacturaService facturaService;

    public PacienteService() {
        this.listaPacientes = new ArrayList<>();
    }

    // Métodos para inyectar dependencias
    public void setCitaService(CitaService citaService) {
        this.citaService = citaService;
    }

    public void setFacturaService(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public Paciente loginPaciente(String dni, String psw) {
        for (Paciente p : listaPacientes) {
            if (p.getDni().equals(dni) && p.autenticar(psw)) {
                return p;
            }
        }
        return null;
    }

    public void registrarPaciente(String nombre, String apellido, String dni, String cel,
            LocalDate nac, String alergias, String psw) {
        Paciente nuevo = new Paciente(nombre, apellido, dni, cel, nac, alergias, psw);
        listaPacientes.add(nuevo);
        System.out.println("✅ Paciente registrado con ID: " + nuevo.getId());
    }

    public void verPacientes() {
        for (Paciente p : listaPacientes) {
            System.out.println(p);
        }
    }

    public ArrayList<Paciente> getListaPacientes() {
        return listaPacientes;
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
            if (c.getEstado() == EstadoCita.ATENDIDO) {
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
