package view;

import model.BatchSelection;
import model.WebStock;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class WebStockManagementView {
    private Scanner scanner;

    public WebStockManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displayWebStockManagementMenu() {
        System.out.println("Web Stock Management Menu:");
        System.out.println("1. Add New Web Stock");
        System.out.println("2. View All Web Stocks");
        System.out.println("3. Reshelf Stock");
        System.out.println("4. Edit Web Stock");
        System.out.println("5. Delete Web Stock");
        System.out.println("6. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine();
    }

    public String getItemCodeForNewWebStock() {
        System.out.println("Enter item code: ");
        return scanner.next();
    }

    public WebStock getNewWebStockDetails(int itemId) {
        System.out.println("Enter web capacity: ");
        int webCapacity = scanner.nextInt();
        WebStock webStock = new WebStock();
        webStock.setItemId(itemId);
        webStock.setWebCapacity(webCapacity);
        webStock.setCurrentQuantity(0);
        return webStock;
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

    public void showAddWebStockSuccess() {
        System.out.println("Web stock added successfully!");
    }

    public void showAddWebStockFailure() {
        System.out.println("Failed to add web stock.");
    }

    public void showReshelfSuccess() {
        System.out.println("Web stock reshelved successfully!");
    }

    public void displayAllWebStocks(Map<WebStock, String> webStocksWithItemCodes) {
        System.out.println("All Web Stocks:");
        System.out.printf("%-10s %-10s %-15s %-15s%n", "Web ID", "Item Code", "Web Capacity", "Current Quantity");
        System.out.println("--------------------------------------------------------");
        for (Map.Entry<WebStock, String> entry : webStocksWithItemCodes.entrySet()) {
            WebStock webStock = entry.getKey();
            String itemCode = entry.getValue();
            System.out.printf("%-10d %-10s %-15d %-15d%n",
                    webStock.getStockId(), itemCode, webStock.getWebCapacity(), webStock.getCurrentQuantity());
        }
        System.out.println("--------------------------------------------------------");
    }

    public int getItemIdForDelete() {
        System.out.println("Enter item ID for deletion: ");
        return scanner.nextInt();
    }

    public boolean confirmDelete(int itemId) {
        System.out.println("Are you sure you want to delete the web stock for item ID: " + itemId + "? (yes/no)");
        String userInput = scanner.next();
        return userInput.equalsIgnoreCase("yes");
    }

    public void showDeleteSuccess() {
        System.out.println("Web stock deleted successfully!");
    }

    public int getItemIdForEdit() {
        System.out.println("Enter item ID for editing: ");
        return scanner.nextInt();
    }

    public WebStock getUpdatedWebStockDetails(int itemId) {
        System.out.println("Enter new web capacity: ");
        int newWebCapacity = scanner.nextInt();
        System.out.println("Enter new current quantity: ");
        int newCurrentQuantity = scanner.nextInt();

        WebStock webStock = new WebStock();
        webStock.setItemId(itemId);
        webStock.setWebCapacity(newWebCapacity);
        webStock.setCurrentQuantity(newCurrentQuantity);

        return webStock;
    }

    public void showEditSuccess() {
        System.out.println("Web stock updated successfully!");
    }
}
