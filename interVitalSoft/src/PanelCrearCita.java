import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PanelCrearCita extends PanelFondoImagen {

    @SuppressWarnings("unused")
    private MainFrame mainFrame;
    private ServiceManager services;
    private Paciente paciente;

    private JComboBox<String> cmbEspecialidad;
    private JComboBox<Medico> cmbMedico;
    private JTextField txtHorario, txtPrecio, txtMotivo;
    private JComboBox<String> cmbDia, cmbMes, cmbAnio;
    private JLabel lblMensaje;

    public PanelCrearCita(MainFrame mainFrame, ServiceManager services, Paciente paciente) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.paciente = paciente;

        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TÍTULO
        JLabel lblTitulo = new JLabel("Solicita Nueva Cita");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        tarjeta.add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        // --- FILA 1: Especialidad y Médico ---
        tarjeta.add(crearLabel("Especialidad:"), configGbc(0, 1));
        cmbEspecialidad = new JComboBox<>();
        cargarEspecialidades();
        tarjeta.add(cmbEspecialidad, configGbc(0, 2));

        tarjeta.add(crearLabel("Médico:"), configGbc(1, 1));
        cmbMedico = new JComboBox<>();
        tarjeta.add(cmbMedico, configGbc(1, 2));

        // Lógica de actualización
        cmbEspecialidad.addActionListener(e -> cargarMedicos((String) cmbEspecialidad.getSelectedItem()));
        cmbMedico.addActionListener(e -> mostrarInfoMedico());

        // --- FILA 2: Horario y Precio ---
        tarjeta.add(crearLabel("Horario (Informativo):"), configGbc(0, 3));
        txtHorario = crearInput();
        txtHorario.setEditable(false);
        txtHorario.setBackground(new Color(245, 245, 245));
        tarjeta.add(txtHorario, configGbc(0, 4));

        tarjeta.add(crearLabel("Precio (S/.):"), configGbc(1, 3));
        txtPrecio = crearInput();
        txtPrecio.setEditable(false);
        txtPrecio.setBackground(new Color(245, 245, 245));
        tarjeta.add(txtPrecio, configGbc(1, 4));

        // --- FILA 3: Fecha y Motivo ---
        tarjeta.add(crearLabel("Fecha Deseada:"), configGbc(0, 5));

        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelFecha.setBackground(Color.WHITE);

        cmbDia = new JComboBox<>();
        for (int i = 1; i <= 31; i++)
            cmbDia.addItem(String.format("%02d", i));

        String[] meses = { "Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic" };
        cmbMes = new JComboBox<>(meses);

        cmbAnio = new JComboBox<>();
        int anioActual = LocalDate.now().getYear();
        cmbAnio.addItem(String.valueOf(anioActual));
        cmbAnio.addItem(String.valueOf(anioActual + 1));

        panelFecha.add(cmbDia);
        panelFecha.add(cmbMes);
        panelFecha.add(cmbAnio);
        tarjeta.add(panelFecha, configGbc(0, 6));

        tarjeta.add(crearLabel("Motivo de Consulta:"), configGbc(1, 5));
        txtMotivo = crearInput();
        tarjeta.add(txtMotivo, configGbc(1, 6));

        // --- BOTÓN CREAR ---
        BotonRedondo btnCrear = new BotonRedondo("AGENDAR CITA", Estilos.TURQUESA_ADMIN);
        btnCrear.setPreferredSize(new Dimension(200, 45));
        btnCrear.addActionListener(e -> accionCrearCita());

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 0, 10, 0);
        tarjeta.add(btnCrear, gbc);

        // MENSAJE
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(Color.WHITE);
        gbc.gridy = 8;
        tarjeta.add(lblMensaje, gbc);

        add(tarjeta);

        // Botón Regresar
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new PanelPaciente(mainFrame, services, paciente));
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        GridBagConstraints gbcFloat = new GridBagConstraints();
        gbcFloat.gridx = 0;
        gbcFloat.gridy = 0;
        gbcFloat.weightx = 1.0;
        gbcFloat.weighty = 1.0;
        gbcFloat.anchor = GridBagConstraints.SOUTHEAST;
        gbcFloat.insets = new Insets(0, 0, 30, 30);
        add(btnRegresar, gbcFloat);

        if (cmbEspecialidad.getItemCount() > 0)
            cmbEspecialidad.setSelectedIndex(0);
    }

    // --- LÓGICA ---
    private void cargarEspecialidades() {
        cmbEspecialidad.removeAllItems();
        for (String esp : services.getMedicoService().obtenerEspecialidades())
            cmbEspecialidad.addItem(esp);
    }

    private void cargarMedicos(String especialidad) {
        cmbMedico.removeAllItems();
        if (especialidad == null)
            return;
        List<Medico> medicos = services.getMedicoService().obtenerMedicosPorEspecialidad(especialidad);
        for (Medico m : medicos)
            cmbMedico.addItem(m);
    }

    private void mostrarInfoMedico() {
        Medico m = (Medico) cmbMedico.getSelectedItem();
        if (m != null) {
            txtPrecio.setText(String.valueOf(m.getPrecioConsulta()));
            txtHorario.setText(m.getDiasAtencion() + " (" + m.getHorarioHora() + ")");
        } else {
            txtPrecio.setText("");
            txtHorario.setText("");
        }
    }

    private void accionCrearCita() {
        lblMensaje.setText(" ");
        lblMensaje.setBackground(Color.WHITE);
        Medico medico = (Medico) cmbMedico.getSelectedItem();
        String motivo = txtMotivo.getText();

        if (medico == null || motivo.trim().isEmpty()) {
            mostrarMensaje("⚠️ Faltan datos.", new Color(255, 235, 238), Color.RED);
            return;
        }

        int dia = Integer.parseInt((String) cmbDia.getSelectedItem());
        int mes = cmbMes.getSelectedIndex() + 1;
        int anio = Integer.parseInt((String) cmbAnio.getSelectedItem());
        LocalDate fechaCita = LocalDate.of(anio, mes, dia);

        if (fechaCita.isBefore(LocalDate.now())) {
            mostrarMensaje("❌ La fecha no puede ser pasada.", new Color(255, 235, 238), Color.RED);
            return;
        }

        services.getCitaService().crearCita(paciente, medico, fechaCita, motivo);
        mostrarMensaje("✅ Cita Creada Correctamente", new Color(200, 230, 201), new Color(27, 94, 32));
        txtMotivo.setText("");
    }

    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(Color.GRAY);
        return l;
    }

    private JTextField crearInput() {
        JTextField t = new JTextField(15);
        t.setPreferredSize(new Dimension(15, 30));
        t.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return t;
    }

    private GridBagConstraints configGbc(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        if (y == 1 || y == 3 || y == 5)
            c.insets = new Insets(15, 15, 5, 15);
        else
            c.insets = new Insets(0, 15, 5, 15);
        return c;
    }

    private void mostrarMensaje(String t, Color f, Color l) {
        lblMensaje.setText(" " + t + " ");
        lblMensaje.setBackground(f);
        lblMensaje.setForeground(l);
    }
}