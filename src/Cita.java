public class Cita {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private String fecha;
    private String motivo;
    private String diagnostico;

    public Cita(String motivo, Paciente paciente, Medico medico, String fecha, int id) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.fecha = fecha;
        this.motivo = motivo;
    }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public void setMotivo(String motivo){ this.motivo = motivo; }
    public String getMotivo() { return motivo; }

    public void setId(int id) { this.id = id; }
    public int getId() {
        return id;
    }

    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Paciente getPaciente() {
        return paciente;
    }

    public void setMedico(Medico medico) { this.medico = medico; }
    public Medico getMedico() {
        return medico;
    }

    public String getFecha() {
        return fecha;
    }
    public void setFecha(String fecha){
        this.fecha= fecha;
    }

    public String toString() {
        String diagnosticoStr = (diagnostico == null || diagnostico.isEmpty()) ? "Pendiente" : diagnostico;
        return "Cita [ID=" + id + ", Paciente=" + paciente.getNombre() +
                ", Medico=" + medico.getNombre() + ", Fecha=" + fecha +
                ", Diagnóstico=" + diagnosticoStr + "]";
    }
}
