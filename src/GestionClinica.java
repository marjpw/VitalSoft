import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GestionClinica {
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Cita> citas;
    private ArrayList<Factura> facturas;
    private ArrayList<Administrador> administradores;

    public GestionClinica() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.facturas = new ArrayList<>();
        this.administradores = new ArrayList<>();

        // DATOS SEMILLA
        administradores.add(new Administrador("Super Admin", "111A", "admin123", "111A"));

        medicos.add(new Medico("Juan", "Perez", "Cardiologia", "10101010", 80.00, "Lunes, Miercoles", "09:00-13:00",
                "1234"));
        medicos.add(
                new Medico("Maria", "Lopez", "Pediatria", "20202020", 60.00, "Martes, Jueves", "15:00-19:00", "1234"));
        medicos.add(new Medico("Carlos", "Ruiz", "Dermatologia", "30303030", 70.00, "Viernes", "10:00-14:00", "1234"));
    }

    // --- LOGIN ---
    public Paciente loginPaciente(String dni, String psw) {
        for (Paciente p : pacientes)
            if (p.getDni().equals(dni) && p.getPsw().equals(psw))
                return p;
        return null;
    }

    public Medico loginMedico(String dni, String psw) {
        for (Medico m : medicos)
            if (m.getDni().equals(dni) && m.getPsw().equals(psw))
                return m;
        return null;
    }

    public Administrador loginAdmin(String user, String psw) {
        for (Administrador a : administradores)
            if (a.getUser().equals(user) && a.getPsw().equals(psw))
                return a;
        return null;
    }

    // --- REGISTROS BÁSICOS ---
    public void registrarPaciente(String nombre, String apellido, String dni, String cel, LocalDate nac,
            String alergias, String psw) {
        Paciente nuevo = new Paciente(nombre, apellido, dni, cel, nac, alergias, psw);
        pacientes.add(nuevo);
        System.out.println("✅ Paciente registrado con ID: " + nuevo.getId());
    }

    public void registrarMedico(String nombre, String apellido, String esp, String dni, double precio, String dias,
            String horas, String psw) {
        medicos.add(new Medico(nombre, apellido, esp, dni, precio, dias, horas, psw));
        System.out.println("✅ Médico registrado correctamente.");
    }

    public void eliminarMedico(int idMedico) {
        for (Cita c : citas) {
            if (c.getMedico().getId() == idMedico && c.getEstado() == EstadoCita.PENDIENTE) {
                System.out.println("❌ No se puede eliminar: Tiene citas pendientes.");
                return;
            }
        }
        medicos.removeIf(m -> m.getId() == idMedico);
        System.out.println("✅ Operación realizada.");
    }

    public void verPacientes() {
        for (Paciente p : pacientes)
            System.out.println(p);
    }

    public void verMedicos() {
        for (Medico m : medicos)
            System.out.println(m);
    }

    // --- LÓGICA DE CITAS ---
    public Set<String> obtenerEspecialidades() {
        Set<String> especialidades = new HashSet<>();
        for (Medico m : medicos)
            especialidades.add(m.getEspecialidad());
        return especialidades;
    }

    public List<Medico> obtenerMedicosPorEspecialidad(String especialidad) {
        return medicos.stream().filter(m -> m.getEspecialidad().equalsIgnoreCase(especialidad))
                .collect(Collectors.toList());
    }

    public int calcularTurno(Medico medico, LocalDate fecha) {
        int contador = 0;
        for (Cita c : citas)
            if (c.getMedico().getId() == medico.getId() && c.getFecha().equals(fecha)
                    && c.getEstado() != EstadoCita.CANCELADO)
                contador++;
        return contador + 1;
    }

    public void crearCita(Paciente paciente, Medico medico, LocalDate fecha, String motivo) {
        for (Cita c : citas) {
            if (c.getPaciente().getId() == paciente.getId() && c.getMedico().getId() == medico.getId()
                    && c.getFecha().equals(fecha) && c.getEstado() != EstadoCita.CANCELADO) {
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
                if (c.getEstado() == EstadoCita.PENDIENTE) {
                    c.setEstado(EstadoCita.CANCELADO);
                    System.out.println("✅ Cita cancelada.");
                } else
                    System.out.println("❌ Ya fue atendida o cancelada.");
                return;
            }
        }
        System.out.println("❌ Cita no encontrada.");
    }

    // TRIAJE (Enfermería)

    public List<Cita> getCitasPendientesTriaje() {
        List<Cita> lista = new ArrayList<>();
        // Filtramos citas de HOY o Futuras que estén Pendientes y NO tengan triaje
        for (Cita c : citas) {
            if (c.getEstado() == EstadoCita.PENDIENTE && !c.isTriajeRealizado()) {
                lista.add(c);
            }
        }
        return lista;
    }

    public void registrarTriaje(int idCita, double peso, double talla, double temp, String presion) {
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                c.setPeso(peso);
                c.setTalla(talla);
                c.setTemperatura(temp);
                c.setPresionArterial(presion);
                c.setTriajeRealizado(true);
                System.out.println("✅ Triaje registrado. El paciente está listo para el médico.");
                return;
            }
        }
        System.out.println("❌ ID no encontrado.");
    }

    // MÓDULO MÉDICO

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

    // : CAJA (Facturación Admin)

    public List<Cita> getCitasPendientesDePago() {
        List<Cita> porCobrar = new ArrayList<>();
        for (Cita c : citas) {
            if (c.getEstado() == EstadoCita.ATENDIDO) {
                boolean yaFacturado = false;
                for (Factura f : facturas) {
                    if (f.getCitaAsociada().getId() == c.getId()) {
                        yaFacturado = true;
                        break;
                    }
                }
                if (!yaFacturado)
                    porCobrar.add(c);
            }
        }
        return porCobrar;
    }

    public void generarFactura(int idCita) {
        for (Factura f : facturas) {
            if (f.getCitaAsociada().getId() == idCita) {
                System.out.println("⚠️ Ya existe factura.");
                return;
            }
        }
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                Factura nuevaFactura = new Factura(c);
                facturas.add(nuevaFactura);
                System.out.println(nuevaFactura);
                return;
            }
        }
        System.out.println("❌ Cita no encontrada.");
    }

    public void reporteFinanciero() {
        double total = 0;
        System.out.println("\n--- CAJA DEL DÍA ---");
        for (Factura f : facturas)
            total += f.getTotal();
        System.out.println("Total Recaudado: S/ " + String.format("%.2f", total));
    }

    // --- CONSULTAS PACIENTE ---
    public void verFacturasDePaciente(Paciente p) {
        boolean f = false;
        for (Factura fa : facturas)
            if (fa.getCitaAsociada().getPaciente().getId() == p.getId()) {
                System.out.println(fa);
                f = true;
            }
        if (!f)
            System.out.println("Sin facturas.");
    }

    public List<Cita> getHistorialPaciente(int idPaciente) {
        List<Cita> lista = new ArrayList<>();
        for (Cita c : citas)
            if (c.getPaciente().getId() == idPaciente)
                lista.add(c);
        return lista;
    }

    public void consultarDiagnostico(Paciente p) {
        List<Cita> hist = getHistorialPaciente(p.getId());
        boolean found = false;
        System.out.println("\n--- HISTORIAL CLÍNICO ---");
        for (Cita c : hist) {
            if (c.getEstado() == EstadoCita.ATENDIDO) {
                System.out.println("FECHA: " + c.getFecha() + " | DR: " + c.getMedico().getApellidos());
                System.out.println(">> Signos Vitales: T " + c.getTemperatura() + "°C | P " + c.getPeso() + "kg");
                System.out.println(">> Diagnóstico:    " + c.getDiagnostico());
                System.out.println(">> Receta:         " + c.getReceta());
                System.out.println("----------------------------------");
                found = true;
            }
        }
        if (!found)
            System.out.println("No hay registros.");
    }
}