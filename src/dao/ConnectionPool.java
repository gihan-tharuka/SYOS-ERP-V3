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
    
    public static ConnectionPool create(String url, String username, String password) throws SQLException {
        ConnectionPool pool = new ConnectionPool(url, username, password);
        pool.initializePool();
        return pool;
    }
    
    private void initializePool() throws SQLException {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(createConnection());
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
        
        if (!connection.isValid(MAX_TIMEOUT)) {
            connection = createConnection();
        }
        
        usedConnections.add(connection);
        return connection;
    }
    
    public synchronized boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }
    
    public synchronized void shutdown() throws SQLException {
        for (Connection connection : usedConnections) {
            connection.close();
        }
        for (Connection connection : connectionPool) {
            connection.close();
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