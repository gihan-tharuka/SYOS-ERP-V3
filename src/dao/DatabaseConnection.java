package dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private ConnectionPool connectionPool;

    private DatabaseConnection() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connectionPool.getConnection();
    }

    public void releaseConnection(Connection connection) {
        connectionPool.releaseConnection(connection);
    }

    public boolean isConnectionValid() {
        try {
            Connection connection = getConnection();
            boolean isValid = connection != null && !connection.isClosed();
            releaseConnection(connection);
            return isValid;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeAllConnections() {
        connectionPool.closeAllConnections();
    }
}
