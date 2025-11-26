import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MedicoService {
    private ArrayList<Medico> listaMedicos;
    private CitaService citaService;

    public MedicoService() {
        this.listaMedicos = new ArrayList<>();
        // Datos semilla: médicos iniciales
        listaMedicos.add(new Medico("Juan", "Perez", "Cardiologia", "10101010", 80.00,
                "Lunes, Miercoles", "09:00-13:00", "1234"));
        listaMedicos.add(new Medico("Maria", "Lopez", "Pediatria", "20202020", 60.00,
                "Martes, Jueves", "15:00-19:00", "1234"));
        listaMedicos.add(new Medico("Carlos", "Ruiz", "Dermatologia", "30303030", 70.00,
                "Viernes", "10:00-14:00", "1234"));
    }

    // Método para inyectar dependencia
    public void setCitaService(CitaService citaService) {
        this.citaService = citaService;
    }

    public Medico loginMedico(String dni, String psw) {
        for (Medico m : listaMedicos) {
            if (m.getDni().equals(dni) && m.autenticar(psw)) {
                return m;
            }
        }
        return null;
    }

    public void registrarMedico(String nombre, String apellido, String esp, String dni,
            double precio, String dias, String horas, String psw) {
        listaMedicos.add(new Medico(nombre, apellido, esp, dni, precio, dias, horas, psw));
        System.out.println("✅ Médico registrado correctamente.");
    }

    public void eliminarMedico(int idMedico) {
        // Validar que no tenga citas pendientes
        if (citaService != null && citaService.tieneCitasPendientes(idMedico)) {
            System.out.println("❌ No se puede eliminar: Tiene citas pendientes.");
            return;
        }

        listaMedicos.removeIf(m -> m.getId() == idMedico);
        System.out.println("✅ Operación realizada.");
    }

    public void verMedicos() {
        for (Medico m : listaMedicos) {
            System.out.println(m);
        }
    }

    public ArrayList<Medico> getListaMedicos() {
        return listaMedicos;
    }

    public Set<String> obtenerEspecialidades() {
        Set<String> especialidades = new HashSet<>();
        for (Medico m : listaMedicos) {
            especialidades.add(m.getEspecialidad());
        }
        return especialidades;
    }

    public List<Medico> obtenerMedicosPorEspecialidad(String especialidad) {
        return listaMedicos.stream()
                .filter(m -> m.atiendeEspecialidad(especialidad))
                .collect(Collectors.toList());
    }
}
