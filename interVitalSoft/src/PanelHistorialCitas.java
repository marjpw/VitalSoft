import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class PanelHistorialCitas extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Paciente paciente;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelHistorialCitas(MainFrame mainFrame, ServiceManager services, Paciente paciente) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.paciente = paciente;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Título
        JLabel lblTitulo = new JLabel("Mi Historial de Citas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        add(lblTitulo, BorderLayout.NORTH);

        // Tabla
        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelFiltros.setBackground(Color.WHITE);
        JButton btnPendientes = new JButton("Citas Pendientes");
        JButton btnHistorial = new JButton("Historial Completo");
        panelFiltros.add(btnPendientes);
        panelFiltros.add(btnHistorial);
        panelCentro.add(panelFiltros, BorderLayout.NORTH);

        String[] columnas = { "ID", "Estado", "Fecha", "Médico", "Especialidad" };
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Anchos
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120); // Estado
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200); // Médico Ancho
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Renderizador de ESTADO (Colores)
        tabla.getColumnModel().getColumn(1).setCellRenderer(new EstadoRenderer());

        JScrollPane scroll = new JScrollPane(tabla);
        panelCentro.add(scroll, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Botón Regresar
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelPaciente(paciente));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Cita> historial = services.getCitaService().getHistorialPaciente(paciente.getId());

        for (Cita c : historial) {
            Object[] fila = {
                    c.getId(),
                    c.getEstado(), // Pasamos el objeto Enum, el Renderizador se encarga del color
                    c.getFecha().toString(),
                    c.getMedico().getNombre() + " " + c.getMedico().getApellido(),
                    c.getMedico().getEspecialidad()
            };
            modelo.addRow(fila);
        }
    }

    class EstadoRenderer extends JLabel implements TableCellRenderer {
        public EstadoRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Arial", Font.BOLD, 12));
            setForeground(Color.WHITE); // Texto blanco
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (value instanceof EstadoCita) {
                EstadoCita estado = (EstadoCita) value;
                setText(estado.toString());

                switch (estado) {
                    case PENDIENTE:
                        setBackground(new Color(243, 156, 18)); // Naranja
                        break;
                    case ATENDIDO:
                        setBackground(new Color(46, 204, 113)); // Verde Teal
                        break;
                    case CANCELADO:
                        setBackground(new Color(231, 76, 60)); // Rojo
                        break;
                    default:
                        setBackground(Color.GRAY);
                }
            }
            return this;
        }
    }
}