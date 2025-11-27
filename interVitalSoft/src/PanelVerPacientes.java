import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelVerPacientes extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblPagina;
    private JButton btnAnterior, btnSiguiente;

    private int paginaActual = 1;
    private final int FILAS_POR_PAGINA = 3;
    @SuppressWarnings("unused")
    private MainFrame mainFrame;
    private ServiceManager services;

    public PanelVerPacientes(MainFrame mainFrame, ServiceManager services) {
        this.mainFrame = mainFrame;
        this.services = services;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNorte.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("Pacientes Registrados");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        panelNorte.add(titulo);
        add(panelNorte, BorderLayout.NORTH);

        String[] columnas = { "ID", "DNI", "Nombre", "Apellido", "Celular", "Edad", "Alergias" };
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // --- Botones ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);

        btnAnterior = new JButton("Anterior");
        lblPagina = new JLabel("  1  ");
        lblPagina.setOpaque(true);
        lblPagina.setBackground(new Color(160, 215, 110)); // Verde
        lblPagina.setForeground(Color.WHITE);
        btnSiguiente = new JButton("Siguiente");

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanel("SeleccionVer"));

        panelSur.add(btnAnterior);
        panelSur.add(lblPagina);
        panelSur.add(btnSiguiente);
        panelSur.add(Box.createHorizontalStrut(30));
        panelSur.add(btnRegresar);

        add(panelSur, BorderLayout.SOUTH);

        cargarDatosTabla();

        btnAnterior.addActionListener(e -> {
            if (paginaActual > 1) {
                paginaActual--;
                cargarDatosTabla();
            }
        });

        btnSiguiente.addActionListener(e -> {
            int total = services.getPacienteService().getListaPacientes().size();
            int maxPaginas = (int) Math.ceil((double) total / FILAS_POR_PAGINA);
            if (paginaActual < maxPaginas) {
                paginaActual++;
                cargarDatosTabla();
            }
        });
    }

    public void cargarDatosTabla() {
        modelo.setRowCount(0);
        ArrayList<Paciente> lista = services.getPacienteService().getListaPacientes();

        int total = lista.size();
        int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
        int fin = Math.min(inicio + FILAS_POR_PAGINA, total);

        for (int i = inicio; i < fin; i++) {
            Paciente p = lista.get(i);
            int idVisual = i + 1;

            Object[] fila = {
                    idVisual,
                    p.getDni(),
                    p.getNombre(),
                    p.getApellido(),
                    p.getTelefono(),
                    p.getEdad(),
                    p.getAlergias()
            };
            modelo.addRow(fila);
        }

        lblPagina.setText("  " + paginaActual + "  ");
        btnAnterior.setEnabled(paginaActual > 1);
        btnSiguiente.setEnabled(fin < total);
    }
}