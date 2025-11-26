import java.util.ArrayList;

public class AdministradorService {
    private ArrayList<Administrador> administradores;

    public AdministradorService() {
        this.administradores = new ArrayList<>();
        // Datos semilla: administrador por defecto
        administradores.add(new Administrador("Super Admin", "111A", "admin123", "111A"));
    }

    public Administrador loginAdmin(String user, String psw) {
        for (Administrador a : administradores) {
            if (a.autenticar(user, psw)) {
                return a;
            }
        }
        return null;
    }
}
