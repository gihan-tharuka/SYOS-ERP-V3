package Testing2.Testing.command;

import command.EditItemCommand;
import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class EditItemCommandTest {
    private ItemDAO itemDAOMock;
    private ItemManagementView viewMock;
    private Item testItem;

    @BeforeEach
    public void setUp() {
        itemDAOMock = mock(ItemDAO.class);
        viewMock = mock(ItemManagementView.class);
        testItem = mock(Item.class);
    }

    @Test
    public void testExecute_WithItem() {
        EditItemCommand command = new EditItemCommand(itemDAOMock, viewMock);
        command.setItem(testItem);
        command.execute();
        verify(itemDAOMock).updateItem(testItem);
        verify(viewMock).showEditItemSuccess();
        verify(viewMock, never()).showItemNotFound();
    }

    @Test
    public void testExecute_NullItem() {
        EditItemCommand command = new EditItemCommand(itemDAOMock, viewMock);
        command.setItem(null);
        command.execute();
        verify(itemDAOMock, never()).updateItem(any());
        verify(viewMock, never()).showEditItemSuccess();
        verify(viewMock).showItemNotFound();
    }
} 