import java.util.ArrayList;

public class FacturaService {
    private ArrayList<Factura> facturas;
    private CitaService citaService;

    public FacturaService() {
        this.facturas = new ArrayList<>();
    }

    // Método para inyectar dependencia
    public void setCitaService(CitaService citaService) {
        this.citaService = citaService;
    }

    public void generarFactura(int idCita) {
        // Verificar que no exista factura ya
        if (citaYaFacturada(idCita)) {
            System.out.println("⚠️ Ya existe factura.");
            return;
        }

        // Buscar la cita
        Cita c = citaService.getCitaPorId(idCita);
        if (c != null) {
            Factura nuevaFactura = new Factura(c);
            facturas.add(nuevaFactura);
            System.out.println(nuevaFactura);
        } else {
            System.out.println("❌ Cita no encontrada.");
        }
    }

    public void reporteFinanciero() {
        double total = 0;
        System.out.println("\n--- CAJA DEL DÍA ---");
        for (Factura f : facturas) {
            total += f.getTotal();
        }
        System.out.println("Total Recaudado: S/ " + String.format("%.2f", total));
    }

    public void verFacturasDePaciente(Paciente p) {
        boolean encontrado = false;
        for (Factura fa : facturas) {
            if (fa.getCitaAsociada().getPaciente().getId() == p.getId()) {
                System.out.println(fa);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Sin facturas.");
        }
    }

    public boolean citaYaFacturada(int idCita) {
        for (Factura f : facturas) {
            if (f.getCitaAsociada().getId() == idCita) {
                return true;
            }
        }
        return false;
    }
}
