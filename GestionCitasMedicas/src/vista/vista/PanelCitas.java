package vista;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.*;
import javax.swing.*;
import modelo.CitaDAO;
import modelo.FechaUtil;
import modelo.PacienteDAO;

public class PanelCitas extends JPanel {
    // Declarar el Logger para registrar eventos
    private static final Logger logger = Logger.getLogger(PanelCitas.class.getName());

    // Componentes de la interfaz gráfica
    private JComboBox<String> comboPacientes, comboEspecialidades, comboMedicos, comboFechas, comboHorarios;
    private JTextField campoSala; // Campo para mostrar la sala del médico seleccionado
    private JButton btnAgendar; // Botón para agendar la cita

    public PanelCitas() {
        // Configuración del Logger (solo se hace una vez)
        try {
            // Crear un archivo de log para registrar los eventos
            FileHandler fileHandler = new FileHandler("log_citas.log", true); // Registra en un archivo
            SimpleFormatter formatter = new SimpleFormatter(); // Formato sencillo para el log
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Error al configurar el archivo de log: " + e.getMessage());
        }

        // Configurar el layout de la interfaz
        setLayout(new BorderLayout());
        setBackground(Color.BLACK); // Establecer el fondo de todo el panel a negro

        // Crear panel para los campos de entrada con GridBagLayout
        JPanel panelContenido = new JPanel(new GridBagLayout());
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Agregar un margen
        panelContenido.setBackground(Color.BLACK); // Fondo negro para el panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado entre los elementos
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Cambiar color del texto de las etiquetas a blanco
        JLabel labelPaciente = new JLabel("Paciente:");
        labelPaciente.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelContenido.add(labelPaciente, gbc);

        // Campo de selección de paciente
        gbc.gridx = 1;
        panelContenido.add(comboPacientes = new JComboBox<>(), gbc); // ComboBox para seleccionar paciente

        // Campo de selección de especialidad
        JLabel labelEspecialidad = new JLabel("Especialidad:");
        labelEspecialidad.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelContenido.add(labelEspecialidad, gbc);
        gbc.gridx = 1;
        panelContenido.add(comboEspecialidades = new JComboBox<>(), gbc); // ComboBox para seleccionar especialidad

        // Campo de selección de médico
        JLabel labelMedico = new JLabel("Médico:");
        labelMedico.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelContenido.add(labelMedico, gbc);
        gbc.gridx = 1;
        panelContenido.add(comboMedicos = new JComboBox<>(), gbc); // ComboBox para seleccionar médico

        // Campo para mostrar la sala del médico seleccionado
        JLabel labelSala = new JLabel("Sala:");
        labelSala.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelContenido.add(labelSala, gbc);
        gbc.gridx = 1;
        campoSala = new JTextField();
        campoSala.setPreferredSize(new Dimension(200, 25)); // Ajustar el tamaño del campo
        campoSala.setEditable(false); // El campo no será editable por el usuario
        campoSala.setBackground(Color.BLACK); // Fondo negro para el campo
        campoSala.setForeground(Color.WHITE); // Texto blanco en el campo
        panelContenido.add(campoSala, gbc);

        // Campo de selección de fecha
        JLabel labelFecha = new JLabel("Fecha:");
        labelFecha.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelContenido.add(labelFecha, gbc);
        gbc.gridx = 1;
        panelContenido.add(comboFechas = new JComboBox<>(), gbc); // ComboBox para seleccionar fecha

        // Campo de selección de horario
        JLabel labelHorario = new JLabel("Horario:");
        labelHorario.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panelContenido.add(labelHorario, gbc);
        gbc.gridx = 1;
        panelContenido.add(comboHorarios = new JComboBox<>(), gbc); // ComboBox para seleccionar horario

        // Botón para agendar cita
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        btnAgendar = new JButton("Agendar Cita"); // Crear botón para agendar cita
        btnAgendar.setPreferredSize(new Dimension(200, 50)); // Ajustar tamaño del botón
        btnAgendar.setBackground(Color.DARK_GRAY); // Fondo oscuro para el botón
        btnAgendar.setForeground(Color.WHITE); // Texto blanco para el botón
        btnAgendar.addActionListener(e -> agendarCita()); // Acción al hacer clic en el botón
        panelContenido.add(btnAgendar, gbc);

        // Agregar el panel de contenido al centro del layout principal
        add(panelContenido, BorderLayout.CENTER);

        // Llamada a métodos para cargar datos de la base de datos y actualizar la
        // interfaz
        actualizarPacientes();
        PacienteDAO pacienteDAO = new PacienteDAO();

        try {
            // Cargar especialidades desde la base de datos
            String[] especialidades = pacienteDAO.obtenerEspecialidades();
            for (String especialidad : especialidades) {
                comboEspecialidades.addItem(especialidad); // Agregar especialidades al ComboBox
            }

            // Cargar las próximas 10 fechas laborales
            List<String> fechasLaborales = FechaUtil.generarFechasLaborales(10);
            for (String fecha : fechasLaborales) {
                comboFechas.addItem(fecha); // Agregar fechas disponibles al ComboBox
            }
        } catch (SQLException ex) {
            logger.severe("Error al cargar datos iniciales: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar datos iniciales: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Acciones para actualizar dinámicamente los médicos y horarios
        comboEspecialidades.addActionListener(e -> cargarMedicos()); // Cuando se cambia la especialidad, cargar médicos
        comboFechas.addActionListener(e -> {
            try {
                cargarHorarios(); // Cuando se cambia la fecha, cargar horarios disponibles
            } catch (SQLException ex) {
                logger.severe("Error al cargar horarios: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Método para actualizar el ComboBox de pacientes
    public void actualizarPacientes() {
        PacienteDAO pacienteDAO = new PacienteDAO();
        try {
            comboPacientes.removeAllItems(); // Limpiar el ComboBox antes de cargar los pacientes
            String ultimoPaciente = pacienteDAO.obtenerUltimoPaciente();
            if (ultimoPaciente != null) {
                comboPacientes.addItem(ultimoPaciente); // Agregar el último paciente registrado
                logger.info("Se ha actualizado el ComboBox de pacientes. Último paciente: " + ultimoPaciente);
            }
        } catch (SQLException ex) {
            logger.severe("Error al actualizar pacientes: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al actualizar pacientes: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cargar médicos basados en la especialidad seleccionada
    private void cargarMedicos() {
        String especialidad = (String) comboEspecialidades.getSelectedItem();
        if (especialidad == null)
            return;

        CitaDAO citaDAO = new CitaDAO();
        try {
            List<String> medicos = citaDAO.obtenerMedicosPorEspecialidad(especialidad); // Obtener médicos por
                                                                                        // especialidad
            comboMedicos.removeAllItems(); // Limpiar el ComboBox antes de cargar los médicos
            for (String medico : medicos) {
                comboMedicos.addItem(medico); // Agregar médicos al ComboBox
            }

            // Actualizar la sala cuando se selecciona un médico
            comboMedicos.addActionListener(e -> {
                String medicoSeleccionado = (String) comboMedicos.getSelectedItem();
                if (medicoSeleccionado != null) {
                    try {
                        String[] nombres = medicoSeleccionado.split(" ");
                        String sala = citaDAO.obtenerSalaPorMedico(nombres[0]); // Obtener sala del médico seleccionado
                        campoSala.setText(sala); // Mostrar la sala en el campo correspondiente
                    } catch (SQLException ex) {
                        logger.severe("Error al obtener la sala del médico: " + ex.getMessage());
                        JOptionPane.showMessageDialog(this, "Error al obtener la sala del médico: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        } catch (SQLException ex) {
            logger.severe("Error al cargar médicos: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Error al cargar médicos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para cargar horarios disponibles en la fecha seleccionada
    private void cargarHorarios() throws SQLException {
        String fechaSeleccionada = (String) comboFechas.getSelectedItem();
        String medicoSeleccionado = (String) comboMedicos.getSelectedItem();

        if (fechaSeleccionada == null || medicoSeleccionado == null) {
            logger.warning("Faltan datos: fecha o médico no seleccionados.");
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un médico y una fecha.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        CitaDAO citaDAO = new CitaDAO();
        List<String> horariosDisponibles = citaDAO.generarHorariosDisponibles(); // Obtener todos los horarios
                                                                                 // disponibles
        List<String> horariosOcupados = citaDAO.obtenerCitasOcupadas(medicoSeleccionado, fechaSeleccionada); // Obtener
                                                                                                             // horarios
                                                                                                             // ocupados

        // Filtrar los horarios ocupados
        horariosDisponibles.removeIf(hora -> horariosOcupados.contains(hora)); // Eliminar los horarios ocupados

        comboHorarios.removeAllItems(); // Limpiar el JComboBox de horarios

        // Agregar los horarios disponibles al JComboBox
        if (horariosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay horarios disponibles para la fecha y médico seleccionados.",
                    "Sin disponibilidad", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String horario : horariosDisponibles) {
                comboHorarios.addItem(horario); // Agregar horarios disponibles
            }
        }

        // Actualizar la interfaz
        comboHorarios.revalidate();
        comboHorarios.repaint();
    }

    // Método para agendar la cita
    private void agendarCita() {
        CitaDAO citaDAO = new CitaDAO();

        try {
            // Obtener el último paciente registrado
            String ultimoPaciente = (String) comboPacientes.getSelectedItem();
            if (ultimoPaciente == null) {
                logger.warning("Intento de agendar cita sin seleccionar un paciente.");
                JOptionPane.showMessageDialog(null, "No hay pacientes registrados aún.");
                return;
            }

            // Obtener la especialidad seleccionada
            String especialidadSeleccionada = (String) comboEspecialidades.getSelectedItem();
            if (especialidadSeleccionada == null) {
                logger.warning("Intento de agendar cita sin seleccionar una especialidad.");
                JOptionPane.showMessageDialog(null, "Debe seleccionar una especialidad.");
                return;
            }

            // Obtener el médico seleccionado
            String medicoSeleccionado = (String) comboMedicos.getSelectedItem();
            if (medicoSeleccionado == null) {
                logger.warning("Intento de agendar cita sin seleccionar un médico.");
                JOptionPane.showMessageDialog(null, "Debe seleccionar un médico.");
                return;
            }

            // Obtener la sala, fecha y hora seleccionados
            String sala = campoSala.getText();
            String fechaSeleccionada = (String) comboFechas.getSelectedItem();
            String horaSeleccionada = (String) comboHorarios.getSelectedItem();

            if (sala.isEmpty() || fechaSeleccionada == null || horaSeleccionada == null) {
                logger.warning("Faltan datos para agendar la cita: Sala, fecha o hora no seleccionados.");
                JOptionPane.showMessageDialog(null, "Debe seleccionar todos los campos correctamente.");
                return;
            }

            // Registrar la cita en la base de datos
            citaDAO.insertarCita(ultimoPaciente, medicoSeleccionado, especialidadSeleccionada, sala, fechaSeleccionada,
                    horaSeleccionada);

            logger.info("Cita agendada correctamente para el paciente: " + ultimoPaciente +
                    ", médico: " + medicoSeleccionado + ", especialidad: " + especialidadSeleccionada +
                    ", sala: " + sala + ", fecha: " + fechaSeleccionada + " a las " + horaSeleccionada);

            // Mostrar detalles de la cita agendada
            String datosCitaVentana = "<html>Cita agendada correctamente con los siguientes detalles:<br>" +
                    "Paciente: <span style='color:green;'>" + ultimoPaciente + "</span><br>" +
                    "Médico: <span style='color:green;'>" + medicoSeleccionado + "</span><br>" +
                    "Especialidad: <span style='color:blue;'>" + especialidadSeleccionada + "</span><br>" +
                    "Sala: <span style='color:blue;'>" + sala + "</span><br>" +
                    "Fecha: <span style='color:blue;'>" + fechaSeleccionada + "</span><br>" +
                    "Hora: <span style='color:blue;'>" + horaSeleccionada + "</span></html>";

            JOptionPane.showMessageDialog(this, datosCitaVentana, "Cita Creada", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            logger.severe("Error al gestionar la cita: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, "Error al gestionar la cita: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
