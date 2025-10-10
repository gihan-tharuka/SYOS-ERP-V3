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
        // This method is kept for compatibility but doesn't need to do anything
        // since we're using web responses instead of console output
    }

    public void displayAllItems(List<Item> items) {
        // This method is kept for compatibility but doesn't need to do anything
        // since we're using web responses instead of console output
    }

    public String getItemCodeToDelete() {
        //clearInput(); // Clear the buffer
        System.out.println("Enter item code to delete: ");
        return scanner.nextLine();
    }

    public void showDeleteItemSuccess() {
        // This method is kept for compatibility but doesn't need to do anything
        // since we're using web responses instead of console output
    }

    public String getItemCodeToEdit() {
        //clearInput(); // Clear the buffer
        System.out.println("Enter item code to edit: ");
        return scanner.nextLine();
    }


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
        // This method is kept for compatibility but doesn't need to do anything
        // since we're using web responses instead of console output
    }

    public void showItemNotFound() {
        // This method is kept for compatibility but doesn't need to do anything
        // since we're using web responses instead of console output
    }
}
