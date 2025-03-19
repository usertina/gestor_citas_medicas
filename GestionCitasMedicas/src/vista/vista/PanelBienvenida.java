package vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Clase PanelBienvenida que muestra un mensaje de bienvenida con animación.
 */
public class PanelBienvenida extends JPanel {

    private JLabel labelBienvenida; // Etiqueta para mostrar el mensaje de bienvenida
    private Timer animacionTimer; // Temporizador para manejar la animación
    private float hue; // Valor para el tono del color dinámico (0.0 a 1.0)

    /**
     * Constructor del panel de bienvenida.
     * Configura el diseño, el mensaje y la animación.
     */
    public PanelBienvenida() {
        // Configuración del layout como BorderLayout
        setLayout(new BorderLayout());

        // Creación del JLabel con el mensaje de bienvenida
        labelBienvenida = new JLabel("BIENVENID@", SwingConstants.CENTER);
        labelBienvenida.setFont(new Font("Arial", Font.BOLD, 48)); // Fuente grande y negrita
        labelBienvenida.setForeground(Color.BLUE); // Color inicial del texto

        // Añadir el JLabel al centro del panel
        add(labelBienvenida, BorderLayout.CENTER);

        // Agregar márgenes al panel
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Inicializar el valor del tono para el color dinámico
        hue = 0.0f;

        // Configurar el temporizador para actualizar el color del texto
        animacionTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarColorTexto();
            }
        });

        // Iniciar la animación
        animacionTimer.start();
    }

    /**
     * Actualiza el color del texto del JLabel de manera dinámica.
     */
    private void actualizarColorTexto() {
        // Incrementar el valor del tono para generar colores en el espectro HSV
        hue += 0.01f;
        if (hue > 1.0f) {
            hue = 0.0f; // Reinicia el ciclo del color
        }

        // Convertir el tono a un color RGB y aplicarlo al texto
        labelBienvenida.setForeground(Color.getHSBColor(hue, 1.0f, 1.0f));

        // Redibujar el panel para reflejar los cambios
        repaint();
    }
}
