package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/GestionCitas"; // Cambia el nombre de la base de datos si es necesario
    private static final String USUARIO = "root";
    private static final String CONTRASEÑA = "admin"; // Cambia la contraseña según tu configuración
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al cargar el driver de MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Método para verificar las credenciales de usuario.
     *
     * @param usuario    Nombre de usuario ingresado.
     * @param contrasena Contraseña ingresada.
     * @return true si las credenciales son correctas, false en caso contrario.
     */
    public static boolean verificarLogin(String usuario, String contrasena) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE usuario = ? AND contraseña = ?";
        Connection conexion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conexion = conectar(); // Obtener conexión a la base de datos
            ps = conexion.prepareStatement(sql);
            ps.setString(1, usuario); // Reemplazar el primer "?" con el usuario
            ps.setString(2, contrasena); // Reemplazar el segundo "?" con la contraseña
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Si el conteo es mayor a 0, las credenciales son válidas
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar credenciales: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                cerrarConexion(conexion);
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return false; // Credenciales incorrectas por defecto
    }
}
