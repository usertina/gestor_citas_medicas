package modelo;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Clase encargada de manejar las operaciones de la base de datos relacionadas con los pacientes
public class PacienteDAO {

    // Método para insertar un nuevo paciente en la base de datos y guardar los datos en un archivo
    public void insertarPaciente(String nombre, String apellido, String telefono, String email) throws SQLException {
        // Consulta SQL para insertar un paciente
        String sql = "INSERT INTO pacientes (nombre, apellido, telefono, email) VALUES (?, ?, ?, ?)";
        
        // Bloque try-with-resources para manejar automáticamente el cierre de la conexión y el PreparedStatement
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            // Configuración de los parámetros de la consulta
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, email);
            pstmt.executeUpdate(); // Ejecución de la consulta
        }

       // Bloque para escribir los datos del paciente en un archivo
try (FileWriter writer = new FileWriter("pacientes.txt", true)) { // Modo append para no sobrescribir
    writer.write("Paciente:\n");
    writer.write("  NOMBRE: " + nombre + "\n");
    writer.write("  APELLIDO: " + apellido + "\n");
    writer.write("  TELÉFONO: " + telefono + "\n");
    writer.write("  EMAIL: " + email + "\n");
    writer.write("-----------------------------\n"); // Separador para distinguir entre pacientes
    System.out.println("Datos del paciente guardados en pacientes.txt");
} catch (IOException e) { // Manejo de posibles errores al escribir en el archivo
    System.err.println("Error al escribir en el archivo: " + e.getMessage());
}

    }

    // Método para obtener el nombre completo del último paciente registrado en la base de datos
    public String obtenerUltimoPaciente() throws SQLException {
        // Consulta SQL para obtener el último paciente basado en el ID
        String sql = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM pacientes ORDER BY id DESC LIMIT 1";
        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { // ResultSet para procesar los resultados

            if (rs.next()) { // Comprueba si hay resultados
                return rs.getString("nombre_completo"); // Devuelve el nombre completo del último paciente
            }
        }
        return null; // Si no hay resultados, devuelve null
    }

    // Método para obtener una lista de especialidades únicas de los médicos en la base de datos
    public String[] obtenerEspecialidades() throws SQLException {
        // Consulta SQL para obtener las especialidades distintas
        String sql = "SELECT DISTINCT especialidad FROM medicos";
        ArrayList<String> especialidades = new ArrayList<>(); // Lista para almacenar las especialidades

        try (Connection conexion = Conexion.conectar();
             PreparedStatement pstmt = conexion.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) { // ResultSet para procesar los resultados

            while (rs.next()) { // Itera por los resultados
                especialidades.add(rs.getString("especialidad")); // Añade cada especialidad a la lista
            }
        }
        return especialidades.toArray(new String[0]); // Convierte la lista a un array de Strings
    }
}
