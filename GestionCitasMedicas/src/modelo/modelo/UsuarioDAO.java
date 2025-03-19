package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Clase encargada de manejar las operaciones relacionadas con los usuarios en la base de datos
public class UsuarioDAO {

    // Método para verificar si un usuario y su contraseña existen en la base de datos
    public boolean verificarUsuario(String usuario, String contraseña) throws SQLException {
        // Consulta SQL que verifica si existe un usuario con la combinación dada de usuario y contraseña
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ? AND contraseña = ?";
        Connection conexion = null; // Objeto de conexión a la base de datos

        try {
            // Intento de conectar con la base de datos
            conexion = Conexion.conectar();
            if (conexion == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }

            // Uso de PreparedStatement para evitar inyecciones SQL
            try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
                pstmt.setString(1, usuario); // Configuración del primer parámetro (usuario)
                pstmt.setString(2, contraseña); // Configuración del segundo parámetro (contraseña)

                // Ejecución de la consulta y procesamiento de resultados
                try (ResultSet resultado = pstmt.executeQuery()) {
                    if (resultado.next()) { // Comprueba si hay un resultado
                        return resultado.getInt(1) > 0; // Retorna true si el usuario existe (COUNT > 0)
                    }
                }
            }
        } finally {
            // Asegura el cierre de la conexión en el bloque finally
            Conexion.cerrarConexion(conexion);
        }
        return false; // Si no se encuentra el usuario o ocurre un error
    }
}
