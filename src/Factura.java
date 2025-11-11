import java.time.LocalDate;

public class Factura {
    private int idFactura;
    private Cita citaAsociada;
    private double importe;
    private LocalDate fechaEmision;
    private boolean pagada;

    public Factura(int idFactura, Cita citaAsociada, double importe) {
        this.idFactura = idFactura;
        this.citaAsociada = citaAsociada;
        this.importe = importe;
        this.fechaEmision = LocalDate.now(); // Se asigna la fecha actual automáticamente
        this.pagada = false; // Por defecto, toda factura nueva está pendiente
    }


    // Getters y Setters
    public int getIdFactura() { return idFactura; }
    public Cita getCitaAsociada() { return citaAsociada; }
    public double getImporte() { return importe; }
    public LocalDate getFechaEmision() { return fechaEmision; }
    public boolean isPagada() { return pagada; }
    public void setPagada(boolean pagada) { this.pagada = pagada; }

    @Override
    public String toString() {
        return "=====================================\n" +
                "          FACTURA CLÍNICA\n" +
                "=====================================\n" +
                "ID Factura: " + idFactura + "\n" +
                "Fecha Emisión: " + fechaEmision + "\n" +
                "-------------------------------------\n" +
                "Datos de la Cita:\n" +
                citaAsociada.toString() + "\n" +
                "-------------------------------------\n" +
                "Importe Total: $" + String.format("%.2f", importe) + "\n" +
                "Estado: " + (pagada ? "PAGADA" : "PENDIENTE DE PAGO") + "\n" +
                "=====================================";
    }
}
