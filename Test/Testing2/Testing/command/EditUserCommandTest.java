package Testing2.Testing.command;

import command.EditUserCommand;
import dao.UserDAO;
import model.User;
import view.UserManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class EditUserCommandTest {
    private UserDAO userDAOMock;
    private UserManagementView viewMock;
    private User testUser;
    private User updatedUser;

    @BeforeEach
    public void setUp() {
        userDAOMock = mock(UserDAO.class);
        viewMock = mock(UserManagementView.class);
        testUser = mock(User.class);
        updatedUser = mock(User.class);
    }

    @Test
    public void testExecute_UserFoundAndUpdated() {
        String role = "admin";
        String username = "testuser";
        when(viewMock.getUsernameForEditing(role)).thenReturn(username);
        when(userDAOMock.getUserByUsername(username, role)).thenReturn(testUser);
        when(viewMock.collectUpdatedUserInfo(testUser, role)).thenReturn(updatedUser);

        EditUserCommand command = new EditUserCommand(userDAOMock, viewMock, role);
        command.execute();

        verify(viewMock).getUsernameForEditing(role);
        verify(userDAOMock).getUserByUsername(username, role);
        verify(viewMock).collectUpdatedUserInfo(testUser, role);
        verify(userDAOMock).updateUser(updatedUser, role);
        verify(viewMock).displayUserUpdatedMessage(role);
    }

    @Test
    public void testExecute_UserNotFound() {
        String role = "admin";
        String username = "nonexistentuser";
        when(viewMock.getUsernameForEditing(role)).thenReturn(username);
        when(userDAOMock.getUserByUsername(username, role)).thenReturn(null);

        EditUserCommand command = new EditUserCommand(userDAOMock, viewMock, role);
        command.execute();

        verify(viewMock).getUsernameForEditing(role);
        verify(userDAOMock).getUserByUsername(username, role);
        verify(viewMock).displayUserNotFoundMessage(role);
        verify(userDAOMock, never()).updateUser(any(), any());
        verify(viewMock, never()).collectUpdatedUserInfo(any(), any());
        verify(viewMock, never()).displayUserUpdatedMessage(any());
    }
} 