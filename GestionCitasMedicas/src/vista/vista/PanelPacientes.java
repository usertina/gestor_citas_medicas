package vista;

import modelo.PacienteDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.logging.*;

/**
 * Panel de gestión de pacientes.
 * Permite agregar pacientes y consultar información.
 */
public class PanelPacientes extends JPanel {
    private static final Logger logger = Logger.getLogger(PanelPacientes.class.getName()); // Logger para el panel

    private PacienteDAO pacienteDAO; // DAO para manejar las operaciones de pacientes.
    private PanelCitas panelCitas; // Referencia al PanelCitas.

    /**
     * Constructor del panel de pacientes.
     * 
     * @param panelCitas Referencia al PanelCitas para actualizar pacientes
     *                   dinámicamente.
     */
    public PanelPacientes(PanelCitas panelCitas) {
        this.panelCitas = panelCitas; // Se guarda la referencia para comunicación entre paneles.
        setLayout(new GridBagLayout()); // Usa un diseño centrado.
        pacienteDAO = new PacienteDAO(); // Inicializa el DAO.

        // Configuración del layout para los componentes.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los botones se expanden horizontalmente.
        gbc.insets = new Insets(10, 20, 10, 20); // Espaciado entre componentes.

        // Botón para agregar pacientes.
        JButton botonAgregarPaciente = new JButton("Añadir Paciente");
        botonAgregarPaciente.setPreferredSize(new Dimension(200, 50)); // Tamaño del botón.
        botonAgregarPaciente.addActionListener(e -> insertarPaciente()); // Acción al hacer clic.
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(botonAgregarPaciente, gbc); // Agrega el botón al panel.

        // Añade un margen alrededor del panel.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configuración del Logger (solo se hace una vez)
        try {
            FileHandler fileHandler = new FileHandler("log_pacientes.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            System.err.println("Error al configurar el archivo de log: " + e.getMessage());
        }
    }
    /**
     * Método para insertar un paciente. Muestra un cuadro de diálogo para ingresar
     * los datos.
     */
    private void insertarPaciente() {
        // Campos para los datos del paciente.
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField emailField = new JTextField();
    
        boolean datosValidos = false; // Variable para controlar si los datos son válidos.
    
        // Bucle hasta que los datos sean válidos
        while (!datosValidos) {
            // Configuración del cuadro de diálogo.
            Object[] fields = {
                    "Nombre:", nombreField,
                    "Apellido:", apellidoField,
                    "Teléfono:", telefonoField,
                    "Email:", emailField
            };
    
            // Muestra el cuadro de diálogo y espera la confirmación del usuario.
            int option = JOptionPane.showConfirmDialog(this, fields, "Añadir Paciente", JOptionPane.OK_CANCEL_OPTION);
    
            if (option == JOptionPane.CANCEL_OPTION) {
                return; // Si el usuario presiona "Cancelar", se cierra el diálogo y no se hace nada.
            }
    
            // Verifica si todos los campos están completos
            if (nombreField.getText().isEmpty() || apellidoField.getText().isEmpty()
                    || telefonoField.getText().isEmpty() || emailField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si hay campos vacíos.
            }
    
            // Validación de nombre y apellido (solo letras y espacios).
            if (!PanelMedicos.esTextoValido(nombreField.getText())
                    || !PanelMedicos.esTextoValido(apellidoField.getText())) {
                JOptionPane.showMessageDialog(this, "Por favor, asegúrase de que los campos Nombre y Apellido contengan sólo letras.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si hay caracteres no válidos en el nombre o apellido.
            }
    
            // Validación del teléfono (solo números y 9 dígitos).
            String telefono = telefonoField.getText();
            if (!telefono.matches("\\d{9}")) {
                JOptionPane.showMessageDialog(this, "El teléfono debe contener exactamente 9 dígitos numéricos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si el teléfono es inválido.
            }
    
            // Validación del correo electrónico (formato adecuado).
            String email = emailField.getText();
            if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un correo electrónico válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si el correo es inválido.
            }
    
            // Si pasa todas las validaciones, se marca como válidos los datos.
            datosValidos = true;
    
            try {
                // Llama al DAO para insertar el paciente.
                pacienteDAO.insertarPaciente(
                        nombreField.getText(),
                        apellidoField.getText(),
                        telefonoField.getText(),
                        emailField.getText());
    
                // Log del evento de inserción
                logger.info("Paciente añadido: " + nombreField.getText() + " " + apellidoField.getText());
    
                // Formatear los datos del paciente para mostrarlos en un cuadro de diálogo
                // estilizado.
                String datosPacienteVentana = "<html>Paciente añadido con los siguientes datos:<br>" +
                        "Nombre: <span style='color:green;'>" + nombreField.getText() + "</span><br>" +
                        "Apellido: <span style='color:green;'>" + apellidoField.getText() + "</span><br>" +
                        "Teléfono: <span style='color:blue;'>" + telefonoField.getText() + "</span><br>" +
                        "Email: <span style='color:blue;'>" + emailField.getText() + "</span></html>";
    
                // Mostrar un mensaje de éxito con los detalles del paciente.
                JOptionPane.showMessageDialog(this, datosPacienteVentana, "Paciente añadido",
                        JOptionPane.INFORMATION_MESSAGE);
    
                // Actualizar pacientes en PanelCitas.
                panelCitas.actualizarPacientes();
            } catch (SQLException ex) { // Maneja errores de la base de datos.
                // Log del error
                logger.severe("Error al añadir paciente: " + ex.getMessage());
                // Mostrar un mensaje de error.
                JOptionPane.showMessageDialog(this, "Error al añadir paciente: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
      
}