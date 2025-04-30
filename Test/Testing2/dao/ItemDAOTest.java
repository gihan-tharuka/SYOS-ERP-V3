package Testing2.dao;

import dao.ItemDAO;
import model.Item;
import observer.ReorderSubject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ItemDAOTest {

    private Connection connection;
    private ReorderSubject reorderSubject;
    private ItemDAO itemDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        reorderSubject = mock(ReorderSubject.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        itemDAO = new ItemDAO(connection, reorderSubject);
    }

    @Test
    public void testAddItem() throws SQLException {
        String query = "INSERT INTO items (item_code, item_name, price, discount) VALUES (?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        Item item = new Item(0, "code1", "Item 1", 100.0, 10.0);
        itemDAO.addItem(item);

        verify(preparedStatement).setString(1, item.getItemCode());
        verify(preparedStatement).setString(2, item.getItemName());
        verify(preparedStatement).setDouble(3, item.getPrice());
        verify(preparedStatement).setDouble(4, item.getDiscount());
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
        verify(reorderSubject).itemAdded(any(Item.class));
    }

    @Test
    public void testGetAllItems() throws SQLException {
        String query = "SELECT * FROM items";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_code")).thenReturn("code1");
        when(resultSet.getString("item_name")).thenReturn("Item 1");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getDouble("discount")).thenReturn(10.0);

        List<Item> items = itemDAO.getAllItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("Item 1", items.get(0).getItemName());
    }

    @Test
    public void testGetItemNameById() throws SQLException {
        String query = "SELECT item_name FROM Items WHERE item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("item_name")).thenReturn("Item 1");

        String itemName = itemDAO.getItemNameById(1);
        assertEquals("Item 1", itemName);
    }

    @Test
    public void testGetItemByCode() throws SQLException {
        String query = "SELECT * FROM Items WHERE item_code = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_code")).thenReturn("code1");
        when(resultSet.getString("item_name")).thenReturn("Item 1");
        when(resultSet.getDouble("price")).thenReturn(100.0);
        when(resultSet.getDouble("discount")).thenReturn(10.0);

        Item item = itemDAO.getItemByCode("code1");
        assertNotNull(item);
        assertEquals("Item 1", item.getItemName());
    }

    @Test
    public void testDeleteItemByCode() throws SQLException {
        String query = "DELETE FROM Items WHERE item_code = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        itemDAO.deleteItemByCode("code1");
        verify(preparedStatement).setString(1, "code1");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateItem() throws SQLException {
        String query = "UPDATE Items SET item_name = ?, price = ?, discount = ? WHERE item_code = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Item item = new Item(1, "code1", "Item 1", 100.0, 10.0);
        itemDAO.updateItem(item);

        verify(preparedStatement).setString(1, item.getItemName());
        verify(preparedStatement).setDouble(2, item.getPrice());
        verify(preparedStatement).setDouble(3, item.getDiscount());
        verify(preparedStatement).setString(4, item.getItemCode());
        verify(preparedStatement).executeUpdate();
    }
}
