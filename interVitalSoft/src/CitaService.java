import java.util.List;

public class CitaService {
    private CitaDAO citaDAO;
    private FacturaService facturaService;

    public CitaService() {
        this.citaDAO = new CitaDAO();
    }

    public void setFacturaService(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public void crearCita(Paciente paciente, Medico medico, java.time.LocalDate fecha, String motivo) {
        // Verificar que no exista cita duplicada (la lógica podría estar en el DAO
        // también)
        List<Cita> historial = citaDAO.obtenerHistorialPaciente(paciente.getId());
        for (Cita c : historial) {
            if (c.getMedico().getId() == medico.getId()
                    && c.getFecha().equals(fecha)
                    && c.getEstado() != EstadoCita.CANCELADO) {
                System.out.println("❌ Error: Ya tiene cita con este médico hoy.");
                return;
            }
        }

        Cita nuevaCita = new Cita(paciente, medico, fecha, motivo, 0); // El turno lo calcula el SP
        citaDAO.insertar(nuevaCita);
    }

    public void cancelarCita(int idCita, Paciente paciente) {
        Cita c = citaDAO.obtenerPorId(idCita);
        if (c != null && c.getPaciente().getId() == paciente.getId()) {
            if (c.puedeSerCancelada()) {
                citaDAO.actualizarEstado(idCita, EstadoCita.CANCELADO);
                System.out.println("✅ Cita cancelada.");
            } else {
                System.out.println("❌ Ya fue atendida o cancelada.");
            }
        } else {
            System.out.println("❌ Cita no encontrada.");
        }
    }

    public int calcularTurno(Medico medico, java.time.LocalDate fecha) {
        // Este método ya no es necesario porque el SP lo calcula
        // Pero lo mantenemos por compatibilidad
        return 0;
    }

    public List<Cita> getCitasPendientesTriaje() {
        return citaDAO.obtenerPendientesTriaje();
    }

    public void registrarTriaje(int idCita, double peso, double talla, double temp, String presion) {
        citaDAO.registrarTriaje(idCita, peso, talla, temp, presion);
    }

    public List<Cita> getCitasMedico(int idMedico) {
        return citaDAO.obtenerPorMedico(idMedico);
    }

    public void registrarDiagnostico(int idCita, String diagnostico, String receta) {
        citaDAO.registrarDiagnostico(idCita, diagnostico, receta);
    }

    public void marcarInasistencia(int idCita) {
        Cita c = citaDAO.obtenerPorId(idCita);
        if (c != null && c.getEstado() == EstadoCita.PENDIENTE) {
            citaDAO.actualizarEstado(idCita, EstadoCita.NO_ASISTIO);
            System.out.println("✅ Marcado como NO ASISTIÓ.");
        } else {
            System.out.println("❌ No se pudo marcar inasistencia.");
        }
    }

    public List<Cita> getCitasPendientesDePago() {
        return citaDAO.obtenerPendientesPago();
    }

    public List<Cita> getHistorialPaciente(int idPaciente) {
        return citaDAO.obtenerHistorialPaciente(idPaciente);
    }

    public boolean tieneCitasPendientes(int idMedico) {
        return citaDAO.tieneCitasPendientes(idMedico);
    }

    // Método para obtener una cita específica por ID
    public Cita getCitaPorId(int idCita) {
        return citaDAO.obtenerPorId(idCita);
    }

    public List<Cita> getHistorialMedico(int idMedico) {
        return citaDAO.obtenerHistorialMedico(idMedico);
    }

    public java.util.List<Paciente> getPacientesDeMedico(int idMedico) {
        return citaDAO.obtenerPacientesDeMedico(idMedico);
    }
}
