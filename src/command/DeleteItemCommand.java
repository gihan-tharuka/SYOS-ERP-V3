package command;

import dao.ItemDAO;
import view.ItemManagementView;

public class DeleteItemCommand implements Command {
    private ItemDAO itemDAO;
    private ItemManagementView view;
    private String itemCode;

    public DeleteItemCommand(ItemDAO itemDAO, ItemManagementView view) {
        this.itemDAO = itemDAO;
        this.view = view;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    @Override
    public void execute() {
        if (itemCode != null) {
            itemDAO.deleteItemByCode(itemCode);
            view.showDeleteItemSuccess();
        }
    }
}
