import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PanelGestionMedicos extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Administrador admin;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelGestionMedicos(MainFrame mainFrame, ServiceManager services, Administrador admin) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.admin = admin;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));

        // --- ENCABEZADO ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(Color.WHITE);

        int cantidad = services.getMedicoService().getListaMedicos().size();
        JLabel lblContador = new JLabel(cantidad + " Médicos Registrados");
        lblContador.setFont(new Font("Arial", Font.PLAIN, 16));
        lblContador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JLabel lblTitulo = new JLabel("Médicos Registrados", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));

        panelNorte.add(lblContador, BorderLayout.WEST);
        panelNorte.add(lblTitulo, BorderLayout.CENTER);
        panelNorte.add(Box.createHorizontalStrut(150), BorderLayout.EAST);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        add(panelNorte, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = { "ID", "DNI", "Nombre y Apellido", "Especialidad", "Precio", "Días", "Horario", "Acción" };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // --- AQUÍ ESTÁ EL CAMBIO DE ANCHOS ---
        TableColumnModel columnModel = tabla.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(30); // ID (Muy angosto)
        columnModel.getColumn(1).setPreferredWidth(80); // DNI
        columnModel.getColumn(2).setPreferredWidth(250); // NOMBRE (Muy ancho para apellidos largos)
        columnModel.getColumn(3).setPreferredWidth(120); // Especialidad
        columnModel.getColumn(4).setPreferredWidth(60); // Precio (Angosto)
        columnModel.getColumn(5).setPreferredWidth(100); // Días
        columnModel.getColumn(6).setPreferredWidth(100); // Horario
        columnModel.getColumn(7).setPreferredWidth(160); // Acción (Botones)
        // -------------------------------------

        // Renderizador y Editor para botones
        columnModel.getColumn(7).setCellRenderer(new BotonesRenderer());
        columnModel.getColumn(7).setCellEditor(new BotonesEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(tabla);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTÓN REGRESAR ---
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelAdmin(admin));

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        panelSur.add(btnRegresar);
        add(panelSur, BorderLayout.SOUTH);

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        ArrayList<Medico> lista = services.getMedicoService().getListaMedicos();
        for (Medico m : lista) {
            Object[] fila = {
                    m.getId(), m.getDni(), m.getNombre() + " " + m.getApellido(),
                    m.getEspecialidad(), "S/. " + m.getPrecioConsulta(),
                    m.getDiasAtencion(), m.getHorarioHora(), ""
            };
            modelo.addRow(fila);
        }
    }

    // CLASES INTERNAS PARA LOS BOTONES
    class BotonesRenderer extends JPanel implements TableCellRenderer {
        private JLabel btnEditar = new JLabel();
        private JLabel btnEliminar = new JLabel();

        public BotonesRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setOpaque(true);
            setBackground(Color.WHITE);
            try {
                btnEditar.setIcon(new ImageIcon(getClass().getResource("/img/btn_editar_full.png")));
                btnEliminar.setIcon(new ImageIcon(getClass().getResource("/img/btn_eliminar_full.png")));
            } catch (Exception e) {
                btnEditar.setText("[EDIT]");
                btnEliminar.setText("[DEL]");
            }
            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isS, boolean hasF, int r, int c) {
            if (isS)
                setBackground(t.getSelectionBackground());
            else
                setBackground(t.getBackground());
            return this;
        }
    }

    class BotonesEditor extends DefaultCellEditor {
        private JPanel panel;
        private JLabel btnEditar, btnEliminar;
        private int filaActual;

        public BotonesEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);
            btnEditar = new JLabel();
            btnEliminar = new JLabel();
            try {
                btnEditar.setIcon(new ImageIcon(getClass().getResource("/img/btn_editar_full.png")));
                btnEliminar.setIcon(new ImageIcon(getClass().getResource("/img/btn_eliminar_full.png")));
            } catch (Exception e) {
                btnEditar.setText("EDIT");
                btnEliminar.setText("DEL");
            }

            btnEditar.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    editarFila(filaActual);
                    fireEditingStopped();
                }
            });

            btnEliminar.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    eliminarFila(filaActual);
                    fireEditingStopped();
                }
            });
            panel.add(btnEditar);
            panel.add(btnEliminar);
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v, boolean isS, int r, int c) {
            filaActual = r;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    // ACCIONES
    private void eliminarFila(int fila) {
        int idMedico = (int) modelo.getValueAt(fila, 0);
        String nombre = (String) modelo.getValueAt(fila, 2);

        DialogoConfirmacion dialog = new DialogoConfirmacion(mainFrame, nombre);
        dialog.setVisible(true);

        if (dialog.fueConfirmado()) {
            services.getMedicoService().eliminarMedico(idMedico);
            cargarDatos();
        }
    }

    private void editarFila(int fila) {
        int idMedico = (int) modelo.getValueAt(fila, 0);
        Medico medicoSeleccionado = null;
        for (Medico m : services.getMedicoService().getListaMedicos()) {
            if (m.getId() == idMedico) {
                medicoSeleccionado = m;
                break;
            }
        }
        if (medicoSeleccionado != null) {
            mainFrame.mostrarPanelRegistroMedico(medicoSeleccionado);
        }
    }
}