import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MedicoService {
    private MedicoDAO medicoDAO;
    private CitaService citaService;

    public MedicoService() {
        this.medicoDAO = new MedicoDAO();
    }

    // Método para inyectar dependencia
    public void setCitaService(CitaService citaService) {
        this.citaService = citaService;
    }

    public Medico loginMedico(String dni, String psw) {
        return medicoDAO.login(dni, psw);
    }

    public void registrarMedico(String nombre, String apellido, String esp, String dni,
            double precio, String dias, String horas, String psw) {
        Medico nuevo = new Medico(nombre, apellido, esp, dni, precio, dias, horas, psw);
        if (medicoDAO.insertar(nuevo)) {
            System.out.println("✅ Médico registrado correctamente.");
        }
    }

    public void actualizarMedico(int id, String nombre, String apellido, String esp, String dni, double precio,
            String dias, String horas, String psw) {
        Medico medico = medicoDAO.obtenerPorId(id);
        if (medico != null) {
            medico.setNombre(nombre);
            medico.setApellidos(apellido);
            medico.setEspecialidad(esp);
            medico.setDni(dni);
            medico.setPrecioConsulta(precio);
            medico.setDiasAtencion(dias);
            medico.setHorarioHora(horas);
            medico.setPsw(psw);

            if (medicoDAO.actualizar(medico)) {
                System.out.println("✅ Datos del médico actualizados.");
            }
        }
    }

    public void eliminarMedico(int idMedico) {
        // Validar que no tenga citas pendientes
        if (citaService != null && citaService.tieneCitasPendientes(idMedico)) {
            System.out.println("❌ No se puede eliminar: Tiene citas pendientes.");
            return;
        }

        if (medicoDAO.eliminar(idMedico)) {
            System.out.println("✅ Operación realizada.");
        }
    }

    public void verMedicos() {
        List<Medico> medicos = medicoDAO.obtenerTodos();
        for (Medico m : medicos) {
            System.out.println(m);
        }
    }

    public ArrayList<Medico> getListaMedicos() {
        return new ArrayList<>(medicoDAO.obtenerTodos());
    }

    public Set<String> obtenerEspecialidades() {
        return medicoDAO.obtenerEspecialidades();
    }

    public List<Medico> obtenerMedicosPorEspecialidad(String especialidad) {
        return medicoDAO.obtenerPorEspecialidad(especialidad);
    }
}
