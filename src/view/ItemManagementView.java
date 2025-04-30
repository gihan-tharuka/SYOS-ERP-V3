package view;

import model.Item;

import java.util.List;
import java.util.Scanner;

public class ItemManagementView {
    private Scanner scanner;

    public ItemManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displayItemManagementMenu() {
        System.out.println("Item Management Menu:");
        System.out.println("1. Add Item");
        System.out.println("2. View All Items");
        System.out.println("3. Delete Item");
        System.out.println("4. Edit Item");
        System.out.println("5. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine();
    }

    public Item getNewItemDetails() {
        //clearInput(); // Clear the buffer

        System.out.println("Enter item code: ");
        String itemCode = scanner.nextLine();

        System.out.println("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.println("Enter item price: ");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.println("Enter item discount (if any): ");
        Double discount = 0.00;
        String discountInput = scanner.nextLine();
        if (!discountInput.trim().isEmpty()) {
            discount = Double.parseDouble(discountInput);
        }

        return new Item(0, itemCode, itemName, price, discount);
    }


    public void showAddItemSuccess() {
        System.out.println("New item added successfully.");
    }

    public void displayAllItems(List<Item> items) {
        System.out.println("List of all items:");
        for (Item item : items) {
            System.out.println("Item Code: " + item.getItemCode() + ", Item Name: " + item.getItemName() + ", Price: " + item.getPrice() + ", Discount: " + item.getDiscount());
        }
    }

    public String getItemCodeToDelete() {
        //clearInput(); // Clear the buffer
        System.out.println("Enter item code to delete: ");
        return scanner.nextLine();
    }

    public void showDeleteItemSuccess() {
        System.out.println("Item deleted successfully.");
    }

    public String getItemCodeToEdit() {
        //clearInput(); // Clear the buffer
        System.out.println("Enter item code to edit: ");
        return scanner.nextLine();
    }

//    public Item getUpdatedItemDetails(Item item) {
//        System.out.println("Enter new item name: ");
//        String itemName = scanner.nextLine();
//        System.out.println("Enter new item price: ");
//        double price = scanner.nextDouble();
//        System.out.println("Enter new item discount (if any): ");
//        double discount = scanner.nextDouble();
//
//        item.setItemName(itemName);
//        item.setPrice(price);
//        item.setDiscount(discount);
//
//        return item;
//    }
//public Item getUpdatedItemDetails(Item item) {
//    System.out.println("Leave field empty to keep the current value.");
//
//    System.out.println("Enter new item name (leave blank to keep current): ");
//    String itemName = scanner.nextLine();
//    if (!itemName.trim().isEmpty()) {
//        item.setItemName(itemName);
//    }
//
//    System.out.println("Enter new item price (leave blank to keep current): ");
//    String priceInput = scanner.nextLine();
//    if (!priceInput.trim().isEmpty()) {
//        double price = Double.parseDouble(priceInput);
//        item.setPrice(price);
//    }
//
//    System.out.println("Enter new item discount (leave blank to keep current): ");
//    String discountInput = scanner.nextLine();
//    if (!discountInput.trim().isEmpty()) {
//        double discount = Double.parseDouble(discountInput);
//        item.setDiscount(discount);
//    }
//
//    return item;
//}
public Item getUpdatedItemDetails(Item item) {
    System.out.println("Leave field empty to keep the current value.");

    System.out.println("Enter new item name (current: " + item.getItemName() + "):");
    String itemName = scanner.nextLine();
    if (!itemName.trim().isEmpty()) {
        item.setItemName(itemName);
    }

    System.out.println("Enter new item price (current: " + item.getPrice() + "):");
    String priceInput = scanner.nextLine();
    if (!priceInput.trim().isEmpty()) {
        double price = Double.parseDouble(priceInput);
        item.setPrice(price);
    }

    System.out.println("Enter new item discount (current: " + item.getDiscount() + "):");
    String discountInput = scanner.nextLine();
    if (!discountInput.trim().isEmpty()) {
        double discount = Double.parseDouble(discountInput);
        item.setDiscount(discount);
    }

    return item;
}

    public void showEditItemSuccess() {
        System.out.println("Item updated successfully.");
    }

    public void showItemNotFound() {
        System.out.println("Item not found.");
    }
}
