package command;

import dao.ItemDAO;
import view.ItemManagementView;

public class DeleteItemCommand implements Command {
    private ItemDAO itemDAO;
    private ItemManagementView view;

    public DeleteItemCommand(ItemDAO itemDAO, ItemManagementView view) {
        this.itemDAO = itemDAO;
        this.view = view;
    }

    @Override
    public void execute() {
        String itemCode = view.getItemCodeToDelete();
        itemDAO.deleteItemByCode(itemCode);
        view.showDeleteItemSuccess();
    }
}
