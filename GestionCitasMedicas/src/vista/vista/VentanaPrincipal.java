package vista;

// Importaciones necesarias para trabajar con la interfaz gráfica de Java
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Clase principal que extiende JFrame para crear la ventana principal del programa
public class VentanaPrincipal extends JFrame {
    // Declaración de paneles que compondrán la interfaz gráfica
    private PanelMedicos panelMedicos;
    private PanelCitas panelCitas;
    private PanelPacientes panelPacientes;
    JPanel panelBienvenida;
    private final LoginPanel loginPanel; // Panel para el inicio de sesión

    // Constructor de la clase
    public VentanaPrincipal() {
        setTitle("Sistema de Gestión Médica"); // Título de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Acción al cerrar la ventana
        setSize(600, 400); // Tamaño inicial de la ventana
        setLayout(new BorderLayout()); // Layout principal de la ventana

        // Inicialización de los paneles
        panelMedicos = new PanelMedicos(); // Panel de gestión de médicos
        panelCitas = new PanelCitas(); // Panel de gestión de citas
        panelPacientes = new PanelPacientes(panelCitas); // Panel de gestión de pacientes con dependencia de panelCitas
        panelBienvenida = new PanelBienvenida(); // Panel de bienvenida
        loginPanel = new LoginPanel(this); // Panel de inicio de sesión vinculado a esta ventana

        // Creación de la barra de menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones"); // Menú principal

        // Elementos del menú
        JMenuItem itemInicio = new JMenuItem("Inicio"); // Opción para volver al inicio
        JMenuItem itemMedicos = new JMenuItem("Gestionar Médicos"); // Opción para gestionar médicos
        JMenuItem itemPacientes = new JMenuItem("Gestionar Pacientes"); // Opción para gestionar pacientes
        JMenuItem itemCitas = new JMenuItem("Gestionar Citas"); // Opción para gestionar citas
        JMenuItem itemSalir = new JMenuItem("Salir"); // Opción para salir del programa
        JCheckBoxMenuItem modoOscuro = new JCheckBoxMenuItem("Modo Oscuro"); // Opción para activar/desactivar el modo oscuro

        // Configuración de las acciones para cada elemento del menú
        itemInicio.addActionListener(e -> mostrarPanel(panelBienvenida)); // Muestra el panel de bienvenida
        itemMedicos.addActionListener(e -> mostrarPanel(panelMedicos)); // Muestra el panel de médicos
        itemPacientes.addActionListener(e -> mostrarPanel(panelPacientes)); // Muestra el panel de pacientes
        itemCitas.addActionListener(e -> mostrarPanel(panelCitas)); // Muestra el panel de citas
        itemSalir.addActionListener(e -> salirDelPrograma()); // Cierra el programa con confirmación
        modoOscuro.addActionListener(e -> toggleModoOscuro(modoOscuro.isSelected())); // Activa/desactiva el modo oscuro

        // Añadir los elementos al menú
        menu.add(itemInicio);
        menu.add(itemMedicos);
        menu.add(itemPacientes);
        menu.add(itemCitas);
        menu.addSeparator(); // Separador visual en el menú
        menu.add(modoOscuro);
        menu.add(itemSalir);
        menuBar.add(menu); // Añadir el menú a la barra de menú
        setJMenuBar(menuBar); // Establecer la barra de menú en la ventana

        // Mostrar el panel de inicio de sesión al iniciar la aplicación
        mostrarPanel(loginPanel);
        setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        setVisible(true); // Hacer visible la ventana
    }

    // Método para alternar el modo oscuro
    private void toggleModoOscuro(boolean oscuro) {
        // Colores para el fondo y el texto dependiendo del modo seleccionado
        Color fondo = oscuro ? Color.DARK_GRAY : Color.WHITE;
        Color texto = oscuro ? Color.LIGHT_GRAY : Color.BLACK;

        // Cambia los colores del panel de bienvenida
        panelBienvenida.setBackground(fondo);
        for (Component c : panelBienvenida.getComponents()) {
            if (c instanceof JLabel) {
                c.setForeground(texto); // Cambia el color del texto
            }
        }

       // Cambiar colores de todos los paneles
       cambiarColoresRecursivo(panelMedicos, fondo, texto);
       cambiarColoresRecursivo(panelCitas, fondo, texto);
       cambiarColoresRecursivo(panelPacientes, fondo, texto);
       cambiarColoresRecursivo(loginPanel, fondo, texto);

       // Actualizar la interfaz gráfica
       SwingUtilities.updateComponentTreeUI(this);
   }

   private void cambiarColoresRecursivo(Component componente, Color fondo, Color texto) {
    // Cambiar el fondo y texto de cada componente
    componente.setBackground(fondo);
    if (componente instanceof JLabel || componente instanceof JButton || componente instanceof JTextField) {
        componente.setForeground(texto);
    }

    // Si es un contenedor, aplicar recursivamente a sus hijos
    if (componente instanceof Container) {
        for (Component child : ((Container) componente).getComponents()) {
            cambiarColoresRecursivo(child, fondo, texto);
        }
    }
     // Configurar estilos específicos para JComboBox si es necesario
        if (componente instanceof JComboBox) {
            ((JComboBox<?>) componente).setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    c.setBackground(isSelected ? fondo.darker() : fondo);
                    c.setForeground(texto);
                    return c;
                }
            });
        }
    }

    // Método para cambiar el panel visible en la ventana principal
    public void mostrarPanel(JPanel panel) {
        getContentPane().removeAll(); // Eliminar todos los componentes del contenedor actual
        getContentPane().add(panel, BorderLayout.CENTER); // Agregar el nuevo panel
        revalidate(); // Realizar la validación del nuevo panel
        repaint(); // Redibujar la ventana
    }

   // Método para salir del programa con confirmación y despedida
private void salirDelPrograma() {
    String[] opciones = {"Sí", "No"};
    int confirm = JOptionPane.showOptionDialog(this,
            "¿Está seguro de que desea salir?", // Mensaje de confirmación
            "Confirmar salida", // Título del diálogo
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[1]); // Valor por defecto (No)

    if (confirm == 0) { // Si el usuario elige "Sí"
        // Mostrar mensaje de despedida con un icono
        ImageIcon icono = new ImageIcon("src/iconos/contento.png"); // Reemplaza con la ruta de tu logo
        JOptionPane.showMessageDialog(this,
                "Gracias por usar este programa.", // Mensaje de despedida
                "Despedida", // Título del mensaje
                JOptionPane.INFORMATION_MESSAGE,
                icono); // Icono personalizado

        System.exit(0); // Termina la aplicación
    }
}


    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VentanaPrincipal::new); // Ejecuta la creación de la ventana en el hilo de eventos
    }
}
