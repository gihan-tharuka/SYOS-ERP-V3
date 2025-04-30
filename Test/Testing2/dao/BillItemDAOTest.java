package Testing2.dao;

import dao.BillItemDAO;
import model.BillItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BillItemDAOTest {

    private Connection connection;
    private BillItemDAO billItemDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        billItemDAO = new BillItemDAO(connection); // Using constructor injection to pass mock connection
    }

    @Test
    public void testAddBillItem() throws SQLException {
        String query = "INSERT INTO Bill_Items (bill_id, item_id, item_name, quantity, item_total_price) VALUES (?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        BillItem billItem = new BillItem();
        billItem.setBillId(1);
        billItem.setItemId(1);
        billItem.setItemName("Item 1");
        billItem.setQuantity(2);
        billItem.setItemTotalPrice(100.0);

        billItemDAO.addBillItem(billItem);

        verify(preparedStatement).setInt(1, billItem.getBillId());
        verify(preparedStatement).setInt(2, billItem.getItemId());
        verify(preparedStatement).setString(3, billItem.getItemName());
        verify(preparedStatement).setInt(4, billItem.getQuantity());
        verify(preparedStatement).setDouble(5, billItem.getItemTotalPrice());
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
    }

    @Test
    public void testGetBillItemsBySerialNumber() throws SQLException {
        String query = "SELECT * FROM Bill_Items WHERE bill_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("bill_item_id")).thenReturn(1);
        when(resultSet.getInt("bill_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_name")).thenReturn("Item 1");
        when(resultSet.getInt("quantity")).thenReturn(2);
        when(resultSet.getDouble("item_total_price")).thenReturn(100.0);

        List<BillItem> billItems = billItemDAO.getBillItemsBySerialNumber(1);
        assertNotNull(billItems);
        assertEquals(1, billItems.size());
        assertEquals(1, billItems.get(0).getBillItemId());
        assertEquals(1, billItems.get(0).getBillId());
        assertEquals(1, billItems.get(0).getItemId());
        assertEquals("Item 1", billItems.get(0).getItemName());
        assertEquals(2, billItems.get(0).getQuantity());
        assertEquals(100.0, billItems.get(0).getItemTotalPrice());
    }

    @Test
    public void testUpdateBillItem() throws SQLException {
        String query = "UPDATE Bill_Items SET item_id = ?, item_name = ?, quantity = ?, item_total_price = ? WHERE bill_item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        BillItem billItem = new BillItem();
        billItem.setBillItemId(1);
        billItem.setItemId(1);
        billItem.setItemName("Item 1");
        billItem.setQuantity(2);
        billItem.setItemTotalPrice(100.0);

        billItemDAO.updateBillItem(billItem);

        verify(preparedStatement).setInt(1, billItem.getItemId());
        verify(preparedStatement).setString(2, billItem.getItemName());
        verify(preparedStatement).setInt(3, billItem.getQuantity());
        verify(preparedStatement).setDouble(4, billItem.getItemTotalPrice());
        verify(preparedStatement).setInt(5, billItem.getBillItemId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteBillItem() throws SQLException {
        String query = "DELETE FROM Bill_Items WHERE bill_item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        billItemDAO.deleteBillItem(1);

        verify(preparedStatement).setInt(1, 1);
        verify(preparedStatement).executeUpdate();
    }
}

