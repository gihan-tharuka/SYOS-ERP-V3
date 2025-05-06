package command;

import dao.ItemDAO;
import model.Item;
import view.ItemManagementView;

import java.util.Scanner;

public class AddItemCommand implements Command {
    private ItemDAO itemDAO;
    //private Scanner scanner = new Scanner(System.in);
    private ItemManagementView view;

    public AddItemCommand(ItemDAO itemDAO, ItemManagementView view) {
        this.itemDAO = itemDAO;
        this.view = view;
    }

    @Override

    public void execute() {
        Item item = view.getNewItemDetails();
        itemDAO.addItem(item);
        view.showAddItemSuccess();
    }
}

