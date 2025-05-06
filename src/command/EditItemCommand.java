package command;

import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;

public class EditItemCommand implements Command {
    private ItemDAO itemDAO;
    private ItemManagementView view;

    public EditItemCommand(ItemDAO itemDAO, ItemManagementView view) {
        this.itemDAO = itemDAO;
        this.view = view;
    }

    @Override
    public void execute() {
        String itemCode = view.getItemCodeToEdit();
        Item item = itemDAO.getItemByCode(itemCode);
        if (item != null) {
            item = view.getUpdatedItemDetails(item);
            itemDAO.updateItem(item);
            view.showEditItemSuccess();
        } else {
            view.showItemNotFound();
        }
    }
}

