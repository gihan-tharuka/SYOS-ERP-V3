package Testing2.dao;

import dao.DatabaseConnection;
import dao.ReportDAO;
import model.Bill;
import model.BillItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportDAOTest {

    private Connection connection;
    private ReportDAO reportDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // Mock the DatabaseConnection instance to return our mock connection
        DatabaseConnection databaseConnection = mock(DatabaseConnection.class);
        when(databaseConnection.getConnection()).thenReturn(connection);

        // Use the singleton instance for testing
        reportDAO = ReportDAO.getInstance();
    }

    @Test
    public void testGetDailySalesDetails() throws SQLException {
        String query = "SELECT bi.bill_item_id, bi.bill_id, bi.item_id, i.item_code, i.item_name, bi.quantity, (bi.quantity * i.price) AS item_total_price " +
                "FROM Bills b " +
                "JOIN bill_items bi ON b.serial_number = bi.bill_id " +
                "JOIN Items i ON bi.item_id = i.item_id " +
                "WHERE b.bill_date = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Date date = new Date(System.currentTimeMillis());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false); // Return true once, then false
        when(resultSet.getInt("bill_item_id")).thenReturn(1);
        when(resultSet.getInt("bill_id")).thenReturn(1);
        when(resultSet.getInt("item_id")).thenReturn(1);
        when(resultSet.getString("item_name")).thenReturn("Item 1");
        when(resultSet.getInt("quantity")).thenReturn(2);
        when(resultSet.getDouble("item_total_price")).thenReturn(200.0);

        List<BillItem> salesDetails = reportDAO.getDailySalesDetails(date);
        assertNotNull(salesDetails);
        assertEquals(1, salesDetails.size());
        BillItem billItem = salesDetails.get(0);
        assertEquals(1, billItem.getBillItemId());
        assertEquals(1, billItem.getBillId());
        assertEquals(1, billItem.getItemId());
        assertEquals("Item 1", billItem.getItemName());
        assertEquals(2, billItem.getQuantity());
        assertEquals(200.0, billItem.getItemTotalPrice());
    }

    @Test
    public void testGetReorderItems() throws SQLException {
        String query = "SELECT i.item_code, i.item_name, rl.total_stock, rl.threshold_quantity " +
                "FROM Items i " +
                "JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                "WHERE rl.total_stock < rl.threshold_quantity";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false); // Return true once, then false
        when(resultSet.getString("item_code")).thenReturn("code1");
        when(resultSet.getString("item_name")).thenReturn("Item 1");
        when(resultSet.getInt("total_stock")).thenReturn(10);
        when(resultSet.getInt("threshold_quantity")).thenReturn(20);

        List<Object[]> reorderItems = reportDAO.getReorderItems();
        assertNotNull(reorderItems);
        assertEquals(1, reorderItems.size());
        Object[] item = reorderItems.get(0);
        assertEquals("code1", item[0]);
        assertEquals("Item 1", item[1]);
        assertEquals(10, item[2]);
        assertEquals(10, item[3]); // Quantity to reorder
    }


    @Test
    public void testGetStockReport() throws SQLException {
        String query = "SELECT i.item_code, i.item_name, ms.batch_code, ms.quantity, ms.expiry_date " +
                "FROM Items i " +
                "JOIN Main_Stock ms ON i.item_id = ms.item_id";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false); // Return true once, then false
        when(resultSet.getString("item_code")).thenReturn("code1");
        when(resultSet.getString("item_name")).thenReturn("Item 1");
        when(resultSet.getString("batch_code")).thenReturn("batch1");
        when(resultSet.getInt("quantity")).thenReturn(50);
        when(resultSet.getDate("expiry_date")).thenReturn(new Date(System.currentTimeMillis()));

        List<Object[]> stockReport = reportDAO.getStockReport();
        assertNotNull(stockReport);
        assertEquals(1, stockReport.size());
        Object[] stock = stockReport.get(0);
        assertEquals("code1", stock[0]);
        assertEquals("Item 1", stock[1]);
        assertEquals("batch1", stock[2]);
        assertEquals(50, stock[3]);
        assertEquals(new Date(System.currentTimeMillis()), stock[4]);
    }


    @Test
    public void testGetBillReport() throws SQLException {
        String query = "SELECT * FROM Bills";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true).thenReturn(false); // Return true once, then false
        when(resultSet.getInt("serial_number")).thenReturn(1);
        when(resultSet.getInt("sale_id")).thenReturn(1);
        when(resultSet.getDouble("total_price")).thenReturn(200.0);
        when(resultSet.getDouble("discount")).thenReturn(10.0);
        when(resultSet.getDouble("cash_tendered")).thenReturn(210.0);
        when(resultSet.getDouble("change_amount")).thenReturn(10.0);
        when(resultSet.getDate("bill_date")).thenReturn(new Date(System.currentTimeMillis()));
        when(resultSet.getString("payment_method")).thenReturn("Cash");

        List<Bill> billReport = reportDAO.getBillReport();
        assertNotNull(billReport);
        assertEquals(1, billReport.size());
        Bill bill = billReport.get(0);
        assertEquals(1, bill.getSerialNumber());
        assertEquals(1, bill.getSaleId());
        assertEquals(200.0, bill.getTotalPrice());
        assertEquals(10.0, bill.getDiscount());
        assertEquals(210.0, bill.getCashTendered());
        assertEquals(10.0, bill.getChangeAmount());
        assertEquals("Cash", bill.getPaymentMethod());
    }

}
