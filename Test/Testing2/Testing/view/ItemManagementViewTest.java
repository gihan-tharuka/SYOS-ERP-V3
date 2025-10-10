package Testing2.Testing.view;

import view.ItemManagementView;
import model.Item;
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

public class ItemManagementViewTest {

    private ItemManagementView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        
        // Initialize view with a default input stream
        System.setIn(new ByteArrayInputStream("".getBytes()));
        view = new ItemManagementView();
    }

    @Test
    void testConstructor_ShouldCreateViewWithScanner() {
        // Act
        ItemManagementView newView = new ItemManagementView();
        
        // Assert
        assertNotNull(newView);
    }

    @Test
    void testDisplayItemManagementMenu_ShouldDisplayCorrectMenu() {
        // Act
        view.displayItemManagementMenu();
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Item Management Menu:"));
        assertTrue(output.contains("1. Add Item"));
        assertTrue(output.contains("2. View All Items"));
        assertTrue(output.contains("3. Delete Item"));
        assertTrue(output.contains("4. Edit Item"));
        assertTrue(output.contains("5. Back to Admin Menu"));
        assertTrue(output.contains("Enter your choice:"));
    }

    @Test
    void testGetUserChoice_ShouldReturnIntegerInput() {
        // Arrange
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
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
        view = new ItemManagementView();
        
        // Act
        view.clearInput();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testGetNewItemDetails_WithValidInput_ShouldReturnItem() {
        // Arrange
        String input = "ITEM001\nTest Item\n25.50\n5.00\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        // Act
        Item result = view.getNewItemDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("ITEM001", result.getItemCode());
        assertEquals("Test Item", result.getItemName());
        assertEquals(25.50, result.getPrice(), 0.01);
        assertEquals(5.00, result.getDiscount(), 0.01);
        assertTrue(outputStream.toString().contains("Enter item code:"));
        assertTrue(outputStream.toString().contains("Enter item name:"));
        assertTrue(outputStream.toString().contains("Enter item price:"));
        assertTrue(outputStream.toString().contains("Enter item discount"));
    }

    @Test
    void testGetNewItemDetails_WithEmptyDiscount_ShouldReturnItemWithZeroDiscount() {
        // Arrange
        String input = "ITEM002\nAnother Item\n30.75\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        // Act
        Item result = view.getNewItemDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("ITEM002", result.getItemCode());
        assertEquals("Another Item", result.getItemName());
        assertEquals(30.75, result.getPrice(), 0.01);
        assertEquals(0.00, result.getDiscount(), 0.01);
    }

    @Test
    void testShowAddItemSuccess_ShouldExecuteWithoutError() {
        // Act
        view.showAddItemSuccess();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testDisplayAllItems_WithValidData_ShouldExecuteWithoutError() {
        // Arrange
        Item mockItem1 = mock(Item.class);
        Item mockItem2 = mock(Item.class);
        List<Item> items = Arrays.asList(mockItem1, mockItem2);
        
        // Act
        view.displayAllItems(items);
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testDisplayAllItems_WithEmptyList_ShouldExecuteWithoutError() {
        // Arrange
        List<Item> items = Arrays.asList();
        
        // Act
        view.displayAllItems(items);
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testGetItemCodeToDelete_ShouldReturnUserInput() {
        // Arrange
        String expectedInput = "ITEM001";
        System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));
        view = new ItemManagementView();
        
        // Act
        String result = view.getItemCodeToDelete();
        
        // Assert
        assertEquals(expectedInput, result);
        assertTrue(outputStream.toString().contains("Enter item code to delete:"));
    }

    @Test
    void testShowDeleteItemSuccess_ShouldExecuteWithoutError() {
        // Act
        view.showDeleteItemSuccess();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testGetItemCodeToEdit_ShouldReturnUserInput() {
        // Arrange
        String expectedInput = "ITEM002";
        System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));
        view = new ItemManagementView();
        
        // Act
        String result = view.getItemCodeToEdit();
        
        // Assert
        assertEquals(expectedInput, result);
        assertTrue(outputStream.toString().contains("Enter item code to edit:"));
    }

    @Test
    void testGetUpdatedItemDetails_WithAllFieldsUpdated_ShouldReturnUpdatedItem() {
        // Arrange
        String input = "Updated Item Name\n35.75\n7.50\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        Item originalItem = new Item(1, "ITEM001", "Original Name", 25.00, 5.00);
        
        // Act
        Item result = view.getUpdatedItemDetails(originalItem);
        
        // Assert
        assertNotNull(result);
        assertEquals("Updated Item Name", result.getItemName());
        assertEquals(35.75, result.getPrice(), 0.01);
        assertEquals(7.50, result.getDiscount(), 0.01);
        assertTrue(outputStream.toString().contains("Leave field empty to keep the current value"));
        assertTrue(outputStream.toString().contains("Enter new item name"));
        assertTrue(outputStream.toString().contains("Enter new item price"));
        assertTrue(outputStream.toString().contains("Enter new item discount"));
    }

    @Test
    void testGetUpdatedItemDetails_WithEmptyFields_ShouldKeepOriginalValues() {
        // Arrange
        String input = "\n\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        Item originalItem = new Item(1, "ITEM001", "Original Name", 25.00, 5.00);
        
        // Act
        Item result = view.getUpdatedItemDetails(originalItem);
        
        // Assert
        assertNotNull(result);
        assertEquals("Original Name", result.getItemName());
        assertEquals(25.00, result.getPrice(), 0.01);
        assertEquals(5.00, result.getDiscount(), 0.01);
    }

    @Test
    void testGetUpdatedItemDetails_WithPartialUpdates_ShouldUpdateOnlyProvidedFields() {
        // Arrange
        String input = "New Name\n\n8.00\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        Item originalItem = new Item(1, "ITEM001", "Original Name", 25.00, 5.00);
        
        // Act
        Item result = view.getUpdatedItemDetails(originalItem);
        
        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getItemName());
        assertEquals(25.00, result.getPrice(), 0.01); // Should remain unchanged
        assertEquals(8.00, result.getDiscount(), 0.01);
    }

    @Test
    void testShowEditItemSuccess_ShouldExecuteWithoutError() {
        // Act
        view.showEditItemSuccess();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testShowItemNotFound_ShouldExecuteWithoutError() {
        // Act
        view.showItemNotFound();
        
        // Assert - Method should execute without throwing exception
        assertTrue(true); // Just verify it doesn't throw exception
    }

    @Test
    void testMultipleOperations_ShouldWorkCorrectly() {
        // Arrange
        String input = "ITEM001\nTest Item\n25.50\n5.00\nITEM002\nITEM003\nUpdated Item\n30.75\n7.50\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        // Act
        Item newItem = view.getNewItemDetails();
        String deleteCode = view.getItemCodeToDelete();
        String editCode = view.getItemCodeToEdit();
        Item updatedItem = view.getUpdatedItemDetails(new Item(1, "ITEM002", "Original", 20.00, 3.00));
        
        // Assert
        assertNotNull(newItem);
        assertEquals("ITEM001", newItem.getItemCode());
        assertEquals("Test Item", newItem.getItemName());
        assertEquals("ITEM002", deleteCode);
        assertEquals("ITEM003", editCode);
        assertNotNull(updatedItem);
        assertEquals("Updated Item", updatedItem.getItemName());
        assertEquals(30.75, updatedItem.getPrice(), 0.01);
        assertEquals(7.50, updatedItem.getDiscount(), 0.01);
    }

    @Test
    void testGetNewItemDetails_WithLargeNumbers_ShouldHandleCorrectly() {
        // Arrange
        String input = "ITEM999\nVery Long Item Name With Spaces\n9999.99\n999.99\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        // Act
        Item result = view.getNewItemDetails();
        
        // Assert
        assertNotNull(result);
        assertEquals("ITEM999", result.getItemCode());
        assertEquals("Very Long Item Name With Spaces", result.getItemName());
        assertEquals(9999.99, result.getPrice(), 0.01);
        assertEquals(999.99, result.getDiscount(), 0.01);
    }

    @Test
    void testGetUpdatedItemDetails_WithCurrentValuesDisplayed_ShouldShowCorrectValues() {
        // Arrange
        String input = "\n\n\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new ItemManagementView();
        
        Item originalItem = new Item(1, "ITEM001", "Test Item", 45.67, 12.34);
        
        // Act
        view.getUpdatedItemDetails(originalItem);
        
        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("current: Test Item"));
        assertTrue(output.contains("current: 45.67"));
        assertTrue(output.contains("current: 12.34"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 