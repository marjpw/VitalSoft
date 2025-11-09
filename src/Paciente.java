import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Paciente {
    private String nombre;
    private String dni;
    private int edad;
    private String psw;
    private int userID;

    public Paciente(String nombre, String dni, int edad, int userID) {
        this.nombre = nombre;
        this.dni= dni;
        this.edad = edad;
        this.userID = userID;
    }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }

    public void setDni(String dni) { this.dni = dni; }
    public String getDni() { return dni; }

    public void setEdad(int edad) { this.edad = edad; }
    public int getEdad() { return edad; }

    public void setPsw(String psw) { this.psw = psw; }
    public String getPsw() { return psw; }

    public void setUserID(int userID) { this.userID = userID; }
    public int getUserID() { return userID; }

    public String toString() {
        return "Paciente [Nombre=" + nombre + ", Edad=" + edad + ", DNI=" + dni + "]";
    }
}
