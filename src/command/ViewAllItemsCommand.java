package command;

import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;

import java.util.List;

public class ViewAllItemsCommand implements Command {
    private ItemDAO itemDAO;
    private ItemManagementView view;

    public ViewAllItemsCommand(ItemDAO itemDAO, ItemManagementView view) {
        this.itemDAO = itemDAO;
        this.view = view;
    }

    @Override
    public void execute() {
        List<Item> items = itemDAO.getAllItems();
        view.displayAllItems(items);
    }
}
