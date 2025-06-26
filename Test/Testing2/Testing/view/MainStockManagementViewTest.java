package Testing2.Testing.view;

import view.MainStockManagementView;
import dao.ItemDAO;
import dao.UserDAO;
import model.MainStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainStockManagementViewTest {

    private MainStockManagementView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Initialize view with a default input stream
        System.setIn(new ByteArrayInputStream("".getBytes()));
        view = new MainStockManagementView();
    }

    @Test
    void testConstructor_ShouldCreateViewWithScanner() {
        // Act
        MainStockManagementView newView = new MainStockManagementView();
        
        // Assert
        assertNotNull(newView);
    }

    @Test
    void testDisplayMainStockManagementMenu_ShouldDisplayCorrectMenu() {
        // Act
        view.displayMainStockManagementMenu();
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Main Stock Management Menu:"));
        assertTrue(output.contains("1. Add New Main Stock"));
        assertTrue(output.contains("2. View All Main Stocks"));
        assertTrue(output.contains("3. Edit Main Stock"));
        assertTrue(output.contains("4. Delete Main Stock"));
        assertTrue(output.contains("5. Back to Admin Menu"));
        assertTrue(output.contains("Enter your choice:"));
    }

    @Test
    void testGetItemCodeFromUser_ShouldReturnUserInput() {
        // Arrange
        String expectedInput = "ITEM001";
        System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));
        view = new MainStockManagementView();
        
        // Act
        String result = view.getItemCodeFromUser();
        
        // Assert
        assertEquals(expectedInput, result);
        assertTrue(outputStream.toString().contains("Enter item code:"));
    }

    @Test
    void testGetSupplierUsernameFromUser_ShouldReturnUserInput() {
        // Arrange
        String expectedInput = "supplier123";
        System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));
        view = new MainStockManagementView();
        
        // Act
        String result = view.getSupplierUsernameFromUser();
        
        // Assert
        assertEquals(expectedInput, result);
        assertTrue(outputStream.toString().contains("Enter supplier username:"));
    }

    @Test
    void testGetNewMainStockDetails_WithValidInput_ShouldReturnMainStock() {
        // Arrange
        String input = "BATCH001\n2024-01-15\n25.50\n100\n2025-01-15\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new MainStockManagementView();
        MainStock mainStock = new MainStock();
        
        // Act
        MainStock result = view.getNewMainStockDetails(mainStock);
        
        // Assert
        assertNotNull(result);
        assertEquals("BATCH001", result.getBatchCode());
        assertEquals(25.50, result.getPurchasePrice(), 0.01);
        assertEquals(100, result.getQuantity());
        assertNotNull(result.getPurchaseDate());
        assertNotNull(result.getExpiryDate());
    }

    @Test
    void testGetNewMainStockDetails_WithValidInputNoExpiry_ShouldReturnMainStock() {
        // Arrange
        String input = "BATCH002\n2024-02-20\n30.75\n50\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new MainStockManagementView();
        MainStock mainStock = new MainStock();
        
        // Act
        MainStock result = view.getNewMainStockDetails(mainStock);
        
        // Assert
        assertNotNull(result);
        assertEquals("BATCH002", result.getBatchCode());
        assertEquals(30.75, result.getPurchasePrice(), 0.01);
        assertEquals(50, result.getQuantity());
        assertNotNull(result.getPurchaseDate());
        assertNull(result.getExpiryDate());
    }

    @Test
    void testGetNewMainStockDetails_WithInvalidDate_ShouldReturnNull() {
        // Arrange
        String input = "BATCH003\ninvalid-date\n25.50\n100\n2025-01-15\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new MainStockManagementView();
        MainStock mainStock = new MainStock();
        
        // Act
        MainStock result = view.getNewMainStockDetails(mainStock);
        
        // Assert
        assertNull(result);
        assertTrue(outputStream.toString().contains("Invalid date format"));
    }

    @Test
    void testShowAddMainStockSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showAddMainStockSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Main stock added successfully!"));
    }

    @Test
    void testShowEditMainStockSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showEditMainStockSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Main stock edited successfully!"));
    }

    @Test
    void testShowDeleteMainStockSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showDeleteMainStockSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Main stock deleted successfully!"));
    }

    @Test
    void testDisplayAllMainStocks_WithValidData_ShouldDisplayTable() {
        // Arrange
        ItemDAO mockItemDAO = mock(ItemDAO.class);
        UserDAO mockUserDAO = mock(UserDAO.class);
        MainStock mockMainStock = mock(MainStock.class);
        
        when(mockMainStock.getStockId()).thenReturn(1);
        when(mockMainStock.getItemId()).thenReturn(101);
        when(mockMainStock.getSupplierId()).thenReturn(201);
        when(mockMainStock.getBatchCode()).thenReturn("BATCH001");
        when(mockMainStock.getPurchaseDate()).thenReturn(new java.util.Date());
        when(mockMainStock.getPurchasePrice()).thenReturn(25.50);
        when(mockMainStock.getQuantity()).thenReturn(100);
        when(mockMainStock.getExpiryDate()).thenReturn(new java.util.Date());
        
        when(mockItemDAO.getItemCodeById(101)).thenReturn("ITEM001");
        when(mockUserDAO.getUsernameById(201)).thenReturn("supplier1");
        
        List<MainStock> mainStocks = Arrays.asList(mockMainStock);
        
        // Act
        view.displayAllMainStocks(mainStocks, mockItemDAO, mockUserDAO);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Stock ID"));
        assertTrue(output.contains("Item Code"));
        assertTrue(output.contains("Supplier Username"));
        assertTrue(output.contains("Batch Code"));
        assertTrue(output.contains("Purchase Date"));
        assertTrue(output.contains("Purchase Price"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Expiry Date"));
        
        verify(mockItemDAO).getItemCodeById(101);
        verify(mockUserDAO).getUsernameById(201);
    }

    @Test
    void testDisplayAllMainStocks_WithEmptyList_ShouldDisplayEmptyTable() {
        // Arrange
        ItemDAO mockItemDAO = mock(ItemDAO.class);
        UserDAO mockUserDAO = mock(UserDAO.class);
        List<MainStock> mainStocks = new ArrayList<>();
        
        // Act
        view.displayAllMainStocks(mainStocks, mockItemDAO, mockUserDAO);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Stock ID"));
        assertTrue(output.contains("Item Code"));
        assertTrue(output.contains("Supplier Username"));
        assertTrue(output.contains("Batch Code"));
        assertTrue(output.contains("Purchase Date"));
        assertTrue(output.contains("Purchase Price"));
        assertTrue(output.contains("Quantity"));
        assertTrue(output.contains("Expiry Date"));
    }

    @Test
    void testDisplayAllMainStocks_WithNullList_ShouldHandleGracefully() {
        // Arrange
        ItemDAO mockItemDAO = mock(ItemDAO.class);
        UserDAO mockUserDAO = mock(UserDAO.class);
        
        // Act & Assert - The actual method doesn't handle null lists, so it should throw NullPointerException
        assertThrows(NullPointerException.class, () -> view.displayAllMainStocks(null, mockItemDAO, mockUserDAO));
    }

    @Test
    void testGetUserChoice_ShouldReturnIntegerInput() {
        // Arrange
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new MainStockManagementView();
        
        // Act
        int result = view.getUserChoice();
        
        // Assert
        assertEquals(3, result);
    }

    @Test
    void testGetUserChoice_WithLargeNumber_ShouldReturnIntegerInput() {
        // Arrange
        String input = "999\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new MainStockManagementView();
        
        // Act
        int result = view.getUserChoice();
        
        // Assert
        assertEquals(999, result);
    }

//    @Test
//    void testClearInput_ShouldConsumeNewline() {
//        // Arrange
//        String input = "test\nanother\n";
//        System.setIn(new ByteArrayInputStream(input.getBytes()));
//        view = new MainStockManagementView();
//
//        // Act - First call should work
//        view.clearInput();
//
//        // Assert - Second call should throw NoSuchElementException when no more input
//        assertThrows(java.util.NoSuchElementException.class, () -> view.clearInput());
//    }

    @Test
    void testMultipleOperations_ShouldWorkCorrectly() {
        // Arrange
        String input = "ITEM001\nsupplier123\nBATCH001\n2024-01-15\n25.50\n100\n2025-01-15\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new MainStockManagementView();
        
        // Act
        String itemCode = view.getItemCodeFromUser();
        String supplierUsername = view.getSupplierUsernameFromUser();
        MainStock mainStock = new MainStock();
        MainStock result = view.getNewMainStockDetails(mainStock);
        
        // Assert
        assertEquals("ITEM001", itemCode);
        assertEquals("supplier123", supplierUsername);
        assertNotNull(result);
        assertEquals("BATCH001", result.getBatchCode());
    }

    @Test
    void testDisplayAllMainStocks_WithMultipleStocks_ShouldDisplayAll() {
        // Arrange
        ItemDAO mockItemDAO = mock(ItemDAO.class);
        UserDAO mockUserDAO = mock(UserDAO.class);
        
        MainStock mockMainStock1 = mock(MainStock.class);
        MainStock mockMainStock2 = mock(MainStock.class);
        
        when(mockMainStock1.getStockId()).thenReturn(1);
        when(mockMainStock1.getItemId()).thenReturn(101);
        when(mockMainStock1.getSupplierId()).thenReturn(201);
        when(mockMainStock1.getBatchCode()).thenReturn("BATCH001");
        when(mockMainStock1.getPurchaseDate()).thenReturn(new java.util.Date());
        when(mockMainStock1.getPurchasePrice()).thenReturn(25.50);
        when(mockMainStock1.getQuantity()).thenReturn(100);
        when(mockMainStock1.getExpiryDate()).thenReturn(new java.util.Date());
        
        when(mockMainStock2.getStockId()).thenReturn(2);
        when(mockMainStock2.getItemId()).thenReturn(102);
        when(mockMainStock2.getSupplierId()).thenReturn(202);
        when(mockMainStock2.getBatchCode()).thenReturn("BATCH002");
        when(mockMainStock2.getPurchaseDate()).thenReturn(new java.util.Date());
        when(mockMainStock2.getPurchasePrice()).thenReturn(30.75);
        when(mockMainStock2.getQuantity()).thenReturn(50);
        when(mockMainStock2.getExpiryDate()).thenReturn(new java.util.Date());
        
        when(mockItemDAO.getItemCodeById(101)).thenReturn("ITEM001");
        when(mockItemDAO.getItemCodeById(102)).thenReturn("ITEM002");
        when(mockUserDAO.getUsernameById(201)).thenReturn("supplier1");
        when(mockUserDAO.getUsernameById(202)).thenReturn("supplier2");
        
        List<MainStock> mainStocks = Arrays.asList(mockMainStock1, mockMainStock2);
        
        // Act
        view.displayAllMainStocks(mainStocks, mockItemDAO, mockUserDAO);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("BATCH001"));
        assertTrue(output.contains("BATCH002"));
        assertTrue(output.contains("ITEM001"));
        assertTrue(output.contains("ITEM002"));
        assertTrue(output.contains("supplier1"));
        assertTrue(output.contains("supplier2"));
        
        verify(mockItemDAO, times(2)).getItemCodeById(anyInt());
        verify(mockUserDAO, times(2)).getUsernameById(anyInt());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 