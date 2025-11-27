import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class BotonRedondo extends JButton {
    private Color colorFondo;

    public BotonRedondo(String texto, Color color) {
        super(texto);
        this.colorFondo = color;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.WHITE);
        setFont(Estilos.FUENTE_BOTONES);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(colorFondo.darker());
        } else {
            g2.setColor(colorFondo);
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();

        super.paintComponent(g);
    }
}