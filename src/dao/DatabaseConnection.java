package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private ConnectionPool connectionPool;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/authentication";
            String username = "root";
            String password = "";

            connectionPool = ConnectionPool.create(url, username, password);
            System.out.println("Database Connection Pool Created Successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public static DatabaseConnection getInstance() {
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

    private void handleSQLException(SQLException e) {
        if (e instanceof CommunicationsException) {
            System.out.println("Error: Unable to connect to the database. Please ensure the database server is running and try again.");
        } else {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public boolean isConnectionValid() {
        try {
            return connectionPool != null && connectionPool.getSize() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void shutdown() throws SQLException {
        if (connectionPool != null) {
            connectionPool.shutdown();
        }
    }
}
