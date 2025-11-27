import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class PanelCancelarCitas extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Paciente paciente;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelCancelarCitas(MainFrame mainFrame, ServiceManager services, Paciente paciente) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.paciente = paciente;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50)); // Márgenes amplios

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Cancelar Citas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        add(lblTitulo, BorderLayout.NORTH);

        // --- CONTENEDOR CENTRAL (Contador + Tabla) ---
        JPanel panelCentro = new JPanel(new BorderLayout(0, 10));
        panelCentro.setBackground(Color.WHITE);
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Contador
        JLabel lblContador = new JLabel("Citas Registradas");
        lblContador.setFont(new Font("Arial", Font.PLAIN, 14));
        lblContador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        JPanel panelContador = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelContador.setBackground(Color.WHITE);
        panelContador.add(lblContador);
        panelCentro.add(panelContador, BorderLayout.NORTH);

        // Tabla
        String[] columnas = { "ID", "Fecha", "Médico", "Especialidad", "Acción" };
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(45); // Filas altas
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(240, 245, 249));

        // Anchos de columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40); // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(100); // Fecha
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200); // Médico
        tabla.getColumnModel().getColumn(3).setPreferredWidth(150); // Especialidad
        tabla.getColumnModel().getColumn(4).setPreferredWidth(120); // Acción

        // Renderizador de Botón Cancelar
        tabla.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scroll = new JScrollPane(tabla);
        panelCentro.add(scroll, BorderLayout.CENTER);

        add(panelCentro, BorderLayout.CENTER);

        // --- BOTÓN REGRESAR ---
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelPaciente(paciente)); // Volver al menú paciente

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Cita> historial = services.getCitaService().getHistorialPaciente(paciente.getId());

        for (Cita c : historial) {
            if (c.getEstado() == EstadoCita.PENDIENTE) {
                Object[] fila = {
                        c.getId(),
                        c.getFecha().toString(),
                        c.getMedico().getNombre() + " " + c.getMedico().getApellido(),
                        c.getMedico().getEspecialidad(),
                        "" // Botón
                };
                modelo.addRow(fila);
            }
        }
    }

    // BOTÓN CANCELAR (ROJO) ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(255, 99, 71)); // Rojo coral
            setForeground(Color.WHITE);
            setText("Cancelar");
            setFont(new Font("Arial", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    // BOTÓN CANCELAR ---
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int filaActual;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Cancelar");
            button.setOpaque(true);
            button.setBackground(new Color(255, 99, 71));
            button.setForeground(Color.WHITE);

            button.addActionListener(e -> {
                fireEditingStopped();
                confirmarCancelacion(filaActual);
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            filaActual = row;
            return button;
        }

        public Object getCellEditorValue() {
            return "Cancelar";
        }
    }

    private void confirmarCancelacion(int fila) {
        DialogoConfirmarCita dialog = new DialogoConfirmarCita(mainFrame);
        dialog.setVisible(true);

        if (dialog.fueConfirmado()) {
            int idCita = (int) modelo.getValueAt(fila, 0);
            services.getCitaService().cancelarCita(idCita, paciente);
            cargarDatos();
        }
    }
}