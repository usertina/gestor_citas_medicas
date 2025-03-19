package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    // Método para insertar un médico en la base de datos
    public void insertarMedico(String nombre, String apellido, String especialidad, String sala) throws SQLException {
        String sql = "INSERT INTO medicos (nombre, apellido, especialidad, sala) VALUES (?, ?, ?, ?)";
        
        // Se conecta a la base de datos y se prepara la consulta
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, especialidad);
            pstmt.setString(4, sala);

            // Ejecutar la consulta y verificar si se insertaron filas
            int filasInsertadas = pstmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Médico insertado correctamente.");
            } else {
                System.err.println("No se pudo insertar el médico.");
            }
        }
    }

    // Método para eliminar un médico de la base de datos
    public void eliminarMedico(String nombre, String apellido) throws SQLException {
        String sql = "DELETE FROM medicos WHERE nombre = ? AND apellido = ?";
        
        // Se conecta a la base de datos y se prepara la consulta para eliminar al médico
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            
            // Ejecutar la consulta de eliminación y verificar si se eliminaron filas
            int filasEliminadas = pstmt.executeUpdate();
            if (filasEliminadas > 0) {
                System.out.println("Médico eliminado correctamente.");
            } else {
                System.err.println("No se pudo eliminar el médico.");
            }
        }
    }
    public boolean existeMedico(String nombre, String apellido) throws SQLException {
        // Consulta SQL para verificar si el médico existe
        String query = "SELECT COUNT(*) FROM medicos WHERE nombre = ? AND apellido = ?";
        
        try (Connection conexion = Conexion.conectar();
            PreparedStatement stmt = conexion.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, apellido);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si el conteo es mayor que 0, el médico existe.
            }
        }
        
        return false; // El médico no existe.
    }
    

    // Método para consultar todos los médicos en la base de datos
    public List<String> consultarMedicos() throws SQLException {
        List<String> medicos = new ArrayList<>();
        String sql = "SELECT nombre, apellido, especialidad, sala FROM medicos";
        
        // Se conecta a la base de datos y prepara la consulta para obtener todos los médicos
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            // Iterar sobre los resultados y agregar los médicos a la lista
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String especialidad = rs.getString("especialidad");
                String sala = rs.getString("sala");
                medicos.add(nombre + " " + apellido + " - Especialidad: " + especialidad + " - Sala: " + sala);
            }
        }
        return medicos; // Retorna la lista de médicos
    }

    // Método para modificar los datos de un médico en la base de datos
    public void modificarMedico(String nombre, String apellido, String especialidadActual, String salaActual, String nuevaEspecialidad, String nuevaSala) throws SQLException {
        String sql = "UPDATE medicos SET especialidad = ?, sala = ? WHERE nombre = ? AND apellido = ? AND especialidad = ? AND sala = ?";
      
        // Se conecta a la base de datos y prepara la consulta para modificar los datos del médico
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            
            // Establecer los valores de la consulta para modificar el médico
            pstmt.setString(1, nuevaEspecialidad);  // Nueva especialidad
        pstmt.setString(2, nuevaSala);  // Nueva sala
        pstmt.setString(3, nombre);  // Nombre del médico
        pstmt.setString(4, apellido);  // Apellido del médico
        pstmt.setString(5, especialidadActual);  // Especialidad actual
        pstmt.setString(6, salaActual);  // Sala actual
        
            // Ejecutar la consulta de actualización y verificar si se modificaron filas
            int filasActualizadas = pstmt.executeUpdate();
            if (filasActualizadas > 0) {
                System.out.println("Médico modificado correctamente.");
            } else {
                System.err.println("No se pudo modificar el médico.");
            }
        }
    }

    // Método para obtener la sala de un médico según su nombre y apellido
   // En MedicoDAO.java
public String obtenerSalaPorMedico(String nombre, String apellido) throws SQLException {
    String sala = null;
    String sql = "SELECT sala FROM medicos WHERE nombre = ? AND apellido = ?";
    
    try (Connection conexion = Conexion.conectar();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        
        pstmt.setString(1, nombre);
        pstmt.setString(2, apellido);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                sala = rs.getString("sala");
            }
        }
    }
    return sala;
}

    // Método para obtener la lista de médicos por especialidad
    public List<String> obtenerMedicosPorEspecialidad(String especialidad) throws SQLException {
        List<String> medicos = new ArrayList<>();
        String sql = "SELECT nombre, apellido, sala FROM medicos WHERE especialidad = ?";
        
        // Se conecta a la base de datos y prepara la consulta para obtener médicos por especialidad
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, especialidad);
        
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String apellido = rs.getString("apellido");
                    String sala = rs.getString("sala");
                    // Agregar los médicos a la lista con el formato "Nombre Apellido - Sala"
                    medicos.add(nombre + " " + apellido + " - Sala " + sala);
                }
            }
        }
        return medicos; // Retorna la lista de médicos con la especialidad seleccionada
    }
    // En MedicoDAO.java
public String obtenerEspecialidad(String nombre, String apellido) throws SQLException {
    String especialidad = null;
    String sql = "SELECT especialidad FROM medicos WHERE nombre = ? AND apellido = ?";

    try (Connection conexion = Conexion.conectar();
         PreparedStatement pstmt = conexion.prepareStatement(sql)) {
        
        pstmt.setString(1, nombre);
        pstmt.setString(2, apellido);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                especialidad = rs.getString("especialidad");
            }
        }
    }
    return especialidad;
}

}
