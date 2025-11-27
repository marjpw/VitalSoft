import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton para gestionar la conexión a la base de datos MySQL.
 * Lee la configuración desde el archivo db.properties.
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;
    private Properties properties;

    // Constructor privado para patrón Singleton
    private DatabaseConnection() {
        try {
            // Cargar configuración desde db.properties
            properties = new Properties();
            String configPath = "../database/db.properties";

            try (FileInputStream fis = new FileInputStream(configPath)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("⚠️ No se pudo cargar db.properties, usando valores por defecto.");
                // Valores por defecto
                properties.setProperty("db.url",
                        "jdbc:mysql://localhost:3306/vitalsoft_db?useSSL=false&serverTimezone=UTC");
                properties.setProperty("db.username", "root");
                properties.setProperty("db.password", "SQLseyer166");
                properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            }

            // Cargar el driver JDBC
            Class.forName(properties.getProperty("db.driver"));

            // Establecer la conexión
            conectar();

            System.out.println("✅ Conexión a base de datos establecida correctamente.");

        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Driver MySQL no encontrado.");
            System.err.println("   Descarga MySQL Connector/J desde: https://dev.mysql.com/downloads/connector/j/");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos:");
            System.err.println("   - Verifica que MySQL esté ejecutándose");
            System.err.println("   - Verifica las credenciales en db.properties");
            System.err.println("   - Verifica que la base de datos 'vitalsoft_db' exista");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la instancia única de DatabaseConnection (Singleton)
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Establece la conexión con la base de datos
     */
    private void conectar() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        connection = DriverManager.getConnection(url, username, password);
    }

    /**
     * Obtiene la conexión activa. Si está cerrada, la reconecta.
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al verificar/reconectar la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ Conexión a base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión.");
            e.printStackTrace();
        }
    }

    /**
     * Verifica si la conexión está activa
     */
    public boolean isConectado() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Obtiene una propiedad de configuración
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
