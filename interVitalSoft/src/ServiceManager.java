public class ServiceManager {
    private PacienteService pacienteService;
    private MedicoService medicoService;
    private CitaService citaService;
    private FacturaService facturaService;
    private AdministradorService administradorService;

    public ServiceManager() {
        // Inicializar servicios
        this.pacienteService = new PacienteService();
        this.medicoService = new MedicoService();
        this.citaService = new CitaService();
        this.facturaService = new FacturaService();
        this.administradorService = new AdministradorService();

        // Configurar dependencias entre servicios
        // CitaService y FacturaService tienen referencia circular, se inyectan
        // mutuamente
        citaService.setFacturaService(facturaService);
        facturaService.setCitaService(citaService);

        // MedicoService necesita CitaService para validar eliminación
        medicoService.setCitaService(citaService);

        // PacienteService necesita CitaService y FacturaService para consultas
        pacienteService.setCitaService(citaService);
        pacienteService.setFacturaService(facturaService);
    }

    // Getters para acceso a servicios
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
