package Testing2.Testing.dao;

import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.ShelfStockDAO;
import model.BatchSelection;
import model.MainStock;
import model.ShelfStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShelfStockDAOTest {
    private Connection connection;
    private ShelfStockDAO shelfStockDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MainStockDAO mainStockDAO;
    private ReorderLevelDAO reorderLevelDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        mainStockDAO = mock(MainStockDAO.class);
        reorderLevelDAO = mock(ReorderLevelDAO.class);
        shelfStockDAO = new ShelfStockDAO(connection);
    }

    @Test
    public void testAddShelfStockSuccess() throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM Shelf_Stock WHERE item_id = ?";
        String insertQuery = "INSERT INTO Shelf_Stock (item_id, shelf_capacity, current_quantity) VALUES (?, ?, ?)";
        
        PreparedStatement countStmt = mock(PreparedStatement.class);
        PreparedStatement insertStmt = mock(PreparedStatement.class);
        ResultSet countResultSet = mock(ResultSet.class);
        ResultSet generatedKeysResultSet = mock(ResultSet.class);
        
        when(connection.prepareStatement(countQuery)).thenReturn(countStmt);
        when(connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)).thenReturn(insertStmt);
        when(countStmt.executeQuery()).thenReturn(countResultSet);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getInt(1)).thenReturn(0); // Item doesn't exist
        when(insertStmt.getGeneratedKeys()).thenReturn(generatedKeysResultSet);
        when(generatedKeysResultSet.next()).thenReturn(true);
        when(generatedKeysResultSet.getInt(1)).thenReturn(1);

        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(100);
        shelfStock.setCurrentQuantity(50);

        boolean result = shelfStockDAO.addShelfStock(shelfStock);

        assertTrue(result);
        assertEquals(1, shelfStock.getStockId());
        verify(countStmt).setInt(eq(1), eq(1));
        verify(insertStmt).setInt(eq(1), eq(1));
        verify(insertStmt).setInt(eq(2), eq(100));
        verify(insertStmt).setInt(eq(3), eq(50));
        verify(insertStmt).executeUpdate();
    }

    @Test
    public void testAddShelfStockAlreadyExists() throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM Shelf_Stock WHERE item_id = ?";
        
        PreparedStatement countStmt = mock(PreparedStatement.class);
        ResultSet countResultSet = mock(ResultSet.class);
        
        when(connection.prepareStatement(countQuery)).thenReturn(countStmt);
        when(countStmt.executeQuery()).thenReturn(countResultSet);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getInt(1)).thenReturn(1); // Item already exists

        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(100);
        shelfStock.setCurrentQuantity(50);

        boolean result = shelfStockDAO.addShelfStock(shelfStock);

        assertFalse(result);
        verify(countStmt).setInt(eq(1), eq(1));
        verify(countStmt).executeQuery();
    }

    @Test
    public void testDoesShelfStockExistByItemIdTrue() throws SQLException {
        String query = "SELECT COUNT(*) FROM Shelf_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        boolean result = shelfStockDAO.doesShelfStockExistByItemId(1);

        assertTrue(result);
        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testDoesShelfStockExistByItemIdFalse() throws SQLException {
        String query = "SELECT COUNT(*) FROM Shelf_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        boolean result = shelfStockDAO.doesShelfStockExistByItemId(1);

        assertFalse(result);
        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetAllShelfStocks() throws SQLException {
        String query = "SELECT * FROM Shelf_Stock";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getInt("shelf_id")).thenReturn(1, 2);
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getInt("shelf_capacity")).thenReturn(100, 200);
        when(resultSet.getInt("current_quantity")).thenReturn(50, 100);

        List<ShelfStock> result = shelfStockDAO.getAllShelfStocks();

        assertNotNull(result);
        assertEquals(2, result.size());
        
        ShelfStock first = result.get(0);
        assertEquals(1, first.getStockId());
        assertEquals(1, first.getItemId());
        assertEquals(100, first.getShelfCapacity());
        assertEquals(50, first.getCurrentQuantity());
        
        ShelfStock second = result.get(1);
        assertEquals(2, second.getStockId());
        assertEquals(2, second.getItemId());
        assertEquals(200, second.getShelfCapacity());
        assertEquals(100, second.getCurrentQuantity());
    }

    @Test
    public void testGetAllShelfStocksWithItemCodes() throws SQLException {
        String query = "SELECT ss.shelf_id, ss.item_id, ss.shelf_capacity, ss.current_quantity, i.item_code FROM Shelf_Stock ss JOIN Items i ON ss.item_id = i.item_id";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getInt("shelf_id")).thenReturn(1, 2);
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getInt("shelf_capacity")).thenReturn(100, 200);
        when(resultSet.getInt("current_quantity")).thenReturn(50, 100);
        when(resultSet.getString("item_code")).thenReturn("ITEM001", "ITEM002");

        Map<ShelfStock, String> result = shelfStockDAO.getAllShelfStocksWithItemCodes();

        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify the map contains the expected entries
        boolean foundItem1 = false, foundItem2 = false;
        for (Map.Entry<ShelfStock, String> entry : result.entrySet()) {
            ShelfStock stock = entry.getKey();
            String code = entry.getValue();
            
            if (stock.getItemId() == 1 && code.equals("ITEM001")) {
                foundItem1 = true;
            } else if (stock.getItemId() == 2 && code.equals("ITEM002")) {
                foundItem2 = true;
            }
        }
        assertTrue(foundItem1);
        assertTrue(foundItem2);
    }

    @Test
    public void testGetShelfStockByItemId() throws SQLException {
        String query = "SELECT * FROM Shelf_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("shelf_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("shelf_capacity")).thenReturn(100);
        when(resultSet.getInt("current_quantity")).thenReturn(50);

        ShelfStock result = shelfStockDAO.getShelfStockByItemId(1);

        assertNotNull(result);
        assertEquals(1, result.getStockId());
        assertEquals(1, result.getItemId());
        assertEquals(100, result.getShelfCapacity());
        assertEquals(50, result.getCurrentQuantity());
        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetShelfStockByItemIdNotFound() throws SQLException {
        String query = "SELECT * FROM Shelf_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        ShelfStock result = shelfStockDAO.getShelfStockByItemId(999);

        assertNull(result);
        verify(preparedStatement).setInt(eq(1), eq(999));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testDeleteShelfStockByItemId() throws SQLException {
        String query = "DELETE FROM Shelf_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        shelfStockDAO.deleteShelfStockByItemId(1);

        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateShelfStock() throws SQLException {
        String query = "UPDATE Shelf_Stock SET shelf_capacity = ?, current_quantity = ? WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(150);
        shelfStock.setCurrentQuantity(75);

        shelfStockDAO.updateShelfStock(shelfStock);

        verify(preparedStatement).setInt(eq(1), eq(150));
        verify(preparedStatement).setInt(eq(2), eq(75));
        verify(preparedStatement).setInt(eq(3), eq(1));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetReshelfQuantities() {
        List<ShelfStock> shelfStocks = new ArrayList<>();
        
        ShelfStock stock1 = new ShelfStock();
        stock1.setItemId(1);
        stock1.setShelfCapacity(100);
        stock1.setCurrentQuantity(50);
        shelfStocks.add(stock1);
        
        ShelfStock stock2 = new ShelfStock();
        stock2.setItemId(2);
        stock2.setShelfCapacity(200);
        stock2.setCurrentQuantity(200); // No reshelf needed
        shelfStocks.add(stock2);
        
        ShelfStock stock3 = new ShelfStock();
        stock3.setItemId(3);
        stock3.setShelfCapacity(150);
        stock3.setCurrentQuantity(100);
        shelfStocks.add(stock3);

        Map<Integer, Integer> result = shelfStockDAO.getReshelfQuantities(shelfStocks);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(50, result.get(1)); // 100 - 50
        assertEquals(50, result.get(3)); // 150 - 100
        assertNull(result.get(2)); // Should not be in map
    }

    @Test
    public void testConfirmReshelving() throws SQLException {
        // Create a spy of the DAO
        ShelfStockDAO spyDAO = spy(shelfStockDAO);
        
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(100);
        shelfStock.setCurrentQuantity(50);
        
        // Mock the getShelfStockByItemId method to return our shelf stock
        doReturn(shelfStock).when(spyDAO).getShelfStockByItemId(1);
        
        // Mock the updateShelfStock method to do nothing (avoid database calls)
        doNothing().when(spyDAO).updateShelfStock(any(ShelfStock.class));
        
        MainStock mainStock = new MainStock();
        mainStock.setItemId(1);
        mainStock.setQuantity(100);
        
        BatchSelection batchSelection = new BatchSelection(mainStock, 25);
        List<BatchSelection> selectedBatches = Arrays.asList(batchSelection);
        
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        reshelvingInfo.put(1, selectedBatches);
        
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        reshelfQuantities.put(1, 25);

        spyDAO.confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);

        verify(spyDAO).getShelfStockByItemId(1);
        verify(spyDAO).updateShelfStock(any(ShelfStock.class));
        verify(mainStockDAO).updateMainStock(mainStock);
        verify(reorderLevelDAO).updateTotalStock(1, 25);
        
        // Verify the shelf stock was updated with new quantity
        assertEquals(75, shelfStock.getCurrentQuantity()); // 50 + 25
    }
} 