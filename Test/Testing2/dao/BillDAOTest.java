package Testing2.dao;

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
        billDAO = new BillDAO(connection); // Using constructor injection to pass mock connection
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
        bill.setTotalPrice(200.0);
        bill.setDiscount(10.0);
        bill.setCashTendered(210.0);
        bill.setChangeAmount(10.0);
        bill.setBillDate(new Date());
        bill.setPaymentMethod("Cash");

        billDAO.addBill(bill);

        verify(preparedStatement).setInt(1, bill.getSaleId());
        verify(preparedStatement).setDouble(2, bill.getTotalPrice());
        verify(preparedStatement).setDouble(3, bill.getDiscount());
        verify(preparedStatement).setDouble(4, bill.getCashTendered());
        verify(preparedStatement).setDouble(5, bill.getChangeAmount());
        verify(preparedStatement).setDate(6, new java.sql.Date(bill.getBillDate().getTime()));
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
        when(resultSet.getDouble("total_price")).thenReturn(200.0);
        when(resultSet.getDouble("discount")).thenReturn(10.0);
        when(resultSet.getDouble("cash_tendered")).thenReturn(210.0);
        when(resultSet.getDouble("change_amount")).thenReturn(10.0);
        when(resultSet.getDate("bill_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getString("payment_method")).thenReturn("Cash");

        Bill bill = billDAO.getBillBySerialNumber(1);
        assertNotNull(bill);
        assertEquals(1, bill.getSerialNumber());
        assertEquals(1, bill.getSaleId());
        assertEquals(200.0, bill.getTotalPrice());
        assertEquals(10.0, bill.getDiscount());
        assertEquals(210.0, bill.getCashTendered());
        assertEquals(10.0, bill.getChangeAmount());
        assertEquals("Cash", bill.getPaymentMethod());
    }
}

