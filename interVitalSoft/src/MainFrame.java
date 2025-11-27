import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private ServiceManager services; // Instancia central de servicios
    private Administrador adminLogueado;

    public MainFrame() {
        services = new ServiceManager(); // Se inicializa UNA SOLA VEZ

        setTitle("VITALSOFT");
        setSize(1050, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Truco rápido para intentar poner la fuente globalmente (si falla, no rompe
        // nada)
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Label.font", new Font("Century Gothic", Font.PLAIN, 14));
            UIManager.put("Button.font", new Font("Century Gothic", Font.BOLD, 14));
        } catch (Exception e) {
        }

        // Cargamos la pantalla de inicio
        cargarPantallaInicio();
    }

    private void cargarPantallaInicio() {
        getContentPane().removeAll();
        setLayout(new GridLayout(1, 2));

        // --- PANEL IZQUIERDO (LOGO) ---
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(Color.WHITE);
        JLabel lblLogo = new JLabel("<html><center>"
                + "<span style='font-size:16px; color:#666666;'>Clínica Médica</span><br>"
                + "<span style='font-size:28px; color:#00796B; font-weight:bold;'>VITALSOFT</span>"
                + "</center></html>");
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/img/logo_full.png"));
            Image imgEscalada = icono.getImage().getScaledInstance(500, 570, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
            lblLogo.setHorizontalTextPosition(JLabel.CENTER);
            lblLogo.setVerticalTextPosition(JLabel.BOTTOM);
        } catch (Exception e) {
        }
        panelIzquierdo.add(lblLogo);

        // --- PANEL DERECHO (BOTONES) ---
        PanelFondoImagen panelDerecho = new PanelFondoImagen();
        panelDerecho.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        // >>>>> AQUÍ ESTÁ EL TEXTO QUE PEDISTE <<<<<
        JLabel lblMensaje = new JLabel("INICIAR SESIÓN COMO:");
        lblMensaje.setFont(new Font("Century Gothic", Font.BOLD, 20));
        lblMensaje.setForeground(Color.WHITE);

        gbc.gridy = 0; // Va arriba del todo
        gbc.insets = new Insets(0, 0, 40, 0); // Espacio hacia abajo
        gbc.anchor = GridBagConstraints.CENTER;
        panelDerecho.add(lblMensaje, gbc);
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        // Configuración para los botones (Van debajo)
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.ipadx = 40;
        gbc.ipady = 15;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        BotonRedondo btnAdmin = new BotonRedondo("ADMINISTRADOR", Estilos.TURQUESA_ADMIN);
        BotonRedondo btnMedico = new BotonRedondo("MÉDICO", Estilos.VERDE_MEDICO);
        BotonRedondo btnPaciente = new BotonRedondo("PACIENTE", Estilos.NARANJA_PACIENTE);

        btnAdmin.addActionListener(e -> abrirLogin("Administrador", Estilos.TURQUESA_ADMIN));
        btnMedico.addActionListener(e -> abrirLogin("Médico", Estilos.VERDE_MEDICO));
        btnPaciente.addActionListener(e -> abrirLogin("Paciente", Estilos.NARANJA_PACIENTE));

        panelDerecho.add(btnAdmin, gbc);
        panelDerecho.add(btnMedico, gbc);
        panelDerecho.add(btnPaciente, gbc);

        add(panelIzquierdo);
        add(panelDerecho);

        revalidate();
        repaint();
    }

    private void abrirLogin(String rol, Color colorTema) {
        LoginDialog dialog = new LoginDialog(this, true, rol, colorTema, services);
        dialog.setVisible(true);
    }

    // --- NAVEGACIÓN (Tus métodos intactos) ---

    public void mostrarPanelAdmin(Administrador admin) {
        this.adminLogueado = admin;
        getContentPane().removeAll();
        add(new PanelAdmin(this, services, admin));
        revalidate();
        repaint();
    }

    public void mostrarPanelPaciente(Paciente paciente) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        add(new PanelPaciente(this, services, paciente));
        revalidate();
        repaint();
    }

    public void mostrarPanel(String nombrePanel) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        if (nombrePanel.equals("SeleccionVer")) {
            add(new PanelSeleccionVer(this, adminLogueado));
        } else if (nombrePanel.equals("VerMedicos")) {
            add(new PanelVerMedicos(this, services));
        } else if (nombrePanel.equals("VerPacientes")) {
            add(new PanelVerPacientes(this, services));
        } else if (nombrePanel.equals("PanelAdmin")) {
            mostrarPanelAdmin(adminLogueado);
            return;
        } else if (nombrePanel.equals("GestionMedicos")) {
            mostrarPanelGestionMedicos();
            return;
        }
        revalidate();
        repaint();
    }

    public void mostrarPanelGestionMedicos() {
        getContentPane().removeAll();
        add(new PanelGestionMedicos(this, services, adminLogueado));
        revalidate();
        repaint();
    }

    public void mostrarPanelRegistroMedico(Medico medicoAEditar) {
        getContentPane().removeAll();
        add(new PanelRegistroMedico(this, services, adminLogueado, medicoAEditar));
        revalidate();
        repaint();
    }

    // Paciente
    public void mostrarPanelCancelarCitas(Paciente paciente) {
        getContentPane().removeAll();
        add(new PanelCancelarCitas(this, services, paciente));
        revalidate();
        repaint();
    }

    public void mostrarPanelHistorialCitas(Paciente paciente) {
        getContentPane().removeAll();
        add(new PanelHistorialCitas(this, services, paciente));
        revalidate();
        repaint();
    }

    // Facturas Paciente
    public void mostrarPanelFacturasPaciente(Paciente paciente) {
        getContentPane().removeAll();
        add(new PanelFacturasPaciente(this, services, paciente));
        revalidate();
        repaint();
    }

    public void mostrarPanelDetalleFactura(Cita cita) {
        getContentPane().removeAll();
        add(new PanelDetalleFactura(this, services, cita));
        revalidate();
        repaint();
    }

    // Diagnóstico Paciente
    public void mostrarPanelDiagnosticosPaciente(Paciente paciente) {
        getContentPane().removeAll();
        add(new PanelDiagnosticosPaciente(this, services, paciente));
        revalidate();
        repaint();
    }

    public void mostrarPanelDetalleDiagnostico(Cita cita) {
        getContentPane().removeAll();
        add(new PanelDetalleDiagnostico(this, services, cita));
        revalidate();
        repaint();
    }

    // Médico
    public void mostrarPanelMedico(Medico medico) {
        getContentPane().removeAll();
        add(new PanelMedico(this, services, medico));
        revalidate();
        repaint();
    }

    public void mostrarPanelAgendaMedico(Medico medico) {
        getContentPane().removeAll();
        add(new PanelAgendaMedico(this, services, medico));
        revalidate();
        repaint();
    }

    public void mostrarPanelAtenderCita(Medico medico) {
        getContentPane().removeAll();
        add(new PanelAtenderCita(this, services, medico));
        revalidate();
        repaint();
    }

    public void mostrarPanelDetalleAtencion(Cita cita) {
        getContentPane().removeAll();
        add(new PanelDetalleAtencion(this, services, cita));
        revalidate();
        repaint();
    }

    public void mostrarPanelSeleccionPaciente(Medico medico) {
        getContentPane().removeAll();
        add(new PanelSeleccionPaciente(this, services, medico));
        revalidate();
        repaint();
    }

    public void mostrarPanelHistorialClinico(Medico medico, Paciente paciente) {
        getContentPane().removeAll();
        add(new PanelHistorialClinico(this, services, medico, paciente));
        revalidate();
        repaint();
    }

    public void mostrarPanelDetalleLectura(Medico medico, Cita cita) {
        getContentPane().removeAll();
        add(new PanelDetalleLectura(this, services, medico, cita));
        revalidate();
        repaint();
    }

    // Caja
    public void mostrarPanelCaja(Administrador admin) {
        getContentPane().removeAll();
        add(new PanelCaja(this, services, admin));
        revalidate();
        repaint();
    }

    public void cerrarSesion() {
        cargarPantallaInicio();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}