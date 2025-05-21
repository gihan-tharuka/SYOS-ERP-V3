package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static final int INITIAL_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int MAX_TIMEOUT = 3000; // 3 seconds
    
    private final String url;
    private final String username;
    private final String password;
    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections;
    
    public ConnectionPool(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connectionPool = new ArrayList<>();
        this.usedConnections = new ArrayList<>();
    }
    
    public static ConnectionPool create(String url, String username, String password) {
        ConnectionPool pool = new ConnectionPool(url, username, password);
        pool.initializePool();
        return pool;
    }
    
    private void initializePool() {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            try {
                connectionPool.add(createConnection());
            } catch (SQLException e) {
                System.out.println("Error creating initial connection: " + e.getMessage());
            }
        }
    }
    
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    public synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection());
            } else {
                throw new SQLException("No available connections in the pool");
            }
        }
        
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        
        try {
            if (!connection.isValid(MAX_TIMEOUT)) {
                connection = createConnection();
            }
        } catch (SQLException e) {
            connection = createConnection();
        }
        
        usedConnections.add(connection);
        return connection;
    }
    
    public synchronized boolean releaseConnection(Connection connection) {
        if (connection != null) {
            connectionPool.add(connection);
            return usedConnections.remove(connection);
        }
        return false;
    }
    
    public synchronized void shutdown() throws SQLException {
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
        usedConnections.clear();
        connectionPool.clear();
    }
    
    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }
    
    public int getAvailableConnections() {
        return connectionPool.size();
    }
    
    public int getUsedConnections() {
        return usedConnections.size();
    }
} 