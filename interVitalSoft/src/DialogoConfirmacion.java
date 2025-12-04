import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DialogoConfirmacion extends JDialog {
    private boolean confirmado = false;

    public DialogoConfirmacion(JFrame parent, String nombreMedico) {
        super(parent, true); // Modal
        setUndecorated(true);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setContentPane(panel);
        setSize(400, 200);
        setLocationRelativeTo(parent);

        // Mensaje
        JLabel lbl = new JLabel("<html><center>¿Eliminar a <b>" + nombreMedico + "</b>?</center></html>");
        lbl.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(lbl);

        // Botones
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);

        // CAMBIO: Botón "No" ahora es VERDE
        JButton btnNo = new JButton("Cancelar ");
        btnNo.setBackground(new Color(163, 163, 163));
        btnNo.setForeground(Color.WHITE);
        btnNo.setFocusPainted(false);
        btnNo.addActionListener(e -> {
            confirmado = false;
            dispose();
        });

        JButton btnSi = new JButton("Eliminar");
        btnSi.setBackground(new Color(171, 67, 67));
        btnSi.setForeground(Color.WHITE);
        btnSi.setFocusPainted(false);
        btnSi.addActionListener(e -> {
            confirmado = true;
            dispose();
        });

        btnPanel.add(btnNo);
        btnPanel.add(btnSi);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        panel.add(btnPanel, gbc);
    }

    public boolean fueConfirmado() {
        return confirmado;
    }
}