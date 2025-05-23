package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static final int POOL_SIZE = 5;
    private static ConnectionPool instance;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections;
    
    private String url;
    private String username;
    private String password;

    private ConnectionPool() {
        connectionPool = new ArrayList<>();
        usedConnections = new ArrayList<>();
        
        // Database configuration
        url = "jdbc:mysql://localhost:3306/authentication";
        username = "root";
        password = "";
        
        initializePool();
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    private void initializePool() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < POOL_SIZE; i++) {
                connectionPool.add(createConnection());
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Error creating connection: " + e.getMessage());
            return null;
        }
    }

    public Connection getConnection() {
        if (connectionPool.isEmpty()) {
            throw new RuntimeException("No available connections in the pool");
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            usedConnections.remove(connection);
            connectionPool.add(connection);
        }
    }

    public void closeAllConnections() {
        for (Connection connection : usedConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
        for (Connection connection : connectionPool) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
        connectionPool.clear();
        usedConnections.clear();
    }
}
