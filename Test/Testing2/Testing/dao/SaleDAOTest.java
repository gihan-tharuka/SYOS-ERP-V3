package Testing2.Testing.dao;

import dao.SaleDAO;
import model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SaleDAOTest {
    private Connection connection;
    private SaleDAO saleDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        saleDAO = new SaleDAO(connection);
    }

    @Test
    public void testAddSale() throws SQLException {
        String query = "INSERT INTO Sales (sale_date, transaction_type, user_id) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        Sale sale = new Sale();
        sale.setSaleDate(new Date());
        sale.setTransactionType("POS");
        sale.setUserId(1);

        saleDAO.addSale(sale);

        verify(preparedStatement).setDate(eq(1), any(java.sql.Date.class));
        verify(preparedStatement).setString(eq(2), eq("POS"));
        verify(preparedStatement).setInt(eq(3), eq(1));
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(eq(1));
        assertEquals(1, sale.getSaleId());
    }

    @Test
    public void testAddSaleWithNullUserId() throws SQLException {
        String query = "INSERT INTO Sales (sale_date, transaction_type, user_id) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(2);

        Sale sale = new Sale();
        sale.setSaleDate(new Date());
        sale.setTransactionType("POS");
        sale.setUserId(null);

        saleDAO.addSale(sale);

        verify(preparedStatement).setDate(eq(1), any(java.sql.Date.class));
        verify(preparedStatement).setString(eq(2), eq("POS"));
        verify(preparedStatement).setNull(eq(3), eq(Types.INTEGER));
        verify(preparedStatement).executeUpdate();
        assertEquals(2, sale.getSaleId());
    }

    @Test
    public void testGetAllSales() throws SQLException {
        String query = "SELECT * FROM Sales";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getInt("sale_id")).thenReturn(1, 2);
        when(resultSet.getDate("sale_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()), new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getString("transaction_type")).thenReturn("POS", "Online");
        when(resultSet.getInt("user_id")).thenReturn(1, 2);
        when(resultSet.wasNull()).thenReturn(false, false);

        List<Sale> sales = saleDAO.getAllSales();
        
        assertNotNull(sales);
        assertEquals(2, sales.size());
        
        Sale firstSale = sales.get(0);
        assertEquals(1, firstSale.getSaleId());
        assertEquals("POS", firstSale.getTransactionType());
        assertEquals(1, firstSale.getUserId());
        
        Sale secondSale = sales.get(1);
        assertEquals(2, secondSale.getSaleId());
        assertEquals("Online", secondSale.getTransactionType());
        assertEquals(2, secondSale.getUserId());
    }

    @Test
    public void testGetAllSalesEmptyList() throws SQLException {
        String query = "SELECT * FROM Sales";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<Sale> sales = saleDAO.getAllSales();
        
        assertNotNull(sales);
        assertEquals(0, sales.size());
    }

    @Test
    public void testGetAllSalesWithNullUserId() throws SQLException {
        String query = "SELECT * FROM Sales";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        
        when(resultSet.getInt("sale_id")).thenReturn(1);
        when(resultSet.getDate("sale_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getString("transaction_type")).thenReturn("POS");
        when(resultSet.getInt("user_id")).thenReturn(0);
        when(resultSet.wasNull()).thenReturn(true);

        List<Sale> sales = saleDAO.getAllSales();
        
        assertNotNull(sales);
        assertEquals(1, sales.size());
        
        Sale sale = sales.get(0);
        assertEquals(1, sale.getSaleId());
        assertEquals("POS", sale.getTransactionType());
        assertNull(sale.getUserId());
    }
} 