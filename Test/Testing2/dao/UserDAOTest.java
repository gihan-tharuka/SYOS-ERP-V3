package Testing2.dao;

import dao.UserDAO;
import model.Admin;
import model.Cashier;
import model.Supplier;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        userDAO = new UserDAO(connection); // Using constructor injection to pass mock connection
    }

    @Test
    public void testGetUserByUsername_Admin() throws SQLException {
        String query = "SELECT * FROM admins WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("admin1");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("email")).thenReturn("admin@example.com");

        User user = userDAO.getUserByUsername("admin1", "admin");
        assertNotNull(user);
        assertEquals("admin1", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("admin@example.com", user.getEmail());
    }

    @Test
    public void testGetUserByUsername_Cashier() throws SQLException {
        String query = "SELECT * FROM cashiers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("cashier1");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("full_name")).thenReturn("Cashier Name");
        when(resultSet.getString("email")).thenReturn("cashier@example.com");
        when(resultSet.getString("mobile")).thenReturn("123456789");

        User user = userDAO.getUserByUsername("cashier1", "cashier");
        assertNotNull(user);
        assertEquals("cashier1", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("Cashier Name", ((Cashier) user).getFullName());
        assertEquals("cashier@example.com", user.getEmail());
        assertEquals("123456789", ((Cashier) user).getMobile());
    }

    @Test
    public void testGetUserByUsername_Supplier() throws SQLException {
        String query = "SELECT * FROM suppliers WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("supplier1");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("company_name")).thenReturn("Supplier Co.");
        when(resultSet.getString("contact_person")).thenReturn("Supplier Contact");
        when(resultSet.getString("email")).thenReturn("supplier@example.com");
        when(resultSet.getString("mobile")).thenReturn("987654321");

        User user = userDAO.getUserByUsername("supplier1", "supplier");
        assertNotNull(user);
        assertEquals("supplier1", user.getUsername());
        assertEquals("password", user.getPassword());
        assertEquals("Supplier Co.", ((Supplier) user).getCompanyName());
        assertEquals("Supplier Contact", ((Supplier) user).getContactPerson());
        assertEquals("supplier@example.com", user.getEmail());
        assertEquals("987654321", ((Supplier) user).getMobile());
    }

    @Test
    public void testAddUser_Admin() throws SQLException {
        String query = "INSERT INTO admins (username, password, email) VALUES (?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Admin admin = new Admin("admin1", "password", "admin@example.com");
        userDAO.addUser(admin, "admin");

        verify(preparedStatement).setString(1, admin.getUsername());
        verify(preparedStatement).setString(2, admin.getPassword());
        verify(preparedStatement).setString(3, admin.getEmail());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testAddUser_Cashier() throws SQLException {
        String query = "INSERT INTO cashiers (username, password, email, full_name, mobile) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class); // Mock PreparedStatement
        when(connection.prepareStatement(query)).thenReturn(mockPreparedStatement);

        Cashier cashier = new Cashier("cashier1", "password", "Cashier Name", "cashier@example.com", "123456789");
        userDAO.addUser(cashier, "cashier");

        verify(mockPreparedStatement).setString(1, cashier.getUsername());
        verify(mockPreparedStatement).setString(2, cashier.getPassword());
        verify(mockPreparedStatement).setString(3, cashier.getEmail());
        verify(mockPreparedStatement).setString(4, cashier.getFullName());
        verify(mockPreparedStatement).setString(5, cashier.getMobile());
        verify(mockPreparedStatement).executeUpdate();
    }



    @Test
    public void testAddUser_Supplier() throws SQLException {
        String query = "INSERT INTO suppliers (username, password, email, company_name, contact_person, mobile) VALUES (?, ?, ?, ?, ?, ?)";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Supplier supplier = new Supplier("supplier1", "password", "Supplier Co.", "Supplier Contact", "supplier@example.com", "987654321");
        userDAO.addUser(supplier, "supplier");

        verify(preparedStatement).setString(1, supplier.getUsername());
        verify(preparedStatement).setString(2, supplier.getPassword());
        verify(preparedStatement).setString(3, supplier.getEmail());
        verify(preparedStatement).setString(4, supplier.getCompanyName());
        verify(preparedStatement).setString(5, supplier.getContactPerson());
        verify(preparedStatement).setString(6, supplier.getMobile());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testGetAllUsers_Admin() throws SQLException {
        String query = "SELECT * FROM admins";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);

        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("username")).thenReturn("admin1");
        when(resultSet.getString("password")).thenReturn("password");
        when(resultSet.getString("email")).thenReturn("admin@example.com");

        List<User> users = userDAO.getAllUsers("admin");
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("admin1", users.get(0).getUsername());
        assertEquals("password", users.get(0).getPassword());
        assertEquals("admin@example.com", users.get(0).getEmail());
    }

    @Test
    public void testUpdateUser_Admin() throws SQLException {
        String query = "UPDATE admins SET password = ?, email = ? WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        Admin admin = new Admin("admin1", "newpassword", "newadmin@example.com");
        userDAO.updateUser(admin, "admin");

        verify(preparedStatement).setString(1, admin.getPassword());
        verify(preparedStatement).setString(2, admin.getEmail());
        verify(preparedStatement).setString(3, admin.getUsername());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    public void testDeleteUser_Admin() throws SQLException {
        String query = "DELETE FROM admins WHERE username = ?";
        when(connection.prepareStatement(query)).thenReturn(preparedStatement);

        userDAO.deleteUser("admin1", "admin");

        verify(preparedStatement).setString(1, "admin1");
        verify(preparedStatement).executeUpdate();
    }
}
