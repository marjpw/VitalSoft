import javax.swing.*;
import java.awt.*;

public class PanelPaciente extends JPanel {

    private MainFrame mainFrame;
    private GestionClinica gestion;
    private Paciente paciente;
    private Image imagenFondo;

    public PanelPaciente(MainFrame mainFrame, GestionClinica gestion, Paciente paciente) {
        this.mainFrame = mainFrame;
        this.gestion = gestion;
        this.paciente = paciente;

        try {
            imagenFondo = new ImageIcon(getClass().getResource("/img/fondo_doctor.jpg")).getImage();
        } catch (Exception e) {
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblTitulo = new JLabel("<html>PANEL<br>PACIENTE</html>");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 48));
        lblTitulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(lblTitulo, gbc);

        JPanel panelBotones = new JPanel(new GridLayout(3, 2, 20, 20));
        panelBotones.setOpaque(false);

        BotonRedondo btnNuevaCita = crearBotonMenu("Cita Nueva", Estilos.TURQUESA_ADMIN, "/img/icono_mas_paciente.png");
        btnNuevaCita.addActionListener(e -> {
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new PanelCrearCita(mainFrame, gestion, paciente));
            mainFrame.revalidate();
            mainFrame.repaint();
        });

        BotonRedondo btnFacturas = crearBotonMenu("Facturas", Estilos.VERDE_MEDICO, "/img/icono_caja.png");
        btnFacturas.addActionListener(e -> {
            System.out.println("--- TUS FACTURAS ---");
            gestion.verFacturasDePaciente(paciente);
            JOptionPane.showMessageDialog(this, "Revisa la consola para ver tus facturas.");
        });

        BotonRedondo btnHistorial = crearBotonMenu("Historial", Estilos.NARANJA_PACIENTE, "/img/icono_buscar.png");
        btnHistorial.addActionListener(e -> {
            System.out.println("--- TU HISTORIAL ---");
            for (Cita c : gestion.getHistorialPaciente(paciente.getId()))
                System.out.println(c);
            JOptionPane.showMessageDialog(this, "Historial enviado a consola.");
        });

        BotonRedondo btnDiagnosticos = crearBotonMenu("Diagnósticos", Estilos.TURQUESA_ADMIN, "/img/icono_triaje.png");
        btnDiagnosticos.addActionListener(e -> {
            gestion.consultarDiagnostico(paciente);
            JOptionPane.showMessageDialog(this, "Diagnósticos enviados a consola.");
        });

        BotonRedondo btnCancelar = crearBotonMenu("Cancelar Cita", Estilos.VERDE_MEDICO, "/img/icono_basura.png");
        btnCancelar.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ID de cita a cancelar:");
            if (id != null)
                gestion.cancelarCita(Integer.parseInt(id), paciente);
        });

        panelBotones.add(btnNuevaCita);
        panelBotones.add(btnFacturas);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnDiagnosticos);
        panelBotones.add(btnCancelar);
        panelBotones.add(new JLabel(""));

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(50, 20, 50, 50);
        add(panelBotones, gbc);

        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setForeground(new Color(220, 53, 69));
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