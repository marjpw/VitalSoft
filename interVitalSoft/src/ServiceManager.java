public class ServiceManager {
    private PacienteService pacienteService;
    private MedicoService medicoService;
    private CitaService citaService;
    private FacturaService facturaService;
    private AdministradorService administradorService;

    public ServiceManager() {

        this.pacienteService = new PacienteService();
        this.medicoService = new MedicoService();
        this.citaService = new CitaService();
        this.facturaService = new FacturaService();
        this.administradorService = new AdministradorService();

        citaService.setFacturaService(facturaService);
        facturaService.setCitaService(citaService);

        medicoService.setCitaService(citaService);

        pacienteService.setCitaService(citaService);
        pacienteService.setFacturaService(facturaService);
    }

    public PacienteService getPacienteService() {
        return pacienteService;
    }

    public MedicoService getMedicoService() {
        return medicoService;
    }

    public CitaService getCitaService() {
        return citaService;
    }

    public FacturaService getFacturaService() {
        return facturaService;
    }

    public AdministradorService getAdministradorService() {
        return administradorService;
    }
}
