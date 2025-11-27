import javax.swing.*;
import java.awt.*;

public class PanelMedico extends PanelFondoImagen {

    private MainFrame mainFrame;
    private ServiceManager services;
    private Medico medico;
    private Image imagenFondo;

    public PanelMedico(MainFrame mainFrame, ServiceManager services, Medico medico) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.medico = medico;

        try {
            imagenFondo = new ImageIcon(getClass().getResource("/img/fondo_doctor.jpg")).getImage();
        } catch (Exception e) {
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- 1. TÍTULO (Izquierda) ---
        JLabel lblTitulo = new JLabel("<html>PANEL<br>MÉDICO</html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitulo.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        // --- 2. BOTONES (Derecha) ---
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 20, 30)); // 3 filas
        panelBotones.setOpaque(false);

        // Botón 1: Ver Agenda (Naranja)
        BotonRedondo btnAgenda = crearBotonMenu("Ver Agenda", Estilos.NARANJA_PACIENTE, "/img/icono_buscar.png"); // Usa
                                                                                                                  // icono
                                                                                                                  // que
                                                                                                                  // tengas
        btnAgenda.addActionListener(e -> mainFrame.mostrarPanelAgendaMedico(medico));

        // Botón 2: Atender Cita (Turquesa)
        BotonRedondo btnAtender = crearBotonMenu("Atender Cita", Estilos.TURQUESA_ADMIN, "/img/icono_mas_medico.png");
        btnAtender.addActionListener(e -> mainFrame.mostrarPanelAtenderCita(medico));

        // Botón 3: Historial Pacientes (Verde)
        // Botón 3: Historial Pacientes
        BotonRedondo btnHistorial = crearBotonMenu("Historial Pacientes", Estilos.VERDE_MEDICO,
                "/img/icono_triaje.png");
        btnHistorial.addActionListener(e -> mainFrame.mostrarPanelSeleccionPaciente(medico)); // CONECTADO

        panelBotones.add(btnAgenda);
        panelBotones.add(btnAtender);
        panelBotones.add(btnHistorial);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 50, 0, 50); // Margenes laterales
        add(panelBotones, gbc);

        // --- 3. CERRAR SESIÓN ---
        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(new Color(220, 53, 69));
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_salir.png"));
            btnSalir.setIcon(new ImageIcon(icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnSalir.addActionListener(e -> mainFrame.cerrarSesion());

        GridBagConstraints gbcSalir = new GridBagConstraints();
        gbcSalir.gridx = 1;
        gbcSalir.gridy = 1;
        gbcSalir.anchor = GridBagConstraints.SOUTHEAST;
        gbcSalir.insets = new Insets(0, 0, 20, 20);
        add(btnSalir, gbcSalir);
    }

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
        if (imagenFondo != null)
            g.drawImage(imagenFondo, 0, 0, w, h, this);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, w, h);
    }
}