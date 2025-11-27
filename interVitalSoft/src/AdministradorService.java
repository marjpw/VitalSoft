public class AdministradorService {
    private AdministradorDAO administradorDAO;

    public AdministradorService() {
        this.administradorDAO = new AdministradorDAO();
    }

    public Administrador loginAdmin(String user, String psw) {
        return administradorDAO.login(user, psw);
    }
}
