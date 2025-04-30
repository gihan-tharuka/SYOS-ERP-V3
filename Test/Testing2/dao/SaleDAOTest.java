package Testing2.dao;

import dao.SaleDAO;
import model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;
import java.util.Date;

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
        saleDAO = new SaleDAO(connection); // Using constructor injection to pass mock connection
    }

    @Test
    public void testAddSale() throws SQLException {
        String query = "INSERT INTO Sales (sale_date, transaction_type) VALUES (?, ?)";
        when(connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        Sale sale = new Sale();
        sale.setSaleDate(new Date());
        sale.setTransactionType("Cash");

        saleDAO.addSale(sale);

        verify(preparedStatement).setDate(1, new java.sql.Date(sale.getSaleDate().getTime()));
        verify(preparedStatement).setString(2, sale.getTransactionType());
        verify(preparedStatement).executeUpdate();
        verify(resultSet).next();
        verify(resultSet).getInt(1);
    }

    @Test
    public void testGetAllSales() throws SQLException {
        String query = "SELECT * FROM Sales";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("sale_id")).thenReturn(1);
        when(resultSet.getDate("sale_date")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
        when(resultSet.getString("transaction_type")).thenReturn("Cash");

        List<Sale> sales = saleDAO.getAllSales();
        assertNotNull(sales);
        assertEquals(1, sales.size());
        assertEquals(1, sales.get(0).getSaleId());
        assertEquals("Cash", sales.get(0).getTransactionType());
    }
}

