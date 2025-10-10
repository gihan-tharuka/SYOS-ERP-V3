package Testing2.Testing.controller;

import controller.AuthenticationController;
import authentication.AuthenticationHandler;
import view.UserAuthenticationView;
import controller.AdminMenuController;
import controller.CashierMenuController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class AuthenticationControllerTest {
    private AuthenticationController controller;
    private UserAuthenticationView mockView;
    private AuthenticationHandler mockAuthHandler;
    private AdminMenuController mockAdminMenuController;
    private CashierMenuController mockCashierMenuController;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        mockView = mock(UserAuthenticationView.class);
        mockAuthHandler = mock(AuthenticationHandler.class);
        mockAdminMenuController = mock(AdminMenuController.class);
        mockCashierMenuController = mock(CashierMenuController.class);
        
        controller = new AuthenticationController(
            mockView, 
            mockAuthHandler, 
            mockAdminMenuController, 
            mockCashierMenuController
        );
    }

    @Test
    void testAuthenticateUser_AdminSuccess() {
        // Arrange
        String[] credentials = {"admin", "adminuser", "password123"};
        Scanner mockScanner = mock(Scanner.class);
        
        when(mockView.getUserCredentials()).thenReturn(credentials);
        when(mockView.getScanner()).thenReturn(mockScanner);
        when(mockAuthHandler.handleRequest("adminuser", "password123", "admin")).thenReturn("Success");
        
        // Act
        controller.authenticateUser();
        
        // Assert
        verify(mockView).getUserCredentials();
        verify(mockAuthHandler).handleRequest("adminuser", "password123", "admin");
        verify(mockAdminMenuController).handleMenu();
        verify(mockCashierMenuController, never()).handleMenu();
    }

    @Test
    void testAuthenticateUser_CashierSuccess() {
        // Arrange
        String[] credentials = {"cashier", "cashieruser", "password456"};
        Scanner mockScanner = mock(Scanner.class);
        
        when(mockView.getUserCredentials()).thenReturn(credentials);
        when(mockView.getScanner()).thenReturn(mockScanner);
        when(mockAuthHandler.handleRequest("cashieruser", "password456", "cashier")).thenReturn("Success");
        
        // Act
        controller.authenticateUser();
        
        // Assert
        verify(mockView).getUserCredentials();
        verify(mockAuthHandler).handleRequest("cashieruser", "password456", "cashier");
        verify(mockCashierMenuController).handleMenu();
        verify(mockAdminMenuController, never()).handleMenu();
    }

    @Test
    void testAuthenticateUser_OtherRoleSuccess() {
        // Arrange
        String[] credentials = {"supplier", "supplieruser", "password789"};
        Scanner mockScanner = mock(Scanner.class);
        
        when(mockView.getUserCredentials()).thenReturn(credentials);
        when(mockView.getScanner()).thenReturn(mockScanner);
        when(mockAuthHandler.handleRequest("supplieruser", "password789", "supplier")).thenReturn("Success");
        
        // Act
        controller.authenticateUser();
        
        // Assert
        verify(mockView).getUserCredentials();
        verify(mockAuthHandler).handleRequest("supplieruser", "password789", "supplier");
        verify(mockAdminMenuController, never()).handleMenu();
        verify(mockCashierMenuController, never()).handleMenu();
        assertTrue(outputStream.toString().contains("Welcome, supplier!"));
    }

    @Test
    void testAuthenticateUser_FailureThenRetry() {
        // Arrange
        String[] credentials1 = {"admin", "wronguser", "wrongpass"};
        String[] credentials2 = {"admin", "adminuser", "password123"};
        Scanner mockScanner = mock(Scanner.class);
        
        when(mockView.getUserCredentials())
            .thenReturn(credentials1)
            .thenReturn(credentials2);
        when(mockView.getScanner()).thenReturn(mockScanner);
        when(mockScanner.nextLine())
            .thenReturn("yes")  // retry
            .thenReturn("no");  // exit after success
        when(mockAuthHandler.handleRequest("wronguser", "wrongpass", "admin")).thenReturn("Invalid credentials");
        when(mockAuthHandler.handleRequest("adminuser", "password123", "admin")).thenReturn("Success");
        
        // Act
        controller.authenticateUser();
        
        // Assert
        verify(mockView, times(2)).getUserCredentials();
        verify(mockAuthHandler, times(2)).handleRequest(anyString(), anyString(), anyString());
        verify(mockAdminMenuController).handleMenu();
        assertTrue(outputStream.toString().contains("Authentication failed: Invalid credentials"));
    }

    @Test
    void testAuthenticateUser_FailureThenExit() {
        // Arrange
        String[] credentials = {"admin", "wronguser", "wrongpass"};
        Scanner mockScanner = mock(Scanner.class);
        
        when(mockView.getUserCredentials()).thenReturn(credentials);
        when(mockView.getScanner()).thenReturn(mockScanner);
        when(mockScanner.nextLine()).thenReturn("no");  // don't retry
        when(mockAuthHandler.handleRequest("wronguser", "wrongpass", "admin")).thenReturn("Invalid credentials");
        
        // Act
        controller.authenticateUser();
        
        // Assert
        verify(mockView).getUserCredentials();
        verify(mockAuthHandler).handleRequest("wronguser", "wrongpass", "admin");
        verify(mockAdminMenuController, never()).handleMenu();
        verify(mockCashierMenuController, never()).handleMenu();
        assertTrue(outputStream.toString().contains("Authentication failed: Invalid credentials"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 