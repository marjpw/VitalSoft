import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelAtenderCita extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Medico medico;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelAtenderCita(MainFrame mainFrame, ServiceManager services, Medico medico) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.medico = medico;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel lblTitulo = new JLabel("Atender Cita", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Century Gothic", Font.BOLD, 32));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // TABLA
        String[] columnas = { "ID", "Turno", "Paciente", "Motivo", "Acción" };
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.getTableHeader().setFont(new Font("Century Gothic", Font.BOLD, 14));

        // Botón Atender (Verde)
        tabla.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Regresar
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelMedico(medico));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        cargarCitasDeHoy();
    }

    private void cargarCitasDeHoy() {
        modelo.setRowCount(0);
        List<Cita> citas = services.getCitaService().getCitasMedico(medico.getId());
        LocalDate hoy = LocalDate.now();

        for (Cita c : citas) {
            // FILTRO: Solo HOY y solo PENDIENTES
            if (c.getFecha().equals(hoy) && c.getEstado() == EstadoCita.PENDIENTE) {
                modelo.addRow(new Object[] {
                        c.getId(), "#" + c.getNumeroTurno(),
                        c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos(),
                        c.getMotivo(), ""
                });
            }
        }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(46, 160, 140)); // Verde Turquesa Oscuro
            setForeground(Color.WHITE);
            setText("Atender");
            setFont(new Font("Century Gothic", Font.BOLD, 14));
        }

        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int fila;

        public ButtonEditor(JCheckBox cb) {
            super(cb);
            button = new JButton("Atender");
            button.setBackground(new Color(46, 160, 140));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> {
                fireEditingStopped();
                atenderCita(fila);
            });
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) {
            fila = r;
            return button;
        }

        public Object getCellEditorValue() {
            return "Atender";
        }
    }

    private void atenderCita(int fila) {
        int idCita = (int) modelo.getValueAt(fila, 0);
        Cita cita = services.getCitaService().getCitaPorId(idCita);
        if (cita != null) {
            mainFrame.mostrarPanelDetalleAtencion(cita);
        }
    }
}