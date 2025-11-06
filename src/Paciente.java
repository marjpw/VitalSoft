import java.util.Scanner;

public class Paciente {
    private String nombre;
    private String dni;
    private int edad;
    private String psw;
    private int userID;

    public static final Scanner sc = new Scanner(System.in);

    public Paciente(){

    }

    public Paciente(int userID, String psw){
        this.userID = userID;
        this.psw = psw;
    }

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

    public boolean menuPaciente(String user, String psw){
        boolean running = true;

        return running;
    }

    public void showMenuPaciente(String user, String psw){
        System.out.println("Hola, paciente!");
    }
}
