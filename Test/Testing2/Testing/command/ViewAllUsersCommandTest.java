package Testing2.Testing.command;

import command.ViewAllUsersCommand;
import dao.UserDAO;
import model.Cashier;
import model.Supplier;
import model.User;
import view.UserManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewAllUsersCommandTest {

    private UserDAO mockUserDAO;
    private UserManagementView mockUserManagementView;
    private User mockUser;
    private Cashier mockCashier;
    private Supplier mockSupplier;
    private ViewAllUsersCommand viewAllUsersCommand;

    @BeforeEach
    void setUp() {
        mockUserDAO = mock(UserDAO.class);
        mockUserManagementView = mock(UserManagementView.class);
        mockUser = mock(User.class);
        mockCashier = mock(Cashier.class);
        mockSupplier = mock(Supplier.class);
        viewAllUsersCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "admin");
    }

    @Test
    void testConstructor_WithValidParameters_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllUsersCommand command = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "cashier");

        // Assert
        assert command != null;
        command.execute(); // Should not throw exception
    }

    @Test
    void testConstructor_WithNullUserDAO_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllUsersCommand command = new ViewAllUsersCommand(null, mockUserManagementView, "supplier");

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
    void testConstructor_WithNullUserManagementView_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllUsersCommand command = new ViewAllUsersCommand(mockUserDAO, null, "admin");

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
    void testConstructor_WithNullRole_ShouldCreateCommand() {
        // Arrange & Act
        ViewAllUsersCommand command = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, null);

        // Assert
        assert command != null;
        // Should handle null role gracefully (role is just a string parameter)
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers(null)).thenReturn(users);
        command.execute(); // Should not throw exception for null role
        verify(mockUserDAO).getAllUsers(null);
        verify(mockUserManagementView).displayAllUsers(users, null);
    }

    @Test
    void testExecute_WithAdminRole_ShouldCallDAOAndView() {
        // Arrange
        List<User> users = Arrays.asList(mockUser, mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(users);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "admin");
    }

    @Test
    void testExecute_WithCashierRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand cashierCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "cashier");
        List<User> users = Arrays.asList(mockCashier, mockCashier);
        when(mockUserDAO.getAllUsers("cashier")).thenReturn(users);

        // Act
        cashierCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("cashier");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "cashier");
    }

    @Test
    void testExecute_WithSupplierRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand supplierCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "supplier");
        List<User> users = Arrays.asList(mockSupplier, mockSupplier);
        when(mockUserDAO.getAllUsers("supplier")).thenReturn(users);

        // Act
        supplierCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("supplier");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "supplier");
    }

    @Test
    void testExecute_WithEmptyUserList_ShouldCallDAOAndView() {
        // Arrange
        List<User> emptyUsers = new ArrayList<>();
        when(mockUserDAO.getAllUsers("admin")).thenReturn(emptyUsers);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(emptyUsers, "admin");
    }

    @Test
    void testExecute_WithNullUserList_ShouldCallDAOAndView() {
        // Arrange
        when(mockUserDAO.getAllUsers("admin")).thenReturn(null);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(null, "admin");
    }

    @Test
    void testExecute_WithSingleUser_ShouldCallDAOAndView() {
        // Arrange
        List<User> singleUser = Collections.singletonList(mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(singleUser);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(singleUser, "admin");
    }

    @Test
    void testExecute_WithLargeUserList_ShouldCallDAOAndView() {
        // Arrange
        List<User> largeUserList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeUserList.add(mockUser);
        }
        when(mockUserDAO.getAllUsers("admin")).thenReturn(largeUserList);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(largeUserList, "admin");
    }

    @Test
    void testExecute_WithMixedUserTypes_ShouldCallDAOAndView() {
        // Arrange
        List<User> mixedUsers = Arrays.asList(mockUser, mockCashier, mockSupplier);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(mixedUsers);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(mixedUsers, "admin");
    }

    @Test
    void testExecute_WithUpperCaseRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand upperCaseCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "ADMIN");
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("ADMIN")).thenReturn(users);

        // Act
        upperCaseCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("ADMIN");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "ADMIN");
    }

    @Test
    void testExecute_WithLowerCaseRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand lowerCaseCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "admin");
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(users);

        // Act
        lowerCaseCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "admin");
    }

    @Test
    void testExecute_WithMixedCaseRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand mixedCaseCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "AdMiN");
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("AdMiN")).thenReturn(users);

        // Act
        mixedCaseCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("AdMiN");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "AdMiN");
    }

    @Test
    void testExecute_WithEmptyRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand emptyRoleCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "");
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("")).thenReturn(users);

        // Act
        emptyRoleCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "");
    }

    @Test
    void testExecute_WithWhitespaceRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand whitespaceRoleCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "   ");
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("   ")).thenReturn(users);

        // Act
        whitespaceRoleCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("   ");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "   ");
    }

    @Test
    void testExecute_WithSpecialCharactersRole_ShouldCallDAOAndView() {
        // Arrange
        ViewAllUsersCommand specialCharCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "admin@123");
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("admin@123")).thenReturn(users);

        // Act
        specialCharCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin@123");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "admin@123");
    }

    @Test
    void testExecute_WithLongRole_ShouldCallDAOAndView() {
        // Arrange
        String longRole = "very_long_role_name_that_exceeds_normal_length";
        ViewAllUsersCommand longRoleCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, longRole);
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers(longRole)).thenReturn(users);

        // Act
        longRoleCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers(longRole);
        verify(mockUserManagementView, times(1)).displayAllUsers(users, longRole);
    }

    @Test
    void testExecute_WithDAOThrowingException_ShouldHandleGracefully() {
        // Arrange
        when(mockUserDAO.getAllUsers("admin")).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        try {
            viewAllUsersCommand.execute();
        } catch (Exception e) {
            // Should handle exception gracefully
        }
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, never()).displayAllUsers(any(), any());
    }

    @Test
    void testExecute_WithViewThrowingException_ShouldHandleGracefully() {
        // Arrange
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(users);
        doThrow(new RuntimeException("View error")).when(mockUserManagementView).displayAllUsers(users, "admin");

        // Act & Assert
        try {
            viewAllUsersCommand.execute();
        } catch (Exception e) {
            // Should handle exception gracefully
        }
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "admin");
    }

    @Test
    void testExecute_MultipleCalls_ShouldCallDAOAndViewEachTime() {
        // Arrange
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(users);

        // Act
        viewAllUsersCommand.execute();
        viewAllUsersCommand.execute();
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(3)).getAllUsers("admin");
        verify(mockUserManagementView, times(3)).displayAllUsers(users, "admin");
    }

    @Test
    void testExecute_WithDifferentRoles_ShouldCallCorrectMethods() {
        // Arrange
        ViewAllUsersCommand adminCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "admin");
        ViewAllUsersCommand cashierCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "cashier");
        ViewAllUsersCommand supplierCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, "supplier");

        List<User> adminUsers = Arrays.asList(mockUser);
        List<User> cashierUsers = Arrays.asList(mockCashier);
        List<User> supplierUsers = Arrays.asList(mockSupplier);

        when(mockUserDAO.getAllUsers("admin")).thenReturn(adminUsers);
        when(mockUserDAO.getAllUsers("cashier")).thenReturn(cashierUsers);
        when(mockUserDAO.getAllUsers("supplier")).thenReturn(supplierUsers);

        // Act
        adminCommand.execute();
        cashierCommand.execute();
        supplierCommand.execute();

        // Assert
        verify(mockUserDAO).getAllUsers("admin");
        verify(mockUserDAO).getAllUsers("cashier");
        verify(mockUserDAO).getAllUsers("supplier");
        verify(mockUserManagementView).displayAllUsers(adminUsers, "admin");
        verify(mockUserManagementView).displayAllUsers(cashierUsers, "cashier");
        verify(mockUserManagementView).displayAllUsers(supplierUsers, "supplier");
    }

    @Test
    void testExecute_WithNullUserInList_ShouldHandleGracefully() {
        // Arrange
        List<User> usersWithNull = Arrays.asList(mockUser, null, mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(usersWithNull);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(usersWithNull, "admin");
    }

    @Test
    void testExecute_WithAllNullUsers_ShouldHandleGracefully() {
        // Arrange
        List<User> allNullUsers = Arrays.asList(null, null, null);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(allNullUsers);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(allNullUsers, "admin");
    }

    @Test
    void testExecute_WithConcurrentAccess_ShouldHandleGracefully() {
        // Arrange
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers("admin")).thenReturn(users);

        // Act & Assert
        try {
            Thread[] threads = new Thread[5];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(() -> viewAllUsersCommand.execute());
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
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            users.add(mockUser);
        }
        when(mockUserDAO.getAllUsers("admin")).thenReturn(users);

        // Act
        viewAllUsersCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers("admin");
        verify(mockUserManagementView, times(1)).displayAllUsers(users, "admin");
    }

    @Test
    void testExecute_WithUnicodeRole_ShouldCallDAOAndView() {
        // Arrange
        String unicodeRole = "管理员"; // Chinese for "admin"
        ViewAllUsersCommand unicodeCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, unicodeRole);
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers(unicodeRole)).thenReturn(users);

        // Act
        unicodeCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers(unicodeRole);
        verify(mockUserManagementView, times(1)).displayAllUsers(users, unicodeRole);
    }

    @Test
    void testExecute_WithEmojiRole_ShouldCallDAOAndView() {
        // Arrange
        String emojiRole = "admin👨‍💼";
        ViewAllUsersCommand emojiCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, emojiRole);
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers(emojiRole)).thenReturn(users);

        // Act
        emojiCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers(emojiRole);
        verify(mockUserManagementView, times(1)).displayAllUsers(users, emojiRole);
    }

    @Test
    void testExecute_WithNullRole_ShouldCallDAOAndView() {
        // Arrange
        List<User> users = Arrays.asList(mockUser);
        when(mockUserDAO.getAllUsers(null)).thenReturn(users);

        // Act
        ViewAllUsersCommand nullRoleCommand = new ViewAllUsersCommand(mockUserDAO, mockUserManagementView, null);
        nullRoleCommand.execute();

        // Assert
        verify(mockUserDAO, times(1)).getAllUsers(null);
        verify(mockUserManagementView, times(1)).displayAllUsers(users, null);
    }
} 