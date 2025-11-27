import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PanelSeleccionPaciente extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Medico medico;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> cmbOrden;

    public PanelSeleccionPaciente(MainFrame mainFrame, ServiceManager services, Medico medico) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.medico = medico;

        setLayout(new BorderLayout());

        // --- FONDO (Imagen del doctor) ---
        // Usamos un panel interno transparente para el contenido,
        // pero pintaremos el fondo en este panel principal

        // Contenedor central blanco (Tarjeta)
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Para centrar la tarjeta, usamos GridBagLayout en el panel principal
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(40, 60, 40, 60); // Margen exterior para ver el fondo
        add(tarjeta, gbc);

        // --- CONTENIDO DE LA TARJETA ---

        // Título y Filtro
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(Color.WHITE);
        JLabel lblTitulo = new JLabel("PACIENTES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBackground(Color.WHITE);
        panelFiltro.add(new JLabel("Ordenar por: "));
        cmbOrden = new JComboBox<>(new String[] { "ID Paciente", "Nombre (A-Z)", "DNI" });
        cmbOrden.setPreferredSize(new Dimension(150, 30));
        cmbOrden.addActionListener(e -> cargarDatos());
        panelFiltro.add(cmbOrden);

        panelNorte.add(panelFiltro, BorderLayout.SOUTH);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        tarjeta.add(panelNorte, BorderLayout.NORTH);

        // Tabla
        String[] columnas = { "ID Paciente", "DNI", "Paciente", "Acción" };
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 3;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(45);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Botón Seleccionar (Verde)
        tabla.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox()));
        tabla.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scroll = new JScrollPane(tabla);
        tarjeta.add(scroll, BorderLayout.CENTER);

        // Botón Regresar
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
        tarjeta.add(panelSur, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Paciente> pacientes = services.getCitaService().getPacientesDeMedico(medico.getId());

        // Ordenar
        String orden = (String) cmbOrden.getSelectedItem();
        if (orden.equals("Nombre (A-Z)")) {
            pacientes.sort(Comparator.comparing(Paciente::getNombre));
        } else if (orden.equals("DNI")) {
            pacientes.sort(Comparator.comparing(Paciente::getDni));
        } else {
            pacientes.sort(Comparator.comparingInt(Paciente::getId));
        }

        for (Paciente p : pacientes) {
            modelo.addRow(new Object[] {
                    p.getId(), p.getDni(), p.getNombre() + " " + p.getApellidos(), ""
            });
        }
    }

    // --- RENDERIZADOR BOTÓN ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(46, 160, 140));
            setForeground(Color.WHITE);
            setText("Seleccionar");
            setFont(new Font("Arial", Font.BOLD, 12));
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
            button = new JButton("Seleccionar");
            button.setBackground(new Color(46, 160, 140));
            button.setForeground(Color.WHITE);
            button.addActionListener(e -> {
                fireEditingStopped();
                seleccionarPaciente(fila);
            });
        }

        public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) {
            fila = r;
            return button;
        }

        public Object getCellEditorValue() {
            return "Seleccionar";
        }
    }

    private void seleccionarPaciente(int fila) {
        int idPac = (int) modelo.getValueAt(fila, 0);
        // Buscar objeto paciente
        // Nota: Podríamos optimizar buscando en la lista local, pero usaremos el
        // servicio general
        // Asumimos que PacienteService tiene un metodo getPacientePorId, si no,
        // iteramos la lista local.
        Paciente pSeleccionado = null;
        for (Paciente p : services.getCitaService().getPacientesDeMedico(medico.getId())) {
            if (p.getId() == idPac) {
                pSeleccionado = p;
                break;
            }
        }

        if (pSeleccionado != null) {
            mainFrame.mostrarPanelHistorialClinico(medico, pSeleccionado);
        }
    }

    // --- FONDO ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image img = new ImageIcon(getClass().getResource("/img/fondo_doctor.jpg")).getImage();
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 150)); // Oscurecer fondo
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (Exception e) {
        }
    }
}