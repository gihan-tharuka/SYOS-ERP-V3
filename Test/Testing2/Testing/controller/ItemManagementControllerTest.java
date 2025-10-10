package Testing2.Testing.controller;

import controller.ItemManagementController;
import command.Command;
import dao.ItemDAO;
import view.ItemManagementView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ItemManagementControllerTest {
    private ItemDAO itemDAO;
    private ItemManagementView view;
    private ItemManagementController controller;

    @BeforeEach
    void setUp() {
        itemDAO = mock(ItemDAO.class);
        view = mock(ItemManagementView.class);
        controller = new ItemManagementController(itemDAO, view);
    }

    @Test
    void testHandleItemManagement_ValidChoices() {
        // Test all valid choices (1-4)
        for (int choice = 1; choice <= 4; choice++) {
            Command mockCommand = mock(Command.class);
            ItemManagementController spyController = spy(controller);
            setCommand(spyController, choice, mockCommand);
            spyController.handleItemManagement(choice);
            verify(mockCommand).execute();
        }
    }

    @Test
    void testHandleItemManagement_InvalidChoice() {
        // No command for choice 99
        // Just check that no exception is thrown and nothing is executed
        ItemManagementController spyController = spy(controller);
        spyController.handleItemManagement(99);
        // No verify needed, just ensure no exception
    }

    // Helper method to inject a mock command into the private itemCommands map
    private void setCommand(ItemManagementController controller, int choice, Command command) {
        try {
            java.lang.reflect.Field field = ItemManagementController.class.getDeclaredField("itemCommands");
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