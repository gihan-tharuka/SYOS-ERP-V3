package Testing2.Testing.controller;

import controller.UserManagementController;
import command.Command;
import dao.UserDAO;
import view.UserManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagementControllerTest {
    private UserDAO userDAO;
    private UserManagementView view;
    private UserManagementController controller;

    @BeforeEach
    void setUp() {
        userDAO = mock(UserDAO.class);
        view = mock(UserManagementView.class);
        controller = new UserManagementController(userDAO, view);
    }

    @Test
    void testHandleUserManagement_AdminValidChoice() {
        Command mockCommand = mock(Command.class);
        UserManagementController spyController = spy(controller);
        // Inject the mock command into the adminCommands map
        setCommand(spyController, "admin", 1, mockCommand);
        spyController.handleUserManagement("admin", 1);
        verify(mockCommand).execute();
    }

    @Test
    void testHandleUserManagement_CashierValidChoice() {
        Command mockCommand = mock(Command.class);
        UserManagementController spyController = spy(controller);
        setCommand(spyController, "cashier", 2, mockCommand);
        spyController.handleUserManagement("cashier", 2);
        verify(mockCommand).execute();
    }

    @Test
    void testHandleUserManagement_SupplierValidChoice() {
        Command mockCommand = mock(Command.class);
        UserManagementController spyController = spy(controller);
        setCommand(spyController, "supplier", 3, mockCommand);
        spyController.handleUserManagement("supplier", 3);
        verify(mockCommand).execute();
    }

    @Test
    void testHandleUserManagement_InvalidChoice() {
        UserManagementController spyController = spy(controller);
        // No command for choice 99
        spyController.handleUserManagement("admin", 99);
        // No verify needed, just ensure no exception
    }

    @Test
    void testHandleUserManagement_InvalidRole() {
        UserManagementController spyController = spy(controller);
        assertThrows(IllegalArgumentException.class, () -> spyController.handleUserManagement("unknown", 1));
    }

    // Helper method to inject a mock command into the private command maps
    private void setCommand(UserManagementController controller, String role, int choice, Command command) {
        try {
            java.lang.reflect.Field field;
            switch (role) {
                case "admin":
                    field = UserManagementController.class.getDeclaredField("adminCommands");
                    break;
                case "cashier":
                    field = UserManagementController.class.getDeclaredField("cashierCommands");
                    break;
                case "supplier":
                    field = UserManagementController.class.getDeclaredField("supplierCommands");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid role");
            }
            field.setAccessible(true);
            // Create a new mutable map with the mock command
            java.util.Map<Integer, Command> newMap = new java.util.HashMap<>();
            newMap.put(choice, command);
            field.set(controller, newMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
} 