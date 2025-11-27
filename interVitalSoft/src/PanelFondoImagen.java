import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Color;

public class PanelFondoImagen extends JPanel {
    private Image imagenFondo;

    public PanelFondoImagen() {
        try {
            // Asegúrate que la imagen se llame EXACTAMENTE así en src/img/
            java.net.URL imgUrl = getClass().getResource("/img/fondo_doctor.jpg");
            if (imgUrl != null) {
                imagenFondo = new ImageIcon(imgUrl).getImage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        // 1. Dibujar la imagen de fondo
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, w, h, this);
        } else {
            // Si no carga la imagen, pintamos de gris para que no se vea roto
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, w, h);
        }

        // 2. Dibujar la superposición diagonal oscura
        // Usamos el color oscuro semi-transparente de Estilos
        g2.setColor(Estilos.OVERLAY_OSCURO);
        
        int[] xPoints = {0, w, w, 0};
        int[] yPoints = {h/4, 0, h, h}; 
        
        g2.fillPolygon(xPoints, yPoints, 4);
    }
}