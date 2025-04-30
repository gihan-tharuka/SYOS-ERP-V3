package controller;

import command.*;
import dao.ItemDAO;
import view.ItemManagementView;

import java.util.Map;

public class ItemManagementController {
    private Map<Integer, Command> itemCommands;

    public ItemManagementController(ItemDAO itemDAO, ItemManagementView view) {
        this.itemCommands = Map.of(
                1, new AddItemCommand(itemDAO, view),
                2, new ViewAllItemsCommand(itemDAO, view),
                3, new DeleteItemCommand(itemDAO, view),
                4, new EditItemCommand(itemDAO, view)
        );
    }

    public void handleItemManagement(int choice) {
        if (itemCommands.containsKey(choice)) {
            itemCommands.get(choice).execute();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}
