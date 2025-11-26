import javax.swing.*;
import java.awt.*;

public class PanelSeleccionVer extends JPanel {
    private MainFrame mainFrame;
    private Image imagenFondo;
    private Administrador admin;

    public PanelSeleccionVer(MainFrame mainFrame, Administrador admin) {
        this.mainFrame = mainFrame;
        this.admin = admin;

        // Cargar imagen de fondo
        try {
            imagenFondo = new ImageIcon(getClass().getResource("/img/fondogrande.png")).getImage();
        } catch (Exception e) {
            // Si no encuentra la imagen, no pasa nada
        }

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- 1. TÍTULO ---
        JLabel lblTitulo = new JLabel("SELECCIONE REGISTRO A VISUALIZAR");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Ocupa todo el ancho
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(100, 0, 50, 0); // Margen superior para bajarlo
        add(lblTitulo, gbc);

        // --- 2. PANEL CONTENEDOR DE BOTONES (Para centrado perfecto) ---
        // Usamos FlowLayout con un 'gap' (espacio) horizontal de 60px entre botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0));
        panelBotones.setOpaque(false); // Transparente

        // Crear los botones
        JButton btnMedicos = crearBotonGrande("Ver Registro Médicos", "/img/icono_ver_medico.png",
                new Color(90, 200, 210));
        btnMedicos.addActionListener(e -> mainFrame.mostrarPanel("VerMedicos"));

        JButton btnPacientes = crearBotonGrande("Ver Registro Pacientes", "/img/icono_ver_paciente.png",
                new Color(160, 215, 110));
        btnPacientes.addActionListener(e -> mainFrame.mostrarPanel("VerPacientes"));

        // Agregarlos al sub-panel
        panelBotones.add(btnMedicos);
        panelBotones.add(btnPacientes);

        // Agregar el sub-panel al GridBag principal
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0); // Sin márgenes extra, el FlowLayout maneja el espacio
        add(panelBotones, gbc);

        // --- 3. BOTÓN REGRESAR (Esquina inferior derecha) ---
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegresar.setBackground(Color.WHITE);
        btnRegresar.setForeground(Color.BLACK);
        btnRegresar.setFocusPainted(false);

        try {
            ImageIcon iconBack = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            Image imgBack = iconBack.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            btnRegresar.setIcon(new ImageIcon(imgBack));
        } catch (Exception e) {
        }

        btnRegresar.addActionListener(e -> mainFrame.mostrarPanel("PanelAdmin"));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 1.0; // Empuja todo lo anterior hacia arriba (ocupa el espacio sobrante abajo)
        gbc.anchor = GridBagConstraints.SOUTHEAST; // Pegado abajo derecha
        gbc.insets = new Insets(0, 0, 40, 40); // Margen desde el borde de la ventana

        add(btnRegresar, gbc);
    }

    private JButton crearBotonGrande(String texto, String rutaIcono, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setBackground(colorFondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(rutaIcono));
            Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
            btn.setVerticalTextPosition(SwingConstants.BOTTOM);
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setIconTextGap(15);
        } catch (Exception e) {
        }

        // Tamaño fijo para que se vean iguales
        btn.setPreferredSize(new Dimension(260, 180));
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }

        // Filtro oscuro opcional para mejorar lectura (descomentar si es necesario)
        /*
         * Graphics2D g2 = (Graphics2D) g;
         * g2.setColor(new Color(0, 0, 0, 40));
         * g2.fillRect(0, 0, getWidth(), getHeight());
         */
    }
}