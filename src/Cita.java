import java.time.LocalDate;

public class Cita {
    private static int contadorIds = 1;

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDate fecha;
    private String motivo;

    // Datos Clínicos
    private String diagnostico;
    private String receta;

    // Datos de Triaje (Signos Vitales)
    private double peso;
    private double talla;
    private double temperatura;
    private String presionArterial;
    private boolean triajeRealizado; // Para saber si ya pasó por enfermería

    private int numeroTurno;
    private EstadoCita estado;

    public Cita(Paciente paciente, Medico medico, LocalDate fecha, String motivo, int numeroTurno) {
        this.id = contadorIds++;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.motivo = motivo;
        this.numeroTurno = numeroTurno;
        this.estado = EstadoCita.PENDIENTE;
        this.diagnostico = "";
        this.receta = "";
        this.triajeRealizado = false; // Nace falso
    }

    // --- Getters y Setters ---
    public int getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getReceta() {
        return receta;
    }

    public void setReceta(String receta) {
        this.receta = receta;
    }

    public int getNumeroTurno() {
        return numeroTurno;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    // Getters y Setters de Triaje
    public boolean isTriajeRealizado() {
        return triajeRealizado;
    }

    public void setTriajeRealizado(boolean triajeRealizado) {
        this.triajeRealizado = triajeRealizado;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getTalla() {
        return talla;
    }

    public void setTalla(double talla) {
        this.talla = talla;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    @Override
    public String toString() {
        String infoTriaje = triajeRealizado
                ? String.format(" [TRIAJE OK: %skg / %s°C]", peso, temperatura)
                : " [FALTA TRIAJE]";

        return String.format("ID: %d | Turno #%d | Fecha: %s | Paciente: %s | Estado: %s %s",
                id, numeroTurno, fecha, paciente.getApellidos(), estado, infoTriaje);
    }
}