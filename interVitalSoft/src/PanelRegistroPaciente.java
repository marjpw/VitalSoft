import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class PanelRegistroPaciente extends PanelFondoImagen {

    @SuppressWarnings("unused")
    private MainFrame mainFrame;
    private ServiceManager services;
    @SuppressWarnings("unused")
    private Administrador admin;

    private JTextField txtNombre, txtApellido, txtDni, txtCelular, txtAlergias, txtPass;
    private JComboBox<String> cmbDia, cmbMes, cmbAnio;
    private JLabel lblMensaje;

    public PanelRegistroPaciente(MainFrame mainFrame, ServiceManager services, Administrador admin) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.admin = admin;

        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Registrar Nuevo Paciente");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        tarjeta.add(lblTitulo, gbc);

        // Campos
        gbc.gridwidth = 1;

        tarjeta.add(new JLabel("Nombre:"), configGbc(0, 1));
        txtNombre = crearInput();
        tarjeta.add(txtNombre, configGbc(0, 2));

        tarjeta.add(new JLabel("Alergias:"), configGbc(1, 1));
        txtAlergias = crearInput();
        tarjeta.add(txtAlergias, configGbc(1, 2));

        tarjeta.add(new JLabel("Apellido:"), configGbc(0, 3));
        txtApellido = crearInput();
        tarjeta.add(txtApellido, configGbc(0, 4));

        tarjeta.add(new JLabel("Contraseña:"), configGbc(1, 3));
        txtPass = crearInput();
        tarjeta.add(txtPass, configGbc(1, 4));

        tarjeta.add(new JLabel("DNI:"), configGbc(0, 5));
        txtDni = crearInput();
        tarjeta.add(txtDni, configGbc(0, 6));

        tarjeta.add(new JLabel("N° de Celular:"), configGbc(1, 5));
        txtCelular = crearInput();
        tarjeta.add(txtCelular, configGbc(1, 6));

        // --- FECHA DE NACIMIENTO (COMBOS) ---
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10);
        tarjeta.add(new JLabel("Fecha de Nacimiento:"), gbc);

        JPanel panelFecha = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelFecha.setBackground(Color.WHITE);

        // Día (1-31)
        cmbDia = new JComboBox<>();
        for (int i = 1; i <= 31; i++)
            cmbDia.addItem(String.format("%02d", i));

        // Mes (Nombres)
        String[] meses = { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre",
                "Octubre", "Noviembre", "Diciembre" };
        cmbMes = new JComboBox<>(meses);

        // Año (2025 hacia atrás hasta 1900)
        cmbAnio = new JComboBox<>();
        int anioActual = LocalDate.now().getYear();
        for (int i = anioActual; i >= 1900; i--)
            cmbAnio.addItem(String.valueOf(i));

        panelFecha.add(cmbDia);
        panelFecha.add(cmbMes);
        panelFecha.add(cmbAnio);

        gbc.gridy = 8;
        tarjeta.add(panelFecha, gbc);

        // Botón Registrar
        BotonRedondo btnRegistrar = new BotonRedondo("REGISTRAR PACIENTE", Estilos.TURQUESA_ADMIN); // Color diferente
                                                                                                    // al del médico
        btnRegistrar.setPreferredSize(new Dimension(200, 40));
        btnRegistrar.addActionListener(e -> accionRegistrar());

        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 10, 10, 10);
        tarjeta.add(btnRegistrar, gbc);

        // Mensaje
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(Color.WHITE);
        gbc.gridy = 10;
        tarjeta.add(lblMensaje, gbc);

        add(tarjeta);

        // Botón Regresar
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelAdmin(admin));

        GridBagConstraints gbcFloat = new GridBagConstraints();
        gbcFloat.gridx = 0;
        gbcFloat.gridy = 0;
        gbcFloat.weightx = 1.0;
        gbcFloat.weighty = 1.0;
        gbcFloat.anchor = GridBagConstraints.SOUTHEAST;
        gbcFloat.insets = new Insets(0, 0, 20, 20);
        add(btnRegresar, gbcFloat);
    }

    private void accionRegistrar() {
        resetBorde(txtNombre);
        resetBorde(txtApellido);
        resetBorde(txtDni);
        resetBorde(txtCelular);
        resetBorde(txtPass);
        lblMensaje.setText(" ");
        lblMensaje.setBackground(Color.WHITE);

        boolean valido = true;
        if (esVacio(txtNombre))
            valido = false;
        if (esVacio(txtApellido))
            valido = false;
        if (esVacio(txtDni))
            valido = false;
        if (esVacio(txtCelular))
            valido = false;
        if (esVacio(txtPass))
            valido = false;

        if (!valido) {
            mostrarMensaje("⚠️ Complete los campos rojos.", new Color(255, 235, 238), Color.RED);
            return;
        }

        // Armar Fecha
        int dia = Integer.parseInt((String) cmbDia.getSelectedItem());
        int mes = cmbMes.getSelectedIndex() + 1; // Enero es 0, sumamos 1
        int anio = Integer.parseInt((String) cmbAnio.getSelectedItem());
        LocalDate nacimiento = LocalDate.of(anio, mes, dia);

        // Alergias opcional
        String alergias = txtAlergias.getText().trim();
        if (alergias.isEmpty())
            alergias = "Ninguna";

        services.getPacienteService().registrarPaciente(
                txtNombre.getText(),
                txtApellido.getText(),
                txtDni.getText(),
                txtCelular.getText(),
                nacimiento,
                alergias,
                txtPass.getText());

        mostrarMensaje("✅ Paciente registrado correctamente", new Color(200, 230, 201), new Color(27, 94, 32));

        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtCelular.setText("");
    }

    // --- Helpers reutilizados ---
    private JTextField crearInput() {
        JTextField txt = new JTextField(15);
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return txt;
    }

    private boolean esVacio(JTextField txt) {
        if (txt.getText().trim().isEmpty()) {
            marcarError(txt);
            return true;
        }
        return false;
    }

    private void marcarError(JTextField txt) {
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void resetBorde(JTextField txt) {
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }

    private void mostrarMensaje(String texto, Color fondo, Color letra) {
        lblMensaje.setText(" " + texto + " ");
        lblMensaje.setBackground(fondo);
        lblMensaje.setForeground(letra);
    }

    private GridBagConstraints configGbc(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 10, 5, 10);
        return c;
    }
}