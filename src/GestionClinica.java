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
    private ArrayList<Factura> facturas;

    public GestionClinica() {
        this.pacientes = new ArrayList<>();
        this.medicos = new ArrayList<>();
        this.citas = new ArrayList<>();
        this.administradores = new ArrayList<>();
        this.facturas = new ArrayList<>();
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

    public void cancelarCita(int idCita, Paciente pacienteLogueado) {
        Cita citaACancelar = null;

        // 1. Buscar la cita por ID
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                citaACancelar = c;
                break;
            }
        }

        if (citaACancelar == null) {
            System.out.println("❌ Error: No se encontró ninguna cita con el ID " + idCita);
            return;
        }

        // 2. Verificar que la cita pertenece al paciente logueado
        if (citaACancelar.getPaciente().getUserID() != pacienteLogueado.getUserID()) {
            System.out.println("❌ Error: No puedes cancelar una cita que no te pertenece.");
            return;
        }

        // 3. Eliminar la cita
        this.citas.remove(citaACancelar);
        System.out.println("✅ Cita con ID " + idCita + " cancelada exitosamente.");
    }

    public void generarFactura(int idCita) {
        // 1. Buscar la cita por ID
        Cita citaFacturar = null;
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                citaFacturar = c;
                break;
            }
        }

        if (citaFacturar == null) {
            System.out.println("❌ Error: No se encontró ninguna cita con el ID " + idCita);
            return;
        }

        // 2. Verificar que ya no existe una factura para esa cita
        for (Factura f : facturas) {
            if (f.getCitaAsociada().getId() == idCita) {
                System.out.println("⚠️ Ya existe una factura para la cita con ID " + idCita);
                return;
            }
        }

        // 3. Calcular el importe (usando el precio del médico)
        double importe = citaFacturar.getMedico().getConsultaPrecio();

        // 4. Crear la factura y añadirla a la lista
        int idFactura = rand.nextInt(100000) + 900000;
        Factura nuevaFactura = new Factura(idFactura, citaFacturar, importe);
        this.facturas.add(nuevaFactura);

        System.out.println("✅ Factura generada con éxito. ID de Factura: " + idFactura);
    }

    public void verFacturasDePaciente(Paciente paciente) {
        System.out.println("--- Historial de Facturas para " + paciente.getNombre() + " ---");
        boolean tieneFacturas = false;
        for (Factura f : facturas) {
            // Comparamos el ID del paciente de la factura con el del paciente logueado
            if (f.getCitaAsociada().getPaciente().getUserID() == paciente.getUserID()) {
                System.out.println(f);
                tieneFacturas = true;
            }
        }
        if (!tieneFacturas) {
            System.out.println("No tienes facturas registradas.");
        }
    }

    public void consultarDiagnostico(Paciente pacienteLogueado) {
        System.out.println("--- Consulta de Diagnósticos para " + pacienteLogueado.getNombre() + " ---");
        boolean tieneDiagnosticos = false;
        for (Cita c : citas) {
            // Verificamos que la cita es del paciente Y que tiene un diagnóstico
            if (c.getPaciente().getUserID() == pacienteLogueado.getUserID() && c.getDiagnostico() != null && !c.getDiagnostico().isEmpty()) {
                System.out.println("-------------------------------------");
                System.out.println("ID Cita: " + c.getId());
                System.out.println("Fecha: " + c.getFecha());
                System.out.println("Diagnóstico: " + c.getDiagnostico());
                System.out.println("-------------------------------------");
                tieneDiagnosticos = true;
            }
        }
        if (!tieneDiagnosticos) {
            System.out.println("No tienes diagnósticos disponibles para consultar.");
        }
    }

    public void verHistorialCitasPaciente(Paciente pacienteLogueado) {
        System.out.println("--- Historial Completo de Citas para " + pacienteLogueado.getNombre() + " ---");
        boolean tieneCitas = false;
        for (Cita c : citas) {
            if (c.getPaciente().getUserID() == pacienteLogueado.getUserID()) {
                System.out.println(c); // Usamos el toString() de Cita que ya es muy completo
                tieneCitas = true;
            }
        }
        if (!tieneCitas) {
            System.out.println("No tienes citas registradas en el sistema.");
        }
    }

    public void verAgendaMedico(Medico medicoLogueado) {
        System.out.println("--- Agenda de Citas para Dr./Dra. " + medicoLogueado.getNombre() + " ---");
        boolean tieneCitas = false;
        for (Cita c : citas) {
            if (c.getMedico().getUserID() == medicoLogueado.getUserID()) {
                System.out.println(c); // El toString() ya muestra la info bien
                tieneCitas = true;
            }
        }
        if (!tieneCitas) {
            System.out.println("No tienes citas programadas.");
        }
    }

    public void verListaDePacientesAtendidos(Medico medicoLogueado) {
        System.out.println("--- Lista de Pacientes Atendidos por Dr./Dra. " + medicoLogueado.getNombre() + " ---");
        // Usamos un HashSet para evitar mostrar pacientes duplicados si tuvieron varias citas
        java.util.HashSet<String> pacientesUnicos = new java.util.HashSet<>();

        for (Cita c : citas) {
            if (c.getMedico().getUserID() == medicoLogueado.getUserID()) {
                pacientesUnicos.add(c.getPaciente().getNombre() + " (DNI: " + c.getPaciente().getDni() + ")");
            }
        }

        if (pacientesUnicos.isEmpty()) {
            System.out.println("No ha atendido a ningún paciente aún.");
        } else {
            for (String p : pacientesUnicos) {
                System.out.println("- " + p);
            }
        }
    }

    public void verHistorialMedicoDePaciente(String dniPaciente, Medico medicoLogueado) {
        // 1. Encontrar al paciente por DNI
        Paciente pacienteSeleccionado = null;
        for (Paciente p : pacientes) {
            if (p.getDni().equals(dniPaciente)) {
                pacienteSeleccionado = p;
                break;
            }
        }

        if (pacienteSeleccionado == null) {
            System.out.println("❌ Error: No se encontró ningún paciente con el DNI " + dniPaciente);
            return;
        }

        // 2. Verificar que el médico ha atendido a este paciente al menos una vez
        boolean haAtendidoAPaciente = false;
        for (Cita c : citas) {
            if (c.getMedico().getUserID() == medicoLogueado.getUserID() && c.getPaciente().getDni().equals(dniPaciente)) {
                haAtendidoAPaciente = true;
                break;
            }
        }

        if (!haAtendidoAPaciente) {
            System.out.println("❌ Error: No tiene permiso para ver el historial de este paciente, ya que no lo ha atendido.");
            return;
        }

        // 3. Mostrar el historial completo del paciente
        System.out.println("\n--- HISTORIAL MÉDICO COMPLETO DE " + pacienteSeleccionado.getNombre().toUpperCase() + " ---");
        System.out.println("DNI: " + pacienteSeleccionado.getDni() + " | Edad: " + pacienteSeleccionado.getEdad());
        System.out.println("================================================");

        boolean tieneHistorial = false;
        for (Cita c : citas) {
            if (c.getPaciente().getDni().equals(dniPaciente)) {
                System.out.println("\nFecha de Cita: " + c.getFecha());
                System.out.println("Médico: " + c.getMedico().getNombre() + " (" + c.getMedico().getEspecialidad() + ")");
                System.out.println("Motivo: " + c.getMotivo());
                // El diagnóstico puede ser nulo si la cita es futura o no se ha registrado
                String diagnosticoStr = (c.getDiagnostico() == null || c.getDiagnostico().isEmpty()) ? "Pendiente" : c.getDiagnostico();
                System.out.println("Diagnóstico: " + diagnosticoStr);
                tieneHistorial = true;
            }
        }

        if (!tieneHistorial) {
            System.out.println("Este paciente no tiene citas registradas en el sistema.");
        }
        System.out.println("================================================");
    }

    public void agregarDiagnostico(int idCita, String diagnostico, Medico medicoLogueado) {
        Cita citaParaDiagnostico = null;
        for (Cita c : citas) {
            if (c.getId() == idCita) {
                citaParaDiagnostico = c;
                break;
            }
        }

        if (citaParaDiagnostico == null) {
            System.out.println("❌ Error: No se encontró la cita con ID " + idCita);
            return;
        }

        // Verificamos que la cita pertenece a este médico
        if (citaParaDiagnostico.getMedico().getUserID() != medicoLogueado.getUserID()) {
            System.out.println("❌ Error: No puedes añadir un diagnóstico a una cita que no es tuya.");
            return;
        }

        // Añadimos el diagnóstico
        citaParaDiagnostico.setDiagnostico(diagnostico);
        System.out.println("✅ Diagnóstico añadido a la cita con ID " + idCita);
    }
}