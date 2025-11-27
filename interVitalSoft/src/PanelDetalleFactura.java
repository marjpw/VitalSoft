import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelDetalleFactura extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Cita cita;

    public PanelDetalleFactura(MainFrame mainFrame, ServiceManager services, Cita cita) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.cita = cita;

        setLayout(new GridBagLayout());

        // --- HOJA DE FACTURA (Blanca centrada) ---
        JPanel hoja = new JPanel(new GridBagLayout());
        hoja.setBackground(Color.WHITE);
        // Sombra o borde sutil
        hoja.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        // Tamaño fijo aproximado para que parezca hoja A4 o recibo
        hoja.setPreferredSize(new Dimension(500, 600));
        add(hoja, gbc);

        // Configuración interna de la hoja
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 20, 5, 20);
        c.gridx = 0;
        c.weightx = 1.0;

        // -Logo y Empresa) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        // Logo (Izquierda)
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/img/logo_clinica.png"));
            Image img = logo.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("[LOGO]");
            lblLogo.setForeground(new Color(0, 121, 107));
            lblLogo.setFont(new Font("Arial", Font.BOLD, 20));
        }

        // Info Empresa (Derecha)
        JPanel infoEmpresa = new JPanel(new GridLayout(4, 1));
        infoEmpresa.setBackground(Color.WHITE);
        JLabel l1 = new JLabel("CLÍNICA VITALSOFT", SwingConstants.RIGHT);
        l1.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel l2 = new JLabel("RUC: 20555123456 - LIMA, PERÚ", SwingConstants.RIGHT);
        l2.setFont(new Font("Arial", Font.PLAIN, 10));
        JLabel l3 = new JLabel("Sede Principal", SwingConstants.RIGHT);
        l3.setFont(new Font("Arial", Font.PLAIN, 10));
        JLabel l4 = new JLabel("Calle Germán Amézaga N° 375", SwingConstants.RIGHT);
        l4.setFont(new Font("Arial", Font.PLAIN, 10));
        infoEmpresa.add(l1);
        infoEmpresa.add(l2);
        infoEmpresa.add(l3);
        infoEmpresa.add(l4);

        header.add(lblLogo, BorderLayout.WEST);
        header.add(infoEmpresa, BorderLayout.EAST);

        c.gridy = 0;
        hoja.add(header, c);

        // Separador
        c.gridy = 1;
        hoja.add(new JLabel(
                "-----------------------------------------------------------------------------------------------"), c);

        // --- 2. DATOS TICKET ---
        c.gridy = 2;
        hoja.add(crearFilaTexto("Nro Ticket:", "F001-" + String.format("%06d", cita.getId())), c);
        c.gridy = 3;
        hoja.add(crearFilaTexto("Ref. Atención:", "Cita #" + cita.getId()), c);
        c.gridy = 4;
        hoja.add(crearFilaTexto("Fecha Emisión:", cita.getFecha().toString()), c);

        c.gridy = 5;
        hoja.add(new JLabel(
                "-----------------------------------------------------------------------------------------------"), c);

        // --- 3. DATOS CLIENTE ---
        Paciente p = cita.getPaciente();
        c.gridy = 6;
        hoja.add(crearFilaTexto("PACIENTE:", p.getNombre() + " " + p.getApellidos()), c);
        c.gridy = 7;
        hoja.add(crearFilaTexto("DNI:", p.getDni()), c);
        c.gridy = 8;
        hoja.add(crearFilaTexto("DESCRIPCIÓN:", "Consulta Médica - " + cita.getMedico().getEspecialidad()), c);

        c.gridy = 9;
        hoja.add(new JLabel(
                "-----------------------------------------------------------------------------------------------"), c);

        // --- 4. CÁLCULOS (Total, IGV, Subtotal) ---
        double total = cita.getMedico().getPrecioConsulta();
        double subtotal = total / 1.18;
        double igv = total - subtotal;

        c.gridy = 10;
        hoja.add(crearFilaMonto("SUBTOTAL:", subtotal), c);
        c.gridy = 11;
        hoja.add(crearFilaMonto("IGV (18%):", igv), c);

        c.gridy = 12;
        c.insets = new Insets(15, 20, 5, 20); // Espacio extra antes del total
        hoja.add(crearFilaMontoBold("TOTAL A PAGAR:", total), c);

        c.gridy = 13;
        hoja.add(new JLabel(
                "-----------------------------------------------------------------------------------------------"), c);

        // --- BOTÓN REGRESAR (Fuera de la hoja o abajo) ---
        // Lo pondremos flotando abajo a la derecha en el panel principal (fondo oscuro)
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBackground(Color.WHITE);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelFacturasPaciente(cita.getPaciente())); // Volver a
                                                                                                        // lista

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 1;
        gbcBtn.anchor = GridBagConstraints.SOUTHEAST;
        gbcBtn.insets = new Insets(0, 0, 20, 20);
        add(btnRegresar, gbcBtn);
    }

    // Helpers para filas de texto alineadas
    private JPanel crearFilaTexto(String label, String valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel v = new JLabel(valor);
        v.setFont(new Font("Arial", Font.PLAIN, 12));
        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.CENTER); // Center lo pega al label, EAST lo mandaria al fondo
        // Ajuste visual: usamos un panel intermedio para separarlos un poco
        JPanel vPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        vPanel.setBackground(Color.WHITE);
        vPanel.add(v);
        p.add(vPanel, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearFilaMonto(String label, double monto) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel v = new JLabel("S/. " + String.format("%.2f", monto));
        v.setFont(new Font("Monospaced", Font.PLAIN, 12));
        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
        return p;
    }

    private JPanel crearFilaMontoBold(String label, double monto) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        JLabel l = new JLabel(label);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel v = new JLabel("S/. " + String.format("%.2f", monto));
        v.setFont(new Font("Arial", Font.BOLD, 14));
        p.add(l, BorderLayout.WEST);
        p.add(v, BorderLayout.EAST);
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