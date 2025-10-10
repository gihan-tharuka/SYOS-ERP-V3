package Testing2.Testing.authentication;

import authentication.AdminAuthenticationHandler;
import authentication.AuthenticationHandler;
import authentication.CashierAuthenticationHandler;
import authentication.CustomerAuthenticationHandler;
import dao.UserDAO;
import model.Admin;
import model.Cashier;
import model.Customer;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationHandlerTest {
    
    private UserDAO userDAO;
    private AdminAuthenticationHandler adminHandler;
    private CashierAuthenticationHandler cashierHandler;
    private CustomerAuthenticationHandler customerHandler;
    private Admin testAdmin;
    private Cashier testCashier;
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        userDAO = mock(UserDAO.class);
        adminHandler = new AdminAuthenticationHandler(userDAO);
        cashierHandler = new CashierAuthenticationHandler(userDAO);
        customerHandler = new CustomerAuthenticationHandler(userDAO);
        
        // Create test users
        testAdmin = new Admin("admin1", "password123", "admin@test.com");
        testCashier = new Cashier("cashier1", "password123", "John Doe", "cashier@test.com", "1234567890");
        testCustomer = new Customer("customer1", "password123", "customer@test.com", "0987654321");
    }

    @Test
    public void testAdminAuthenticationHandlerSuccess() {
        when(userDAO.getUserByUsername("admin1", "admin")).thenReturn(testAdmin);
        
        String result = adminHandler.handleRequest("admin1", "password123", "admin");
        
        assertEquals("Success", result);
        verify(userDAO).getUserByUsername("admin1", "admin");
    }

    @Test
    public void testAdminAuthenticationHandlerInvalidUsername() {
        when(userDAO.getUserByUsername("invalid", "admin")).thenReturn(null);
        
        String result = adminHandler.handleRequest("invalid", "password123", "admin");
        
        assertEquals("Invalid username.", result);
        verify(userDAO).getUserByUsername("invalid", "admin");
    }

    @Test
    public void testAdminAuthenticationHandlerInvalidPassword() {
        when(userDAO.getUserByUsername("admin1", "admin")).thenReturn(testAdmin);
        
        String result = adminHandler.handleRequest("admin1", "wrongpassword", "admin");
        
        assertEquals("Invalid password.", result);
        verify(userDAO).getUserByUsername("admin1", "admin");
    }

    @Test
    public void testAdminAuthenticationHandlerWrongRole() {
        String result = adminHandler.handleRequest("admin1", "password123", "cashier");
        
        assertEquals("Invalid role.", result);
        verify(userDAO, never()).getUserByUsername(anyString(), anyString());
    }

    @Test
    public void testCashierAuthenticationHandlerSuccess() {
        when(userDAO.getUserByUsername("cashier1", "cashier")).thenReturn(testCashier);
        
        String result = cashierHandler.handleRequest("cashier1", "password123", "cashier");
        
        assertEquals("Success", result);
        verify(userDAO).getUserByUsername("cashier1", "cashier");
    }

    @Test
    public void testCashierAuthenticationHandlerInvalidUsername() {
        when(userDAO.getUserByUsername("invalid", "cashier")).thenReturn(null);
        
        String result = cashierHandler.handleRequest("invalid", "password123", "cashier");
        
        assertEquals("Invalid username.", result);
        verify(userDAO).getUserByUsername("invalid", "cashier");
    }

    @Test
    public void testCashierAuthenticationHandlerInvalidPassword() {
        when(userDAO.getUserByUsername("cashier1", "cashier")).thenReturn(testCashier);
        
        String result = cashierHandler.handleRequest("cashier1", "wrongpassword", "cashier");
        
        assertEquals("Invalid password.", result);
        verify(userDAO).getUserByUsername("cashier1", "cashier");
    }

    @Test
    public void testCustomerAuthenticationHandlerSuccess() {
        when(userDAO.getUserByUsername("customer1", "customer")).thenReturn(testCustomer);
        
        String result = customerHandler.handleRequest("customer1", "password123", "customer");
        
        assertEquals("Success", result);
        verify(userDAO).getUserByUsername("customer1", "customer");
    }

    @Test
    public void testCustomerAuthenticationHandlerInvalidCredentials() {
        when(userDAO.getUserByUsername("invalid", "customer")).thenReturn(null);
        
        String result = customerHandler.handleRequest("invalid", "wrongpassword", "customer");
        
        assertEquals("Invalid username or password", result);
        verify(userDAO).getUserByUsername("invalid", "customer");
    }

    @Test
    public void testCustomerAuthenticationHandlerWrongRole() {
        String result = customerHandler.handleRequest("customer1", "password123", "admin");
        
        assertEquals("Invalid role", result);
        verify(userDAO, never()).getUserByUsername(anyString(), anyString());
    }

    @Test
    public void testChainOfResponsibility() {
        // Set up the chain: admin -> cashier -> customer
        adminHandler.setNextHandler(cashierHandler);
        cashierHandler.setNextHandler(customerHandler);
        
        // Test admin role (should be handled by admin handler)
        when(userDAO.getUserByUsername("admin1", "admin")).thenReturn(testAdmin);
        String adminResult = adminHandler.handleRequest("admin1", "password123", "admin");
        assertEquals("Success", adminResult);
        
        // Test cashier role (should be handled by cashier handler)
        when(userDAO.getUserByUsername("cashier1", "cashier")).thenReturn(testCashier);
        String cashierResult = adminHandler.handleRequest("cashier1", "password123", "cashier");
        assertEquals("Success", cashierResult);
        
        // Test customer role (should be handled by customer handler)
        when(userDAO.getUserByUsername("customer1", "customer")).thenReturn(testCustomer);
        String customerResult = adminHandler.handleRequest("customer1", "password123", "customer");
        assertEquals("Success", customerResult);
    }

    @Test
    public void testChainOfResponsibilityWithInvalidRole() {
        // Set up the chain: admin -> cashier -> customer
        adminHandler.setNextHandler(cashierHandler);
        cashierHandler.setNextHandler(customerHandler);
        
        // Test invalid role (should return "Invalid role" from customer handler)
        String result = adminHandler.handleRequest("user1", "password123", "invalid_role");
        assertEquals("Invalid role", result);
    }

    @Test
    public void testSetNextHandler() {
        AuthenticationHandler nextHandler = mock(AuthenticationHandler.class);
        adminHandler.setNextHandler(nextHandler);
        
        // Verify that setNextHandler doesn't throw any exception
        assertDoesNotThrow(() -> adminHandler.setNextHandler(nextHandler));
    }

    @Test
    public void testCaseInsensitiveRoleHandling() {
        when(userDAO.getUserByUsername("admin1", "ADMIN")).thenReturn(testAdmin);
        when(userDAO.getUserByUsername("cashier1", "CASHIER")).thenReturn(testCashier);
        when(userDAO.getUserByUsername("customer1", "CUSTOMER")).thenReturn(testCustomer);
        
        // Test uppercase roles
        String adminResult = adminHandler.handleRequest("admin1", "password123", "ADMIN");
        String cashierResult = cashierHandler.handleRequest("cashier1", "password123", "CASHIER");
        String customerResult = customerHandler.handleRequest("customer1", "password123", "CUSTOMER");
        
        assertEquals("Success", adminResult);
        assertEquals("Success", cashierResult);
        assertEquals("Success", customerResult);
    }

    @Test
    public void testHandlerInheritance() {
        // Test that all handlers extend AuthenticationHandler
        assertTrue(adminHandler instanceof AuthenticationHandler);
        assertTrue(cashierHandler instanceof AuthenticationHandler);
        assertTrue(customerHandler instanceof AuthenticationHandler);
    }

    @Test
    public void testConstructorWithUserDAO() {
        // Test that constructors work properly
        assertNotNull(adminHandler);
        assertNotNull(cashierHandler);
        assertNotNull(customerHandler);
    }
} 