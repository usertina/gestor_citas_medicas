package vista;

import modelo.MedicoDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.*;

/**
 * Panel de gestión de médicos que interactúa con la base de datos
 * usando la clase MedicoDAO.
 */
public class PanelMedicos extends JPanel {

    public static boolean esTextoValido(String texto) {
        return texto.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+");
    }

    private static final Logger logger = Logger.getLogger(PanelMedicos.class.getName()); // Logger para el panel

    private MedicoDAO medicoDAO; // DAO para manejar las operaciones de la base de datos.

    /**
     * Constructor del panel que inicializa los botones y acciones.
     */
    public PanelMedicos() {
        setLayout(new GridBagLayout()); // Usa un diseño que permite centrar los componentes.
        medicoDAO = new MedicoDAO(); // Inicializa el DAO.

        // Configuración del layout para posicionar los botones.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Los botones se expanden horizontalmente.
        gbc.insets = new Insets(10, 20, 10, 20); // Espaciado entre los componentes.

        // Botón para agregar médicos.
        JButton botonAgregarMedico = new JButton("Agregar Médico");
        botonAgregarMedico.setPreferredSize(new Dimension(200, 50)); // Tamaño del botón.
        botonAgregarMedico.addActionListener(e -> insertarMedico()); // Acción al hacer clic.
        gbc.gridx = 0; // Posición X en el layout.
        gbc.gridy = 0; // Posición Y en el layout.
        add(botonAgregarMedico, gbc); // Agrega el botón al panel.

        // Botón para eliminar médicos.
        JButton botonEliminarMedico = new JButton("Eliminar Médico");
        botonEliminarMedico.setPreferredSize(new Dimension(200, 50));
        botonEliminarMedico.addActionListener(e -> eliminarMedico());
        gbc.gridy = 1; // Cambia la posición vertical.
        add(botonEliminarMedico, gbc);

        // Botón para consultar médicos.
        JButton botonConsultarMedico = new JButton("Consultar Médicos");
        botonConsultarMedico.setPreferredSize(new Dimension(200, 50));
        botonConsultarMedico.addActionListener(e -> consultarMedicos());
        gbc.gridy = 2; // Cambia la posición vertical.
        add(botonConsultarMedico, gbc);

        // Botón para modificar médicos.
        JButton botonModificarMedico = new JButton("Modificar Médico");
        botonModificarMedico.setPreferredSize(new Dimension(200, 50));
        botonModificarMedico.addActionListener(e -> modificarMedico());
        gbc.gridy = 3; // Cambia la posición vertical.
        add(botonModificarMedico, gbc);

        // Establece un margen alrededor del panel.
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configuración del Logger (solo se hace una vez)
        try {
            FileHandler fileHandler = new FileHandler("log_medicos.log", true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            System.err.println("Error al configurar el archivo de log: " + e.getMessage());
        }
    }

    /**
     * Método para insertar un médico. Muestra un cuadro de diálogo para ingresar
     * los datos.
     */
    private void insertarMedico() {
        // Campos de texto para los datos del médico.
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField especialidadField = new JTextField();
        JTextField salaField = new JTextField();
        
        boolean datosValidos = false; // Variable para controlar si los datos son válidos.
        
        // Bucle hasta que los datos sean válidos
        while (!datosValidos) {
            // Configuración del cuadro de diálogo.
            Object[] fields = {
                    "Nombre:", nombreField,
                    "Apellido:", apellidoField,
                    "Especialidad:", especialidadField,
                    "Sala:", salaField
            };
        
            // Muestra el cuadro de diálogo y espera la confirmación del usuario.
            int option = JOptionPane.showConfirmDialog(this, fields, "Agregar Médico", JOptionPane.OK_CANCEL_OPTION);
        
            if (option == JOptionPane.CANCEL_OPTION) {
                return; // Si el usuario presiona "Cancelar", se cierra el diálogo y no se hace nada.
            }
        
            // Verifica si todos los campos están completos
            if (nombreField.getText().isEmpty() || apellidoField.getText().isEmpty()
                    || especialidadField.getText().isEmpty() || salaField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si hay campos vacíos.
            }
        
            // Validación de nombre, apellido y especialidad (solo letras y espacios).
            if (!esTextoValido(nombreField.getText()) ||
                    !esTextoValido(apellidoField.getText()) ||
                    !esTextoValido(especialidadField.getText())) {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa solo letras en los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si hay caracteres no válidos en los campos.
            }
    
            // Validación de la sala: debe tener una letra mayúscula seguida de 3 dígitos.
            String sala = salaField.getText();
            if (!sala.matches("^[A-Z]\\d{3}$")) {
                JOptionPane.showMessageDialog(this, "La sala debe tener una letra mayúscula seguida de 3 dígitos numéricos (ejemplo: A123).", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                continue; // Vuelve a mostrar el cuadro de diálogo si la sala no es válida.
            }
        
            // Si pasa todas las validaciones, se marca como válidos los datos.
            datosValidos = true;
        
            try {
                // Llama al DAO para insertar el médico.
                medicoDAO.insertarMedico(
                        nombreField.getText(),
                        apellidoField.getText(),
                        especialidadField.getText(),
                        salaField.getText());
        
                // Log del evento de inserción
                logger.info("Médico agregado: " + nombreField.getText() + " " + apellidoField.getText());
        
                // Formatear los datos del médico para mostrarlos en un cuadro de diálogo estilizado.
                String datosMedicoVentana = "<html>Médico agregado con los siguientes datos:<br>" +
                        "Nombre: <span style='color:green;'>" + nombreField.getText() + "</span><br>" +
                        "Apellido: <span style='color:green;'>" + apellidoField.getText() + "</span><br>" +
                        "Especialidad: <span style='color:blue;'>" + especialidadField.getText() + "</span><br>" +
                        "Sala: <span style='color:blue;'>" + salaField.getText() + "</span></html>";
        
                // Mostrar un mensaje de éxito con los detalles del médico.
                JOptionPane.showMessageDialog(this, datosMedicoVentana, "Médico agregado",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) { // Maneja errores de la base de datos.
                // Log del error
                logger.severe("Error al agregar médico: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al agregar médico: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    

    /**
     * Método auxiliar para validar que una cadena solo contenga letras y espacios.
     *
     * @param texto Texto a validar.
     * @return true si el texto solo contiene letras y espacios; false en caso
     *         contrario.
     */

    /**
     * Método para eliminar un médico. Solicita nombre y apellido.
     */
    private void eliminarMedico() {
        // Campos para los datos necesarios.
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
    
        // Configuración del cuadro de diálogo.
        Object[] fields = {
                "Nombre:", nombreField,
                "Apellido:", apellidoField
        };
    
        // Muestra el cuadro de diálogo.
        int option = JOptionPane.showConfirmDialog(this, fields, "Eliminar Médico", JOptionPane.OK_CANCEL_OPTION);
    
        if (option == JOptionPane.OK_OPTION) { // Si el usuario presiona "OK".
            // Validar que solo haya letras en los campos de nombre y apellido.
            if (!esTextoValido(nombreField.getText()) || !esTextoValido(apellidoField.getText())) {
                JOptionPane.showMessageDialog(this, "Por favor, asegúrese de ingresar solo letras en los campos Nombre y Apellido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            try {
                // Verificar si el médico existe en la base de datos antes de intentar eliminarlo
                boolean existeMedico = medicoDAO.existeMedico(nombreField.getText(), apellidoField.getText());
    
                if (!existeMedico) {
                    JOptionPane.showMessageDialog(this, "No se encontró un médico con ese nombre y apellido. Verifique los datos.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return; // Si no existe, no continúa con la eliminación.
                }
    
                // Llama al DAO para eliminar el médico si existe.
                medicoDAO.eliminarMedico(nombreField.getText(), apellidoField.getText());
    
                // Log del evento de eliminación
                logger.info("Médico eliminado: " + nombreField.getText() + " " + apellidoField.getText());
    
                // Mostrar mensaje de éxito
                JOptionPane.showMessageDialog(this, "Médico eliminado correctamente.");
    
            } catch (SQLException ex) { // Maneja errores de la base de datos.
                // Log del error
                logger.severe("Error al eliminar médico: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al eliminar médico: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    /**
     * Método para consultar todos los médicos y mostrarlos en un cuadro de diálogo.
     */
    private void consultarMedicos() {
        try {
            // Obtiene la lista de médicos del DAO.
            List<String> medicos = medicoDAO.consultarMedicos();

            if (medicos.isEmpty()) { // Si no hay médicos registrados.
                JOptionPane.showMessageDialog(this, "No hay médicos registrados.", "Consulta",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Muestra la lista de médicos en un cuadro con scroll.
                JList<String> listaMedicos = new JList<>(medicos.toArray(new String[0]));
                JScrollPane scrollPane = new JScrollPane(listaMedicos);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(this, scrollPane, "Lista de Médicos", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) { // Maneja errores de la base de datos.
            // Log del error
            logger.severe("Error al consultar médicos: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al consultar médicos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para modificar un médico. Solicita los datos actuales y nuevos.
     */
    private void modificarMedico() {
        // Campos para los datos necesarios.
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField nuevaEspecialidadField = new JTextField();
        JTextField nuevaSalaField = new JTextField();

        // Configuración del cuadro de diálogo.
        Object[] fields = {
                "Nombre:", nombreField,
                "Apellido:", apellidoField,
                "Nueva Especialidad:", nuevaEspecialidadField,
                "Nueva Sala:", nuevaSalaField
        };

        // Muestra el cuadro de diálogo.
        int option = JOptionPane.showConfirmDialog(this, fields, "Modificar Médico", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) { // Si el usuario presiona "OK".
            if (!esTextoValido(nombreField.getText()) ||
                    !esTextoValido(apellidoField.getText()) ||
                    !esTextoValido(nuevaEspecialidadField.getText()))

            {
                JOptionPane.showMessageDialog(this, "Por favor, ingresa solo letras en los campos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Obtiene la especialidad y sala actuales del médico.
                String especialidadActual = medicoDAO.obtenerEspecialidad(nombreField.getText(),
                        apellidoField.getText());
                String salaActual = medicoDAO.obtenerSalaPorMedico(nombreField.getText(), apellidoField.getText());

                if (especialidadActual != null && salaActual != null) { // Verifica que el médico exista.
                    // Llama al DAO para modificar el médico.
                    medicoDAO.modificarMedico(
                            nombreField.getText(),
                            apellidoField.getText(),
                            especialidadActual,
                            salaActual,
                            nuevaEspecialidadField.getText(),
                            nuevaSalaField.getText());
                    // Log del evento de modificación
                    logger.info("Médico modificado: " + nombreField.getText() + " " + apellidoField.getText());
                    JOptionPane.showMessageDialog(this, "Médico modificado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Médico no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) { // Maneja errores de la base de datos.
                // Log del error
                logger.severe("Error al modificar médico: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al modificar médico: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
