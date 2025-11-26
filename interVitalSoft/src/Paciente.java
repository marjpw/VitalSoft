import java.time.LocalDate;
import java.time.Period;

public class Paciente {
    // Contador estático para que el ID sea único y automático (1, 2, 3...)
    private static int contadorIds = 1;

    private int id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String celular;
    private LocalDate fechaNacimiento;
    private String alergias;
    private String psw; // Contraseña

    // Atributos de triaje (AGREGADOS para que guarde los datos del IMC)
    private String imc;
    private String categoriaIMC;

    public Paciente(String nombre, String apellidos, String dni, String celular, LocalDate fechaNacimiento,
            String alergias, String psw) {
        this.id = contadorIds++;
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

    // --- ESTOS SON LOS NUEVOS MÉTODOS DE COMPATIBILIDAD ---
    public String getApellidos() {
        return apellidos;
    }

    public String getApellido() {
        return apellidos;
    } // Alias singular para evitar errores

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

    public String getTelefono() {
        return celular;
    } // Alias para evitar errores

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

    // Getters y Setters para IMC (Agregados según tu solicitud anterior)
    public String getImc() {
        return imc;
    }

    public void setImc(String imc) {
        this.imc = imc;
    }

    public String getCategoriaIMC() {
        return categoriaIMC;
    }

    public void setCategoriaIMC(String categoriaIMC) {
        this.categoriaIMC = categoriaIMC;
    }

    @Override
    public String toString() {
        return "Paciente: " + nombre + " " + apellidos + " | DNI: " + dni + " | Edad: " + getEdad() + " años";
    }
}