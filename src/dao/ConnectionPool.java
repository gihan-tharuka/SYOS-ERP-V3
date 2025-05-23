package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConnectionPool {
    private final BlockingQueue<Connection> connectionPool;
    private final String url;
    private final String username;
    private final String password;
    private final int initialPoolSize;
    private final int maxPoolSize;
    private final long connectionTimeout;

    public ConnectionPool(String url, String username, String password, 
                        int initialPoolSize, int maxPoolSize, 
                        long connectionTimeoutSeconds) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.initialPoolSize = initialPoolSize;
        this.maxPoolSize = maxPoolSize;
        this.connectionTimeout = connectionTimeoutSeconds;
        this.connectionPool = new LinkedBlockingQueue<>(maxPoolSize);
        
        initializePool();
    }

    private void initializePool() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < initialPoolSize; i++) {
                connectionPool.offer(createNewConnection());
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    private Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection() throws SQLException {
        try {
            // Try to get existing connection
            Connection connection = connectionPool.poll(connectionTimeout, TimeUnit.SECONDS);
            
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            
            // Create new connection if pool isn't full
            synchronized (this) {
                if (connectionPool.size() < maxPoolSize) {
                    return createNewConnection();
                }
            }
            
            throw new SQLException("Connection pool exhausted");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for connection", e);
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    // Reset connection state
                    if (!connection.getAutoCommit()) {
                        connection.setAutoCommit(true);
                    }
                    connectionPool.offer(connection);
                }
            } catch (SQLException e) {
                // If we can't return it to pool, close it
                try {
                    connection.close();
                } catch (SQLException ex) {
                    System.err.println("Error closing connection: " + ex.getMessage());
                }
            }
        }
    }

    public int getAvailableConnections() {
        return connectionPool.size();
    }

    public void closeAllConnections() {
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        connectionPool.clear();
    }
}