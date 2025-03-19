package vista;

import java.awt.*;
import javax.swing.*;
import modelo.Conexion;

/**
 * Panel de inicio de sesión.
 */
public class LoginPanel extends JPanel {
    private JTextField usuarioField;             // Campo de texto para el usuario
    private JPasswordField contrasenaField;      // Campo de texto para la contraseña
    private JButton mostrarContrasenaButton;    // Botón para mostrar/ocultar contraseña
    /**
     * Constructor del panel de inicio de sesión.
     * 
     * @param ventanaPrincipal Referencia a la ventana principal para cambiar paneles.
     */
    public LoginPanel(VentanaPrincipal ventanaPrincipal) {
        setLayout(new GridBagLayout());          // Usar GridBagLayout para organizar componentes
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Etiqueta y campo de usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuario:"), gbc);

        usuarioField = new JTextField(20);
        gbc.gridx = 1;
        add(usuarioField, gbc);

        // Etiqueta y campo de contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contraseña:"), gbc);

        contrasenaField = new JPasswordField(20);
        gbc.gridx = 1;
        add(contrasenaField, gbc);

        // Botón para mostrar/ocultar contraseña
        mostrarContrasenaButton = new JButton("Mostrar");
        gbc.gridx = 2;
        add(mostrarContrasenaButton, gbc);
        mostrarContrasenaButton.addActionListener(e -> {
            if ("Mostrar".equals(mostrarContrasenaButton.getText())) {
                contrasenaField.setEchoChar((char) 0); // Mostrar contraseña
                mostrarContrasenaButton.setText("Ocultar");
            } else {
                contrasenaField.setEchoChar('\u2022'); // Ocultar contraseña
                mostrarContrasenaButton.setText("Mostrar");
            }
        });

        // Botón de login
        JButton loginButton = new JButton("Iniciar Sesión");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String usuario = usuarioField.getText();
            String contrasena = new String(contrasenaField.getPassword());
        
            // Validar las credenciales con la base de datos
            if (Conexion.verificarLogin(usuario, contrasena)) {
                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso.");
                
                // Después de que el usuario cierre el JOptionPane, cambiamos al PanelBienvenida
                ventanaPrincipal.mostrarPanel(ventanaPrincipal.panelBienvenida); // Cambiar al panel de bienvenida
        
            } else {
                // Mostrar mensaje de error si las credenciales son incorrectas
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
           


    }
}
