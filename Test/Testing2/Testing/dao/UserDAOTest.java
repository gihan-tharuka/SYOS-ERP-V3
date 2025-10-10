package Testing2.Testing.dao;

import dao.UserDAO;
import model.Admin;
import model.Cashier;
import model.Customer;
import model.Supplier;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOTest {
    private Connection connection;
    private UserDAO userDAO;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        userDAO = new UserDAO(connection);
    }

    @Test
    public void testGetUserByUsernameAdmin() throws SQLException {
        String query = "SELECT * FROM admins WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("admin1");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("email")).thenReturn("admin@test.com");

        User result = userDAO.getUserByUsername("admin1", "admin");

        assertNotNull(result);
        assertTrue(result instanceof Admin);
        Admin admin = (Admin) result;
        assertEquals("admin1", admin.getUsername());
        assertEquals("password123", admin.getPassword());
        assertEquals("admin@test.com", admin.getEmail());
        verify(preparedStatement).setString(eq(1), eq("admin1"));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetUserByUsernameCashier() throws SQLException {
        String query = "SELECT * FROM cashiers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("cashier1");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("full_name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("cashier@test.com");
        when(resultSet.getString("mobile")).thenReturn("1234567890");

        User result = userDAO.getUserByUsername("cashier1", "cashier");

        assertNotNull(result);
        assertTrue(result instanceof Cashier);
        Cashier cashier = (Cashier) result;
        assertEquals("cashier1", cashier.getUsername());
        assertEquals("password123", cashier.getPassword());
        assertEquals("John Doe", cashier.getFullName());
        assertEquals("cashier@test.com", cashier.getEmail());
        assertEquals("1234567890", cashier.getMobile());
    }

    @Test
    public void testGetUserByUsernameSupplier() throws SQLException {
        String query = "SELECT * FROM suppliers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("supplier1");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("company_name")).thenReturn("Test Company");
        when(resultSet.getString("contact_person")).thenReturn("Jane Smith");
        when(resultSet.getString("email")).thenReturn("supplier@test.com");
        when(resultSet.getString("mobile")).thenReturn("0987654321");

        User result = userDAO.getUserByUsername("supplier1", "supplier");

        assertNotNull(result);
        assertTrue(result instanceof Supplier);
        Supplier supplier = (Supplier) result;
        assertEquals("supplier1", supplier.getUsername());
        assertEquals("password123", supplier.getPassword());
        assertEquals("Test Company", supplier.getCompanyName());
        assertEquals("Jane Smith", supplier.getContactPerson());
        assertEquals("supplier@test.com", supplier.getEmail());
        assertEquals("0987654321", supplier.getMobile());
    }

    @Test
    public void testGetUserByUsernameCustomer() throws SQLException {
        String query = "SELECT * FROM customers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("customer1");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("email")).thenReturn("customer@test.com");
        when(resultSet.getString("mobile")).thenReturn("5555555555");

        User result = userDAO.getUserByUsername("customer1", "customer");

        assertNotNull(result);
        assertTrue(result instanceof Customer);
        Customer customer = (Customer) result;
        assertEquals("customer1", customer.getUsername());
        assertEquals("password123", customer.getPassword());
        assertEquals("customer@test.com", customer.getEmail());
        assertEquals("5555555555", customer.getMobile());
    }

    @Test
    public void testGetUserByUsernameNotFound() throws SQLException {
        String query = "SELECT * FROM admins WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User result = userDAO.getUserByUsername("nonexistent", "admin");

        assertNull(result);
        verify(preparedStatement).setString(eq(1), eq("nonexistent"));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetUserByUsernameInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.getUserByUsername("test", "invalid_role");
        });
    }

    @Test
    public void testAddUserAdmin() throws SQLException {
        String query = "INSERT INTO admins (username, password, email) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Admin admin = new Admin("admin1", "password123", "admin@test.com");
        userDAO.addUser(admin, "admin");

        verify(preparedStatement).setString(eq(1), eq("admin1"));
        verify(preparedStatement).setString(eq(2), eq("password123"));
        verify(preparedStatement).setString(eq(3), eq("admin@test.com"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddUserCashier() throws SQLException {
        String query = "INSERT INTO cashiers (username, password, email, full_name, mobile) VALUES (?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Cashier cashier = new Cashier("cashier1", "password123", "John Doe", "cashier@test.com", "1234567890");
        userDAO.addUser(cashier, "cashier");

        verify(preparedStatement).setString(eq(1), eq("cashier1"));
        verify(preparedStatement).setString(eq(2), eq("password123"));
        verify(preparedStatement).setString(eq(3), eq("cashier@test.com"));
        verify(preparedStatement).setString(eq(4), eq("John Doe"));
        verify(preparedStatement).setString(eq(5), eq("1234567890"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddUserSupplier() throws SQLException {
        String query = "INSERT INTO suppliers (username, password, email, company_name, contact_person, mobile) VALUES (?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Supplier supplier = new Supplier("supplier1", "password123", "Test Company", "Jane Smith", "supplier@test.com", "0987654321");
        userDAO.addUser(supplier, "supplier");

        verify(preparedStatement).setString(eq(1), eq("supplier1"));
        verify(preparedStatement).setString(eq(2), eq("password123"));
        verify(preparedStatement).setString(eq(3), eq("supplier@test.com"));
        verify(preparedStatement).setString(eq(4), eq("Test Company"));
        verify(preparedStatement).setString(eq(5), eq("Jane Smith"));
        verify(preparedStatement).setString(eq(6), eq("0987654321"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddUserCustomer() throws SQLException {
        String query = "INSERT INTO customers (username, password, email, mobile) VALUES (?, ?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Customer customer = new Customer("customer1", "password123", "customer@test.com", "5555555555");
        userDAO.addUser(customer, "customer");

        verify(preparedStatement).setString(eq(1), eq("customer1"));
        verify(preparedStatement).setString(eq(2), eq("password123"));
        verify(preparedStatement).setString(eq(3), eq("customer@test.com"));
        verify(preparedStatement).setString(eq(4), eq("5555555555"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddUserInvalidRole() {
        Admin admin = new Admin("admin1", "password123", "admin@test.com");
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.addUser(admin, "invalid_role");
        });
    }

    @Test
    public void testGetAllUsersAdmin() throws SQLException {
        String query = "SELECT * FROM admins";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        
        when(resultSet.getString("username")).thenReturn("admin1", "admin2");
        when(resultSet.getString("password")).thenReturn("password123", "password456");
        when(resultSet.getString("email")).thenReturn("admin1@test.com", "admin2@test.com");

        List<User> result = userDAO.getAllUsers("admin");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0) instanceof Admin);
        assertTrue(result.get(1) instanceof Admin);
        assertEquals("admin1", result.get(0).getUsername());
        assertEquals("admin2", result.get(1).getUsername());
    }

    @Test
    public void testGetAllUsersCashier() throws SQLException {
        String query = "SELECT * FROM cashiers";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        
        when(resultSet.getString("username")).thenReturn("cashier1");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("full_name")).thenReturn("John Doe");
        when(resultSet.getString("email")).thenReturn("cashier@test.com");
        when(resultSet.getString("mobile")).thenReturn("1234567890");

        List<User> result = userDAO.getAllUsers("cashier");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Cashier);
        Cashier cashier = (Cashier) result.get(0);
        assertEquals("cashier1", cashier.getUsername());
        assertEquals("John Doe", cashier.getFullName());
    }

    @Test
    public void testGetAllUsersSupplier() throws SQLException {
        String query = "SELECT * FROM suppliers";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        
        when(resultSet.getString("username")).thenReturn("supplier1");
        when(resultSet.getString("password")).thenReturn("password123");
        when(resultSet.getString("company_name")).thenReturn("Test Company");
        when(resultSet.getString("contact_person")).thenReturn("Jane Smith");
        when(resultSet.getString("email")).thenReturn("supplier@test.com");
        when(resultSet.getString("mobile")).thenReturn("0987654321");

        List<User> result = userDAO.getAllUsers("supplier");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0) instanceof Supplier);
        Supplier supplier = (Supplier) result.get(0);
        assertEquals("supplier1", supplier.getUsername());
        assertEquals("Test Company", supplier.getCompanyName());
    }

    @Test
    public void testGetAllUsersInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.getAllUsers("invalid_role");
        });
    }

    @Test
    public void testUpdateUser() throws SQLException {
        String query = "UPDATE admins SET password = ?, email = ? WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Admin admin = new Admin("admin1", "newpassword", "newemail@test.com");
        userDAO.updateUser(admin, "admin");

        verify(preparedStatement).setString(eq(1), eq("newpassword"));
        verify(preparedStatement).setString(eq(2), eq("newemail@test.com"));
        verify(preparedStatement).setString(eq(3), eq("admin1"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testUpdateUserInvalidRole() {
        Admin admin = new Admin("admin1", "password123", "admin@test.com");
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.updateUser(admin, "invalid_role");
        });
    }

    @Test
    public void testDeleteUserSuccess() throws SQLException {
        String query = "DELETE FROM admins WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userDAO.deleteUser("admin1", "admin");

        assertTrue(result);
        verify(preparedStatement).setString(eq(1), eq("admin1"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteUserNotFound() throws SQLException {
        String query = "DELETE FROM admins WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = userDAO.deleteUser("nonexistent", "admin");

        assertFalse(result);
        verify(preparedStatement).setString(eq(1), eq("nonexistent"));
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteUserInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> {
            userDAO.deleteUser("test", "invalid_role");
        });
    }

    @Test
    public void testGetSupplierIdByUsername() throws SQLException {
        String query = "SELECT supplier_id FROM suppliers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("supplier_id")).thenReturn(123);

        Integer result = userDAO.getSupplierIdByUsername("supplier1");

        assertEquals(123, result);
        verify(preparedStatement).setString(eq(1), eq("supplier1"));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetSupplierIdByUsernameNotFound() throws SQLException {
        String query = "SELECT supplier_id FROM suppliers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        Integer result = userDAO.getSupplierIdByUsername("nonexistent");

        assertNull(result);
        verify(preparedStatement).setString(eq(1), eq("nonexistent"));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetUsernameById() throws SQLException {
        String query = "SELECT username FROM suppliers WHERE supplier_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("supplier1");

        String result = userDAO.getUsernameById(123);

        assertEquals("supplier1", result);
        verify(preparedStatement).setInt(eq(1), eq(123));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetUsernameByIdNotFound() throws SQLException {
        String query = "SELECT username FROM suppliers WHERE supplier_id = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        String result = userDAO.getUsernameById(999);

        assertNull(result);
        verify(preparedStatement).setInt(eq(1), eq(999));
        verify(preparedStatement).executeQuery();
    }

    @Test
    public void testGetUserIdByUsernameFoundInAdmins() throws SQLException {
        // Mock the first query (admins table)
        String query1 = "SELECT admin_id FROM admins WHERE username = ?";
        PreparedStatement stmt1 = mock(PreparedStatement.class);
        ResultSet rs1 = mock(ResultSet.class);
        
        when(connection.prepareStatement(query1)).thenReturn(stmt1);
        when(stmt1.executeQuery()).thenReturn(rs1);
        when(rs1.next()).thenReturn(true);
        when(rs1.getInt("admin_id")).thenReturn(456);

        Integer result = userDAO.getUserIdByUsername("admin1");

        assertEquals(456, result);
        verify(stmt1).setString(eq(1), eq("admin1"));
        verify(stmt1).executeQuery();
    }

    @Test
    public void testGetUserIdByUsernameFoundInCashiers() throws SQLException {
        // Mock the first query (admins table) - no result
        String query1 = "SELECT admin_id FROM admins WHERE username = ?";
        PreparedStatement stmt1 = mock(PreparedStatement.class);
        ResultSet rs1 = mock(ResultSet.class);
        
        when(connection.prepareStatement(query1)).thenReturn(stmt1);
        when(stmt1.executeQuery()).thenReturn(rs1);
        when(rs1.next()).thenReturn(false);
        
        // Mock the second query (cashiers table) - found
        String query2 = "SELECT cashier_id FROM cashiers WHERE username = ?";
        PreparedStatement stmt2 = mock(PreparedStatement.class);
        ResultSet rs2 = mock(ResultSet.class);
        
        when(connection.prepareStatement(query2)).thenReturn(stmt2);
        when(stmt2.executeQuery()).thenReturn(rs2);
        when(rs2.next()).thenReturn(true);
        when(rs2.getInt("cashier_id")).thenReturn(789);

        Integer result = userDAO.getUserIdByUsername("cashier1");

        assertEquals(789, result);
        verify(stmt1).setString(eq(1), eq("cashier1"));
        verify(stmt2).setString(eq(1), eq("cashier1"));
    }

    @Test
    public void testGetUserIdByUsernameNotFound() throws SQLException {
        // Mock all queries to return no results
        String[] queries = {
            "SELECT admin_id FROM admins WHERE username = ?",
            "SELECT cashier_id FROM cashiers WHERE username = ?",
            "SELECT supplier_id FROM suppliers WHERE username = ?",
            "SELECT customer_id FROM customers WHERE username = ?"
        };
        
        for (String query : queries) {
            PreparedStatement stmt = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);
            when(connection.prepareStatement(query)).thenReturn(stmt);
            when(stmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false);
        }

        Integer result = userDAO.getUserIdByUsername("nonexistent");

        assertNull(result);
    }
} 