package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class CitaDAO {

  // Método para obtener los horarios ocupados de la base de datos
  public List<String> obtenerCitasOcupadas(String medico, String fecha) throws SQLException {
    List<String> horariosOcupados = new ArrayList<>();
    String sql = "SELECT hora FROM citas WHERE medico = ? AND fecha = ?";

    try (Connection conexion = Conexion.conectar();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setString(1, medico);
        pstmt.setString(2, fecha);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Obtener la hora con formato hh:mm:ss y recortarla a hh:mm
                String horaCompleta = rs.getString("hora");
                String horaSinSegundos = horaCompleta.substring(0, 5); // Recorta los segundos
                horariosOcupados.add(horaSinSegundos);
            }
        }
    }
    return horariosOcupados;
}

// Método para calcular horarios disponibles, excluyendo los ocupados
public List<String> generarHorariosDisponibles() {
    List<String> horariosDisponibles = new ArrayList<>();
    LocalTime inicio = LocalTime.of(8, 30);
    LocalTime fin = LocalTime.of(16, 30);

    while (!inicio.isAfter(fin)) {
        // Convertir la hora a formato hh:mm (sin segundos)
        String hora = inicio.toString().substring(0, 5); // Recorta los segundos
        horariosDisponibles.add(hora);
        inicio = inicio.plusMinutes(10); // Sumar 10 minutos a la hora actual
    }

    return horariosDisponibles;
}


// Método para insertar una nueva cita
public void insertarCita(String nombrePaciente, String nombreMedico, String especialidad, String sala, String fecha, String hora) throws SQLException {
    String sql = "INSERT INTO citas (paciente, medico, especialidad, sala, fecha, hora) VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conexion = Conexion.conectar(); 
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {

        pstmt.setString(1, nombrePaciente);
        pstmt.setString(2, nombreMedico);
        pstmt.setString(3, especialidad);
        pstmt.setString(4, sala);
        pstmt.setString(5, fecha);
        pstmt.setString(6, hora);

        int filasInsertadas = pstmt.executeUpdate();
        if (filasInsertadas <= 0) {
            throw new SQLException("No se pudo registrar la cita.");
        }
    } catch (SQLException ex) {
        throw new SQLException("Error al registrar la cita: " + ex.getMessage());
    }
}


// Método para obtener la sala de un médico
public String obtenerSalaPorMedico(String medicoNombre) throws SQLException {
    String sql = "SELECT sala FROM medicos WHERE nombre = ?";
    String sala = null;

    try (Connection conexion = Conexion.conectar(); 
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {

        pstmt.setString(1, medicoNombre);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                sala = rs.getString("sala");
            }
        }
    }
    return sala;
}

// Método para obtener los médicos por especialidad
public List<String> obtenerMedicosPorEspecialidad(String especialidad) throws SQLException {
    List<String> medicos = new ArrayList<>();
    String sql = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM medicos WHERE especialidad = ?";

    try (Connection conexion = Conexion.conectar();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        pstmt.setString(1, especialidad); // Establecer la especialidad en la consulta

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Obtener el nombre completo del médico y agregarlo a la lista
                medicos.add(rs.getString("nombre_completo"));
            }
        }
    }
    return medicos;
}
}