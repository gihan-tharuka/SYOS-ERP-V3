package Testing2.Testing.dao;

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
        billItemDAO = new BillItemDAO(connection);
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
        billItem.setItemName("Test Item");
        billItem.setQuantity(5);
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
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("bill_item_id")).thenReturn(1, 2);
        when(resultSet.getInt("bill_id")).thenReturn(1, 1);
        when(resultSet.getInt("item_id")).thenReturn(1, 2);
        when(resultSet.getString("item_name")).thenReturn("Item 1", "Item 2");
        when(resultSet.getInt("quantity")).thenReturn(5, 3);
        when(resultSet.getDouble("item_total_price")).thenReturn(100.0, 75.0);

        List<BillItem> billItems = billItemDAO.getBillItemsBySerialNumber(1);
        assertNotNull(billItems);
        assertEquals(2, billItems.size());
        
        BillItem firstItem = billItems.get(0);
        assertEquals(1, firstItem.getBillItemId());
        assertEquals(1, firstItem.getBillId());
        assertEquals(1, firstItem.getItemId());
        assertEquals("Item 1", firstItem.getItemName());
        assertEquals(5, firstItem.getQuantity());
        assertEquals(100.0, firstItem.getItemTotalPrice());
        
        BillItem secondItem = billItems.get(1);
        assertEquals(2, secondItem.getBillItemId());
        assertEquals(1, secondItem.getBillId());
        assertEquals(2, secondItem.getItemId());
        assertEquals("Item 2", secondItem.getItemName());
        assertEquals(3, secondItem.getQuantity());
        assertEquals(75.0, secondItem.getItemTotalPrice());
    }

    @Test
    public void testGetBillItemsBySerialNumberEmptyList() throws SQLException {
        String query = "SELECT * FROM Bill_Items WHERE bill_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<BillItem> billItems = billItemDAO.getBillItemsBySerialNumber(999);
        assertNotNull(billItems);
        assertEquals(0, billItems.size());
    }

    @Test
    public void testUpdateBillItem() throws SQLException {
        String query = "UPDATE Bill_Items SET item_id = ?, item_name = ?, quantity = ?, item_total_price = ? WHERE bill_item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        BillItem billItem = new BillItem();
        billItem.setBillItemId(1);
        billItem.setItemId(2);
        billItem.setItemName("Updated Item");
        billItem.setQuantity(10);
        billItem.setItemTotalPrice(200.0);

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

    @Test
    public void testAddBillItemWithZeroQuantity() throws SQLException {
        String query = "INSERT INTO Bill_Items (bill_id, item_id, item_name, quantity, item_total_price) VALUES (?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(2);

        BillItem billItem = new BillItem();
        billItem.setBillId(2);
        billItem.setItemId(3);
        billItem.setItemName("Zero Quantity Item");
        billItem.setQuantity(0);
        billItem.setItemTotalPrice(0.0);

        billItemDAO.addBillItem(billItem);

        verify(preparedStatement).setInt(4, 0);
        verify(preparedStatement).setDouble(5, 0.0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddBillItemWithDecimalPrice() throws SQLException {
        String query = "INSERT INTO Bill_Items (bill_id, item_id, item_name, quantity, item_total_price) VALUES (?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        BillItem billItem = new BillItem();
        billItem.setBillId(3);
        billItem.setItemId(4);
        billItem.setItemName("Decimal Price Item");
        billItem.setQuantity(2);
        billItem.setItemTotalPrice(45.99);

        billItemDAO.addBillItem(billItem);

        verify(preparedStatement).setDouble(5, 45.99);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetBillItemsBySerialNumberSingleItem() throws SQLException {
        String query = "SELECT * FROM Bill_Items WHERE bill_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("bill_item_id")).thenReturn(1);
        when(resultSet.getInt("bill_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_name")).thenReturn("Single Item");
        when(resultSet.getInt("quantity")).thenReturn(1);
        when(resultSet.getDouble("item_total_price")).thenReturn(25.50);

        List<BillItem> billItems = billItemDAO.getBillItemsBySerialNumber(1);
        assertNotNull(billItems);
        assertEquals(1, billItems.size());
        
        BillItem item = billItems.get(0);
        assertEquals(1, item.getBillItemId());
        assertEquals(1, item.getBillId());
        assertEquals(1, item.getItemId());
        assertEquals("Single Item", item.getItemName());
        assertEquals(1, item.getQuantity());
        assertEquals(25.50, item.getItemTotalPrice(), 0.01);
    }

    @Test
    public void testUpdateBillItemWithConstructor() throws SQLException {
        String query = "UPDATE Bill_Items SET item_id = ?, item_name = ?, quantity = ?, item_total_price = ? WHERE bill_item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        BillItem billItem = new BillItem(1, 1, 5, "Constructor Item", 7, 175.0);

        billItemDAO.updateBillItem(billItem);

        verify(preparedStatement).setInt(1, 5);
        verify(preparedStatement).setString(2, "Constructor Item");
        verify(preparedStatement).setInt(3, 7);
        verify(preparedStatement).setDouble(4, 175.0);
        verify(preparedStatement).setInt(5, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteBillItemWithDifferentId() throws SQLException {
        String query = "DELETE FROM Bill_Items WHERE bill_item_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        billItemDAO.deleteBillItem(999);

        verify(preparedStatement).setInt(1, 999);
        verify(preparedStatement).executeUpdate();
    }
} 