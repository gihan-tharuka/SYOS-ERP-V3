package dao;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final ConnectionPool connectionPool;

    private DatabaseConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/authentication";
        String username = "root";
        String password = "";
        
        this.connectionPool = new ConnectionPool(
            url, username, password,
            10,  // initial pool size
            20,  // max pool size
            5    // connection timeout in seconds
        );
    }

    public static synchronized DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void releaseConnection(Connection connection) {
        connectionPool.releaseConnection(connection);
    }

    public void handleSQLException(SQLException e) {
        if (e instanceof CommunicationsException) {
            System.out.println("Error: Unable to connect to the database. Please ensure the database server is running.");
        } else {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public void shutdown() {
        connectionPool.closeAllConnections();
    }
}