import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class PanelFacturasPaciente extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Paciente paciente;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelFacturasPaciente(MainFrame mainFrame, ServiceManager services, Paciente paciente) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.paciente = paciente;

        setLayout(new GridBagLayout());

        // --- TARJETA PRINCIPAL ---
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(30, 50, 30, 50);
        add(tarjeta, gbc);

        // --- HEADER ---
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Mis Facturas Disponibles", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelNorte.add(lblTitulo);
        panelNorte.add(Box.createVerticalStrut(20));

        // Datos del Paciente (Estilo recuadro de tu imagen)
        JPanel panelDatos = new JPanel(new GridLayout(4, 2, 10, 5));
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(BorderFactory.createTitledBorder(null, "DATOS DEL PACIENTE",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12)));

        panelDatos.add(new JLabel("Paciente: " + paciente.getNombre() + " " + paciente.getApellidos()));
        panelDatos.add(new JLabel("DNI: " + paciente.getDni()));
        panelDatos.add(new JLabel("Fecha Nac: " + paciente.getFechaNacimiento()));
        panelDatos.add(new JLabel("Edad: " + paciente.getEdad()));

        panelNorte.add(panelDatos);
        panelNorte.add(Box.createVerticalStrut(20));
        tarjeta.add(panelNorte, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = { "ID Cita", "Fecha", "Médico", "Especialidad", "Motivo", "Acción" };
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) {
                return c == 5;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

        // Botón "Ver Factura"
        tabla.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
        tabla.getColumnModel().getColumn(5).setPreferredWidth(130);

        tarjeta.add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- REGRESAR ---
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
        tarjeta.add(panelSur, BorderLayout.SOUTH);

        cargarCitasAtendidas();
    }

    private void cargarCitasAtendidas() {
        modelo.setRowCount(0);
        List<Cita> historial = services.getCitaService().getHistorialPaciente(paciente.getId());

        for (Cita c : historial) {
            // FILTRO: Solo mostramos si ya fue ATENDIDO
            if (c.getEstado() == EstadoCita.ATENDIDO) {
                modelo.addRow(new Object[] {
                        c.getId(), c.getFecha(),
                        c.getMedico().getApellidos(),
                        c.getMedico().getEspecialidad(),
                        c.getMotivo(), ""
                });
            }
        }
    }

    // Renderizadores
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(93, 173, 226));
            setForeground(Color.WHITE);
            setText("Ver Factura");
            setFont(new Font("Arial", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int fila;

        public ButtonEditor(JCheckBox cb) {
            super(cb);
            button = new JButton("Ver Factura");
            button.setBackground(new Color(93, 173, 226));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> {
                fireEditingStopped();
                verFactura(fila);
            });
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) {
            fila = r;
            return button;
        }

        public Object getCellEditorValue() {
            return "Ver Factura";
        }
    }

    private void verFactura(int fila) {
        int idCita = (int) modelo.getValueAt(fila, 0);
        Cita c = services.getCitaService().getCitaPorId(idCita);
        if (c != null) {
            mainFrame.mostrarPanelDetalleFactura(c);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image img = new ImageIcon(getClass().getResource("/img/fondo_doctor.jpg")).getImage();
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (Exception e) {
        }
    }
}