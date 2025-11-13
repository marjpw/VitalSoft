import java.util.InputMismatchException;
import java.util.Scanner;

public class Medico {
    public static final Scanner sc = new Scanner(System.in);
    private String nombre;
    private String especialidad;
    private String dni;
    private String psw;
    private int userID;
    private double consultarPrecio;

    public Medico(String nombre, String especialidad, String dni, int userID) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.dni= dni;
        this.userID = userID;
    }

    public Medico(String nombre, String especialidad, String dni, int userID, double consultarPrecio) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.dni= dni;
        this.userID = userID;
        this.consultarPrecio = consultarPrecio;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombre(){ return nombre; }

    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getEspecialidad() {
        return especialidad;
    }

    public void setDni(String dni) { this.dni = dni; }
    public String getDni(){ return dni; }

    public void setUserID(int userID) { this.userID = userID; }
    public int getUserID() { return userID; }

    public void setPsw(String psw) { this.psw = psw; }
    public String getPsw() { return psw; }

    public double getConsultaPrecio() { return consultarPrecio; }
    public void setConsultaPrecio(double consultaPrecio) { this.consultarPrecio = consultaPrecio; }

    public String toString(){
        return "Medico [DNI=" + dni + ", Nombre=" + nombre + ", Especialidad=" + especialidad + "]";
    }

}
