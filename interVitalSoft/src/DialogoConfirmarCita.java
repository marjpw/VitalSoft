import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogoConfirmarCita extends JDialog {
    private boolean confirmado = false;

    public DialogoConfirmarCita(JFrame parent) {
        super(parent, true); // Modal
        setUndecorated(true); // Sin bordes de ventana

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setContentPane(panel);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Mensaje
        JLabel lbl = new JLabel("<html><center>¿Seguro que desea cancelar esta cita?</center></html>");
        lbl.setFont(new Font("Arial", Font.BOLD, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(lbl, gbc);

        // Botones
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        btnPanel.setBackground(Color.WHITE);

        // Botón NO (Verde)
        JButton btnNo = new JButton("No");
        btnNo.setBackground(new Color(72, 201, 176)); // Verde Turquesa
        btnNo.setForeground(Color.WHITE);
        btnNo.setFocusPainted(false);
        btnNo.setPreferredSize(new Dimension(100, 35));
        btnNo.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        // Botón SÍ (Rojo)
        JButton btnSi = new JButton("Sí");
        btnSi.setBackground(new Color(255, 99, 71)); // Rojo Tomate
        btnSi.setForeground(Color.WHITE);
        btnSi.setFocusPainted(false);
        btnSi.setPreferredSize(new Dimension(100, 35));
        btnSi.addActionListener(e -> {
            confirmado = true;
            dispose();
        });

        btnPanel.add(btnNo);
        btnPanel.add(btnSi);

        gbc.gridy = 1;
        panel.add(btnPanel, gbc);
    }

    public boolean fueConfirmado() {
        return confirmado;
    }
}