package Testing2.dao;

import dao.ShelfStockDAO;
import model.ShelfStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShelfStockDAOTest {

    private Connection connection;
    private ShelfStockDAO shelfStockDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        shelfStockDAO = new ShelfStockDAO(connection); // Using constructor injection to pass mock connection
    }

    @Test
    public void testAddShelfStock() throws SQLException {
        String query = "INSERT INTO Shelf_Stock (item_id, shelf_capacity, current_quantity) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(100);
        shelfStock.setCurrentQuantity(50);

        shelfStockDAO.addShelfStock(shelfStock);

        verify(preparedStatement).setInt(1, shelfStock.getItemId());
        verify(preparedStatement).setInt(2, shelfStock.getShelfCapacity());
        verify(preparedStatement).setInt(3, shelfStock.getCurrentQuantity());
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
    }

    @Test
    public void testGetAllShelfStocks() throws SQLException {
        String query = "SELECT * FROM Shelf_Stock";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("shelf_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getInt("shelf_capacity")).thenReturn(100);
        when(resultSet.getInt("current_quantity")).thenReturn(50);

        List<ShelfStock> shelfStocks = shelfStockDAO.getAllShelfStocks();
        assertNotNull(shelfStocks);
        assertEquals(1, shelfStocks.size());
        assertEquals(1, shelfStocks.get(0).getStockId());
        assertEquals(1, shelfStocks.get(0).getItemId());
        assertEquals(100, shelfStocks.get(0).getShelfCapacity());
        assertEquals(50, shelfStocks.get(0).getCurrentQuantity());
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

        ShelfStock shelfStock = shelfStockDAO.getShelfStockByItemId(1);
        assertNotNull(shelfStock);
        assertEquals(1, shelfStock.getStockId());
        assertEquals(1, shelfStock.getItemId());
        assertEquals(100, shelfStock.getShelfCapacity());
        assertEquals(50, shelfStock.getCurrentQuantity());
    }

    @Test
    public void testDeleteShelfStockByItemId() throws SQLException {
        String query = "DELETE FROM Shelf_Stock WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        shelfStockDAO.deleteShelfStockByItemId(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateShelfStock() throws SQLException {
        String query = "UPDATE Shelf_Stock SET shelf_capacity = ?, current_quantity = ? WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(1);
        shelfStock.setShelfCapacity(200);
        shelfStock.setCurrentQuantity(100);

        shelfStockDAO.updateShelfStock(shelfStock);

        verify(preparedStatement).setInt(1, shelfStock.getShelfCapacity());
        verify(preparedStatement).setInt(2, shelfStock.getCurrentQuantity());
        verify(preparedStatement).setInt(3, shelfStock.getItemId());
        verify(preparedStatement).executeUpdate();
    }
}
