import java.time.LocalDate;
import java.time.Period;

public class Paciente {
    // Contador estático para que el ID sea único y automático (1, 2, 3...)
    private static int contadorIds = 1;

    private int id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String celular; // Nuevo
    private LocalDate fechaNacimiento; // Nuevo (reemplaza a edad fija)
    private String alergias; // Nuevo
    private String psw; // Contraseña

    public Paciente(String nombre, String apellidos, String dni, String celular, LocalDate fechaNacimiento,
            String alergias, String psw) {
        this.id = contadorIds++; // Se asigna y luego aumenta
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.celular = celular;
        this.fechaNacimiento = fechaNacimiento;
        this.alergias = alergias;
        this.psw = psw;
    }

    // Calcula la edad exacta basándose en la fecha de hoy
    public int getEdad() {
        if (fechaNacimiento == null)
            return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
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

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    @Override
    public String toString() {
        return "Paciente: " + nombre + " " + apellidos + " | DNI: " + dni + " | Edad: " + getEdad() + " años";
    }
}
