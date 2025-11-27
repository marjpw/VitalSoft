import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class PanelCaja extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Administrador admin;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JLabel lblTotalRecaudado;

    public PanelCaja(MainFrame mainFrame, ServiceManager services, Administrador admin) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.admin = admin;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // --- HEADER ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Gestión de Caja y Cobros", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JLabel lblSub = new JLabel("Citas Atendidas Pendientes de Pago", SwingConstants.LEFT);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSub.setForeground(Color.GRAY);
        panelNorte.add(lblSub, BorderLayout.SOUTH);

        panelNorte.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(panelNorte, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = { "ID Cita", "Paciente", "Médico", "Monto (S/.)", "Acción" };
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Anchos
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120);

        // Botón Cobrar
        tabla.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- FOOTER (Total y Regresar) ---
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Recuadro de Total
        JPanel panelTotal = new JPanel();
        panelTotal.setBackground(new Color(240, 248, 255)); // Azul muy claro
        panelTotal.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237), 2));

        lblTotalRecaudado = new JLabel("$$ DINERO EN CAJA: S/. 0.00 $$");
        lblTotalRecaudado.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblTotalRecaudado.setForeground(new Color(25, 25, 112)); // Azul oscuro
        panelTotal.add(lblTotalRecaudado);

        panelSur.add(panelTotal, BorderLayout.WEST);

        // Botón Regresar
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelAdmin(admin));

        JPanel pBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pBtn.setBackground(Color.WHITE);
        pBtn.add(btnRegresar);

        panelSur.add(pBtn, BorderLayout.EAST);
        add(panelSur, BorderLayout.SOUTH);

        cargarDatos();
        actualizarTotal();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        // Usamos el método que ya tienes en CitaService: getCitasPendientesDePago()
        List<Cita> pendientes = services.getCitaService().getCitasPendientesDePago();

        for (Cita c : pendientes) {
            modelo.addRow(new Object[] {
                    c.getId(),
                    c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos(),
                    c.getMedico().getApellidos(),
                    String.format("%.2f", c.getMedico().getPrecioConsulta()),
                    "" // Botón
            });
        }
    }

    private void actualizarTotal() {
        double total = services.getFacturaService().calcularTotalRecaudado();
        lblTotalRecaudado.setText("$$ DINERO EN CAJA: S/. " + String.format("%.2f", total) + " $$");
    }

    // --- BOTÓN COBRAR (VERDE) ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(46, 204, 113)); // Verde Esmeralda
            setForeground(Color.WHITE);
            setText("COBRAR");
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
            button = new JButton("COBRAR");
            button.setBackground(new Color(46, 204, 113));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> {
                fireEditingStopped();
                cobrarCita(fila);
            });
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) {
            fila = r;
            return button;
        }

        public Object getCellEditorValue() {
            return "COBRAR";
        }
    }

    private void cobrarCita(int fila) {
        int idCita = (int) modelo.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Confirmar pago de la cita #" + idCita + "?",
                "Cobrar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Generar la factura en el sistema
            services.getFacturaService().generarFactura(idCita);

            // Refrescar la interfaz
            cargarDatos(); // Quita la fila de la tabla
            actualizarTotal(); // Suma el dinero abajo

            JOptionPane.showMessageDialog(this, "¡Cobro registrado correctamente!");
        }
    }
}