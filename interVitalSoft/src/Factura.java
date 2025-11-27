import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Factura {
    private static int contadorIds = 1000;

    private int id;
    private Cita cita;
    private double subtotal;
    private double igv;
    private double total;
    private LocalDateTime fechaEmision;

    public Factura(Cita cita) {
        this.id = contadorIds++;
        this.cita = cita;
        this.fechaEmision = LocalDateTime.now();
        this.total = cita.getMedico().getPrecioConsulta();
        this.subtotal = total / 1.18;
        this.igv = total - subtotal;
    }

    public int getId() {
        return id;
    }

    public Cita getCitaAsociada() {
        return cita;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================================\n");
        sb.append("           CLÍNICA VITALSOFT             \n");
        sb.append("      RUC: 20555123456  -  LIMA, PERÚ    \n");
        sb.append("==========================================\n");
        sb.append("Nro Ticket:    F001-" + id + "\n");
        sb.append("Ref. Atención: Cita #" + cita.getId() + "\n");
        sb.append("Fecha Emisión: " + fechaEmision.format(fmt) + "\n");
        sb.append("------------------------------------------\n");
        sb.append("PACIENTE:\n");
        sb.append(cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellidos() + "\n");
        sb.append("DNI: " + cita.getPaciente().getDni() + "\n");
        sb.append("------------------------------------------\n");
        sb.append(String.format("%-20s %10s\n", "DESCRIPCION", "IMPORTE"));
        sb.append(String.format("%-20s %10.2f\n", "Consulta Médica", total));
        sb.append(String.format("%-20s %10s\n", cita.getMedico().getEspecialidad(), ""));
        sb.append("------------------------------------------\n");
        sb.append(String.format("SUBTOTAL:             S/ %8.2f\n", subtotal));
        sb.append(String.format("IGV (18%%):            S/ %8.2f\n", igv));
        sb.append(String.format("TOTAL A PAGAR:        S/ %8.2f\n", total));
        sb.append("==========================================\n");

        return sb.toString();
    }
}