import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private GestionClinica gestion; // Referencia a la lógica
    private Administrador adminLogueado;

    public MainFrame() {
        gestion = new GestionClinica(); // Inicializamos el backend

        setTitle("VITALSOFT");
        setSize(1000, 600); // Tamaño similar al boceto
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar

        // Layout principal: Dividir la pantalla en 2 columnas iguales
        setLayout(new GridLayout(1, 2));

        // --- PANEL IZQUIERDO (BLANCO CON LOGO) ---
        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(Color.WHITE);

        // Usamos HTML en el JLabel para poner texto en dos líneas y con distintos
        // estilos fácilmente
        JLabel lblLogo = new JLabel("<html><center>"
                + "<span style='font-size:16px; color:#666666;'>Clínica Médica</span><br>"
                + "<span style='font-size:28px; color:#00796B; font-weight:bold;'>VITALSOFT</span>"
                + "</center></html>");

        // Intentar cargar el icono al lado del texto
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/img/logo_full.png"));
            // Escalar el logo si es muy grande (ej. 100x100)
            Image imgEscalada = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
            // Poner el texto debajo del icono
            lblLogo.setHorizontalTextPosition(JLabel.CENTER);
            lblLogo.setVerticalTextPosition(JLabel.BOTTOM);
        } catch (Exception e) {
            System.out.println("Nota: No se encontró logo_full.png, mostrando solo texto.");
        }
        panelIzquierdo.add(lblLogo); // GridBagLayout lo centra automáticamente

        // --- PANEL DERECHO (IMAGEN CON BOTONES) ---
        // Usamos nuestro panel personalizado que dibuja el fondo y la diagonal
        PanelFondoImagen panelDerecho = new PanelFondoImagen();
        // Usamos GridBagLayout para centrar los botones verticalmente
        panelDerecho.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Uno debajo del otro
        gbc.insets = new Insets(10, 0, 10, 0); // Espacio vertical entre botones
        gbc.ipadx = 40; // Hacer los botones más anchos internamente
        gbc.ipady = 15; // Hacer los botones más altos internamente
        gbc.fill = GridBagConstraints.HORIZONTAL; // Que todos tengan el mismo ancho

        // Creamos los botones redondos con sus colores específicos
        BotonRedondo btnAdmin = new BotonRedondo("ADMINISTRADOR", Estilos.TURQUESA_ADMIN);
        BotonRedondo btnMedico = new BotonRedondo("MÉDICO", Estilos.VERDE_MEDICO);
        BotonRedondo btnPaciente = new BotonRedondo("PACIENTE", Estilos.NARANJA_PACIENTE);

        // Acciones de los botones: Abrir el Dialog de Login
        btnAdmin.addActionListener(e -> abrirLogin("Administrador", Estilos.TURQUESA_ADMIN));
        btnMedico.addActionListener(e -> abrirLogin("Médico", Estilos.VERDE_MEDICO));
        btnPaciente.addActionListener(e -> abrirLogin("Paciente", Estilos.NARANJA_PACIENTE));

        panelDerecho.add(btnAdmin, gbc);
        panelDerecho.add(btnMedico, gbc);
        panelDerecho.add(btnPaciente, gbc);

        // Agregar los dos paneles principales a la ventana
        add(panelIzquierdo);
        add(panelDerecho);
    }

    // Método auxiliar para abrir la ventanita modal
    private void abrirLogin(String rol, Color colorTema) {
        // 'this' es el padre, true hace que sea modal (bloquea la ventana de atrás)
        LoginDialog dialog = new LoginDialog(this, true, rol, colorTema, gestion);
        dialog.setVisible(true);
    }

    // --- MÉTODOS PARA CAMBIAR DE PANTALLA --

    public void mostrarPanelAdmin(Administrador admin) {
        this.adminLogueado = admin; // <--- GUARDAMOS AL ADMIN AQUÍ

        getContentPane().removeAll();
        PanelAdmin panelAdmin = new PanelAdmin(this, gestion, admin);
        add(panelAdmin);
        revalidate();
        repaint();
    }

    public void mostrarPanelPaciente(Paciente paciente) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Creamos el panel del paciente pasándole sus datos
        PanelPaciente panel = new PanelPaciente(this, gestion, paciente);
        add(panel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    public void mostrarPanel(String nombrePanel) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        if (nombrePanel.equals("SeleccionVer")) {
            // Ahora le pasamos el admin guardado
            add(new PanelSeleccionVer(this, adminLogueado));
        } else if (nombrePanel.equals("VerMedicos")) {
            add(new PanelVerMedicos(this));
        } else if (nombrePanel.equals("VerPacientes")) {
            add(new PanelVerPacientes(this));
        } else if (nombrePanel.equals("PanelAdmin")) {
            // Nueva opción para el botón regresar
            mostrarPanelAdmin(adminLogueado);
            return;
        }

        revalidate();
        repaint();
    }

    // Método para volver al Login (Cerrar Sesión)
    public void cerrarSesion() {
        getContentPane().removeAll();

        // Volvemos a crear el diseño original (Login)
        // (Copiamos la lógica del constructor aquí para regenerarlo)
        setLayout(new GridLayout(1, 2));

        JPanel panelIzquierdo = new JPanel(new GridBagLayout());
        panelIzquierdo.setBackground(Color.WHITE);
        JLabel lblLogo = new JLabel(
                "<html><center><span style='font-size:28px; color:#00796B; font-weight:bold;'>VITALSOFT</span></center></html>");
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/img/logo_full.png"));
            Image imgEscalada = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imgEscalada));
            lblLogo.setHorizontalTextPosition(JLabel.CENTER);
            lblLogo.setVerticalTextPosition(JLabel.BOTTOM);
        } catch (Exception e) {
        }
        panelIzquierdo.add(lblLogo);

        PanelFondoImagen panelDerecho = new PanelFondoImagen();
        panelDerecho.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}