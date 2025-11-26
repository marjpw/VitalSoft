import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

// Un botón personalizado que se dibuja con bordes redondeados
public class BotonRedondo extends JButton {
    private Color colorFondo;

    public BotonRedondo(String texto, Color color) {
        super(texto);
        this.colorFondo = color;
        setContentAreaFilled(false); // Evita que se pinte el fondo cuadrado estándar
        setFocusPainted(false); // Quita el recuadro de foco feo
        setBorderPainted(false); // Quita el borde 3D feo
        setForeground(Color.WHITE);
        setFont(Estilos.FUENTE_BOTONES);
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Manito al pasar el mouse
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        // Activar suavizado de bordes (Antialiasing) para que se vea nítido
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Si el botón está presionado, oscurecemos un poco el color
        if (getModel().isPressed()) {
            g2.setColor(colorFondo.darker());
        } else {
            g2.setColor(colorFondo);
        }

        // Dibujar un rectángulo con esquinas redondeadas (radio 30)
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        g2.dispose();

        // Pintar el texto encima
        super.paintComponent(g);
    }
}