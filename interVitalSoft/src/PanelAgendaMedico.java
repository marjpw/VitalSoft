import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class PanelAgendaMedico extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Medico medico;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JComboBox<String> cmbFiltro;

    public PanelAgendaMedico(MainFrame mainFrame, ServiceManager services, Medico medico) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.medico = medico;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // --- TÍTULO Y FILTRO ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Agenda", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        panelNorte.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.setBackground(Color.WHITE);
        panelFiltro.add(new JLabel("Filtrar por: "));

        cmbFiltro = new JComboBox<>(new String[] { "Todas las citas", "Pendientes", "Atendidas", "Canceladas" });
        cmbFiltro.setPreferredSize(new Dimension(200, 30));
        cmbFiltro.addActionListener(e -> cargarDatos()); // Recargar al cambiar
        panelFiltro.add(cmbFiltro);

        panelNorte.add(panelFiltro, BorderLayout.SOUTH);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        add(panelNorte, BorderLayout.NORTH);

        // --- TABLA ---
        String[] columnas = { "ID", "Estado", "Fecha", "Paciente", "Motivo" };
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        // Anchos
        tabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(120); // Estado
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200); // Paciente ancho
        tabla.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Colores de estado (Reutilizamos la lógica visual)
        tabla.getColumnModel().getColumn(1).setCellRenderer(new EstadoRenderer());

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // --- BOTÓN REGRESAR ---
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

        cargarDatos();
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        // Obtenemos citas del médico desde el servicio (Necesitas tener este método o
        // similar)
        // Si no tienes getCitasMedico(id), filtraremos manual aqui
        List<Cita> todas = services.getCitaService().getHistorialMedico(medico.getId());

        String filtro = (String) cmbFiltro.getSelectedItem();

        for (Cita c : todas) {
            boolean mostrar = false;
            if (filtro.equals("Todas las citas"))
                mostrar = true;
            else if (filtro.equals("Pendientes") && c.getEstado() == EstadoCita.PENDIENTE)
                mostrar = true;
            else if (filtro.equals("Atendidas") && c.getEstado() == EstadoCita.ATENDIDO)
                mostrar = true;
            else if (filtro.equals("Canceladas") && c.getEstado() == EstadoCita.CANCELADO)
                mostrar = true;

            if (mostrar) {
                modelo.addRow(new Object[] {
                        c.getId(), c.getEstado(), c.getFecha(),
                        c.getPaciente().getNombre() + " " + c.getPaciente().getApellidos(),
                        c.getMotivo()
                });
            }
        }
    }

    // Clase interna para colorear estados (Copia exacta de la del paciente)
    class EstadoRenderer extends JLabel implements TableCellRenderer {
        public EstadoRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setForeground(Color.WHITE);
            font(new Font("Arial", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
            if (v instanceof EstadoCita) {
                EstadoCita e = (EstadoCita) v;
                setText(e.toString());
                if (e == EstadoCita.PENDIENTE)
                    setBackground(new Color(243, 156, 18));
                else if (e == EstadoCita.ATENDIDO)
                    setBackground(new Color(46, 204, 113));
                else
                    setBackground(new Color(231, 76, 60));
            }
            return this;
        }

        private void font(Font f) {
            setFont(f);
        }
    }
}
