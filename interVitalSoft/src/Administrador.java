public class Administrador {
    // este es el bueno 26/11 5am
    private String nombre;
    private String dni;
    private String psw;
    private String user;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDni() {
        return dni;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getPsw() {
        return psw;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public Administrador(String nombre, String dni, String psw, String user) {
        this.nombre = nombre;
        this.dni = dni;
        this.psw = psw;
        this.user = user;
    }

    // Método de autenticación
    public boolean autenticar(String user, String psw) {
        return this.user != null && this.user.equals(user)
                && this.psw != null && this.psw.equals(psw);
    }
}
