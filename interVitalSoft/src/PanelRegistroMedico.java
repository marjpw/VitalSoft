import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PanelRegistroMedico extends PanelFondoImagen {

    @SuppressWarnings("unused")
    private MainFrame mainFrame;
    private ServiceManager services;
    @SuppressWarnings("unused")
    private Administrador admin;

    private JTextField txtNombre, txtApellido, txtEspecialidad, txtDni, txtPrecio, txtPass;
    private JTextField txtHoraInicio, txtHoraFin;
    private JComboBox<String> cmbAmPmInicio, cmbAmPmFin;
    private JCheckBox[] diasChecks;
    private JLabel lblMensaje;

    public PanelRegistroMedico(MainFrame mainFrame, ServiceManager services, Administrador admin) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.admin = admin;

        setLayout(new GridBagLayout()); // Centrar la tarjeta

        // --- TARJETA BLANCA ---
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        // Aumentamos el padding interno de la tarjeta (Más espacio alrededor)
        tarjeta.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. TÍTULO (Con más espacio abajo)
        JLabel lblTitulo = new JLabel("Registrar Nuevo Médico");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28)); // Letra más grande
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0); // 30px de espacio debajo del título
        tarjeta.add(lblTitulo, gbc);

        // 2. CAMPOS
        gbc.gridwidth = 1; // Regresamos a 1 columna

        // Fila 1
        tarjeta.add(crearLabel("Nombre:"), configGbc(0, 1));
        txtNombre = crearInput();
        tarjeta.add(txtNombre, configGbc(0, 2));

        tarjeta.add(crearLabel("Precio Consulta (S/.):"), configGbc(1, 1));
        txtPrecio = crearInput();
        tarjeta.add(txtPrecio, configGbc(1, 2));

        // Fila 2 (Aumentamos el 'gridy' para bajar)
        tarjeta.add(crearLabel("Apellido:"), configGbc(0, 3));
        txtApellido = crearInput();
        tarjeta.add(txtApellido, configGbc(0, 4));

        tarjeta.add(crearLabel("DNI:"), configGbc(1, 3));
        txtDni = crearInput();
        tarjeta.add(txtDni, configGbc(1, 4));

        // Fila 3
        tarjeta.add(crearLabel("Especialidad:"), configGbc(0, 5));
        txtEspecialidad = crearInput();
        tarjeta.add(txtEspecialidad, configGbc(0, 6));

        tarjeta.add(crearLabel("Contraseña:"), configGbc(1, 5));
        txtPass = crearInput();
        tarjeta.add(txtPass, configGbc(1, 6));

        // --- SECCIÓN HORARIOS ---
        // Título de sección con espacio arriba
        JLabel lblHorario = new JLabel("Horario de Consulta:");
        lblHorario.setFont(new Font("SansSerif", Font.BOLD, 16));

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 10, 10, 10); // Más aire arriba (25px)
        tarjeta.add(lblHorario, gbc);

        // Checkboxes Días
        JPanel panelDias = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0)); // 15px espacio entre checks
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

        // Horas
        JPanel panelHoras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHoras.setBackground(Color.WHITE);

        panelHoras.add(new JLabel("Desde: "));
        txtHoraInicio = new JTextField(4);
        txtHoraInicio.setFont(Estilos.FUENTE_NORMAL);
        cmbAmPmInicio = new JComboBox<>(new String[] { "AM", "PM" });
        panelHoras.add(txtHoraInicio);
        panelHoras.add(cmbAmPmInicio);

        panelHoras.add(Box.createHorizontalStrut(30)); // Separador grande

        panelHoras.add(new JLabel("Hasta: "));
        txtHoraFin = new JTextField(4);
        txtHoraFin.setFont(Estilos.FUENTE_NORMAL);
        cmbAmPmFin = new JComboBox<>(new String[] { "AM", "PM" });
        cmbAmPmFin.setSelectedIndex(1);
        panelHoras.add(txtHoraFin);
        panelHoras.add(cmbAmPmFin);

        gbc.gridy = 9;
        tarjeta.add(panelHoras, gbc);

        // --- BOTÓN REGISTRAR ---
        BotonRedondo btnRegistrar = new BotonRedondo("REGISTRAR MÉDICO", Estilos.VERDE_MEDICO);
        btnRegistrar.setPreferredSize(new Dimension(250, 45)); // Botón más grande
        btnRegistrar.addActionListener(e -> accionRegistrar());

        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(35, 10, 10, 10); // Bastante espacio arriba del botón
        tarjeta.add(btnRegistrar, gbc);

        // Mensaje
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(Color.WHITE);
        gbc.gridy = 11;
        gbc.insets = new Insets(10, 10, 0, 10);
        tarjeta.add(lblMensaje, gbc);

        add(tarjeta);

        // Botón Regresar flotante
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
        gbcFloat.insets = new Insets(0, 0, 30, 30);
        add(btnRegresar, gbcFloat);
    }

    // --- CONFIGURACIÓN DE ESPACIADO (GRIDBAG) ---
    private GridBagConstraints configGbc(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;

        // AQUÍ ESTÁ EL CAMBIO CLAVE: MÁRGENES (Top, Left, Bottom, Right)
        // Si es una fila par (etiquetas 1, 3, 5), damos espacio arriba (20px)
        if (y == 1 || y == 3 || y == 5) {
            c.insets = new Insets(15, 15, 5, 15);
        } else {
            // Si es una fila impar (inputs 2, 4, 6), pegado a su etiqueta de arriba
            c.insets = new Insets(0, 15, 5, 15);
        }
        return c;
    }

    // --- HELPERS VISUALES ---
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        lbl.setForeground(Color.GRAY);
        return lbl;
    }

    private JTextField crearInput() {
        JTextField txt = new JTextField(15);
        txt.setPreferredSize(new Dimension(15, 30)); // Hacemos el input más alto (30px)
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10))); // Padding interno del texto
        return txt;
    }

    // --- LÓGICA DE VALIDACIÓN ---
    private void accionRegistrar() {
        resetBorde(txtNombre);
        resetBorde(txtApellido);
        resetBorde(txtDni);
        resetBorde(txtEspecialidad);
        resetBorde(txtPrecio);
        resetBorde(txtPass);
        resetBorde(txtHoraInicio);
        resetBorde(txtHoraFin);
        lblMensaje.setText(" ");
        lblMensaje.setBackground(Color.WHITE);

        boolean valido = true;
        if (esVacio(txtNombre))
            valido = false;
        if (esVacio(txtApellido))
            valido = false;
        if (esVacio(txtDni))
            valido = false;
        if (esVacio(txtEspecialidad))
            valido = false;
        if (esVacio(txtPass))
            valido = false;
        if (esVacio(txtHoraInicio))
            valido = false;
        if (esVacio(txtHoraFin))
            valido = false;

        double precio = 0;
        try {
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            marcarError(txtPrecio);
            valido = false;
        }

        if (!valido) {
            mostrarMensaje("⚠️ Complete los campos requeridos.", new Color(255, 235, 238), Color.RED);
            return;
        }

        // Construir Horario
        StringBuilder diasStr = new StringBuilder();
        for (JCheckBox cb : diasChecks)
            if (cb.isSelected())
                diasStr.append(cb.getText()).append(", ");
        if (diasStr.length() > 2)
            diasStr.setLength(diasStr.length() - 2);

        String horarioStr = txtHoraInicio.getText() + cmbAmPmInicio.getSelectedItem() + " - " +
                txtHoraFin.getText() + cmbAmPmFin.getSelectedItem();

        services.getMedicoService().registrarMedico(txtNombre.getText(), txtApellido.getText(),
                txtEspecialidad.getText(), txtDni.getText(),
                precio, diasStr.toString(), horarioStr, txtPass.getText());

        mostrarMensaje("✅ Médico registrado correctamente", new Color(200, 230, 201), new Color(27, 94, 32));

        // Limpiar
        txtNombre.setText("");
        txtApellido.setText("");
        txtDni.setText("");
        txtEspecialidad.setText("");
        txtPrecio.setText("");
        txtPass.setText("");
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
    }

    private boolean esVacio(JTextField txt) {
        if (txt.getText().trim().isEmpty()) {
            marcarError(txt);
            return true;
        }
        return false;
    }

    private void marcarError(JTextField txt) {
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private void resetBorde(JTextField txt) {
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private void mostrarMensaje(String texto, Color fondo, Color letra) {
        lblMensaje.setText(" " + texto + " ");
        lblMensaje.setBackground(fondo);
        lblMensaje.setForeground(letra);
    }
}