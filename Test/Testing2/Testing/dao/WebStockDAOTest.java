package Testing2.Testing.dao;

import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.WebStockDAO;
import model.BatchSelection;
import model.MainStock;
import model.WebStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WebStockDAOTest {
    private Connection connection;
    private WebStockDAO webStockDAO;
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
        webStockDAO = new WebStockDAO(connection);
    }

    @Test
    public void testAddWebStockSuccess() throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM Web_Stock WHERE item_id = ?";
        String insertQuery = "INSERT INTO Web_Stock (item_id, web_capacity, current_quantity) VALUES (?, ?, ?)";
        
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

        WebStock webStock = new WebStock();
        webStock.setItemId(1);
        webStock.setWebCapacity(100);
        webStock.setCurrentQuantity(50);

        boolean result = webStockDAO.addWebStock(webStock);

        assertTrue(result);
        assertEquals(1, webStock.getStockId());
        verify(countStmt).setInt(eq(1), eq(1));
        verify(insertStmt).setInt(eq(1), eq(1));
        verify(insertStmt).setInt(eq(2), eq(100));
        verify(insertStmt).setInt(eq(3), eq(50));
        verify(insertStmt).executeUpdate();
    }

    @Test
    public void testAddWebStockAlreadyExists() throws SQLException {
        String countQuery = "SELECT COUNT(*) FROM Web_Stock WHERE item_id = ?";
        
        PreparedStatement countStmt = mock(PreparedStatement.class);
        ResultSet countResultSet = mock(ResultSet.class);
        
        when(connection.prepareStatement(countQuery)).thenReturn(countStmt);
        when(countStmt.executeQuery()).thenReturn(countResultSet);
        when(countResultSet.next()).thenReturn(true);
        when(countResultSet.getInt(1)).thenReturn(1); // Item already exists

        WebStock webStock = new WebStock();
        webStock.setItemId(1);
        webStock.setWebCapacity(100);
        webStock.setCurrentQuantity(50);

        boolean result = webStockDAO.addWebStock(webStock);

        assertFalse(result);
        verify(countStmt).setInt(eq(1), eq(1));
        verify(countStmt).executeQuery();
    }

    @Test
    public void testDoesWebStockExistByItemIdTrue() throws SQLException {
        String query = "SELECT COUNT(*) FROM Web_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        boolean result = webStockDAO.doesWebStockExistByItemId(1);

        assertTrue(result);
        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testDoesWebStockExistByItemIdFalse() throws SQLException {
        String query = "SELECT COUNT(*) FROM Web_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        boolean result = webStockDAO.doesWebStockExistByItemId(1);

        assertFalse(result);
        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetAllWebStocks() throws SQLException {
        String query = "SELECT * FROM Web_Stock";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getInt("web_id")).thenReturn(1, 2);
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getInt("web_capacity")).thenReturn(100, 200);
        when(resultSet.getInt("current_quantity")).thenReturn(50, 100);

        List<WebStock> result = webStockDAO.getAllWebStocks();

        assertNotNull(result);
        assertEquals(2, result.size());
        
        WebStock first = result.get(0);
        assertEquals(1, first.getStockId());
        assertEquals(1, first.getItemId());
        assertEquals(100, first.getWebCapacity());
        assertEquals(50, first.getCurrentQuantity());
        
        WebStock second = result.get(1);
        assertEquals(2, second.getStockId());
        assertEquals(2, second.getItemId());
        assertEquals(200, second.getWebCapacity());
        assertEquals(100, second.getCurrentQuantity());
    }

    @Test
    public void testGetAllWebStocksWithItemCodes() throws SQLException {
        String query = "SELECT ws.web_id, ws.item_id, ws.web_capacity, ws.current_quantity, i.item_code FROM Web_Stock ws JOIN Items i ON ws.item_id = i.item_id";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getInt("web_id")).thenReturn(1, 2);
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getInt("web_capacity")).thenReturn(100, 200);
        when(resultSet.getInt("current_quantity")).thenReturn(50, 100);
        when(resultSet.getString("item_code")).thenReturn("ITEM001", "ITEM002");

        Map<WebStock, String> result = webStockDAO.getAllWebStocksWithItemCodes();

        assertNotNull(result);
        assertEquals(2, result.size());
        
        // Verify the map contains the expected entries
        boolean foundItem1 = false, foundItem2 = false;
        for (Map.Entry<WebStock, String> entry : result.entrySet()) {
            WebStock stock = entry.getKey();
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
    public void testGetWebStockByItemId() throws SQLException {
        String query = "SELECT * FROM Web_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("web_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("web_capacity")).thenReturn(100);
        when(resultSet.getInt("current_quantity")).thenReturn(50);

        WebStock result = webStockDAO.getWebStockByItemId(1);

        assertNotNull(result);
        assertEquals(1, result.getStockId());
        assertEquals(1, result.getItemId());
        assertEquals(100, result.getWebCapacity());
        assertEquals(50, result.getCurrentQuantity());
        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetWebStockByItemIdNotFound() throws SQLException {
        String query = "SELECT * FROM Web_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        WebStock result = webStockDAO.getWebStockByItemId(999);

        assertNull(result);
        verify(preparedStatement).setInt(eq(1), eq(999));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testDeleteWebStockByItemId() throws SQLException {
        String query = "DELETE FROM Web_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        webStockDAO.deleteWebStockByItemId(1);

        verify(preparedStatement).setInt(eq(1), eq(1));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateWebStock() throws SQLException {
        String query = "UPDATE Web_Stock SET web_capacity = ?, current_quantity = ? WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        WebStock webStock = new WebStock();
        webStock.setItemId(1);
        webStock.setWebCapacity(150);
        webStock.setCurrentQuantity(75);

        webStockDAO.updateWebStock(webStock);

        verify(preparedStatement).setInt(eq(1), eq(150));
        verify(preparedStatement).setInt(eq(2), eq(75));
        verify(preparedStatement).setInt(eq(3), eq(1));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetReshelfQuantities() {
        List<WebStock> webStocks = new ArrayList<>();
        
        WebStock stock1 = new WebStock();
        stock1.setItemId(1);
        stock1.setWebCapacity(100);
        stock1.setCurrentQuantity(50);
        webStocks.add(stock1);
        
        WebStock stock2 = new WebStock();
        stock2.setItemId(2);
        stock2.setWebCapacity(200);
        stock2.setCurrentQuantity(200); // No reshelf needed
        webStocks.add(stock2);
        
        WebStock stock3 = new WebStock();
        stock3.setItemId(3);
        stock3.setWebCapacity(150);
        stock3.setCurrentQuantity(100);
        webStocks.add(stock3);

        Map<Integer, Integer> result = webStockDAO.getReshelfQuantities(webStocks);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(50, result.get(1)); // 100 - 50
        assertEquals(50, result.get(3)); // 150 - 100
        assertNull(result.get(2)); // Should not be in map
    }

    @Test
    public void testConfirmReshelving() throws SQLException {
        // Create a spy of the DAO
        WebStockDAO spyDAO = spy(webStockDAO);
        
        WebStock webStock = new WebStock();
        webStock.setItemId(1);
        webStock.setWebCapacity(100);
        webStock.setCurrentQuantity(50);
        
        // Mock the getWebStockByItemId method to return our web stock
        doReturn(webStock).when(spyDAO).getWebStockByItemId(1);
        
        // Mock the updateWebStock method to do nothing (avoid database calls)
        doNothing().when(spyDAO).updateWebStock(any(WebStock.class));
        
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

        verify(spyDAO).getWebStockByItemId(1);
        verify(spyDAO).updateWebStock(any(WebStock.class));
        verify(mainStockDAO).updateMainStock(mainStock);
        verify(reorderLevelDAO).updateTotalStock(1, 25);
        
        // Verify the web stock was updated with new quantity
        assertEquals(75, webStock.getCurrentQuantity()); // 50 + 25
    }
} 