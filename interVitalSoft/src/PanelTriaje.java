import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class PanelTriaje extends PanelFondoImagen {

    @SuppressWarnings("unused")
    private MainFrame mainFrame;
    private ServiceManager services;
    @SuppressWarnings("unused")
    private Administrador admin;

    private JComboBox<Cita> cmbCitasPendientes;
    private JTextField txtPeso, txtTalla, txtPresion, txtTemperatura;
    private JTextField txtIMC, txtCategoria;
    private JLabel lblMensaje;

    public PanelTriaje(MainFrame mainFrame, ServiceManager services, Administrador admin) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.admin = admin;

        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel lblTitulo = new JLabel("Módulo de Triaje");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 30, 0);
        tarjeta.add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        // --- SELECCIÓN DE PACIENTE ---
        tarjeta.add(crearLabel("Seleccionar Paciente (Pendiente):"), configGbc(0, 1));
        cmbCitasPendientes = new JComboBox<>();
        cargarCitasPendientes();

        GridBagConstraints gbcCombo = configGbc(0, 2);
        gbcCombo.gridwidth = 2;
        tarjeta.add(cmbCitasPendientes, gbcCombo);

        JLabel lblSub = new JLabel("Registro de Signos Vitales");
        lblSub.setFont(new Font("SansSerif", Font.BOLD, 14));
        GridBagConstraints gbcSub = configGbc(0, 3);
        gbcSub.gridwidth = 2;
        gbcSub.insets = new Insets(20, 15, 10, 15);
        tarjeta.add(lblSub, gbcSub);

        tarjeta.add(crearLabel("Peso (Kg):"), configGbc(0, 4));
        txtPeso = crearInput();
        tarjeta.add(txtPeso, configGbc(0, 5));

        tarjeta.add(crearLabel("Talla (m) ej: 1.70:"), configGbc(0, 6));
        txtTalla = crearInput();
        tarjeta.add(txtTalla, configGbc(0, 7));

        tarjeta.add(crearLabel("Presión Arterial:"), configGbc(0, 8));
        txtPresion = crearInput();
        tarjeta.add(txtPresion, configGbc(0, 9));

        tarjeta.add(crearLabel("Temperatura (°C):"), configGbc(0, 10));
        txtTemperatura = crearInput();
        tarjeta.add(txtTemperatura, configGbc(0, 11));

        tarjeta.add(crearLabel("IMC (Automático):"), configGbc(1, 4));
        txtIMC = crearInput();
        txtIMC.setEditable(false);
        txtIMC.setBackground(new Color(240, 240, 240));
        tarjeta.add(txtIMC, configGbc(1, 5));

        tarjeta.add(crearLabel("Categoría:"), configGbc(1, 6));
        txtCategoria = crearInput();
        txtCategoria.setEditable(false);
        txtCategoria.setBackground(new Color(240, 240, 240));
        txtCategoria.setFont(new Font("SansSerif", Font.BOLD, 12));
        tarjeta.add(txtCategoria, configGbc(1, 7));

        // --- LÓGICA DE CÁLCULO EN TIEMPO REAL ---
        KeyAdapter calculador = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularIMC();
            }
        };
        txtPeso.addKeyListener(calculador);
        txtTalla.addKeyListener(calculador);

        // --- BOTÓN REGISTRAR ---
        BotonRedondo btnRegistrar = new BotonRedondo("Registrar Triaje", Estilos.TURQUESA_ADMIN);
        btnRegistrar.setPreferredSize(new Dimension(200, 45));
        btnRegistrar.addActionListener(e -> accionRegistrar());

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 12;
        gbcBtn.gridwidth = 2;
        gbcBtn.insets = new Insets(30, 0, 10, 0);
        gbcBtn.anchor = GridBagConstraints.CENTER;
        tarjeta.add(btnRegistrar, gbcBtn);

        // MENSAJE
        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblMensaje.setOpaque(true);
        lblMensaje.setBackground(Color.WHITE);
        GridBagConstraints gbcMsg = new GridBagConstraints();
        gbcMsg.gridx = 0;
        gbcMsg.gridy = 13;
        gbcMsg.gridwidth = 2;
        gbcMsg.insets = new Insets(10, 0, 0, 0);
        gbcMsg.anchor = GridBagConstraints.CENTER;
        tarjeta.add(lblMensaje, gbcMsg);

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
            mainFrame.mostrarPanelAdmin(admin);
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

    private void cargarCitasPendientes() {
        cmbCitasPendientes.removeAllItems();
        List<Cita> pendientes = services.getCitaService().getCitasPendientesTriaje();
        for (Cita c : pendientes) {
            cmbCitasPendientes.addItem(c);
        }
        if (pendientes.isEmpty()) {
            lblMensaje.setText("No hay pacientes esperando triaje.");
            lblMensaje.setForeground(Color.RED);
        }
    }

    private void calcularIMC() {
        try {
            String pesoTxt = txtPeso.getText();
            String tallaTxt = txtTalla.getText();

            if (!pesoTxt.isEmpty() && !tallaTxt.isEmpty()) {
                double p = Double.parseDouble(pesoTxt);
                double t = Double.parseDouble(tallaTxt);

                if (t > 0) {
                    double imc = p / (t * t);
                    txtIMC.setText(String.format("%.2f", imc));

                    String cat = "";
                    Color colorCat = Color.BLACK;

                    if (imc < 18.5) {
                        cat = "Bajo peso";
                        colorCat = Color.ORANGE;
                    } else if (imc < 25) {
                        cat = "Normal";
                        colorCat = new Color(0, 150, 0);
                    } // Verde
                    else if (imc < 30) {
                        cat = "Sobrepeso";
                        colorCat = Color.ORANGE;
                    } else if (imc < 35) {
                        cat = "Obesidad I";
                        colorCat = Color.RED;
                    } else if (imc < 40) {
                        cat = "Obesidad II";
                        colorCat = Color.RED;
                    } else {
                        cat = "Obesidad III";
                        colorCat = new Color(150, 0, 0);
                    } // Rojo oscuro

                    txtCategoria.setText(cat);
                    txtCategoria.setForeground(colorCat);
                }
            } else {
                txtIMC.setText("");
                txtCategoria.setText("");
            }
        } catch (NumberFormatException ex) {

        }
    }

    private void accionRegistrar() {
        Cita citaSel = (Cita) cmbCitasPendientes.getSelectedItem();
        if (citaSel == null) {
            mostrarMensaje("⚠️ Seleccione un paciente.", new Color(255, 235, 238), Color.RED);
            return;
        }

        try {
            double peso = Double.parseDouble(txtPeso.getText());
            double talla = Double.parseDouble(txtTalla.getText());
            double temp = Double.parseDouble(txtTemperatura.getText());
            String presion = txtPresion.getText();

            if (presion.isEmpty())
                throw new Exception();

            services.getCitaService().registrarTriaje(citaSel.getId(), peso, talla, temp, presion);

            mostrarMensaje("✅ Triaje realizado correctamente", new Color(200, 230, 201), new Color(27, 94, 32));

            txtPeso.setText("");
            txtTalla.setText("");
            txtPresion.setText("");
            txtTemperatura.setText("");
            txtIMC.setText("");
            txtCategoria.setText("");
            cargarCitasPendientes();

        } catch (Exception e) {
            mostrarMensaje("⚠️ Verifique los datos numéricos.", new Color(255, 235, 238), Color.RED);
        }
    }

    // --- HELPERS ---
    private JLabel crearLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(Color.GRAY);
        return l;
    }

    private JTextField crearInput() {
        JTextField t = new JTextField(10);
        t.setPreferredSize(new Dimension(10, 30));
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
        if (y == 1 || y == 3 || y == 5 || y == 7 || y == 9 || y == 11)
            c.insets = new Insets(0, 15, 5, 15);
        else
            c.insets = new Insets(10, 15, 5, 15);
        return c;
    }

    private void mostrarMensaje(String t, Color f, Color l) {
        lblMensaje.setText(" " + t + " ");
        lblMensaje.setBackground(f);
        lblMensaje.setForeground(l);
    }
}