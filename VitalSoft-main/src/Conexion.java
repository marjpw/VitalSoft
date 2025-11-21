import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_hospitalario" +
                                     "?useSSL=false" +
                                     "&serverTimezone=UTC" +
                                     "&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "admin123"; // ✅ CORREGIDO
    
    public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conectado a MySQL exitosamente");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Driver MySQL no encontrado");
            System.err.println("   Verifica que mysql-connector-j-9.5.0.jar esté en lib/");
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión a MySQL");
            System.err.println("   Causa: " + e.getMessage());
            System.err.println("\n🔧 POSIBLES SOLUCIONES:");
            System.err.println("   1. Verifica que MySQL esté corriendo");
            System.err.println("   2. Verifica usuario: root");
            System.err.println("   3. Verifica password: admin123");
            System.err.println("   4. Verifica que existe la BD: sistema_hospitalario");
            System.err.println("   5. Verifica puerto: 3306");
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean probarConexion() {
        System.out.println("\n🔍 Probando conexión a MySQL...");
        System.out.println("   Host: localhost:3306");
        System.out.println("   Usuario: root");
        System.out.println("   Base de datos: sistema-hospitalario");
        
        try (Connection conn = conectar()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Conexión exitosa\n");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Conexión fallida\n");
        }
        return false;
    }
}
