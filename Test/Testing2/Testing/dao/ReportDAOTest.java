package Testing2.Testing.dao;

import dao.ReportDAO;
import dao.DatabaseConnection;
import model.BillItem;
import model.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportDAOTest {
    private Connection connection;
    private ReportDAO reportDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private DatabaseConnection databaseConnection;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        databaseConnection = mock(DatabaseConnection.class);
        
        when(databaseConnection.getConnection()).thenReturn(connection);
    }

    @Test
    public void testGetDailySalesDetails() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT bi.bill_item_id, bi.bill_id, bi.item_id, i.item_code, i.item_name, bi.quantity, (bi.quantity * i.price) AS item_total_price " +
                    "FROM Bills b " +
                    "JOIN bill_items bi ON b.serial_number = bi.bill_id " +
                    "JOIN Items i ON bi.item_id = i.item_id " +
                    "WHERE b.bill_date = ?";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            
            when(resultSet.getInt("bill_item_id")).thenReturn(1, 2);
            when(resultSet.getInt("bill_id")).thenReturn(1, 1);
            when(resultSet.getInt("item_id")).thenReturn(1, 2);
            when(resultSet.getString("item_name")).thenReturn("Item 1", "Item 2");
            when(resultSet.getInt("quantity")).thenReturn(5, 3);
            when(resultSet.getDouble("item_total_price")).thenReturn(100.0, 75.0);

            reportDAO = new ReportDAO();
            java.sql.Date testDate = new java.sql.Date(System.currentTimeMillis());
            List<BillItem> salesDetails = reportDAO.getDailySalesDetails(testDate);
            
            assertNotNull(salesDetails);
            assertEquals(2, salesDetails.size());
            
            BillItem firstItem = salesDetails.get(0);
            assertEquals(1, firstItem.getBillItemId());
            assertEquals(1, firstItem.getBillId());
            assertEquals(1, firstItem.getItemId());
            assertEquals("Item 1", firstItem.getItemName());
            assertEquals(5, firstItem.getQuantity());
            assertEquals(100.0, firstItem.getItemTotalPrice());
            
            BillItem secondItem = salesDetails.get(1);
            assertEquals(2, secondItem.getBillItemId());
            assertEquals(1, secondItem.getBillId());
            assertEquals(2, secondItem.getItemId());
            assertEquals("Item 2", secondItem.getItemName());
            assertEquals(3, secondItem.getQuantity());
            assertEquals(75.0, secondItem.getItemTotalPrice());
            
            verify(preparedStatement).setDate(1, testDate);
        }
    }

    @Test
    public void testGetDailySalesDetailsEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT bi.bill_item_id, bi.bill_id, bi.item_id, i.item_code, i.item_name, bi.quantity, (bi.quantity * i.price) AS item_total_price " +
                    "FROM Bills b " +
                    "JOIN bill_items bi ON b.serial_number = bi.bill_id " +
                    "JOIN Items i ON bi.item_id = i.item_id " +
                    "WHERE b.bill_date = ?";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            reportDAO = new ReportDAO();
            java.sql.Date testDate = new java.sql.Date(System.currentTimeMillis());
            List<BillItem> salesDetails = reportDAO.getDailySalesDetails(testDate);
            
            assertNotNull(salesDetails);
            assertEquals(0, salesDetails.size());
        }
    }

    @Test
    public void testGetReorderItems() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT i.item_code, i.item_name, rl.total_stock, rl.threshold_quantity " +
                    "FROM Items i " +
                    "JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                    "WHERE rl.total_stock < rl.threshold_quantity";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            
            when(resultSet.getString("item_code")).thenReturn("ITEM001", "ITEM002");
            when(resultSet.getString("item_name")).thenReturn("Item 1", "Item 2");
            when(resultSet.getInt("total_stock")).thenReturn(5, 10);
            when(resultSet.getInt("threshold_quantity")).thenReturn(10, 20);

            reportDAO = new ReportDAO();
            List<Object[]> reorderItems = reportDAO.getReorderItems();
            
            assertNotNull(reorderItems);
            assertEquals(2, reorderItems.size());
            
            Object[] firstItem = reorderItems.get(0);
            assertEquals("ITEM001", firstItem[0]);
            assertEquals("Item 1", firstItem[1]);
            assertEquals(5, firstItem[2]);
            assertEquals(5, firstItem[3]); // threshold - current = 10 - 5 = 5
            
            Object[] secondItem = reorderItems.get(1);
            assertEquals("ITEM002", secondItem[0]);
            assertEquals("Item 2", secondItem[1]);
            assertEquals(10, secondItem[2]);
            assertEquals(10, secondItem[3]); // threshold - current = 20 - 10 = 10
        }
    }

    @Test
    public void testGetReorderItemsEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT i.item_code, i.item_name, rl.total_stock, rl.threshold_quantity " +
                    "FROM Items i " +
                    "JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                    "WHERE rl.total_stock < rl.threshold_quantity";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            reportDAO = new ReportDAO();
            List<Object[]> reorderItems = reportDAO.getReorderItems();
            
            assertNotNull(reorderItems);
            assertEquals(0, reorderItems.size());
        }
    }

    @Test
    public void testGetStockReport() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT i.item_code, i.item_name, ms.batch_code, ms.quantity, ms.expiry_date " +
                    "FROM Items i " +
                    "JOIN Main_Stock ms ON i.item_id = ms.item_id";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            
            when(resultSet.getString("item_code")).thenReturn("ITEM001", "ITEM002");
            when(resultSet.getString("item_name")).thenReturn("Item 1", "Item 2");
            when(resultSet.getString("batch_code")).thenReturn("BATCH001", "BATCH002");
            when(resultSet.getInt("quantity")).thenReturn(100, 50);
            when(resultSet.getDate("expiry_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis() + 86400000));

            reportDAO = new ReportDAO();
            List<Object[]> stockReport = reportDAO.getStockReport();
            
            assertNotNull(stockReport);
            assertEquals(2, stockReport.size());
            
            Object[] firstStock = stockReport.get(0);
            assertEquals("ITEM001", firstStock[0]);
            assertEquals("Item 1", firstStock[1]);
            assertEquals("BATCH001", firstStock[2]);
            assertEquals(100, firstStock[3]);
            assertNotNull(firstStock[4]); // expiry_date
            
            Object[] secondStock = stockReport.get(1);
            assertEquals("ITEM002", secondStock[0]);
            assertEquals("Item 2", secondStock[1]);
            assertEquals("BATCH002", secondStock[2]);
            assertEquals(50, secondStock[3]);
            assertNotNull(secondStock[4]); // expiry_date
        }
    }

    @Test
    public void testGetStockReportEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT i.item_code, i.item_name, ms.batch_code, ms.quantity, ms.expiry_date " +
                    "FROM Items i " +
                    "JOIN Main_Stock ms ON i.item_id = ms.item_id";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            reportDAO = new ReportDAO();
            List<Object[]> stockReport = reportDAO.getStockReport();
            
            assertNotNull(stockReport);
            assertEquals(0, stockReport.size());
        }
    }

    @Test
    public void testGetBillReport() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT * FROM Bills";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            
            when(resultSet.getInt("serial_number")).thenReturn(1, 2);
            when(resultSet.getInt("sale_id")).thenReturn(1, 2);
            when(resultSet.getDouble("total_price")).thenReturn(100.0, 200.0);
            when(resultSet.getDouble("discount")).thenReturn(10.0, 20.0);
            when(resultSet.getDouble("cash_tendered")).thenReturn(90.0, 180.0);
            when(resultSet.getDouble("change_amount")).thenReturn(0.0, 0.0);
            when(resultSet.getDate("bill_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
            when(resultSet.getString("payment_method")).thenReturn("Cash", "Card");

            reportDAO = new ReportDAO();
            List<Bill> billReport = reportDAO.getBillReport();
            
            assertNotNull(billReport);
            assertEquals(2, billReport.size());
            
            Bill firstBill = billReport.get(0);
            assertEquals(1, firstBill.getSerialNumber());
            assertEquals(1, firstBill.getSaleId());
            assertEquals(100.0, firstBill.getTotalPrice());
            assertEquals(10.0, firstBill.getDiscount());
            assertEquals(90.0, firstBill.getCashTendered());
            assertEquals(0.0, firstBill.getChangeAmount());
            assertEquals("Cash", firstBill.getPaymentMethod());
            
            Bill secondBill = billReport.get(1);
            assertEquals(2, secondBill.getSerialNumber());
            assertEquals(2, secondBill.getSaleId());
            assertEquals(200.0, secondBill.getTotalPrice());
            assertEquals(20.0, secondBill.getDiscount());
            assertEquals(180.0, secondBill.getCashTendered());
            assertEquals(0.0, secondBill.getChangeAmount());
            assertEquals("Card", secondBill.getPaymentMethod());
        }
    }

    @Test
    public void testGetBillReportEmptyList() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT * FROM Bills";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            reportDAO = new ReportDAO();
            List<Bill> billReport = reportDAO.getBillReport();
            
            assertNotNull(billReport);
            assertEquals(0, billReport.size());
        }
    }

    @Test
    public void testGetInstance() {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            ReportDAO instance1 = ReportDAO.getInstance();
            ReportDAO instance2 = ReportDAO.getInstance();
            
            assertNotNull(instance1);
            assertNotNull(instance2);
            assertSame(instance1, instance2); // Singleton pattern verification
        }
    }

    @Test
    public void testGetDailySalesDetailsWithZeroQuantity() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT bi.bill_item_id, bi.bill_id, bi.item_id, i.item_code, i.item_name, bi.quantity, (bi.quantity * i.price) AS item_total_price " +
                    "FROM Bills b " +
                    "JOIN bill_items bi ON b.serial_number = bi.bill_id " +
                    "JOIN Items i ON bi.item_id = i.item_id " +
                    "WHERE b.bill_date = ?";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            
            when(resultSet.getInt("bill_item_id")).thenReturn(1);
            when(resultSet.getInt("bill_id")).thenReturn(1);
            when(resultSet.getInt("item_id")).thenReturn(1);
            when(resultSet.getString("item_name")).thenReturn("Zero Quantity Item");
            when(resultSet.getInt("quantity")).thenReturn(0);
            when(resultSet.getDouble("item_total_price")).thenReturn(0.0);

            reportDAO = new ReportDAO();
            java.sql.Date testDate = new java.sql.Date(System.currentTimeMillis());
            List<BillItem> salesDetails = reportDAO.getDailySalesDetails(testDate);
            
            assertNotNull(salesDetails);
            assertEquals(1, salesDetails.size());
            
            BillItem item = salesDetails.get(0);
            assertEquals(0, item.getQuantity());
            assertEquals(0.0, item.getItemTotalPrice());
        }
    }

    @Test
    public void testGetReorderItemsWithZeroStock() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT i.item_code, i.item_name, rl.total_stock, rl.threshold_quantity " +
                    "FROM Items i " +
                    "JOIN reorder_levels rl ON i.item_id = rl.item_id " +
                    "WHERE rl.total_stock < rl.threshold_quantity";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            
            when(resultSet.getString("item_code")).thenReturn("ITEM001");
            when(resultSet.getString("item_name")).thenReturn("Out of Stock Item");
            when(resultSet.getInt("total_stock")).thenReturn(0);
            when(resultSet.getInt("threshold_quantity")).thenReturn(10);

            reportDAO = new ReportDAO();
            List<Object[]> reorderItems = reportDAO.getReorderItems();
            
            assertNotNull(reorderItems);
            assertEquals(1, reorderItems.size());
            
            Object[] item = reorderItems.get(0);
            assertEquals("ITEM001", item[0]);
            assertEquals("Out of Stock Item", item[1]);
            assertEquals(0, item[2]);
            assertEquals(10, item[3]); // threshold - current = 10 - 0 = 10
        }
    }

    @Test
    public void testGetStockReportWithNullExpiryDate() throws SQLException {
        try (MockedStatic<DatabaseConnection> mockedDatabaseConnection = mockStatic(DatabaseConnection.class)) {
            mockedDatabaseConnection.when(DatabaseConnection::getInstance).thenReturn(databaseConnection);
            
            String query = "SELECT i.item_code, i.item_name, ms.batch_code, ms.quantity, ms.expiry_date " +
                    "FROM Items i " +
                    "JOIN Main_Stock ms ON i.item_id = ms.item_id";
            when(connection.prepareStatement(query)).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, false);
            
            when(resultSet.getString("item_code")).thenReturn("ITEM001");
            when(resultSet.getString("item_name")).thenReturn("No Expiry Item");
            when(resultSet.getString("batch_code")).thenReturn("BATCH001");
            when(resultSet.getInt("quantity")).thenReturn(100);
            when(resultSet.getDate("expiry_date")).thenReturn(null);

            reportDAO = new ReportDAO();
            List<Object[]> stockReport = reportDAO.getStockReport();
            
            assertNotNull(stockReport);
            assertEquals(1, stockReport.size());
            
            Object[] stock = stockReport.get(0);
            assertEquals("ITEM001", stock[0]);
            assertEquals("No Expiry Item", stock[1]);
            assertEquals("BATCH001", stock[2]);
            assertEquals(100, stock[3]);
            assertNull(stock[4]); // expiry_date is null
        }
    }
} 