public class FacturaService {
    private FacturaDAO facturaDAO;
    private CitaService citaService;

    public FacturaService() {
        this.facturaDAO = new FacturaDAO();
    }

    // Método para inyectar dependencia
    public void setCitaService(CitaService citaService) {
        this.citaService = citaService;
    }

    public void generarFactura(int idCita) {
        facturaDAO.generarFactura(idCita);
    }

    public void reporteFinanciero() {
        facturaDAO.reporteFinanciero();
    }

    public void verFacturasDePaciente(Paciente p) {
        facturaDAO.verFacturasDePaciente(p);
    }

    public double calcularTotalRecaudado() {
        return facturaDAO.calcularTotalRecaudado();
    }

    public boolean citaYaFacturada(int idCita) {
        return facturaDAO.citaYaFacturada(idCita);
    }
}
