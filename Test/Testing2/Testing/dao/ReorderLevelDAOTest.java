package Testing2.Testing.dao;

import dao.ReorderLevelDAO;
import model.ReorderLevel;
import model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReorderLevelDAOTest {
    private Connection connection;
    private ReorderLevelDAO reorderLevelDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        reorderLevelDAO = new ReorderLevelDAO(connection);
    }

    @Test
    public void testGetAllReorderLevels() throws SQLException {
        String query = "SELECT rl.*, i.item_code, i.item_name FROM reorder_levels rl " +
                      "JOIN items i ON rl.item_id = i.item_id";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        // First row
        when(resultSet.getInt("reorder_level_id")).thenReturn(1, 2);
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getString("item_code")).thenReturn("ITEM001", "ITEM002");
        when(resultSet.getString("item_name")).thenReturn("Item 1", "Item 2");
        when(resultSet.getInt("threshold_quantity")).thenReturn(10, 20);
        when(resultSet.getInt("total_stock")).thenReturn(5, 15);

        List<Map<String, Object>> reorderLevels = reorderLevelDAO.getAllReorderLevels();
        assertNotNull(reorderLevels);
        assertEquals(2, reorderLevels.size());
        
        Map<String, Object> firstLevel = reorderLevels.get(0);
        assertEquals(1, firstLevel.get("reorderLevelId"));
        assertEquals(1, firstLevel.get("itemId"));
        assertEquals("ITEM001", firstLevel.get("itemCode"));
        assertEquals("Item 1", firstLevel.get("itemName"));
        assertEquals(10, firstLevel.get("thresholdQuantity"));
        assertEquals(5, firstLevel.get("totalStock"));
        
        Map<String, Object> secondLevel = reorderLevels.get(1);
        assertEquals(2, secondLevel.get("reorderLevelId"));
        assertEquals(2, secondLevel.get("itemId"));
        assertEquals("ITEM002", secondLevel.get("itemCode"));
        assertEquals("Item 2", secondLevel.get("itemName"));
        assertEquals(20, secondLevel.get("thresholdQuantity"));
        assertEquals(15, secondLevel.get("totalStock"));
    }

    @Test
    public void testGetAllReorderLevelsEmptyList() throws SQLException {
        String query = "SELECT rl.*, i.item_code, i.item_name FROM reorder_levels rl " +
                      "JOIN items i ON rl.item_id = i.item_id";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Map<String, Object>> reorderLevels = reorderLevelDAO.getAllReorderLevels();
        assertNotNull(reorderLevels);
        assertEquals(0, reorderLevels.size());
    }

    @Test
    public void testGetAvailableItems() throws SQLException {
        String query = "SELECT i.* FROM items i " +
                      "LEFT JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                      "WHERE rl.item_id IS NULL";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getString("item_code")).thenReturn("ITEM001", "ITEM002");
        when(resultSet.getString("item_name")).thenReturn("Item 1", "Item 2");
        when(resultSet.getDouble("price")).thenReturn(100.0, 200.0);
        when(resultSet.getDouble("discount")).thenReturn(10.0, 20.0);

        List<Item> items = reorderLevelDAO.getAvailableItems();
        assertNotNull(items);
        assertEquals(2, items.size());
        
        Item firstItem = items.get(0);
        assertEquals(1, firstItem.getItemId());
        assertEquals("ITEM001", firstItem.getItemCode());
        assertEquals("Item 1", firstItem.getItemName());
        assertEquals(100.0, firstItem.getPrice());
        assertEquals(10.0, firstItem.getDiscount());
        
        Item secondItem = items.get(1);
        assertEquals(2, secondItem.getItemId());
        assertEquals("ITEM002", secondItem.getItemCode());
        assertEquals("Item 2", secondItem.getItemName());
        assertEquals(200.0, secondItem.getPrice());
        assertEquals(20.0, secondItem.getDiscount());
    }

    @Test
    public void testGetAvailableItemsEmptyList() throws SQLException {
        String query = "SELECT i.* FROM items i " +
                      "LEFT JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                      "WHERE rl.item_id IS NULL";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Item> items = reorderLevelDAO.getAvailableItems();
        assertNotNull(items);
        assertEquals(0, items.size());
    }

    @Test
    public void testGetReorderLevelById() throws SQLException {
        String query = "SELECT * FROM reorder_levels WHERE reorder_level_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("reorder_level_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("threshold_quantity")).thenReturn(10);
        when(resultSet.getInt("total_stock")).thenReturn(5);

        ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelById(1);
        assertNotNull(reorderLevel);
        assertEquals(1, reorderLevel.getReorderLevelId());
        assertEquals(1, reorderLevel.getItemId());
        assertEquals(10, reorderLevel.getThresholdQuantity());
        assertEquals(5, reorderLevel.getTotalStock());
    }

    @Test
    public void testGetReorderLevelByIdReturnsNull() throws SQLException {
        String query = "SELECT * FROM reorder_levels WHERE reorder_level_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        ReorderLevel reorderLevel = reorderLevelDAO.getReorderLevelById(999);
        assertNull(reorderLevel);
    }

    @Test
    public void testAddReorderLevel() throws SQLException {
        String query = "INSERT INTO reorder_levels (item_id, threshold_quantity, total_stock) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.addReorderLevel(1, 10);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).setInt(2, 10);
        verify(preparedStatement).setInt(3, 0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateReorderLevel() throws SQLException {
        String query = "UPDATE reorder_levels SET threshold_quantity = ? WHERE reorder_level_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.updateReorderLevel(1, 15);

        verify(preparedStatement).setInt(1, 15);
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteReorderLevel() throws SQLException {
        String query = "DELETE FROM reorder_levels WHERE reorder_level_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.deleteReorderLevel(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateTotalStock() throws SQLException {
        String query = "UPDATE reorder_levels SET total_stock = total_stock - ? WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.updateTotalStock(1, 5);

        verify(preparedStatement).setInt(1, 5);
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddReorderLevelWithZeroThreshold() throws SQLException {
        String query = "INSERT INTO reorder_levels (item_id, threshold_quantity, total_stock) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.addReorderLevel(2, 0);

        verify(preparedStatement).setInt(1, 2);
        verify(preparedStatement).setInt(2, 0);
        verify(preparedStatement).setInt(3, 0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateReorderLevelWithZeroThreshold() throws SQLException {
        String query = "UPDATE reorder_levels SET threshold_quantity = ? WHERE reorder_level_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.updateReorderLevel(2, 0);

        verify(preparedStatement).setInt(1, 0);
        verify(preparedStatement).setInt(2, 2);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateTotalStockWithZeroQuantity() throws SQLException {
        String query = "UPDATE reorder_levels SET total_stock = total_stock - ? WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        reorderLevelDAO.updateTotalStock(3, 0);

        verify(preparedStatement).setInt(1, 0);
        verify(preparedStatement).setInt(2, 3);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetAllReorderLevelsSingleItem() throws SQLException {
        String query = "SELECT rl.*, i.item_code, i.item_name FROM reorder_levels rl " +
                      "JOIN items i ON rl.item_id = i.item_id";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        
        when(resultSet.getInt("reorder_level_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_code")).thenReturn("ITEM001");
        when(resultSet.getString("item_name")).thenReturn("Single Item");
        when(resultSet.getInt("threshold_quantity")).thenReturn(25);
        when(resultSet.getInt("total_stock")).thenReturn(30);

        List<Map<String, Object>> reorderLevels = reorderLevelDAO.getAllReorderLevels();
        assertNotNull(reorderLevels);
        assertEquals(1, reorderLevels.size());
        
        Map<String, Object> level = reorderLevels.get(0);
        assertEquals(1, level.get("reorderLevelId"));
        assertEquals(1, level.get("itemId"));
        assertEquals("ITEM001", level.get("itemCode"));
        assertEquals("Single Item", level.get("itemName"));
        assertEquals(25, level.get("thresholdQuantity"));
        assertEquals(30, level.get("totalStock"));
    }

    @Test
    public void testGetAvailableItemsSingleItem() throws SQLException {
        String query = "SELECT i.* FROM items i " +
                      "LEFT JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                      "WHERE rl.item_id IS NULL";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_code")).thenReturn("ITEM001");
        when(resultSet.getString("item_name")).thenReturn("Single Available Item");
        when(resultSet.getDouble("price")).thenReturn(150.0);
        when(resultSet.getDouble("discount")).thenReturn(15.0);

        List<Item> items = reorderLevelDAO.getAvailableItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        
        Item item = items.get(0);
        assertEquals(1, item.getItemId());
        assertEquals("ITEM001", item.getItemCode());
        assertEquals("Single Available Item", item.getItemName());
        assertEquals(150.0, item.getPrice());
        assertEquals(15.0, item.getDiscount());
    }
} 