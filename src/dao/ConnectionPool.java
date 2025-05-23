package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ConnectionPool {
    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());
    private static final int POOL_SIZE = 20; // Increased pool size
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;
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
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private void initializePool() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection conn = createConnection();
                if (conn != null) {
                    connectionPool.add(conn);
                }
            }
            if (connectionPool.isEmpty()) {
                throw new RuntimeException("Failed to initialize connection pool - no valid connections created");
            }
            logger.info("Connection pool initialized with " + connectionPool.size() + " connections");
        } catch (ClassNotFoundException e) {
            logger.severe("MySQL JDBC Driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    private Connection createConnection() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                Connection conn = DriverManager.getConnection(url, username, password);
                if (conn != null && conn.isValid(5)) {
                    return conn;
                }
            } catch (SQLException e) {
                logger.warning("Failed to create connection (attempt " + (retries + 1) + "): " + e.getMessage());
            }
            retries++;
            if (retries < MAX_RETRIES) {
                try {
                    TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        return null;
    }

    public synchronized Connection getConnection() {
        if (connectionPool.isEmpty()) {
            // Try to create a new connection if pool is empty
            Connection newConn = createConnection();
            if (newConn != null) {
                usedConnections.add(newConn);
                return newConn;
            }
            throw new RuntimeException("No available connections in the pool and failed to create new connection");
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        try {
            if (!connection.isValid(5)) {
                // If connection is invalid, create a new one
                connection.close();
                connection = createConnection();
                if (connection == null) {
                    throw new RuntimeException("Failed to create new connection");
                }
            }
        } catch (SQLException e) {
            logger.warning("Error validating connection: " + e.getMessage());
            connection = createConnection();
            if (connection == null) {
                throw new RuntimeException("Failed to create new connection");
            }
        }
        
        usedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed() && connection.isValid(5)) {
                    usedConnections.remove(connection);
                    connectionPool.add(connection);
                } else {
                    // If connection is invalid, close it and create a new one
                    connection.close();
                    Connection newConn = createConnection();
                    if (newConn != null) {
                        connectionPool.add(newConn);
                    }
                }
            } catch (SQLException e) {
                logger.warning("Error releasing connection: " + e.getMessage());
                try {
                    connection.close();
                } catch (SQLException ex) {
                    logger.warning("Error closing invalid connection: " + ex.getMessage());
                }
            }
        }
    }

    public synchronized void closeAllConnections() {
        for (Connection connection : usedConnections) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.warning("Error closing used connection: " + e.getMessage());
            }
        }
        for (Connection connection : connectionPool) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.warning("Error closing pooled connection: " + e.getMessage());
            }
        }
        connectionPool.clear();
        usedConnections.clear();
        logger.info("All connections closed");
    }
}
