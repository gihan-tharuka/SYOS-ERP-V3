package Testing2.Testing.command;

import command.AddItemCommand;
import model.Item;
import dao.ItemDAO;
import view.ItemManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddItemCommandTest {
    private ItemDAO itemDAOMock;
    private ItemManagementView viewMock;
    private Item testItem;

    @BeforeEach
    public void setUp() {
        itemDAOMock = mock(ItemDAO.class);
        viewMock = mock(ItemManagementView.class);
        testItem = new Item(1, "ITEM001", "Test Item", 10.0, 0.0);
    }

    @Test
    public void testExecute_AddsItemAndShowsSuccess() {
        AddItemCommand command = new AddItemCommand(itemDAOMock, viewMock);
        command.setItem(testItem);
        command.execute();
        verify(itemDAOMock).addItem(testItem);
        verify(viewMock).showAddItemSuccess();
    }

    @Test
    public void testExecute_NullItem() {
        AddItemCommand command = new AddItemCommand(itemDAOMock, viewMock);
        command.setItem(null);
        command.execute();
        verify(itemDAOMock, never()).addItem(any());
        verify(viewMock, never()).showAddItemSuccess();
    }
} 