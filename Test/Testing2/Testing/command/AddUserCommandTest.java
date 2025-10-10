package Testing2.Testing.command;

import command.AddUserCommand;
import dao.UserDAO;
import model.User;
import view.UserManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class AddUserCommandTest {
    private UserDAO userDAOMock;
    private UserManagementView viewMock;
    private User testUser;

    @BeforeEach
    public void setUp() {
        userDAOMock = mock(UserDAO.class);
        viewMock = mock(UserManagementView.class);
        testUser = mock(User.class);
    }

    @Test
    public void testExecute_UserAdded() {
        String role = "admin";
        when(viewMock.collectUserInfo(role)).thenReturn(testUser);

        AddUserCommand command = new AddUserCommand(userDAOMock, viewMock, role);
        command.execute();

        verify(viewMock).collectUserInfo(role);
        verify(userDAOMock).addUser(testUser, role);
    }

    @Test
    public void testExecute_UserNotAdded() {
        String role = "admin";
        when(viewMock.collectUserInfo(role)).thenReturn(null);

        AddUserCommand command = new AddUserCommand(userDAOMock, viewMock, role);
        command.execute();

        verify(viewMock).collectUserInfo(role);
        verify(userDAOMock, never()).addUser(any(), any());
    }
} 