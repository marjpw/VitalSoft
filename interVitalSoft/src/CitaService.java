import java.util.ArrayList;
import java.util.List;

public class CitaService {
    private ArrayList<Cita> citas;
    // Referencia a FacturaService se inyectará después de la construcción
    private FacturaService facturaService;

    public CitaService() {
        this.citas = new ArrayList<>();
    }

    // Método para inyectar dependencia (evita referencia circular en constructor)
    public void setFacturaService(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    public void crearCita(Paciente paciente, Medico medico, java.time.LocalDate fecha, String motivo) {
        // Validar que no tenga cita duplicada con el mismo médico en la misma fecha
        for (Cita c : citas) {
            if (c.getPaciente().getId() == paciente.getId()
                    && c.getMedico().getId() == medico.getId()
                    && c.getFecha().equals(fecha)
                    && c.getEstado() != EstadoCita.CANCELADO) {
                System.out.println("❌ Error: Ya tiene cita con este médico hoy.");
                return;
            }
        }

        int turno = calcularTurno(medico, fecha);
        citas.add(new Cita(paciente, medico, fecha, motivo, turno));
        System.out.println("✅ CITA CREADA. Turno #" + turno + " | ID Cita: " + citas.get(citas.size() - 1).getId());
    }

    public void cancelarCita(int idCita, Paciente paciente) {
        for (Cita c : citas) {
            if (c.getId() == idCita && c.getPaciente().getId() == paciente.getId()) {
                if (c.puedeSerCancelada()) {
                    c.setEstado(EstadoCita.CANCELADO);
                    System.out.println("✅ Cita cancelada.");
                } else {
                    System.out.println("❌ Ya fue atendida o cancelada.");
                }
                return;
            }
        }
        System.out.println("❌ Cita no encontrada.");
    }

    public int calcularTurno(Medico medico, java.time.LocalDate fecha) {
        int contador = 0;
        for (Cita c : citas) {
            if (c.getMedico().getId() == medico.getId()
                    && c.getFecha().equals(fecha)
                    && c.getEstado() != EstadoCita.CANCELADO) {
                contador++;
            }
        }
        return contador + 1;
    }

    public List<Cita> getCitasPendientesTriaje() {
        List<Cita> lista = new ArrayList<>();
        for (Cita c : citas) {
            if (c.necesitaTriaje()) {
                lista.add(c);
            }
        }
        return lista;
    }

    public void registrarTriaje(int idCita, double peso, double talla, double temp, String presion) {
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                c.registrarSignosVitales(peso, talla, temp, presion);
                System.out.println("✅ Triaje registrado. El paciente está listo para el médico.");
                return;
            }
        }
        System.out.println("❌ ID no encontrado.");
    }

    public List<Cita> getCitasMedico(int idMedico) {
        List<Cita> lista = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getMedico().getId() == idMedico && c.getEstado() == EstadoCita.PENDIENTE) {
                lista.add(c);
            }
        }
        return lista;
    }

    public void registrarDiagnostico(int idCita, String diagnostico, String receta) {
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                c.setDiagnostico(diagnostico);
                c.setReceta(receta);
                c.setEstado(EstadoCita.ATENDIDO);
                System.out.println("✅ Atención médica guardada.");
                return;
            }
        }
        System.out.println("❌ Cita no encontrada.");
    }

    public void marcarInasistencia(int idCita) {
        for (Cita c : citas) {
            if (c.getId() == idCita && c.getEstado() == EstadoCita.PENDIENTE) {
                c.setEstado(EstadoCita.NO_ASISTIO);
                System.out.println("✅ Marcado como NO ASISTIÓ.");
                return;
            }
        }
        System.out.println("❌ No se pudo marcar inasistencia.");
    }

    public List<Cita> getCitasPendientesDePago() {
        List<Cita> porCobrar = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getEstado() == EstadoCita.ATENDIDO) {
                if (facturaService != null && !facturaService.citaYaFacturada(c.getId())) {
                    porCobrar.add(c);
                }
            }
        }
        return porCobrar;
    }

    public List<Cita> getHistorialPaciente(int idPaciente) {
        List<Cita> lista = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getPaciente().getId() == idPaciente) {
                lista.add(c);
            }
        }
        return lista;
    }

    public boolean tieneCitasPendientes(int idMedico) {
        for (Cita c : citas) {
            if (c.getMedico().getId() == idMedico && c.getEstado() == EstadoCita.PENDIENTE) {
                return true;
            }
        }
        return false;
    }

    // Método para obtener una cita específica por ID
    public Cita getCitaPorId(int idCita) {
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                return c;
            }
        }
        return null;
    }
}
