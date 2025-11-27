import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class LoginDialog extends JDialog {

    private ServiceManager services;
    private String rolActual;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginDialog(Frame parent, boolean modal, String rol, Color colorTema, ServiceManager services) {
        super(parent, modal);
        this.services = services;
        this.rolActual = rol;

        setTitle("Iniciar Sesión");
        setSize(400, 350);
        setLocationRelativeTo(parent); // ESTO CENTRA LA VENTANA RESPECTO A LA APP
        setLayout(new BorderLayout());

        // Panel principal
        JPanel panelContenido = new JPanel();
        panelContenido.setBackground(Color.WHITE);
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Título
        JLabel lblTitulo = new JLabel("Iniciar Sesión como " + rol);
        lblTitulo.setFont(Estilos.FUENTE_TITULO_MODAL);
        lblTitulo.setForeground(Estilos.COLOR_TEXTO_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT); // CENTRADO

        // Campos
        txtUser = crearCampoTexto();
        txtPass = crearCampoPassword();

        // Etiquetas (MODIFICADO PARA QUE ESTÉN EN EL MEDIO)
        JLabel lblUser = new JLabel("Usuario / DNI");
        lblUser.setFont(Estilos.FUENTE_NORMAL);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT); // AHORA CENTRADO

        JLabel lblPass = new JLabel("Contraseña");
        lblPass.setFont(Estilos.FUENTE_NORMAL);
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT); // AHORA CENTRADO

        // Botón Ingresar
        BotonRedondo btnIngresar = new BotonRedondo("Ingresar", colorTema);
        btnIngresar.setAlignmentX(Component.CENTER_ALIGNMENT); // CENTRADO
        btnIngresar.setPreferredSize(new Dimension(200, 40));
        btnIngresar.setMaximumSize(new Dimension(200, 40));

        // Lógica del botón
        btnIngresar.addActionListener(e -> procesarLogin());

        // Armado
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

        MainFrame main = (MainFrame) getParent();

        // 1. ADMINISTRADOR
        if (rolActual.equals("Administrador")) {
            Administrador a = services.getAdministradorService().loginAdmin(user, pass);
            if (a != null) {
                dispose();
                main.mostrarPanelAdmin(a);
            } else {
                mostrarError();
            }
        }
        // 2. MÉDICO
        else if (rolActual.equals("Médico")) {
            Medico m = services.getMedicoService().loginMedico(user, pass);
            if (m != null) {
                dispose();
                main.mostrarPanelMedico(m);
            } else {
                mostrarError();
            }
        }
        // 3. PACIENTE
        else if (rolActual.equals("Paciente")) {
            Paciente p = services.getPacienteService().loginPaciente(user, pass);
            if (p != null) {
                dispose();
                main.mostrarPanelPaciente(p);
            } else {
                mostrarError();
            }
        }
    }

    private void mostrarError() {
        JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Métodos auxiliares visuales
    private JTextField crearCampoTexto() {
        JTextField txt = new JTextField();
        txt.setFont(Estilos.FUENTE_NORMAL);
        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT); // Caja centrada en el panel
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return txt;
    }

    private JPasswordField crearCampoPassword() {
        JPasswordField txt = new JPasswordField();
        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return txt;
    }
}