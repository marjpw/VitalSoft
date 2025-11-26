public class Medico {
    private static int contadorIds = 1; // ID autoincrementable

    private int id;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String dni;
    private double precioConsulta;
    private String psw;

    private String diasAtencion; // Ej: "Lunes, Miercoles, Viernes"
    private String horarioHora; // Ej: "09:00 - 13:00"

    public Medico(String nombre, String apellidos, String especialidad, String dni, double precioConsulta,
            String diasAtencion, String horarioHora, String psw) {
        this.id = contadorIds++;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.dni = dni;
        this.precioConsulta = precioConsulta;
        this.diasAtencion = diasAtencion;
        this.horarioHora = horarioHora;
        this.psw = psw;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    // --- NUEVO: Método puente para que PanelVerMedicos no falle al buscar
    // "getApellido" ---
    public String getApellido() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public double getPrecioConsulta() {
        return precioConsulta;
    }

    public void setPrecioConsulta(double precioConsulta) {
        this.precioConsulta = precioConsulta;
    }

    public String getDiasAtencion() {
        return diasAtencion;
    }

    public void setDiasAtencion(String diasAtencion) {
        this.diasAtencion = diasAtencion;
    }

    public String getHorarioHora() {
        return horarioHora;
    }

    // --- NUEVO: Método puente para que PanelVerMedicos no falle al buscar
    // "getHorario" ---
    public String getHorario() {
        return horarioHora;
    }

    public void setHorarioHora(String horarioHora) {
        this.horarioHora = horarioHora;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    // Métodos de validación (encapsulación de lógica)
    public boolean autenticar(String psw) {
        return this.psw != null && this.psw.equals(psw);
    }

    public boolean atiendeEspecialidad(String esp) {
        return this.especialidad != null && this.especialidad.equalsIgnoreCase(esp);
    }

    @Override
    public String toString() {
        return String.format("Dr/a. %s %s | Esp: %s | Horario: %s (%s)",
                nombre, apellidos, especialidad, diasAtencion, horarioHora);
    }
}