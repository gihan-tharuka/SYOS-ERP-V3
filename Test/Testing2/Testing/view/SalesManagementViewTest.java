package Testing2.Testing.view;

import view.SalesManagementView;
import model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class SalesManagementViewTest {

    private SalesManagementView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Initialize view with a default input stream
        System.setIn(new ByteArrayInputStream("".getBytes()));
        view = new SalesManagementView();
    }

    @Test
    void testConstructor_ShouldCreateViewWithScanner() {
        // Act
        SalesManagementView newView = new SalesManagementView();
        
        // Assert
        assertNotNull(newView);
    }

    @Test
    void testDisplaySalesManagementMenu_ShouldDisplayCorrectMenu() {
        // Act
        view.displaySalesManagementMenu();
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Sales Management Menu:"));
        assertTrue(output.contains("1. View All Sales"));
        assertTrue(output.contains("2. Create New Sale"));
        assertTrue(output.contains("3. View Bill"));
        assertTrue(output.contains("Enter your choice:"));
    }

    @Test
    void testGetUserChoice_ShouldReturnIntegerInput() {
        // Arrange
        String input = "2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        int result = view.getUserChoice();
        
        // Assert
        assertEquals(2, result);
    }

    @Test
    void testClearInput_ShouldConsumeNewline() {
        // Arrange
        String input = "test\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        view.clearInput();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testGetNewSaleDetails_WithValidInput_ShouldReturnSale() {
        // Arrange
        String input = "\nover-the-counter\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        Sale result = view.getNewSaleDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("over-the-counter", result.getTransactionType());
        assertNotNull(result.getSaleDate());
        assertTrue(outputStream.toString().contains("Enter transaction type"));
    }

    @Test
    void testGetNewSaleDetails_WithOnlineTransaction_ShouldReturnSale() {
        // Arrange
        String input = "\nonline\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        Sale result = view.getNewSaleDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("online", result.getTransactionType());
        assertNotNull(result.getSaleDate());
    }

    @Test
    void testGetNewSaleDetails_WithComplexTransactionType_ShouldReturnSale() {
        // Arrange
        String input = "\nover-the-counter with delivery\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        Sale result = view.getNewSaleDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("over-the-counter with delivery", result.getTransactionType());
        assertNotNull(result.getSaleDate());
    }

    @Test
    void testDisplayAllSales_WithValidData_ShouldDisplaySales() {
        // Arrange
        Sale mockSale1 = mock(Sale.class);
        Sale mockSale2 = mock(Sale.class);
        
        when(mockSale1.getSaleId()).thenReturn(1);
        when(mockSale1.getSaleDate()).thenReturn(new java.util.Date());
        when(mockSale1.getTransactionType()).thenReturn("over-the-counter");
        
        when(mockSale2.getSaleId()).thenReturn(2);
        when(mockSale2.getSaleDate()).thenReturn(new java.util.Date());
        when(mockSale2.getTransactionType()).thenReturn("online");
        
        List<Sale> sales = Arrays.asList(mockSale1, mockSale2);
        
        // Act
        view.displayAllSales(sales);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("All Sales:"));
        assertTrue(output.contains("Sale ID: 1"));
        assertTrue(output.contains("Sale ID: 2"));
        assertTrue(output.contains("Sale Date:"));
        assertTrue(output.contains("Transaction Type: over-the-counter"));
        assertTrue(output.contains("Transaction Type: online"));
        
        verify(mockSale1).getSaleId();
        verify(mockSale1).getSaleDate();
        verify(mockSale1).getTransactionType();
        verify(mockSale2).getSaleId();
        verify(mockSale2).getSaleDate();
        verify(mockSale2).getTransactionType();
    }

    @Test
    void testDisplayAllSales_WithEmptyList_ShouldDisplayEmptyMessage() {
        // Arrange
        List<Sale> sales = Arrays.asList();
        
        // Act
        view.displayAllSales(sales);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("All Sales:"));
    }

    @Test
    void testDisplayAllSales_WithSingleSale_ShouldDisplaySale() {
        // Arrange
        Sale mockSale = mock(Sale.class);
        when(mockSale.getSaleId()).thenReturn(1);
        when(mockSale.getSaleDate()).thenReturn(new java.util.Date());
        when(mockSale.getTransactionType()).thenReturn("over-the-counter");
        
        List<Sale> sales = Arrays.asList(mockSale);
        
        // Act
        view.displayAllSales(sales);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("All Sales:"));
        assertTrue(output.contains("Sale ID: 1"));
        assertTrue(output.contains("Transaction Type: over-the-counter"));
        
        verify(mockSale).getSaleId();
        verify(mockSale).getSaleDate();
        verify(mockSale).getTransactionType();
    }

    @Test
    void testShowSaleCreatedSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showSaleCreatedSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Sale created successfully!"));
    }

    @Test
    void testShowBillCreatedSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showBillCreatedSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Bill created successfully!"));
    }

    @Test
    void testShowChangeAmount_WithPositiveAmount_ShouldDisplayChange() {
        // Arrange
        double changeAmount = 15.75;
        
        // Act
        view.showChangeAmount(changeAmount);
        
        // Assert
        assertTrue(outputStream.toString().contains("Change Amount: 15.75"));
    }

    @Test
    void testShowChangeAmount_WithZeroAmount_ShouldDisplayZero() {
        // Arrange
        double changeAmount = 0.0;
        
        // Act
        view.showChangeAmount(changeAmount);
        
        // Assert
        assertTrue(outputStream.toString().contains("Change Amount: 0.0"));
    }

    @Test
    void testShowChangeAmount_WithLargeAmount_ShouldDisplayLargeAmount() {
        // Arrange
        double changeAmount = 999.99;
        
        // Act
        view.showChangeAmount(changeAmount);
        
        // Assert
        assertTrue(outputStream.toString().contains("Change Amount: 999.99"));
    }

//    @Test
//    void testMultipleOperations_ShouldWorkCorrectly() {
//        // Arrange
//        String input = "2\n\nover-the-counter\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        view = new SalesManagementView();
//
//        // Act
//        int userChoice = view.getUserChoice();
//        Sale newSale = view.getNewSaleDetails();
//
//        // Assert
//        assertEquals(2, userChoice);
//        assertNotNull(newSale);
//        assertEquals("over-the-counter", newSale.getTransactionType());
//        assertNotNull(newSale.getSaleDate());
//    }

    @Test
    void testGetUserChoice_WithLargeNumber_ShouldReturnIntegerInput() {
        // Arrange
        String input = "999\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        int result = view.getUserChoice();
        
        // Assert
        assertEquals(999, result);
    }

    @Test
    void testGetNewSaleDetails_WithEmptyTransactionType_ShouldReturnSale() {
        // Arrange
        String input = "\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new SalesManagementView();
        
        // Act
        Sale result = view.getNewSaleDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("", result.getTransactionType());
        assertNotNull(result.getSaleDate());
    }

    @Test
    void testDisplayAllSales_WithNullSaleDate_ShouldHandleGracefully() {
        // Arrange
        Sale mockSale = mock(Sale.class);
        when(mockSale.getSaleId()).thenReturn(1);
        when(mockSale.getSaleDate()).thenReturn(null);
        when(mockSale.getTransactionType()).thenReturn("over-the-counter");
        
        List<Sale> sales = Arrays.asList(mockSale);
        
        // Act
        view.displayAllSales(sales);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("All Sales:"));
        assertTrue(output.contains("Sale ID: 1"));
        assertTrue(output.contains("Sale Date: null"));
        assertTrue(output.contains("Transaction Type: over-the-counter"));
        
        verify(mockSale).getSaleId();
        verify(mockSale).getSaleDate();
        verify(mockSale).getTransactionType();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 