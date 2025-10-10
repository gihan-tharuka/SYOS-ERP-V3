package Testing2.Testing.dao;

import dao.BillDAO;
import model.Bill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BillDAOTest {
    private Connection connection;
    private BillDAO billDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        billDAO = new BillDAO(connection);
    }

    @Test
    public void testAddBill() throws SQLException {
        String query = "INSERT INTO Bills (sale_id, total_price, discount, cash_tendered, change_amount, bill_date, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        Bill bill = new Bill();
        bill.setSaleId(1);
        bill.setTotalPrice(100.0);
        bill.setDiscount(10.0);
        bill.setCashTendered(90.0);
        bill.setChangeAmount(0.0);
        bill.setBillDate(new Date());
        bill.setPaymentMethod("Cash");

        billDAO.addBill(bill);

        verify(preparedStatement).setInt(1, bill.getSaleId());
        verify(preparedStatement).setDouble(2, bill.getTotalPrice());
        verify(preparedStatement).setDouble(3, bill.getDiscount());
        verify(preparedStatement).setDouble(4, bill.getCashTendered());
        verify(preparedStatement).setDouble(5, bill.getChangeAmount());
        verify(preparedStatement).setDate(eq(6), any(java.sql.Date.class));
        verify(preparedStatement).setString(7, bill.getPaymentMethod());
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
    }

    @Test
    public void testGetBillBySerialNumber() throws SQLException {
        String query = "SELECT * FROM Bills WHERE serial_number = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("serial_number")).thenReturn(1);
        when(resultSet.getInt("sale_id")).thenReturn(1);
        when(resultSet.getDouble("total_price")).thenReturn(100.0);
        when(resultSet.getDouble("discount")).thenReturn(10.0);
        when(resultSet.getDouble("cash_tendered")).thenReturn(90.0);
        when(resultSet.getDouble("change_amount")).thenReturn(0.0);
        when(resultSet.getDate("bill_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getString("payment_method")).thenReturn("Cash");

        Bill bill = billDAO.getBillBySerialNumber(1);
        assertNotNull(bill);
        assertEquals(1, bill.getSerialNumber());
        assertEquals(1, bill.getSaleId());
        assertEquals(100.0, bill.getTotalPrice());
        assertEquals(10.0, bill.getDiscount());
        assertEquals(90.0, bill.getCashTendered());
        assertEquals(0.0, bill.getChangeAmount());
        assertEquals("Cash", bill.getPaymentMethod());
    }

    @Test
    public void testGetBillBySerialNumberReturnsNull() throws SQLException {
        String query = "SELECT * FROM Bills WHERE serial_number = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Bill bill = billDAO.getBillBySerialNumber(999);
        assertNull(bill);
    }

    @Test
    public void testAddBillWithDifferentPaymentMethod() throws SQLException {
        String query = "INSERT INTO Bills (sale_id, total_price, discount, cash_tendered, change_amount, bill_date, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(2);

        Bill bill = new Bill();
        bill.setSaleId(2);
        bill.setTotalPrice(200.0);
        bill.setDiscount(20.0);
        bill.setCashTendered(200.0);
        bill.setChangeAmount(0.0);
        bill.setBillDate(new Date());
        bill.setPaymentMethod("Card");

        billDAO.addBill(bill);

        verify(preparedStatement).setString(7, "Card");
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddBillWithZeroDiscount() throws SQLException {
        String query = "INSERT INTO Bills (sale_id, total_price, discount, cash_tendered, change_amount, bill_date, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(3);

        Bill bill = new Bill();
        bill.setSaleId(3);
        bill.setTotalPrice(150.0);
        bill.setDiscount(0.0);
        bill.setCashTendered(150.0);
        bill.setChangeAmount(0.0);
        bill.setBillDate(new Date());
        bill.setPaymentMethod("Cash");

        billDAO.addBill(bill);

        verify(preparedStatement).setDouble(3, 0.0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddBillWithChangeAmount() throws SQLException {
        String query = "INSERT INTO Bills (sale_id, total_price, discount, cash_tendered, change_amount, bill_date, payment_method) VALUES (?, ?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(4);

        Bill bill = new Bill();
        bill.setSaleId(4);
        bill.setTotalPrice(50.0);
        bill.setDiscount(5.0);
        bill.setCashTendered(100.0);
        bill.setChangeAmount(45.0);
        bill.setBillDate(new Date());
        bill.setPaymentMethod("Cash");

        billDAO.addBill(bill);

        verify(preparedStatement).setDouble(5, 45.0);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetBillBySerialNumberWithAllFields() throws SQLException {
        String query = "SELECT * FROM Bills WHERE serial_number = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("serial_number")).thenReturn(5);
        when(resultSet.getInt("sale_id")).thenReturn(5);
        when(resultSet.getDouble("total_price")).thenReturn(75.50);
        when(resultSet.getDouble("discount")).thenReturn(7.55);
        when(resultSet.getDouble("cash_tendered")).thenReturn(80.0);
        when(resultSet.getDouble("change_amount")).thenReturn(12.05);
        when(resultSet.getDate("bill_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getString("payment_method")).thenReturn("Card");

        Bill bill = billDAO.getBillBySerialNumber(5);
        assertNotNull(bill);
        assertEquals(5, bill.getSerialNumber());
        assertEquals(5, bill.getSaleId());
        assertEquals(75.50, bill.getTotalPrice(), 0.01);
        assertEquals(7.55, bill.getDiscount(), 0.01);
        assertEquals(80.0, bill.getCashTendered(), 0.01);
        assertEquals(12.05, bill.getChangeAmount(), 0.01);
        assertEquals("Card", bill.getPaymentMethod());
    }
} 