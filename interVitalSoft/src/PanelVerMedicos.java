import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class PanelVerMedicos extends JPanel {
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblPagina;
    private JButton btnAnterior, btnSiguiente;

    // Configuración de Paginación
    private int paginaActual = 1;
    private final int FILAS_POR_PAGINA = 3;
    private MainFrame mainFrame;

    public PanelVerMedicos(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- MÁRGENES (AQUÍ ESTÁ EL CAMBIO) ---
        // Top: 20, Left: 60, Bottom: 20, Right: 60 (aprox 2 cm a los lados)
        setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        // --- Título ---
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelNorte.setBackground(Color.WHITE);
        JLabel titulo = new JLabel("Médicos Registrados");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        panelNorte.add(titulo);
        add(panelNorte, BorderLayout.NORTH);

        // --- Tabla ---
        String[] columnas = { "ID", "DNI", "Nombre y Apellido", "Especialidad", "Precio", "Días de Atención",
                "Horario" };
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(40); // Filas más altas
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // --- Botones de Control (Abajo) ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);

        btnAnterior = new JButton("Anterior");
        lblPagina = new JLabel("  1  ");
        lblPagina.setOpaque(true);
        lblPagina.setBackground(new Color(90, 200, 210)); // Color temático médico
        lblPagina.setForeground(Color.WHITE);
        btnSiguiente = new JButton("Siguiente");

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanel("SeleccionVer"));

        panelSur.add(btnAnterior);
        panelSur.add(lblPagina);
        panelSur.add(btnSiguiente);
        panelSur.add(Box.createHorizontalStrut(30)); // Espacio separador
        panelSur.add(btnRegresar);

        add(panelSur, BorderLayout.SOUTH);

        // --- Lógica de Botones ---
        cargarDatosTabla();

        btnAnterior.addActionListener(e -> {
            if (paginaActual > 1) {
                paginaActual--;
                cargarDatosTabla();
            }
        });

        btnSiguiente.addActionListener(e -> {
            int total = GestionClinica.listaMedicos.size();
            int maxPaginas = (int) Math.ceil((double) total / FILAS_POR_PAGINA);
            if (paginaActual < maxPaginas) {
                paginaActual++;
                cargarDatosTabla();
            }
        });
    }

    // --- Lógica para traer datos REALES ---
    public void cargarDatosTabla() {
        modelo.setRowCount(0); // Limpiar tabla
        ArrayList<Medico> lista = GestionClinica.listaMedicos;

        int total = lista.size();
        int inicio = (paginaActual - 1) * FILAS_POR_PAGINA;
        int fin = Math.min(inicio + FILAS_POR_PAGINA, total);

        for (int i = inicio; i < fin; i++) {
            Medico m = lista.get(i);
            int idVisual = i + 1;

            Object[] fila = {
                    idVisual,
                    m.getDni(),
                    m.getNombre() + " " + m.getApellido(),
                    m.getEspecialidad(),
                    "S/. " + m.getPrecioConsulta(),
                    m.getDiasAtencion(),
                    m.getHorario()
            };
            modelo.addRow(fila);
        }

        lblPagina.setText("  " + paginaActual + "  ");
        btnAnterior.setEnabled(paginaActual > 1);
        btnSiguiente.setEnabled(fin < total);
    }
}