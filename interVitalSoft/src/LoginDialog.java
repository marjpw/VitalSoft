import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class LoginDialog extends JDialog {

    private GestionClinica gestion;
    private String rolActual;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginDialog(Frame parent, boolean modal, String rol, Color colorTema, GestionClinica gestion) {
        super(parent, modal);
        this.gestion = gestion;
        this.rolActual = rol;

        setTitle("Iniciar Sesión");
        setSize(400, 350);
        setLocationRelativeTo(parent); // Aparece centrada sobre la ventana principal
        setLayout(new BorderLayout());

        // Panel principal con fondo blanco y padding
        JPanel panelContenido = new JPanel();
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(30, 40, 30, 40)); // Márgenes internos

        // Título dinámico
        JLabel lblTitulo = new JLabel("Iniciar Sesión como " + rol);
        lblTitulo.setFont(Estilos.FUENTE_TITULO_MODAL);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campos de texto (Personalizados para que se vean más limpios)
        txtUser = crearCampoTexto();
        txtPass = crearCampoPassword();

        // Etiquetas
        JLabel lblUser = new JLabel("Usuario / DNI");
        lblUser.setFont(Estilos.FUENTE_NORMAL);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(Estilos.FUENTE_NORMAL);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botón Ingresar (Usa el color del rol seleccionado)
        BotonRedondo btnIngresar = new BotonRedondo("Ingresar", colorTema);
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Hacer el botón un poco más grande
        btnIngresar.setPreferredSize(new Dimension(200, 40));
        btnIngresar.setMaximumSize(new Dimension(200, 40));

        // Lógica del botón Ingresar
        btnIngresar.addActionListener(e -> procesarLogin());

        // Armar el formulario con espacios (struts)
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createVerticalStrut(30));
        panelContenido.add(lblUser);
        panelContenido.add(Box.createVerticalStrut(5));
        panelContenido.add(txtUser);
        panelContenido.add(Box.createVerticalStrut(20));
        panelContenido.add(lblPass);
        panelContenido.add(Box.createVerticalStrut(5));
        panelContenido.add(txtPass);
        panelContenido.add(Box.createVerticalStrut(30));
        panelContenido.add(btnIngresar);

        add(panelContenido, BorderLayout.CENTER);
    }

    private void procesarLogin() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        boolean exito = false;
        String nombreUsuario = "";

        // Conectar con la lógica de GestionClinica según el rol
        switch (rolActual) {
            case "Administrador":
                Administrador a = gestion.loginAdmin(user, pass);
                if (a != null) {
                    exito = true;
                    nombreUsuario = a.getNombre();
                }
                break;
            case "Médico":
                Medico m = gestion.loginMedico(user, pass);
                if (m != null) {
                    exito = true;
                    nombreUsuario = m.getApellidos();
                }
                break;
            case "Paciente":
                Paciente p = gestion.loginPaciente(user, pass);
                if (p != null) {
                    exito = true;
                    nombreUsuario = p.getNombre();

                    // CONEXIÓN NUEVA:
                    MainFrame main = (MainFrame) getParent();
                    main.mostrarPanelPaciente(p); // <--- Llamamos al método nuevo
                }
                break;
        }

        if (exito) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido/a " + nombreUsuario + "!");
            dispose(); // Cerramos la ventanita flotante

            // --- NUEVO: CAMBIAR PANTALLA SEGÚN ROL ---
            if (rolActual.equals("Administrador")) {
                // Hacemos un casting porque 'getParent()' devuelve un Frame genérico
                MainFrame main = (MainFrame) getParent();
                // Buscamos el objeto Admin real para pasarlo
                Administrador adminObj = gestion.loginAdmin(user, pass);
                main.mostrarPanelAdmin(adminObj);
            }
            // Aquí agregaremos luego los de Médico y Paciente...
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Métodos auxiliares para dar estilo a los campos de texto (borde inferior
    // simple)
    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField();
        txt.setFont(Estilos.FUENTE_NORMAL);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Solo borde inferior
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Padding interno
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35)); // Altura fija
        return txt;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField txt = new JPasswordField();
        // (Nota: Poner el icono de "ojo" sin librerías es muy complejo, lo dejaremos
        // estándar por ahora)
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return txt;
    }
}