package view;

import model.BatchSelection;
import model.MainStock;
import model.ShelfStock;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ShelfStockManagementView {
    private Scanner scanner;

    public ShelfStockManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displayShelfStockManagementMenu() {
        System.out.println("Shelf Stock Management Menu:");
        System.out.println("1. Add New Shelf Stock");
        System.out.println("2. View All Shelf Stocks");
        System.out.println("3. Reshelf Stock");
        System.out.println("4. Edit Shelf Stock");
        System.out.println("5. Delete Shelf Stock");
        System.out.println("5. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }
    public void clearInput() {
        scanner.nextLine();
    }

//    public ShelfStock getNewShelfStockDetails() {
//        System.out.println("Enter item ID: ");
//        int itemId = scanner.nextInt();
//        System.out.println("Enter shelf capacity: ");
//        int shelfCapacity = scanner.nextInt();
//
//        ShelfStock shelfStock = new ShelfStock();
//        shelfStock.setItemId(itemId);
//        shelfStock.setShelfCapacity(shelfCapacity);
//        shelfStock.setCurrentQuantity(0);
//
//        return shelfStock;
//    }
    public String getItemCodeForNewShelfStock() {
        System.out.println("Enter item code: ");
        return scanner.next();
    }

    public ShelfStock getNewShelfStockDetails(int itemId) {
        System.out.println("Enter shelf capacity: ");
        int shelfCapacity = scanner.nextInt();
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(itemId);
        shelfStock.setShelfCapacity(shelfCapacity);
        shelfStock.setCurrentQuantity(0);
        return shelfStock;
    }


    public void displayReshelvingInfo(Map<Integer, List<BatchSelection>> reshelvingInfo, Map<Integer, Integer> reshelfQuantities, Map<Integer, String> itemNames) {
        System.out.println("Reshelving Info:");
        for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
            int itemId = entry.getKey();
            List<BatchSelection> selectedBatches = entry.getValue();
            int reshelfQuantity = reshelfQuantities.get(itemId);
            String itemName = itemNames.get(itemId);

            System.out.println("Item ID: " + itemId + ", Item Name: " + itemName + ", Reshelf Quantity: " + reshelfQuantity);
            for (BatchSelection selection : selectedBatches) {
                System.out.println("  Batch Code: " + selection.getBatch().getBatchCode() + ", Reshelf Quantity: " + selection.getReshelfQuantity() + ", Expiry Date: " + selection.getBatch().getExpiryDate());
            }
            System.out.println("---------------");
        }
    }

    public boolean confirmReshelfAll() {
        System.out.println("Do you want to proceed with reshelfing for all items? (yes/no)");
        String userInput = scanner.next();
        return userInput.equalsIgnoreCase("yes");
    }




    public void showAddShelfStockSuccess() {
        System.out.println("Shelf stock added successfully!");
    }

    public void showAddShelfStockFailure() {
        System.out.println("Failed to add shelf stock.");
    }
    public void showReshelfSuccess() {
        System.out.println("Shelf stock reshelved successfully!");
    }

//    public void displayAllShelfStocks(List<ShelfStock> shelfStocks) {
//        System.out.println("All Shelf Stocks:");
//        for (ShelfStock shelfStock : shelfStocks) {
//            System.out.println("Shelf ID: " + shelfStock.getStockId());
//            System.out.println("Item ID: " + shelfStock.getItemId());
//            System.out.println("Shelf Capacity: " + shelfStock.getShelfCapacity());
//            System.out.println("Current Quantity: " + shelfStock.getCurrentQuantity());
//            System.out.println("---------------");
//        }
//    }
//public void displayAllShelfStocks(List<ShelfStock> shelfStocks) {
//    System.out.println("All Shelf Stocks:");
//    System.out.printf("%-10s %-10s %-15s %-15s%n", "Shelf ID", "Item ID", "Shelf Capacity", "Current Quantity");
//    System.out.println("--------------------------------------------------------");
//    for (ShelfStock shelfStock : shelfStocks) {
//        System.out.printf("%-10d %-10d %-15d %-15d%n",
//                shelfStock.getStockId(), shelfStock.getItemId(), shelfStock.getShelfCapacity(), shelfStock.getCurrentQuantity());
//    }
//    System.out.println("--------------------------------------------------------");
//}
public void displayAllShelfStocks(Map<ShelfStock, String> shelfStocksWithItemCodes) {
    System.out.println("All Shelf Stocks:");
    System.out.printf("%-10s %-10s %-15s %-15s%n", "Shelf ID", "Item Code", "Shelf Capacity", "Current Quantity");
    System.out.println("--------------------------------------------------------");
    for (Map.Entry<ShelfStock, String> entry : shelfStocksWithItemCodes.entrySet()) {
        ShelfStock shelfStock = entry.getKey();
        String itemCode = entry.getValue();
        System.out.printf("%-10d %-10s %-15d %-15d%n",
                shelfStock.getStockId(), itemCode, shelfStock.getShelfCapacity(), shelfStock.getCurrentQuantity());
    }
    System.out.println("--------------------------------------------------------");
}

    public int getItemIdForDelete() {
        System.out.println("Enter item ID for deletion: ");
        return scanner.nextInt();
    }

    public boolean confirmDelete(int itemId) {
        System.out.println("Are you sure you want to delete the shelf stock for item ID: " + itemId + "? (yes/no)");
        String userInput = scanner.next();
        return userInput.equalsIgnoreCase("yes");
    }

    public void showDeleteSuccess() {
        System.out.println("Shelf stock deleted successfully!");
    }

    public int getItemIdForEdit() {
        System.out.println("Enter item ID for editing: ");
        return scanner.nextInt();
    }

    public ShelfStock getUpdatedShelfStockDetails(int itemId) {
        System.out.println("Enter new shelf capacity: ");
        int newShelfCapacity = scanner.nextInt();
        System.out.println("Enter new current quantity: ");
        int newCurrentQuantity = scanner.nextInt();

        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setItemId(itemId);
        shelfStock.setShelfCapacity(newShelfCapacity);
        shelfStock.setCurrentQuantity(newCurrentQuantity);

        return shelfStock;
    }

    public void showEditSuccess() {
        System.out.println("Shelf stock updated successfully!");
    }
}


