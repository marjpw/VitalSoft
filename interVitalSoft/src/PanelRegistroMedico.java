import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRegistroMedico extends PanelFondoImagen {

    private MainFrame mainFrame;
    private ServiceManager services;
    private Administrador admin;
    private Medico medicoEditar; // Variable clave para saber si editamos

    private JTextField txtNombre, txtApellido, txtEspecialidad, txtDni, txtPrecio, txtPass;
    private JTextField txtHoraInicio, txtHoraFin;
    private JComboBox<String> cmbAmPmInicio, cmbAmPmFin;
    private JCheckBox[] diasChecks;
    private JLabel lblMensaje, lblTitulo;
    private BotonRedondo btnRegistrar;

    // Constructor 1: Para CREAR (Registro Nuevo) - Llama al segundo constructor
    // pasando null
    public PanelRegistroMedico(MainFrame mainFrame, ServiceManager services, Administrador admin) {
        this(mainFrame, services, admin, null);
    }

    // Constructor 2: Para EDITAR (Recibe el médico) - ESTE ES EL QUE TE FALTA
    public PanelRegistroMedico(MainFrame mainFrame, ServiceManager services, Administrador admin, Medico medicoEditar) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.admin = admin;
        this.medicoEditar = medicoEditar;

        initUI();

        if (medicoEditar != null) {
            cargarDatosParaEditar();
        }
    }

    private void initUI() {
        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TÍTULO CAMBIANTE
        String titulo = (medicoEditar == null) ? "Registrar Nuevo Médico" : "Editar Médico";
        lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        tarjeta.add(lblTitulo, gbc);

        // CAMPOS
        gbc.gridwidth = 1;

        tarjeta.add(crearLabel("Nombre:"), configGbc(0, 1));
        txtNombre = crearInput();
        tarjeta.add(txtNombre, configGbc(0, 2));

        tarjeta.add(crearLabel("Precio Consulta (S/.):"), configGbc(1, 1));
        txtPrecio = crearInput();
        tarjeta.add(txtPrecio, configGbc(1, 2));

        tarjeta.add(crearLabel("Apellido:"), configGbc(0, 3));
        txtApellido = crearInput();
        tarjeta.add(txtApellido, configGbc(0, 4));

        tarjeta.add(crearLabel("DNI:"), configGbc(1, 3));
        txtDni = crearInput();
        tarjeta.add(txtDni, configGbc(1, 4));

        tarjeta.add(crearLabel("Especialidad:"), configGbc(0, 5));
        txtEspecialidad = crearInput();
        tarjeta.add(txtEspecialidad, configGbc(0, 6));

        tarjeta.add(crearLabel("Contraseña:"), configGbc(1, 5));
        txtPass = crearInput();
        tarjeta.add(txtPass, configGbc(1, 6));

        // HORARIOS
        JLabel lblHorario = new JLabel("Horario de Consulta:");
        lblHorario.setFont(new Font("SansSerif", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10);
        tarjeta.add(lblHorario, gbc);

        JPanel panelDias = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelDias.setBackground(Color.WHITE);
        String[] dias = { "Lunes", "Martes", "Miércoles", "Jueves", "Viernes" };
        diasChecks = new JCheckBox[dias.length];
        for (int i = 0; i < dias.length; i++) {
            diasChecks[i] = new JCheckBox(dias[i]);
            diasChecks[i].setBackground(Color.WHITE);
            diasChecks[i].setFont(Estilos.FUENTE_NORMAL);
            panelDias.add(diasChecks[i]);
        }
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 5, 15, 5);
        tarjeta.add(panelDias, gbc);

        JPanel panelHoras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHoras.setBackground(Color.WHITE);
        panelHoras.add(new JLabel("Desde: "));
        txtHoraInicio = new JTextField(4);
        txtHoraInicio.setFont(Estilos.FUENTE_NORMAL);
        cmbAmPmInicio = new JComboBox<>(new String[] { "AM", "PM" });
        panelHoras.add(txtHoraInicio);
        panelHoras.add(cmbAmPmInicio);

        panelHoras.add(Box.createHorizontalStrut(30));

        panelHoras.add(new JLabel("Hasta: "));
        txtHoraFin = new JTextField(4);
        txtHoraFin.setFont(Estilos.FUENTE_NORMAL);
        cmbAmPmFin = new JComboBox<>(new String[] { "AM", "PM" });
        cmbAmPmFin.setSelectedIndex(1);
        panelHoras.add(txtHoraFin);
        panelHoras.add(cmbAmPmFin);

        gbc.gridy = 9;
        tarjeta.add(panelHoras, gbc);

        // BOTÓN ACCIÓN
        String btnTexto = (medicoEditar == null) ? "REGISTRAR MÉDICO" : "GUARDAR CAMBIOS";
        btnRegistrar = new BotonRedondo(btnTexto, Estilos.VERDE_MEDICO);
        btnRegistrar.setPreferredSize(new Dimension(250, 45));
        btnRegistrar.addActionListener(e -> accionGuardar());

        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(35, 10, 10, 10);
        tarjeta.add(btnRegistrar, gbc);

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(Color.WHITE);
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 10, 0, 10);
        tarjeta.add(lblMensaje, gbc);

        add(tarjeta);

        // BOTÓN REGRESAR
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }

        btnRegresar.addActionListener(e -> {
            if (medicoEditar != null) {
                // Si estamos editando, volver a la tabla de gestión
                mainFrame.mostrarPanelGestionMedicos();
            } else {
                // Si estamos creando nuevo, volver al menú admin
                mainFrame.mostrarPanelAdmin(admin);
            }
        });

        GridBagConstraints gbcFloat = new GridBagConstraints();
        gbcFloat.gridx = 0;
        gbcFloat.gridy = 0;
        gbcFloat.weightx = 1.0;
        gbcFloat.weighty = 1.0;
        gbcFloat.anchor = GridBagConstraints.SOUTHEAST;
        gbcFloat.insets = new Insets(0, 0, 30, 30);
        add(btnRegresar, gbcFloat);
    }

    private void cargarDatosParaEditar() {
        txtNombre.setText(medicoEditar.getNombre());
        txtApellido.setText(medicoEditar.getApellido());
        txtDni.setText(medicoEditar.getDni());
        txtEspecialidad.setText(medicoEditar.getEspecialidad());
        txtPrecio.setText(String.valueOf(medicoEditar.getPrecioConsulta()));
        txtPass.setText(medicoEditar.getPsw());

        String diasStr = medicoEditar.getDiasAtencion();
        for (JCheckBox cb : diasChecks) {
            if (diasStr.contains(cb.getText()))
                cb.setSelected(true);
        }
    }

    private void accionGuardar() {
        // Validaciones básicas
        if (txtNombre.getText().isEmpty() || txtDni.getText().isEmpty()) {
            mostrarMensaje("⚠️ Complete los campos requeridos.", new Color(255, 235, 238), Color.RED);
            return;
        }

        double precio = 0;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (Exception e) {
        }

        StringBuilder diasStr = new StringBuilder();
        for (JCheckBox cb : diasChecks)
            if (cb.isSelected())
                diasStr.append(cb.getText()).append(", ");
        if (diasStr.length() > 2)
            diasStr.setLength(diasStr.length() - 2);

        String horarioStr = txtHoraInicio.getText() + cmbAmPmInicio.getSelectedItem() + " - " +
                txtHoraFin.getText() + cmbAmPmFin.getSelectedItem();

        if (medicoEditar == null) {
            // REGISTRO NUEVO
            services.getMedicoService().registrarMedico(txtNombre.getText(), txtApellido.getText(),
                    txtEspecialidad.getText(), txtDni.getText(),
                    precio, diasStr.toString(), horarioStr, txtPass.getText());
            mostrarMensaje("✅ Médico registrado correctamente", new Color(200, 230, 201), new Color(27, 94, 32));
            limpiarCampos();
        } else {
            // ACTUALIZACIÓN
            services.getMedicoService().actualizarMedico(medicoEditar.getId(), txtNombre.getText(),
                    txtApellido.getText(),
                    txtEspecialidad.getText(), txtDni.getText(),
                    precio, diasStr.toString(), horarioStr, txtPass.getText());
            mostrarMensaje("✅ Cambios guardados.", new Color(200, 230, 201), new Color(27, 94, 32));
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtEspecialidad.setText("");
        txtPrecio.setText("");
        txtPass.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        for (JCheckBox cb : diasChecks)
            cb.setSelected(false);
    }

    // Helpers visuales
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

    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }

    private JTextField crearInput() {
        JTextField txt = new JTextField(15);
        txt.setPreferredSize(new Dimension(15, 30));
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        return txt;
    }

    private void mostrarMensaje(String texto, Color fondo, Color letra) {
        lblMensaje.setText(" " + texto + " ");
        lblMensaje.setBackground(fondo);
        lblMensaje.setForeground(letra);
    }
}