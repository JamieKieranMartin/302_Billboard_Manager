package server.services;

import common.utils.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A singleton Class that handles all the database interactions for the server.
 *
 * @author Jamie Martin
 * @author Hieu Nghia Huynh
 * @author Perdana Bailey
 */
public class DataService {
    private Connection connection;

    /**
     * Generates a Dataservice Instance.
     *
     * @throws Exception Thrown when unable to connect to database.
     */
    protected DataService() {
        this.connection = startConnection();
    }

    /**
     * Ensures the DataService is a singleton when getConnection() is called.
     */
    private static class DataServiceHolder {
        private final static DataService INSTANCE = new DataService();
    }

    public static Connection getConnection() {
        return DataServiceHolder.INSTANCE.connection;
    }

    /**
     * Get a connection to database.
     *
     * @return Connection object.
     * @throws Exception Thrown when unable to configure database connection from props.
     */
    private static Connection startConnection() {
        try {
            // Configure the database from the prop file, throws error if one
            Properties props = Props.getProps("./db.props");

            String url = props.getProperty("jdbc.url");
            String schema = props.getProperty("jdbc.schema");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");

            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to the database" + e.getMessage());
        }
    }

    /**
     * This closes an active database connection.
     *
     * @throws SQLException Thrown when unable to close connection to database.
     */
    public void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
