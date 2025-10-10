package command;

import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;

public class AddItemCommand implements Command {
    private ItemDAO itemDAO;
    private ItemManagementView view;
    private Item item;

    public AddItemCommand(ItemDAO itemDAO, ItemManagementView view) {
        this.itemDAO = itemDAO;
        this.view = view;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public void execute() {
        if (item != null) {
            itemDAO.addItem(item);
            view.showAddItemSuccess();
        }
    }
}

