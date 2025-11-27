import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class PanelDetalleAtencion extends JPanel {
    private MainFrame mainFrame;
    private ServiceManager services;
    private Cita cita;
    private JTextArea txtDiagnostico, txtReceta;

    public PanelDetalleAtencion(MainFrame mainFrame, ServiceManager services, Cita cita) {
        this.mainFrame = mainFrame;
        this.services = services;
        this.cita = cita;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Fondo blanco o gris muy claro
        setBackground(new Color(245, 245, 245));

        // --- CONTENEDOR PRINCIPAL (Tarjeta Blanca) ---
        JPanel tarjeta = new JPanel(new GridBagLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(20, 30, 20, 30)));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(tarjeta, gbc);

        // TITULO
        JLabel lblTitulo = new JLabel("Atender Cita", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        GridBagConstraints gbcT = new GridBagConstraints();
        gbcT.gridx = 0;
        gbcT.gridy = 0;
        gbcT.gridwidth = 2;
        gbcT.insets = new Insets(0, 0, 20, 0);
        tarjeta.add(lblTitulo, gbcT);

        // --- SECCIÓN 1: DATOS PACIENTE ---
        agregarSeccion(tarjeta, "Datos del Paciente:", 1);
        Paciente p = cita.getPaciente();
        agregarCampo(tarjeta, "Nombre:", p.getNombre(), 0, 2);
        agregarCampo(tarjeta, "Alergias:", p.getAlergias(), 1, 2);
        agregarCampo(tarjeta, "Apellido:", p.getApellidos(), 0, 3);
        // Calcular edad solo para mostrar (puedes usar p.getFechaNacimiento() si tienes
        // el metodo)
        agregarCampo(tarjeta, "DNI:", p.getDni(), 0, 4);
        agregarCampo(tarjeta, "Edad:", String.valueOf(p.getEdad()), 1, 4);

        // --- SECCIÓN 2: TRIAJE ---
        agregarSeccion(tarjeta, "Datos del Triaje:", 5);
        // Validamos si hay triaje
        String peso = cita.isTriajeRealizado() ? cita.getPeso() + " Kg" : "Pendiente";
        String talla = cita.isTriajeRealizado() ? cita.getTalla() + " m" : "Pendiente";
        String presion = cita.isTriajeRealizado() ? cita.getPresionArterial() : "Pendiente";
        String imc = cita.isTriajeRealizado() ? cita.getIMC() : "-";
        String cat = cita.isTriajeRealizado() ? cita.getCategoriaIMC() : "-";

        agregarCampo(tarjeta, "Peso:", peso, 0, 6);
        agregarCampo(tarjeta, "Presión Arterial:", presion, 1, 6);
        agregarCampo(tarjeta, "Talla:", talla, 0, 7);
        agregarCampo(tarjeta, "IMC:", imc, 0, 8);
        agregarCampo(tarjeta, "Categoría:", cat, 1, 8);

        // --- SECCIÓN 3: DIAGNÓSTICO (Editable) ---
        agregarSeccion(tarjeta, "Diagnóstico y Tratamiento:", 9);

        txtDiagnostico = new JTextArea(3, 20);
        txtReceta = new JTextArea(3, 20);
        JScrollPane spDiag = new JScrollPane(txtDiagnostico);
        JScrollPane spRec = new JScrollPane(txtReceta);

        bordeInput(txtDiagnostico);
        bordeInput(txtReceta);

        GridBagConstraints gbcIn = new GridBagConstraints();
        gbcIn.fill = GridBagConstraints.BOTH;
        gbcIn.insets = new Insets(5, 5, 5, 5);
        gbcIn.weightx = 0.5;

        gbcIn.gridx = 0;
        gbcIn.gridy = 10;
        tarjeta.add(new JLabel("Diagnóstico:"), gbcIn);
        gbcIn.gridx = 1;
        tarjeta.add(new JLabel("Receta:"), gbcIn);

        gbcIn.gridx = 0;
        gbcIn.gridy = 11;
        tarjeta.add(spDiag, gbcIn);
        gbcIn.gridx = 1;
        tarjeta.add(spRec, gbcIn);

        // --- BOTÓN GUARDAR ---
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(46, 160, 140));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(150, 40));
        btnGuardar.addActionListener(e -> guardarAtencion());

        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.gridx = 0;
        gbcBtn.gridy = 12;
        gbcBtn.gridwidth = 2;
        gbcBtn.insets = new Insets(20, 0, 0, 0);
        tarjeta.add(btnGuardar, gbcBtn);

        // Regresar
        JButton btnRegresar = new JButton("Regresar");
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/img/icono_regresar.png"));
            btnRegresar.setIcon(new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
        }
        btnRegresar.addActionListener(e -> mainFrame.mostrarPanelAtenderCita(cita.getMedico())); // Volver a lista

        GridBagConstraints gbcBack = new GridBagConstraints();
        gbcBack.gridx = 0;
        gbcBack.gridy = 1;
        gbcBack.anchor = GridBagConstraints.SOUTHEAST;
        gbcBack.insets = new Insets(0, 0, 20, 20);
        add(btnRegresar, gbcBack);
    }

    private void guardarAtencion() {
        // --- CAMBIO AQUÍ: VALIDACIÓN DE TRIAJE ---
        if (!cita.isTriajeRealizado()) {
            JOptionPane.showMessageDialog(this,
                    "⚠ El paciente aún no ha pasado por Triaje.\nNo se puede atender la cita.",
                    "Error de Proceso", JOptionPane.ERROR_MESSAGE);
            return; // Detiene el guardado
        }
        // -----------------------------------------

        if (txtDiagnostico.getText().isEmpty() || txtReceta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar diagnóstico y receta.");
            return;
        }

        services.getCitaService().registrarDiagnostico(cita.getId(), txtDiagnostico.getText(), txtReceta.getText());
        JOptionPane.showMessageDialog(this, "Cita atendida correctamente");
        mainFrame.mostrarPanelAtenderCita(cita.getMedico());
    }

    // Helpers
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
        JPanel panelCampo = new JPanel(new BorderLayout(5, 0));
        panelCampo.setBackground(Color.WHITE);
        panelCampo.add(new JLabel(label), BorderLayout.WEST);
        JTextField txt = new JTextField(valor);
        txt.setEditable(false);
        txt.setBackground(Color.WHITE);
        txt.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panelCampo.add(txt, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(2, 5, 2, 5);
        p.add(panelCampo, c);
    }

    private void bordeInput(JComponent c) {
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    }
}