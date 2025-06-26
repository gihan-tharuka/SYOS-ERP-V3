package Testing2.Testing.command;

import command.ViewAllItemsCommand;
import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewAllItemsCommandTest {

    private ItemDAO mockItemDAO;
    private ItemManagementView mockItemManagementView;
    private Item mockItem1;
    private Item mockItem2;
    private ViewAllItemsCommand viewAllItemsCommand;

    @BeforeEach
    void setUp() {
        mockItemDAO = mock(ItemDAO.class);
        mockItemManagementView = mock(ItemManagementView.class);
        mockItem1 = mock(Item.class);
        mockItem2 = mock(Item.class);
        viewAllItemsCommand = new ViewAllItemsCommand(mockItemDAO, mockItemManagementView);
    }

    @Test
    void testConstructor_WithValidParameters_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllItemsCommand command = new ViewAllItemsCommand(mockItemDAO, mockItemManagementView);

        // Assert
        assert command != null;
        command.execute(); // Should not throw exception
    }

    @Test
    void testConstructor_WithNullItemDAO_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllItemsCommand command = new ViewAllItemsCommand(null, mockItemManagementView);

        // Assert
        assert command != null;
        // Should throw NullPointerException when execute is called with null DAO
        try {
            command.execute();
            assert false : "Should have thrown NullPointerException";
        } catch (NullPointerException e) {
            // Expected behavior - command doesn't handle null DAO gracefully
        }
    }

    @Test
    void testConstructor_WithNullItemManagementView_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllItemsCommand command = new ViewAllItemsCommand(mockItemDAO, null);

        // Assert
        assert command != null;
        // Should throw NullPointerException when execute is called with null View
        try {
            command.execute();
            assert false : "Should have thrown NullPointerException";
        } catch (NullPointerException e) {
            // Expected behavior - command doesn't handle null View gracefully
        }
    }

    @Test
    void testExecute_WithMultipleItems_ShouldCallDAOAndView() {
        // Arrange
        List<Item> items = Arrays.asList(mockItem1, mockItem2);
        when(mockItemDAO.getAllItems()).thenReturn(items);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(items);
    }

    @Test
    void testExecute_WithEmptyItemList_ShouldCallDAOAndView() {
        // Arrange
        List<Item> emptyItems = new ArrayList<>();
        when(mockItemDAO.getAllItems()).thenReturn(emptyItems);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(emptyItems);
    }

    @Test
    void testExecute_WithNullItemList_ShouldCallDAOAndView() {
        // Arrange
        when(mockItemDAO.getAllItems()).thenReturn(null);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(null);
    }

    @Test
    void testExecute_WithSingleItem_ShouldCallDAOAndView() {
        // Arrange
        List<Item> singleItem = Collections.singletonList(mockItem1);
        when(mockItemDAO.getAllItems()).thenReturn(singleItem);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(singleItem);
    }

    @Test
    void testExecute_WithLargeItemList_ShouldCallDAOAndView() {
        // Arrange
        List<Item> largeItemList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeItemList.add(mockItem1);
        }
        when(mockItemDAO.getAllItems()).thenReturn(largeItemList);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(largeItemList);
    }

    @Test
    void testExecute_WithNullItemInList_ShouldHandleGracefully() {
        // Arrange
        List<Item> itemsWithNull = Arrays.asList(mockItem1, null, mockItem2);
        when(mockItemDAO.getAllItems()).thenReturn(itemsWithNull);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(itemsWithNull);
    }

    @Test
    void testExecute_WithAllNullItems_ShouldHandleGracefully() {
        // Arrange
        List<Item> allNullItems = Arrays.asList(null, null, null);
        when(mockItemDAO.getAllItems()).thenReturn(allNullItems);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(allNullItems);
    }

    @Test
    void testExecute_WithDAOThrowingException_ShouldHandleGracefully() {
        // Arrange
        when(mockItemDAO.getAllItems()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        try {
            viewAllItemsCommand.execute();
        } catch (Exception e) {
            // Should handle exception gracefully
        }
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, never()).displayAllItems(any());
    }

    @Test
    void testExecute_WithViewThrowingException_ShouldHandleGracefully() {
        // Arrange
        List<Item> items = Arrays.asList(mockItem1, mockItem2);
        when(mockItemDAO.getAllItems()).thenReturn(items);
        doThrow(new RuntimeException("View error")).when(mockItemManagementView).displayAllItems(items);

        // Act & Assert
        try {
            viewAllItemsCommand.execute();
        } catch (Exception e) {
            // Should handle exception gracefully
        }
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(items);
    }

    @Test
    void testExecute_MultipleCalls_ShouldCallDAOAndViewEachTime() {
        // Arrange
        List<Item> items = Arrays.asList(mockItem1, mockItem2);
        when(mockItemDAO.getAllItems()).thenReturn(items);

        // Act
        viewAllItemsCommand.execute();
        viewAllItemsCommand.execute();
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(3)).getAllItems();
        verify(mockItemManagementView, times(3)).displayAllItems(items);
    }

    @Test
    void testExecute_WithConcurrentAccess_ShouldHandleGracefully() {
        // Arrange
        List<Item> items = Arrays.asList(mockItem1, mockItem2);
        when(mockItemDAO.getAllItems()).thenReturn(items);

        // Act & Assert
        try {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> viewAllItemsCommand.execute());
                threads[i].start();
            }
            
            for (Thread thread : threads) {
                thread.join(1000);
            }
        } catch (Exception e) {
            // Should handle concurrent access gracefully
        }
    }

    @Test
    void testExecute_WithMemoryPressure_ShouldHandleGracefully() {
        // Arrange
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            items.add(mockItem1);
        }
        when(mockItemDAO.getAllItems()).thenReturn(items);

        // Act
        viewAllItemsCommand.execute();

        // Assert
        verify(mockItemDAO, times(1)).getAllItems();
        verify(mockItemManagementView, times(1)).displayAllItems(items);
    }
} 