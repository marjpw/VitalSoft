import javax.swing.*;
import java.awt.*;

public class PanelAdmin extends JPanel {

    private MainFrame mainFrame;
    private GestionClinica gestion;
    private Administrador admin;
    private Image imagenFondo;

    public PanelAdmin(MainFrame mainFrame, GestionClinica gestion, Administrador admin) {
        this.mainFrame = mainFrame;
        this.gestion = gestion;
        this.admin = admin;

        // Cargar imagen de fondo (reusamos la del doctor)
        try {
            imagenFondo = new ImageIcon(getClass().getResource("/img/fondo_doctor.jpg")).getImage();
        } catch (Exception e) {
        }

        setLayout(new GridBagLayout()); // Usamos GridBag para ubicar todo con precisión
        GridBagConstraints gbc = new GridBagConstraints();

        // --- 1. TITULO (Parte Izquierda) ---
        JLabel lblTitulo = new JLabel("<html>PANEL<br>ADMINISTRADOR</html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitulo.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4; // Ocupa el 40% del ancho
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        // --- 2. GRID DE BOTONES (Parte Derecha) ---
        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 20, 20)); // 3 filas, 2 columnas, espacio de 20px
        panelBotones.setOpaque(false); // Transparente para ver el fondo

        // Botón 1: Registrar Médico
        BotonRedondo btnRegMed = crearBotonMenu("Nuevo Médico", Estilos.TURQUESA_ADMIN, "/img/icono_mas_medico.png");
        btnRegMed.addActionListener(e -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new PanelRegistroMedico(mainFrame, gestion, admin));
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Botón 2: Registrar Paciente
        BotonRedondo btnRegPac = crearBotonMenu("Nuevo Paciente", Estilos.VERDE_MEDICO, "/img/icono_mas_paciente.png");
        btnRegPac.addActionListener(e -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new PanelRegistroPaciente(mainFrame, gestion, admin));
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Botón 3: Eliminar Médico
        BotonRedondo btnEliminar = crearBotonMenu("Eliminar Médico", Estilos.NARANJA_PACIENTE, "/img/icono_basura.png");
        btnEliminar.addActionListener(e -> {
            String idStr = JOptionPane.showInputDialog("Ingrese ID del Médico a eliminar:");
            if (idStr != null) {
                try {
                    gestion.eliminarMedico(Integer.parseInt(idStr));
                    JOptionPane.showMessageDialog(this, "Proceso finalizado.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "ID inválido");
                }
            }
        });

        // Botón 4: Ver Médicos/Pacientes
        BotonRedondo btnVer = crearBotonMenu("Ver Médicos / Pacientes", Estilos.TURQUESA_ADMIN,
                "/img/icono_buscar.png");

        // --- AQUÍ ESTÁ EL CAMBIO CLAVE ---
        btnVer.addActionListener(e -> {
            // Ahora redirige a la pantalla intermedia de selección que creamos
            mainFrame.mostrarPanel("SeleccionVer");
        });
        // -------------------------------

        // Botón 5: Triaje
        BotonRedondo btnTriaje = crearBotonMenu("TRIAJE", Color.WHITE, "/img/icono_triaje.png");
        btnTriaje.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        btnTriaje.addActionListener(e -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new PanelTriaje(mainFrame, gestion, admin));
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        // Botón 6: Caja
        BotonRedondo btnCaja = crearBotonMenu("CAJA (Cobrar)", Estilos.TURQUESA_ADMIN, "/img/icono_caja.png");
        btnCaja.addActionListener(e -> JOptionPane.showMessageDialog(this, "Aquí se abre la Caja"));

        // Agregamos botones al panel internoo
        panelBotones.add(btnRegMed);
        panelBotones.add(btnRegPac);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVer);
        panelBotones.add(btnTriaje);
        panelBotones.add(btnCaja);

        gbc.gridx = 1; // Columna derecha
        gbc.weightx = 0.6; // Ocupa el 60%
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(50, 20, 50, 50); // Márgenes para que no toque los bordes
        add(panelBotones, gbc);

        // --- 3. BOTÓN CERRAR SESIÓN (Abajo Derecha) ---
        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(Color.RED);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_salir.png"));
            btnSalir.setIcon(new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (Exception ex) {
        }

        btnSalir.addActionListener(e -> mainFrame.cerrarSesion());

        // Lo ponemos flotando abajo a la derecha usando otro constraint
        GridBagConstraints gbcSalir = new GridBagConstraints();
        gbcSalir.gridx = 1;
        gbcSalir.gridy = 1;
        gbcSalir.anchor = GridBagConstraints.SOUTHEAST;
        gbcSalir.insets = new Insets(0, 0, 20, 20);
        add(btnSalir, gbcSalir);
    }

    // Método auxiliar para crear botones con icono y texto rápido
    private BotonRedondo crearBotonMenu(String texto, Color color, String rutaIcono) {
        BotonRedondo btn = new BotonRedondo(texto, color);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setIconTextGap(15);
        } catch (Exception e) {
        }
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();

        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, w, h, this);
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 180)); // Negro semi-transparente
        g2.fillRect(0, 0, w, h);
    }
}