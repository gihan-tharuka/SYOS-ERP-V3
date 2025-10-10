package Testing2.Testing.dao;

import dao.MainStockDAO;
import model.MainStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainStockDAOTest {
    private Connection connection;
    private MainStockDAO mainStockDAO;
    private PreparedStatement preparedStatement;
    private PreparedStatement adjustStockStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        adjustStockStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        mainStockDAO = new MainStockDAO(connection);
    }

    @Test
    public void testAddMainStock() throws SQLException {
        String insertQuery = "INSERT INTO Main_Stock (item_id, supplier_id, batch_code, purchase_date, purchase_price, quantity, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String adjustQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        
        when(connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(connection.prepareStatement(adjustQuery)).thenReturn(adjustStockStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        MainStock mainStock = new MainStock();
        mainStock.setItemId(1);
        mainStock.setSupplierId(1);
        mainStock.setBatchCode("BATCH001");
        mainStock.setPurchaseDate(new Date());
        mainStock.setPurchasePrice(100.0);
        mainStock.setQuantity(50);
        mainStock.setExpiryDate(new Date());

        mainStockDAO.addMainStock(mainStock);

        verify(preparedStatement).setInt(1, mainStock.getItemId());
        verify(preparedStatement).setInt(2, mainStock.getSupplierId());
        verify(preparedStatement).setString(3, mainStock.getBatchCode());
        verify(preparedStatement).setDate(eq(4), any(java.sql.Date.class));
        verify(preparedStatement).setDouble(5, mainStock.getPurchasePrice());
        verify(preparedStatement).setInt(6, mainStock.getQuantity());
        verify(preparedStatement).setDate(eq(7), any(java.sql.Date.class));
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
        
        // Verify adjustTotalStock was called
        verify(adjustStockStatement).setInt(1, mainStock.getQuantity());
        verify(adjustStockStatement).setInt(2, mainStock.getItemId());
        verify(adjustStockStatement).executeUpdate();
    }

    @Test
    public void testAddMainStockWithNullExpiryDate() throws SQLException {
        String insertQuery = "INSERT INTO Main_Stock (item_id, supplier_id, batch_code, purchase_date, purchase_price, quantity, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String adjustQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        
        when(connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(connection.prepareStatement(adjustQuery)).thenReturn(adjustStockStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        MainStock mainStock = new MainStock();
        mainStock.setItemId(1);
        mainStock.setSupplierId(1);
        mainStock.setBatchCode("BATCH001");
        mainStock.setPurchaseDate(new Date());
        mainStock.setPurchasePrice(100.0);
        mainStock.setQuantity(50);
        mainStock.setExpiryDate(null);

        mainStockDAO.addMainStock(mainStock);

        verify(preparedStatement).setNull(7, java.sql.Types.DATE);
        verify(preparedStatement).executeUpdate();
        
        // Verify adjustTotalStock was called
        verify(adjustStockStatement).setInt(1, mainStock.getQuantity());
        verify(adjustStockStatement).setInt(2, mainStock.getItemId());
        verify(adjustStockStatement).executeUpdate();
    }

    @Test
    public void testDoesMainStockExist() throws SQLException {
        String query = "SELECT COUNT(*) FROM Main_Stock WHERE item_id = ? AND batch_code = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        boolean exists = mainStockDAO.doesMainStockExist(1, "BATCH001");
        assertTrue(exists);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).setString(2, "BATCH001");
        verify(preparedStatement).executeQuery();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
    }

    @Test
    public void testDoesMainStockExistReturnsFalse() throws SQLException {
        String query = "SELECT COUNT(*) FROM Main_Stock WHERE item_id = ? AND batch_code = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        boolean exists = mainStockDAO.doesMainStockExist(1, "BATCH001");
        assertFalse(exists);
    }

    @Test
    public void testGetAllMainStocks() throws SQLException {
        String query = "SELECT * FROM Main_Stock";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("stock_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("supplier_id")).thenReturn(1);
        when(resultSet.getString("batch_code")).thenReturn("BATCH001");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(50);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        List<MainStock> mainStocks = mainStockDAO.getAllMainStocks();
        assertNotNull(mainStocks);
        assertEquals(1, mainStocks.size());
        assertEquals(1, mainStocks.get(0).getStockId());
        assertEquals("BATCH001", mainStocks.get(0).getBatchCode());
    }

    @Test
    public void testEditMainStock() throws SQLException {
        String selectQuery = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        String updateQuery = "UPDATE Main_Stock SET item_id = ?, supplier_id = ?, batch_code = ?, purchase_date = ?, purchase_price = ?, quantity = ?, expiry_date = ? WHERE stock_id = ?";
        String adjustQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        
        when(connection.prepareStatement(selectQuery)).thenReturn(preparedStatement);
        when(connection.prepareStatement(updateQuery)).thenReturn(preparedStatement);
        when(connection.prepareStatement(adjustQuery)).thenReturn(adjustStockStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("stock_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("supplier_id")).thenReturn(1);
        when(resultSet.getString("batch_code")).thenReturn("BATCH001");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(30);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        MainStock mainStock = new MainStock();
        mainStock.setStockId(1);
        mainStock.setItemId(1);
        mainStock.setSupplierId(1);
        mainStock.setBatchCode("BATCH001");
        mainStock.setPurchaseDate(new Date());
        mainStock.setPurchasePrice(100.0);
        mainStock.setQuantity(50);
        mainStock.setExpiryDate(new Date());

        mainStockDAO.editMainStock(mainStock);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(adjustStockStatement).executeUpdate();
    }

    @Test
    public void testGetMainStockById() throws SQLException {
        String query = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("stock_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("supplier_id")).thenReturn(1);
        when(resultSet.getString("batch_code")).thenReturn("BATCH001");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(50);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        MainStock mainStock = mainStockDAO.getMainStockById(1);
        assertNotNull(mainStock);
        assertEquals(1, mainStock.getStockId());
        assertEquals("BATCH001", mainStock.getBatchCode());
    }

    @Test
    public void testGetMainStockByIdReturnsNull() throws SQLException {
        String query = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        MainStock mainStock = mainStockDAO.getMainStockById(999);
        assertNull(mainStock);
    }

    @Test
    public void testDeleteMainStock() throws SQLException {
        String selectQuery = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        String deleteQuery = "DELETE FROM Main_Stock WHERE stock_id = ?";
        String adjustQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        
        when(connection.prepareStatement(selectQuery)).thenReturn(preparedStatement);
        when(connection.prepareStatement(deleteQuery)).thenReturn(preparedStatement);
        when(connection.prepareStatement(adjustQuery)).thenReturn(adjustStockStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("stock_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("supplier_id")).thenReturn(1);
        when(resultSet.getString("batch_code")).thenReturn("BATCH001");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(50);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        mainStockDAO.deleteMainStock(1);

        verify(preparedStatement, times(2)).setInt(1, 1);
        verify(preparedStatement, times(1)).executeUpdate();
        verify(adjustStockStatement).executeUpdate();
    }

    @Test
    public void testDeleteMainStockWithNullStock() throws SQLException {
        String selectQuery = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        when(connection.prepareStatement(selectQuery)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        mainStockDAO.deleteMainStock(999);

        verify(preparedStatement, never()).executeUpdate();
    }

    @Test
    public void testAdjustTotalStock() throws SQLException {
        String query = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        mainStockDAO.adjustTotalStock(1, 10);

        verify(preparedStatement).setInt(1, 10);
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetMainStocksByItemId() throws SQLException {
        String query = "SELECT * FROM Main_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("stock_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("supplier_id")).thenReturn(1);
        when(resultSet.getString("batch_code")).thenReturn("BATCH001");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(50);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        List<MainStock> mainStocks = mainStockDAO.getMainStocksByItemId(1);
        assertNotNull(mainStocks);
        assertEquals(1, mainStocks.size());
        assertEquals(1, mainStocks.get(0).getItemId());
    }

    @Test
    public void testUpdateMainStock() throws SQLException {
        String query = "UPDATE Main_Stock SET quantity = ? WHERE stock_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        MainStock mainStock = new MainStock();
        mainStock.setStockId(1);
        mainStock.setQuantity(75);

        mainStockDAO.updateMainStock(mainStock);

        verify(preparedStatement).setInt(1, 75);
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
    }
} 