package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        } catch (Exception e) {
            System.out.println("Error initializing connection pool: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            return connectionPool.getConnection();
        } catch (SQLException e) {
            System.out.println("Error getting connection: " + e.getMessage());
            return null;
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.releaseConnection(connection);
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

    public void shutdown() {
        try {
            if (connectionPool != null) {
                connectionPool.shutdown();
            }
        } catch (SQLException e) {
            System.out.println("Error shutting down connection pool: " + e.getMessage());
        }
    }
}
