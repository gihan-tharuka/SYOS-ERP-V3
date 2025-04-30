package Testing2.dao;

import dao.MainStockDAO;
import model.MainStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainStockDAOTest {

    private Connection connection;
    private MainStockDAO mainStockDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        mainStockDAO = new MainStockDAO(connection); // Using constructor injection to pass mock connection
    }

    @Test
    public void testAddMainStock() throws SQLException {
        String query = "INSERT INTO Main_Stock (item_id, supplier_id, batch_code, purchase_date, purchase_price, quantity, expiry_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        MainStock mainStock = new MainStock();
        mainStock.setItemId(1);
        mainStock.setSupplierId(1);
        mainStock.setBatchCode("batch1");
        mainStock.setPurchaseDate(new Date());
        mainStock.setPurchasePrice(100.0);
        mainStock.setQuantity(10);
        mainStock.setExpiryDate(new Date());

        String updateTotalStockQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        PreparedStatement updateTotalStockStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(updateTotalStockQuery)).thenReturn(updateTotalStockStatement);

        mainStockDAO.addMainStock(mainStock);

        verify(preparedStatement).setInt(1, mainStock.getItemId());
        verify(preparedStatement).setInt(2, mainStock.getSupplierId());
        verify(preparedStatement).setString(3, mainStock.getBatchCode());
        verify(preparedStatement).setDate(4, new java.sql.Date(mainStock.getPurchaseDate().getTime()));
        verify(preparedStatement).setDouble(5, mainStock.getPurchasePrice());
        verify(preparedStatement).setInt(6, mainStock.getQuantity());
        verify(preparedStatement).setDate(7, new java.sql.Date(mainStock.getExpiryDate().getTime()));
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);

        verify(updateTotalStockStatement).setInt(1, mainStock.getQuantity());
        verify(updateTotalStockStatement).setInt(2, mainStock.getItemId());
        verify(updateTotalStockStatement).executeUpdate();
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
        when(resultSet.getString("batch_code")).thenReturn("batch1");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(10);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        List<MainStock> mainStocks = mainStockDAO.getAllMainStocks();
        assertNotNull(mainStocks);
        assertEquals(1, mainStocks.size());
        assertEquals("batch1", mainStocks.get(0).getBatchCode());
    }

    @Test
    public void testEditMainStock() throws SQLException {
        MainStock existingStock = new MainStock();
        existingStock.setStockId(1);
        existingStock.setItemId(1);
        existingStock.setSupplierId(1);
        existingStock.setBatchCode("batch1");
        existingStock.setPurchaseDate(new Date());
        existingStock.setPurchasePrice(100.0);
        existingStock.setQuantity(10);
        existingStock.setExpiryDate(new Date());

        // Mock the result set to return the existing stock
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("stock_id")).thenReturn(existingStock.getStockId());
        when(resultSet.getInt("item_id")).thenReturn(existingStock.getItemId());
        when(resultSet.getInt("supplier_id")).thenReturn(existingStock.getSupplierId());
        when(resultSet.getString("batch_code")).thenReturn(existingStock.getBatchCode());
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(existingStock.getPurchaseDate().getTime()));
        when(resultSet.getDouble("purchase_price")).thenReturn(existingStock.getPurchasePrice());
        when(resultSet.getInt("quantity")).thenReturn(existingStock.getQuantity());
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(existingStock.getExpiryDate().getTime()));

        // Mock the select query
        String selectQuery = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        PreparedStatement selectStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(selectQuery)).thenReturn(selectStatement);
        when(selectStatement.executeQuery()).thenReturn(resultSet);

        // Mock the update query
        String updateQuery = "UPDATE Main_Stock SET item_id = ?, supplier_id = ?, batch_code = ?, purchase_date = ?, purchase_price = ?, quantity = ?, expiry_date = ? WHERE stock_id = ?";
        when(connection.prepareStatement(updateQuery)).thenReturn(preparedStatement);

        // Mock the update total stock query
        String updateTotalStockQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        PreparedStatement updateTotalStockStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(updateTotalStockQuery)).thenReturn(updateTotalStockStatement);

        MainStock updatedStock = new MainStock();
        updatedStock.setStockId(1);
        updatedStock.setItemId(1);
        updatedStock.setSupplierId(1);
        updatedStock.setBatchCode("batch2");
        updatedStock.setPurchaseDate(new Date());
        updatedStock.setPurchasePrice(120.0);
        updatedStock.setQuantity(15);
        updatedStock.setExpiryDate(new Date());

        mainStockDAO.editMainStock(updatedStock);

        verify(selectStatement).setInt(1, existingStock.getStockId());
        verify(selectStatement).executeQuery();
        verify(preparedStatement).setInt(1, updatedStock.getItemId());
        verify(preparedStatement).setInt(2, updatedStock.getSupplierId());
        verify(preparedStatement).setString(3, updatedStock.getBatchCode());
        verify(preparedStatement).setDate(4, new java.sql.Date(updatedStock.getPurchaseDate().getTime()));
        verify(preparedStatement).setDouble(5, updatedStock.getPurchasePrice());
        verify(preparedStatement).setInt(6, updatedStock.getQuantity());
        verify(preparedStatement).setDate(7, new java.sql.Date(updatedStock.getExpiryDate().getTime()));
        verify(preparedStatement).setInt(8, updatedStock.getStockId());
        verify(preparedStatement).executeUpdate();

        int quantityDifference = updatedStock.getQuantity() - existingStock.getQuantity();
        verify(updateTotalStockStatement).setInt(1, quantityDifference);
        verify(updateTotalStockStatement).setInt(2, updatedStock.getItemId());
        verify(updateTotalStockStatement, times(1)).executeUpdate();  // Ensure updateTotalStockStatement is called once
    }


    @Test
    public void testDeleteMainStock() throws SQLException {
        MainStock existingStock = new MainStock();
        existingStock.setStockId(1);
        existingStock.setItemId(1);
        existingStock.setSupplierId(1);
        existingStock.setBatchCode("batch1");
        existingStock.setPurchaseDate(new Date());
        existingStock.setPurchasePrice(100.0);
        existingStock.setQuantity(10);
        existingStock.setExpiryDate(new Date());

        // Mock the result set to return the existing stock
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("stock_id")).thenReturn(existingStock.getStockId());
        when(resultSet.getInt("item_id")).thenReturn(existingStock.getItemId());
        when(resultSet.getInt("supplier_id")).thenReturn(existingStock.getSupplierId());
        when(resultSet.getString("batch_code")).thenReturn(existingStock.getBatchCode());
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(existingStock.getPurchaseDate().getTime()));
        when(resultSet.getDouble("purchase_price")).thenReturn(existingStock.getPurchasePrice());
        when(resultSet.getInt("quantity")).thenReturn(existingStock.getQuantity());
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(existingStock.getExpiryDate().getTime()));

        // Mock the select query
        String selectQuery = "SELECT * FROM Main_Stock WHERE stock_id = ?";
        PreparedStatement selectStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(selectQuery)).thenReturn(selectStatement);
        when(selectStatement.executeQuery()).thenReturn(resultSet);

        // Mock the delete query
        String deleteQuery = "DELETE FROM Main_Stock WHERE stock_id = ?";
        PreparedStatement deleteStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(deleteQuery)).thenReturn(deleteStatement);

        // Mock the update total stock query
        String updateTotalStockQuery = "UPDATE Reorder_Levels SET total_stock = total_stock + ? WHERE item_id = ?";
        PreparedStatement updateTotalStockStatement = mock(PreparedStatement.class);
        when(connection.prepareStatement(updateTotalStockQuery)).thenReturn(updateTotalStockStatement);

        // Execute the deleteMainStock method
        mainStockDAO.deleteMainStock(existingStock.getStockId());

        // Verify the interactions
        verify(selectStatement).setInt(1, existingStock.getStockId());
        verify(selectStatement).executeQuery();
        verify(deleteStatement).setInt(1, existingStock.getStockId());
        verify(deleteStatement).executeUpdate();
        verify(updateTotalStockStatement).setInt(1, -existingStock.getQuantity());
        verify(updateTotalStockStatement).setInt(2, existingStock.getItemId());
        verify(updateTotalStockStatement).executeUpdate();
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
        when(resultSet.getString("batch_code")).thenReturn("batch1");
        when(resultSet.getDate("purchase_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getDouble("purchase_price")).thenReturn(100.0);
        when(resultSet.getInt("quantity")).thenReturn(10);
        when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));

        MainStock mainStock = mainStockDAO.getMainStockById(1);
        assertNotNull(mainStock);
        assertEquals("batch1", mainStock.getBatchCode());
    }
}
