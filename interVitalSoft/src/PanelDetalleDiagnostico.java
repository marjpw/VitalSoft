import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelDetalleDiagnostico extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Cita cita;

    public PanelDetalleDiagnostico(MainFrame mainFrame, ServiceManager services, Cita cita) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.cita = cita;

        setLayout(new GridBagLayout());

        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(20, 30, 20, 30)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 40, 20, 40);
        add(tarjeta, gbc);

        // TITULO
        JLabel lblTitulo = new JLabel("Detalles de la Cita", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        GridBagConstraints gbcT = new GridBagConstraints();
        gbcT.gridx = 0;
        gbcT.gridy = 0;
        gbcT.gridwidth = 2;
        gbcT.insets = new Insets(0, 0, 20, 0);
        tarjeta.add(lblTitulo, gbcT);

        // --- DATOS PACIENTE ---
        agregarSeccion(tarjeta, "Datos del Paciente:", 1);
        Paciente p = cita.getPaciente();
        agregarCampo(tarjeta, "Nombre:", p.getNombre(), 0, 2);
        agregarCampo(tarjeta, "Alergias:", p.getAlergias(), 1, 2);
        agregarCampo(tarjeta, "Apellido:", p.getApellidos(), 0, 3);
        agregarCampo(tarjeta, "Fecha Nacimiento:", p.getFechaNacimiento().toString(), 1, 3);
        agregarCampo(tarjeta, "DNI:", p.getDni(), 0, 4);
        agregarCampo(tarjeta, "Edad:", String.valueOf(p.getEdad()), 1, 4);

        // --- TRIAJE ---
        agregarSeccion(tarjeta, "Datos del Triaje:", 5);
        String peso = String.valueOf(cita.getPeso());
        String talla = String.valueOf(cita.getTalla());
        String presion = cita.getPresionArterial();
        String imc = cita.getIMC();
        String cat = cita.getCategoriaIMC();

        agregarCampo(tarjeta, "Paciente (Kg):", peso, 0, 6);
        agregarCampo(tarjeta, "Presión Arterial:", presion, 1, 6);
        agregarCampo(tarjeta, "Talla (m):", talla, 0, 7);

        // IMC y Categoría
        JPanel panelIMC = new JPanel(new GridLayout(1, 2, 10, 0));
        panelIMC.setBackground(Color.WHITE);
        panelIMC.add(crearCampoSimple("IMC", imc));
        panelIMC.add(crearCampoSimple("Categoría", cat));
        GridBagConstraints cImc = new GridBagConstraints();
        cImc.gridx = 1;
        cImc.gridy = 7;
        cImc.fill = GridBagConstraints.HORIZONTAL;
        cImc.insets = new Insets(2, 5, 2, 5);
        tarjeta.add(panelIMC, cImc);

        // --- DIAGNÓSTICO ---
        agregarSeccion(tarjeta, "Resultados", 8);
        agregarCampo(tarjeta, "Diagnóstico:", cita.getDiagnostico(), 0, 9);
        agregarCampo(tarjeta, "Receta:", cita.getReceta(), 1, 9);

        // --- FIRMA ---
        JPanel panelFirma = new JPanel();
        panelFirma.setBackground(Color.WHITE);
        panelFirma.setLayout(new BoxLayout(panelFirma, BoxLayout.Y_AXIS));

        try {
            ImageIcon firma = new ImageIcon(getClass().getResource("/img/firma.png"));
            Image imgFirma = firma.getImage().getScaledInstance(120, 50, Image.SCALE_SMOOTH);
            JLabel lblFirmaImg = new JLabel(new ImageIcon(imgFirma));
            lblFirmaImg.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelFirma.add(lblFirmaImg);
        } catch (Exception e) {
            panelFirma.add(new JLabel("[Firma]"));
        }

        JSeparator linea = new JSeparator();
        linea.setMaximumSize(new Dimension(150, 2));
        panelFirma.add(linea);

        JLabel lblDr = new JLabel(cita.getMedico().getNombre() + " " + cita.getMedico().getApellidos());
        lblDr.setFont(new Font("Arial", Font.BOLD, 12));
        lblDr.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFirma.add(lblDr);

        JLabel lblEsp = new JLabel("LICENCIADO EN " + cita.getMedico().getEspecialidad().toUpperCase());
        lblEsp.setFont(new Font("Arial", Font.PLAIN, 10));
        lblEsp.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFirma.add(lblEsp);

        GridBagConstraints gbcFirma = new GridBagConstraints();
        gbcFirma.gridx = 0;
        gbcFirma.gridy = 10;
        gbcFirma.gridwidth = 2;
        gbcFirma.insets = new Insets(30, 0, 0, 0);
        tarjeta.add(panelFirma, gbcFirma);

        // --- REGRESAR ---
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }

        // Volver a la lista de diagnósticos del PACIENTE
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelDiagnosticosPaciente(cita.getPaciente()));

        GridBagConstraints gbcBack = new GridBagConstraints();
        gbcBack.gridx = 0;
        gbcBack.gridy = 1;
        gbcBack.anchor = GridBagConstraints.SOUTHEAST;
        gbcBack.insets = new Insets(0, 0, 20, 20);
        add(btnRegresar, gbcBack);
    }

    // Helpers visuales
    private void agregarSeccion(JPanel p, String titulo, int y) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = y;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(15, 5, 5, 5);
        p.add(lbl, c);
    }

    private void agregarCampo(JPanel p, String label, String valor, int x, int y) {
        p.add(crearCampoSimple(label, valor), configC(x, y));
    }

    private GridBagConstraints configC(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(2, 5, 2, 5);
        return c;
    }

    private JPanel crearCampoSimple(String label, String valor) {
        JPanel p = new JPanel(new BorderLayout(5, 0));
        p.setBackground(Color.WHITE);
        p.add(new JLabel(label), BorderLayout.WEST);
        JTextField txt = new JTextField(valor);
        txt.setEditable(false);
        txt.setBackground(Color.WHITE);
        txt.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image img = new ImageIcon(getClass().getResource("/img/fondo_doctor.jpg")).getImage();
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
        } catch (Exception e) {
        }
    }
}