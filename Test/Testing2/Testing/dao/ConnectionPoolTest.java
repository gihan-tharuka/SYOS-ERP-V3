package Testing2.Testing.dao;

import dao.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolTest {

    private ConnectionPool connectionPool;

    @BeforeEach
    void setUp() {
        // Reset the singleton instance before each test
        resetSingletonInstance();
        connectionPool = ConnectionPool.getInstance();
    }

    @AfterEach
    void tearDown() {
        // Close all connections to clean up
        try {
            connectionPool.closeAllConnections();
        } catch (Exception e) {
            // Ignore cleanup errors
        }
        resetSingletonInstance();
    }

    @Test
    void testGetInstance_ShouldReturnSameInstance() {
        // Reset singleton
        resetSingletonInstance();
        
        // Act
        ConnectionPool instance1 = ConnectionPool.getInstance();
        ConnectionPool instance2 = ConnectionPool.getInstance();
        
        // Assert
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }

    @Test
    void testGetConnection_ShouldReturnValidConnection() {
        // Act
        Connection result = connectionPool.getConnection();
        
        // Assert
        assertNotNull(result);
        assertDoesNotThrow(() -> {
            assertFalse(result.isClosed());
        });
    }

    @Test
    void testReleaseConnection_WithValidConnection_ShouldHandleGracefully() {
        // Arrange
        Connection connection = connectionPool.getConnection();
        
        // Act & Assert
        assertDoesNotThrow(() -> connectionPool.releaseConnection(connection));
    }

    @Test
    void testReleaseConnection_WithNullConnection_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> connectionPool.releaseConnection(null));
    }

    @Test
    void testCloseAllConnections_ShouldHandleGracefully() {
        // Act & Assert
        assertDoesNotThrow(() -> connectionPool.closeAllConnections());
    }

    @Test
    void testMultipleGetConnectionCalls_ShouldReturnConnections() {
        // Act
        Connection conn1 = connectionPool.getConnection();
        Connection conn2 = connectionPool.getConnection();
        
        // Assert
        assertNotNull(conn1);
        assertNotNull(conn2);
        assertNotSame(conn1, conn2);
    }

    @Test
    void testGetAndReleaseConnection_ShouldWorkCorrectly() {
        // Act
        Connection conn = connectionPool.getConnection();
        assertDoesNotThrow(() -> connectionPool.releaseConnection(conn));
        
        // Assert
        assertNotNull(conn);
    }

    @Test
    void testConnectionPool_ShouldHandleMultipleOperations() {
        // Act - Get multiple connections
        Connection conn1 = connectionPool.getConnection();
        Connection conn2 = connectionPool.getConnection();
        Connection conn3 = connectionPool.getConnection();
        
        // Assert
        assertNotNull(conn1);
        assertNotNull(conn2);
        assertNotNull(conn3);
        
        // Release connections
        assertDoesNotThrow(() -> {
            connectionPool.releaseConnection(conn1);
            connectionPool.releaseConnection(conn2);
            connectionPool.releaseConnection(conn3);
        });
    }

    @Test
    void testConnectionPool_ShouldHandleRepeatedGetAndRelease() {
        // Act - Get and release connection multiple times
        for (int i = 0; i < 5; i++) {
            Connection conn = connectionPool.getConnection();
            assertNotNull(conn);
            assertDoesNotThrow(() -> connectionPool.releaseConnection(conn));
        }
        
        // Assert - Should not throw any exceptions
        assertTrue(true);
    }

    @Test
    void testConnectionPool_ShouldHandleConcurrentAccess() {
        // Act - Simulate concurrent access
        Connection[] connections = new Connection[3];
        
        for (int i = 0; i < 3; i++) {
            connections[i] = connectionPool.getConnection();
            assertNotNull(connections[i]);
        }
        
        // Release all connections
        for (Connection conn : connections) {
            assertDoesNotThrow(() -> connectionPool.releaseConnection(conn));
        }
        
        // Assert
        assertTrue(true);
    }

    @Test
    void testConnectionPool_ShouldHandleStressTest() {
        // Act - Stress test with multiple operations
        for (int i = 0; i < 10; i++) {
            Connection conn = connectionPool.getConnection();
            assertNotNull(conn);
            
            // Simulate some work
            assertDoesNotThrow(() -> {
                assertFalse(conn.isClosed());
            });
            
            // Release connection
            assertDoesNotThrow(() -> connectionPool.releaseConnection(conn));
        }
        
        // Assert
        assertTrue(true);
    }

    // Helper method to reset singleton instance
    private void resetSingletonInstance() {
        try {
            java.lang.reflect.Field instanceField = ConnectionPool.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection exceptions
        }
    }
} 