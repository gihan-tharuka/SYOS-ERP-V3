package Testing2.dao;

import dao.ReorderLevelDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReorderLevelDAOTest {

    private Connection connection;
    private ReorderLevelDAO reorderLevelDAO;
    private PreparedStatement preparedStatement;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        reorderLevelDAO = new ReorderLevelDAO(connection); // Using constructor injection to pass mock connection
    }

    @Test
    public void testAddReorderLevel() throws SQLException {
        String query = "INSERT INTO reorder_levels (item_id, threshold_quantity, total_stock) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        int itemId = 1;

        reorderLevelDAO.addReorderLevel(itemId);

        verify(preparedStatement).setInt(1, itemId);
        verify(preparedStatement).setInt(2, 50); // Default threshold_quantity
        verify(preparedStatement).setInt(3, 0); // Default total_stock
        verify(preparedStatement).executeUpdate();
    }
}
