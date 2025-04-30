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
//    public void execute() {
//        System.out.println("Enter item code: ");
//        String itemCode = scanner.nextLine();
//
//        System.out.println("Enter item name: ");
//        String itemName = scanner.nextLine();
//
//        System.out.println("Enter item price: ");
//        double price = scanner.nextDouble();
//
//        System.out.println("Enter item discount (if any): ");
//        Double discount = null;
//        if (scanner.hasNextDouble()) {
//            discount = scanner.nextDouble();
//        }
//
//        Item item = new Item(0, itemCode, itemName, price, discount);
//        itemDAO.addItem(item);
//        System.out.println("New item added successfully.");
//    }
    public void execute() {
        Item item = view.getNewItemDetails();
        itemDAO.addItem(item);
        view.showAddItemSuccess();
    }
}

