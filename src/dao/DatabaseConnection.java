package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/authentication";
            String username = "root";
            String password = "";

            connection = DriverManager.getConnection(url, username, password);

            System.out.println("Database Connected Successfully!");
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

    public Connection getConnection() {
        return connection;
    }

    private void handleSQLException(SQLException e) {
        if (e instanceof com.mysql.cj.jdbc.exceptions.CommunicationsException) {
            System.out.println("Error: Unable to connect to the database. Please ensure the database server is running and try again.");
        } else {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    public boolean isConnectionValid() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
