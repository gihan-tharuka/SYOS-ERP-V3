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

//package command;
//
//import dao.ItemDAO;
//import model.Item;
//import java.util.Scanner;
//
//public class EditItemCommand implements Command {
//    private ItemDAO itemDAO;
//    private Scanner scanner = new Scanner(System.in);
//
//    public EditItemCommand(ItemDAO itemDAO) {
//        this.itemDAO = itemDAO;
//    }
//
//    @Override
//    public void execute() {
//        System.out.println("Enter item code to edit: ");
//        String itemCode = scanner.nextLine();
//
//        Item item = itemDAO.getItemByCode(itemCode);
//        if (item != null) {
//            System.out.println("Enter new item name: ");
//            String itemName = scanner.nextLine();
//            System.out.println("Enter new item price: ");
//            double price = scanner.nextDouble();
//            System.out.println("Enter new item discount (if any): ");
//            double discount = scanner.nextDouble();
//
//            item.setItemName(itemName);
//            item.setPrice(price);
//            item.setDiscount(discount);
//
//            itemDAO.updateItem(item);
//            System.out.println("Item updated successfully.");
//        } else {
//            System.out.println("Item not found.");
//        }
//    }
//}
