import java.util.InputMismatchException;
import java.util.Scanner;

public class Medico {
    public static final Scanner sc = new Scanner(System.in);
    private String nombre;
    private String especialidad;
    private String dni;
    private String psw;
    private int userID;

    public Medico(){

    }

    public Medico(int userID, String psw){
        this.userID = userID;
        this.psw = psw;
    }

    public Medico(String nombre, String especialidad, String dni, int userID) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.dni= dni;
        this.userID = userID;
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

    public boolean menuMedico(String user, String psw){
        boolean running = true;

        return running;
    }

    public void showMenuMedico(String user, String psw){


    }
}
