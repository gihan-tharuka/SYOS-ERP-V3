package Testing2.Testing.dao;

import dao.DatabaseConnection;
import dao.ConnectionPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionTest {

    private DatabaseConnection databaseConnection;
    private ConnectionPool mockConnectionPool;
    private Connection mockConnection;

    @BeforeEach
    void setUp() {
        // Reset the singleton instance before each test
        resetSingletonInstance();
        
        // Create mocks
        mockConnectionPool = mock(ConnectionPool.class);
        mockConnection = mock(Connection.class);
        
        // Create instance using reflection to inject mock
        databaseConnection = createDatabaseConnectionWithMockPool(mockConnectionPool);
    }

    @AfterEach
    void tearDown() {
        resetSingletonInstance();
    }

    @Test
    void testGetInstance_ShouldReturnSameInstance() {
        // Reset singleton
        resetSingletonInstance();
        
        // Act
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();
        
        // Assert
        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2);
    }

    @Test
    void testGetConnection_ShouldReturnConnectionFromPool() {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        
        // Act
        Connection result = databaseConnection.getConnection();
        
        // Assert
        assertNotNull(result);
        assertEquals(mockConnection, result);
        verify(mockConnectionPool, times(1)).getConnection();
    }

    @Test
    void testGetConnection_WhenPoolReturnsNull_ShouldReturnNull() {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(null);
        
        // Act
        Connection result = databaseConnection.getConnection();
        
        // Assert
        assertNull(result);
        verify(mockConnectionPool, times(1)).getConnection();
    }

    @Test
    void testReleaseConnection_ShouldCallPoolRelease() {
        // Arrange
        Connection connectionToRelease = mock(Connection.class);
        
        // Act
        databaseConnection.releaseConnection(connectionToRelease);
        
        // Assert
        verify(mockConnectionPool, times(1)).releaseConnection(connectionToRelease);
    }

    @Test
    void testReleaseConnection_WithNullConnection_ShouldHandleGracefully() {
        // Act
        databaseConnection.releaseConnection(null);
        
        // Assert
        verify(mockConnectionPool, times(1)).releaseConnection(null);
    }

    @Test
    void testIsConnectionValid_WithValidConnection_ShouldReturnTrue() throws SQLException {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isClosed()).thenReturn(false);
        
        // Act
        boolean result = databaseConnection.isConnectionValid();
        
        // Assert
        assertTrue(result);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).isClosed();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void testIsConnectionValid_WithClosedConnection_ShouldReturnFalse() throws SQLException {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isClosed()).thenReturn(true);
        
        // Act
        boolean result = databaseConnection.isConnectionValid();
        
        // Assert
        assertFalse(result);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).isClosed();
        verify(mockConnectionPool, times(1)).releaseConnection(mockConnection);
    }

    @Test
    void testIsConnectionValid_WithNullConnection_ShouldReturnFalse() {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(null);
        
        // Act
        boolean result = databaseConnection.isConnectionValid();
        
        // Assert
        assertFalse(result);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnectionPool, times(1)).releaseConnection(null);
    }

    @Test
    void testIsConnectionValid_WhenSQLExceptionOccurs_ShouldReturnFalse() throws SQLException {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        when(mockConnection.isClosed()).thenThrow(new SQLException("Test exception"));
        
        // Act
        boolean result = databaseConnection.isConnectionValid();
        
        // Assert
        assertFalse(result);
        verify(mockConnectionPool, times(1)).getConnection();
        verify(mockConnection, times(1)).isClosed();
        verify(mockConnectionPool, never()).releaseConnection(any());
    }

    @Test
    void testCloseAllConnections_ShouldCallPoolCloseAll() {
        // Act
        databaseConnection.closeAllConnections();
        
        // Assert
        verify(mockConnectionPool, times(1)).closeAllConnections();
    }

    @Test
    void testMultipleGetConnectionCalls_ShouldCallPoolEachTime() {
        // Arrange
        when(mockConnectionPool.getConnection()).thenReturn(mockConnection);
        
        // Act
        databaseConnection.getConnection();
        databaseConnection.getConnection();
        databaseConnection.getConnection();
        
        // Assert
        verify(mockConnectionPool, times(3)).getConnection();
    }

    @Test
    void testMultipleReleaseConnectionCalls_ShouldCallPoolEachTime() {
        // Arrange
        Connection connection1 = mock(Connection.class);
        Connection connection2 = mock(Connection.class);
        
        // Act
        databaseConnection.releaseConnection(connection1);
        databaseConnection.releaseConnection(connection2);
        
        // Assert
        verify(mockConnectionPool, times(1)).releaseConnection(connection1);
        verify(mockConnectionPool, times(1)).releaseConnection(connection2);
    }

    // Helper methods to work with singleton pattern
    private void resetSingletonInstance() {
        try {
            java.lang.reflect.Field instanceField = DatabaseConnection.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            // Ignore reflection exceptions
        }
    }

    private DatabaseConnection createDatabaseConnectionWithMockPool(ConnectionPool mockPool) {
        try {
            // Create instance using reflection
            DatabaseConnection instance = DatabaseConnection.getInstance();
            
            // Inject mock connection pool using reflection
            java.lang.reflect.Field poolField = DatabaseConnection.class.getDeclaredField("connectionPool");
            poolField.setAccessible(true);
            poolField.set(instance, mockPool);
            
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create DatabaseConnection with mock pool", e);
        }
    }
} 