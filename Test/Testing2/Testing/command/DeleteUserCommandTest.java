package Testing2.Testing.command;

import command.DeleteUserCommand;
import dao.UserDAO;
import view.UserManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class DeleteUserCommandTest {
    private UserDAO userDAOMock;
    private UserManagementView viewMock;

    @BeforeEach
    public void setUp() {
        userDAOMock = mock(UserDAO.class);
        viewMock = mock(UserManagementView.class);
    }

    @Test
    public void testExecute_WithUsername() {
        String role = "admin";
        String username = "testuser";
        when(viewMock.getUsernameForDeletion(role)).thenReturn(username);
        when(userDAOMock.deleteUser(username, role)).thenReturn(true);

        DeleteUserCommand command = new DeleteUserCommand(userDAOMock, viewMock, role);
        command.execute();

        verify(viewMock).getUsernameForDeletion(role);
        verify(userDAOMock).deleteUser(username, role);
        verify(viewMock).displayDeletionResult(true, role);
    }

    @Test
    public void testExecute_NullUsername() {
        String role = "admin";
        when(viewMock.getUsernameForDeletion(role)).thenReturn(null);

        DeleteUserCommand command = new DeleteUserCommand(userDAOMock, viewMock, role);
        command.execute();

        verify(viewMock).getUsernameForDeletion(role);
        verify(userDAOMock, never()).deleteUser(any(), any());
        verify(viewMock, never()).displayDeletionResult(anyBoolean(), any());
    }
} 