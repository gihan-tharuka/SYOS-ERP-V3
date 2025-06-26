package Testing2.Testing.command;

import command.DeleteItemCommand;
import dao.ItemDAO;
import view.ItemManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class DeleteItemCommandTest {
    private ItemDAO itemDAOMock;
    private ItemManagementView viewMock;

    @BeforeEach
    public void setUp() {
        itemDAOMock = mock(ItemDAO.class);
        viewMock = mock(ItemManagementView.class);
    }

    @Test
    public void testExecute_WithItemCode() {
        DeleteItemCommand command = new DeleteItemCommand(itemDAOMock, viewMock);
        command.setItemCode("ITEM001");
        command.execute();
        verify(itemDAOMock).deleteItemByCode("ITEM001");
        verify(viewMock).showDeleteItemSuccess();
    }

    @Test
    public void testExecute_NullItemCode() {
        DeleteItemCommand command = new DeleteItemCommand(itemDAOMock, viewMock);
        command.setItemCode(null);
        command.execute();
        verify(itemDAOMock, never()).deleteItemByCode(any());
        verify(viewMock, never()).showDeleteItemSuccess();
    }
} 