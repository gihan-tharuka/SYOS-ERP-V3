package Testing2.Testing.view;

import view.ShelfStockManagementView;
import model.ShelfStock;
import model.BatchSelection;
import model.MainStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class ShelfStockManagementViewTest {

    private ShelfStockManagementView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Initialize view with a default input stream
        System.setIn(new ByteArrayInputStream("".getBytes()));
        view = new ShelfStockManagementView();
    }

    @Test
    void testConstructor_ShouldCreateViewWithScanner() {
        // Act
        ShelfStockManagementView newView = new ShelfStockManagementView();
        
        // Assert
        assertNotNull(newView);
    }

    @Test
    void testDisplayShelfStockManagementMenu_ShouldDisplayCorrectMenu() {
        // Act
        view.displayShelfStockManagementMenu();
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Shelf Stock Management Menu:"));
        assertTrue(output.contains("1. Add New Shelf Stock"));
        assertTrue(output.contains("2. View All Shelf Stocks"));
        assertTrue(output.contains("3. Reshelf Stock"));
        assertTrue(output.contains("4. Edit Shelf Stock"));
        assertTrue(output.contains("5. Delete Shelf Stock"));
        assertTrue(output.contains("5. Back to Admin Menu"));
        assertTrue(output.contains("Enter your choice:"));
    }

    @Test
    void testGetUserChoice_ShouldReturnIntegerInput() {
        // Arrange
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        int result = view.getUserChoice();
        
        // Assert
        assertEquals(3, result);
    }

    @Test
    void testClearInput_ShouldConsumeNewline() {
        // Arrange
        String input = "test\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        view.clearInput();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testGetItemCodeForNewShelfStock_ShouldReturnUserInput() {
        // Arrange
        String expectedInput = "ITEM001";
        System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        String result = view.getItemCodeForNewShelfStock();
        
        // Assert
        assertEquals(expectedInput, result);
        assertTrue(outputStream.toString().contains("Enter item code:"));
    }

    @Test
    void testGetNewShelfStockDetails_ShouldReturnShelfStock() {
        // Arrange
        String input = "100\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        int itemId = 1;
        
        // Act
        ShelfStock result = view.getNewShelfStockDetails(itemId);
        
        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.getItemId());
        assertEquals(100, result.getShelfCapacity());
        assertEquals(0, result.getCurrentQuantity());
        assertTrue(outputStream.toString().contains("Enter shelf capacity:"));
    }

    @Test
    void testDisplayReshelvingInfo_WithValidData_ShouldDisplayInfo() {
        // Arrange
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        
        // Create mock data
        MainStock mockMainStock = mock(MainStock.class);
        when(mockMainStock.getBatchCode()).thenReturn("BATCH001");
        when(mockMainStock.getExpiryDate()).thenReturn(new Date());
        
        BatchSelection mockSelection = mock(BatchSelection.class);
        when(mockSelection.getBatch()).thenReturn(mockMainStock);
        when(mockSelection.getReshelfQuantity()).thenReturn(50);
        
        List<BatchSelection> selections = Arrays.asList(mockSelection);
        reshelvingInfo.put(1, selections);
        reshelfQuantities.put(1, 100);
        itemNames.put(1, "Test Item");
        
        // Act
        view.displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Reshelving Info:"));
        assertTrue(output.contains("Item ID: 1"));
        assertTrue(output.contains("Test Item"));
        assertTrue(output.contains("Reshelf Quantity: 100"));
        assertTrue(output.contains("BATCH001"));
        assertTrue(output.contains("Reshelf Quantity: 50"));
        
        verify(mockMainStock).getBatchCode();
        verify(mockMainStock).getExpiryDate();
        verify(mockSelection, times(2)).getBatch();
        verify(mockSelection).getReshelfQuantity();
    }

    @Test
    void testDisplayReshelvingInfo_WithEmptyData_ShouldDisplayEmptyInfo() {
        // Arrange
        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
        Map<Integer, String> itemNames = new HashMap<>();
        
        // Act
        view.displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Reshelving Info:"));
    }

    @Test
    void testConfirmReshelfAll_WithYes_ShouldReturnTrue() {
        // Arrange
        String input = "yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        boolean result = view.confirmReshelfAll();
        
        // Assert
        assertTrue(result);
        assertTrue(outputStream.toString().contains("Do you want to proceed with reshelfing for all items?"));
    }

    @Test
    void testConfirmReshelfAll_WithNo_ShouldReturnFalse() {
        // Arrange
        String input = "no\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        boolean result = view.confirmReshelfAll();
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testConfirmReshelfAll_WithYesUpperCase_ShouldReturnTrue() {
        // Arrange
        String input = "YES\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        boolean result = view.confirmReshelfAll();
        
        // Assert
        assertTrue(result);
    }

    @Test
    void testShowAddShelfStockSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showAddShelfStockSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Shelf stock added successfully!"));
    }

    @Test
    void testShowAddShelfStockFailure_ShouldDisplayFailureMessage() {
        // Act
        view.showAddShelfStockFailure();
        
        // Assert
        assertTrue(outputStream.toString().contains("Failed to add shelf stock."));
    }

    @Test
    void testShowReshelfSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showReshelfSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Shelf stock reshelved successfully!"));
    }

    @Test
    void testDisplayAllShelfStocks_WithValidData_ShouldDisplayTable() {
        // Arrange
        Map<ShelfStock, String> shelfStocksWithItemCodes = new HashMap<>();
        
        ShelfStock mockShelfStock = mock(ShelfStock.class);
        when(mockShelfStock.getStockId()).thenReturn(1);
        when(mockShelfStock.getItemId()).thenReturn(101);
        when(mockShelfStock.getShelfCapacity()).thenReturn(100);
        when(mockShelfStock.getCurrentQuantity()).thenReturn(50);
        
        shelfStocksWithItemCodes.put(mockShelfStock, "ITEM001");
        
        // Act
        view.displayAllShelfStocks(shelfStocksWithItemCodes);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("All Shelf Stocks:"));
        assertTrue(output.contains("Shelf ID"));
        assertTrue(output.contains("Item Code"));
        assertTrue(output.contains("Shelf Capacity"));
        assertTrue(output.contains("Current Quantity"));
        assertTrue(output.contains("ITEM001"));
        
        verify(mockShelfStock).getStockId();
        verify(mockShelfStock).getShelfCapacity();
        verify(mockShelfStock).getCurrentQuantity();
    }

    @Test
    void testDisplayAllShelfStocks_WithEmptyData_ShouldDisplayEmptyTable() {
        // Arrange
        Map<ShelfStock, String> shelfStocksWithItemCodes = new HashMap<>();
        
        // Act
        view.displayAllShelfStocks(shelfStocksWithItemCodes);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("All Shelf Stocks:"));
        assertTrue(output.contains("Shelf ID"));
        assertTrue(output.contains("Item Code"));
        assertTrue(output.contains("Shelf Capacity"));
        assertTrue(output.contains("Current Quantity"));
    }

    @Test
    void testGetItemIdForDelete_ShouldReturnUserInput() {
        // Arrange
        String input = "5\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        int result = view.getItemIdForDelete();
        
        // Assert
        assertEquals(5, result);
        assertTrue(outputStream.toString().contains("Enter item ID for deletion:"));
    }

    @Test
    void testConfirmDelete_WithYes_ShouldReturnTrue() {
        // Arrange
        String input = "yes\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        int itemId = 5;
        
        // Act
        boolean result = view.confirmDelete(itemId);
        
        // Assert
        assertTrue(result);
        assertTrue(outputStream.toString().contains("Are you sure you want to delete the shelf stock for item ID: 5?"));
    }

    @Test
    void testConfirmDelete_WithNo_ShouldReturnFalse() {
        // Arrange
        String input = "no\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        int itemId = 5;
        
        // Act
        boolean result = view.confirmDelete(itemId);
        
        // Assert
        assertFalse(result);
    }

    @Test
    void testGetItemIdForEdit_ShouldReturnUserInput() {
        // Arrange
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        int result = view.getItemIdForEdit();
        
        // Assert
        assertEquals(3, result);
        assertTrue(outputStream.toString().contains("Enter item ID for editing:"));
    }

    @Test
    void testGetUpdatedShelfStockDetails_ShouldReturnUpdatedShelfStock() {
        // Arrange
        String input = "150\n75\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        int itemId = 2;
        
        // Act
        ShelfStock result = view.getUpdatedShelfStockDetails(itemId);
        
        // Assert
        assertNotNull(result);
        assertEquals(itemId, result.getItemId());
        assertEquals(150, result.getShelfCapacity());
        assertEquals(75, result.getCurrentQuantity());
        assertTrue(outputStream.toString().contains("Enter new shelf capacity:"));
        assertTrue(outputStream.toString().contains("Enter new current quantity:"));
    }

    @Test
    void testShowDeleteSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showDeleteSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Shelf stock deleted successfully!"));
    }

    @Test
    void testShowEditSuccess_ShouldDisplaySuccessMessage() {
        // Act
        view.showEditSuccess();
        
        // Assert
        assertTrue(outputStream.toString().contains("Shelf stock updated successfully!"));
    }

    @Test
    void testMultipleOperations_ShouldWorkCorrectly() {
        // Arrange
        String input = "ITEM001\n100\n3\n5\nyes\n2\n150\n75\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ShelfStockManagementView();
        
        // Act
        String itemCode = view.getItemCodeForNewShelfStock();
        ShelfStock newStock = view.getNewShelfStockDetails(1);
        int userChoice = view.getUserChoice();
        int deleteId = view.getItemIdForDelete();
        boolean confirmDelete = view.confirmDelete(deleteId);
        int editId = view.getItemIdForEdit();
        ShelfStock updatedStock = view.getUpdatedShelfStockDetails(editId);
        
        // Assert
        assertEquals("ITEM001", itemCode);
        assertNotNull(newStock);
        assertEquals(1, newStock.getItemId());
        assertEquals(100, newStock.getShelfCapacity());
        assertEquals(3, userChoice);
        assertEquals(5, deleteId);
        assertTrue(confirmDelete);
        assertEquals(2, editId);
        assertNotNull(updatedStock);
        assertEquals(2, updatedStock.getItemId());
        assertEquals(150, updatedStock.getShelfCapacity());
        assertEquals(75, updatedStock.getCurrentQuantity());
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 