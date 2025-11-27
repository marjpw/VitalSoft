import java.awt.Color;
import java.awt.Font;

public class Estilos {
    // Colores extraídos de tu boceto
    public static final Color TURQUESA_ADMIN = new Color(0, 188, 212);
    public static final Color VERDE_MEDICO = new Color(139, 195, 74);
    public static final Color NARANJA_PACIENTE = new Color(255, 152, 0);

    public static final Color COLOR_TEXTO_PRINCIPAL = new Color(66, 66, 66);
    public static final Color COLOR_BLANCO = Color.WHITE;
    // Color negro semi-transparente para la superposición diagonal
    public static final Color OVERLAY_OSCURO = new Color(0, 0, 0, 150);

    // Fuentes
    // Usamos "SansSerif" que se ve moderna en cualquier PC (Windows/Mac/Linux)
    public static final Font FUENTE_TITULO_LOGO = new Font("Century Gothic", Font.BOLD, 28);
    public static final Font FUENTE_SUBTITULO_LOGO = new Font("Century Gothic", Font.PLAIN, 16);
    public static final Font FUENTE_BOTONES = new Font("Century Gothic", Font.BOLD, 16);
    public static final Font FUENTE_TITULO_MODAL = new Font("Century Gothic", Font.BOLD, 20);
    public static final Font FUENTE_NORMAL = new Font("Century Gothic", Font.PLAIN, 14);
}