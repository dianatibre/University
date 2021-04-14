package ro.ubb.socket.server.utils;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection;
    private Connection connection;

    public Connection getConnectionInstance() {
        return this.connection;
    }

    public static DatabaseConnection getConnection() {
        if (databaseConnection == null) {
            databaseConnection = new DatabaseConnection();
        }
        return databaseConnection;
    }

    private DatabaseConnection() {
        this.connection = initConnection();
    }

    private Connection initConnection() {
        try {
            //loading from Properties file
            final Properties properties = new Properties();
            final URL url = ClassLoader.getSystemResource("dbConfig.properties");
            properties.load(url.openStream());

            //Separating properties
            final String dbDriver = properties.getProperty("dbDriver");
            final String databaseUrl = properties.getProperty("databaseUrl");
            final String accessUsername = properties.getProperty("accessUsername");
            final String accessPassword = properties.getProperty("accessPassword");

            //Loading properties into classpath to be able to successfully connect to MySQL Database
            Class.forName(dbDriver);
            if (Objects.isNull(connection)) {
                connection = DriverManager.getConnection(
                        databaseUrl,
                        accessUsername,
                        accessPassword
                );
            }

        } catch (SQLException |
                ClassNotFoundException |
                IOException ex) {
            System.err.println(ex.getMessage());
        }
        return connection;
    }
}
