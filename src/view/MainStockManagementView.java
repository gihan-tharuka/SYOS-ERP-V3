package view;

import dao.ItemDAO;
import dao.UserDAO;
import model.MainStock;

import java.util.List;
import java.util.Scanner;

public class MainStockManagementView {
    private Scanner scanner;

    public MainStockManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displayMainStockManagementMenu() {
        System.out.println("Main Stock Management Menu:");
        System.out.println("1. Add New Main Stock");
        System.out.println("2. View All Main Stocks");
        System.out.println("3. Edit Main Stock");
        System.out.println("4. Delete Main Stock");
        System.out.println("5. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }


    public String getItemCodeFromUser() {
        System.out.println("Enter item code: ");
        return scanner.nextLine();
    }

    public String getSupplierUsernameFromUser() {
        System.out.println("Enter supplier username: ");
        return scanner.nextLine();
    }

    public MainStock getNewMainStockDetails(MainStock mainStock) {
        //System.out.println("Enter supplier ID: ");
        //int supplierId = scanner.nextInt();
        //scanner.nextLine(); // Consume newline
        System.out.println("Enter batch code: ");
        String batchCode = scanner.nextLine();
        System.out.println("Enter purchase date (yyyy-mm-dd): ");
        String purchaseDateString = scanner.nextLine();
        System.out.println("Enter purchase price: ");
        double purchasePrice = scanner.nextDouble();
        System.out.println("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        // Optional expiry date input
        System.out.println("Enter expiry date (yyyy-mm-dd) or press Enter to skip: ");
        String expiryDateString = scanner.nextLine();

        try {
            java.util.Date purchaseDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(purchaseDateString);
            java.util.Date expiryDate = null;

            if (!expiryDateString.isEmpty()) {
                expiryDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(expiryDateString);
            }

            //mainStock.setSupplierId(supplierId);
            mainStock.setBatchCode(batchCode);
            mainStock.setPurchaseDate(purchaseDate);
            mainStock.setPurchasePrice(purchasePrice);
            mainStock.setQuantity(quantity);
            mainStock.setExpiryDate(expiryDate);

            return mainStock;
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format. Please try again.");
            return null;
        }
    }



    public void showAddMainStockSuccess() {
        System.out.println("Main stock added successfully!");
    }

    public void showEditMainStockSuccess() {
        System.out.println("Main stock edited successfully!");
    }

    public void showDeleteMainStockSuccess() {
        System.out.println("Main stock deleted successfully!");
    }


//    public void displayAllMainStocks(List<MainStock> mainStocks) {
//        System.out.println("All Main Stocks:");
//        for (MainStock mainStock : mainStocks) {
//            System.out.println("Stock ID: " + mainStock.getStockId());
//            System.out.println("Item ID: " + mainStock.getItemId());
//            System.out.println("Supplier ID: " + mainStock.getSupplierId());
//            System.out.println("Batch Code: " + mainStock.getBatchCode());
//            System.out.println("Purchase Date: " + mainStock.getPurchaseDate());
//            System.out.println("Purchase Price: " + mainStock.getPurchasePrice());
//            System.out.println("Quantity: " + mainStock.getQuantity());
//            System.out.println("Expiry Date: " + mainStock.getExpiryDate());
//            System.out.println("---------------");
//        }
//    }
//public void displayAllMainStocks(List<MainStock> mainStocks) {
//    String leftAlignFormat = "| %-8d | %-7d | %-11d | %-11s | %-12s | %-14.2f | %-8d | %-11s |%n";
//
//    System.out.format("+----------+---------+-------------+-------------+--------------+----------------+----------+-------------+%n");
//    System.out.format("| Stock ID | Item ID | Supplier ID | Batch Code  | Purchase Date| Purchase Price | Quantity | Expiry Date |%n");
//    System.out.format("+----------+---------+-------------+-------------+--------------+----------------+----------+-------------+%n");
//
//    for (MainStock mainStock : mainStocks) {
//        System.out.format(leftAlignFormat,
//                mainStock.getStockId(),
//                mainStock.getItemId(),
//                mainStock.getSupplierId(),
//                mainStock.getBatchCode(),
//                mainStock.getPurchaseDate(),
//                mainStock.getPurchasePrice(),
//                mainStock.getQuantity(),
//                mainStock.getExpiryDate()
//        );
//    }
//
//    System.out.format("+----------+---------+-------------+-------------+--------------+----------------+----------+-------------+%n");
//}
public void displayAllMainStocks(List<MainStock> mainStocks, ItemDAO itemDAO, UserDAO userDAO) {
    String leftAlignFormat = "| %-8d | %-10s | %-15s | %-11s | %-12s | %-14.2f | %-8d | %-11s |%n";

    System.out.format("+----------+------------+-----------------+-------------+--------------+----------------+----------+-------------+%n");
    System.out.format("| Stock ID | Item Code  | Supplier Username| Batch Code  | Purchase Date| Purchase Price | Quantity | Expiry Date |%n");
    System.out.format("+----------+------------+-----------------+-------------+--------------+----------------+----------+-------------+%n");

    for (MainStock mainStock : mainStocks) {
        String itemCode = itemDAO.getItemCodeById(mainStock.getItemId());
        String supplierUsername = userDAO.getUsernameById(mainStock.getSupplierId());

        System.out.format(leftAlignFormat,
                mainStock.getStockId(),
                itemCode,
                supplierUsername,
                mainStock.getBatchCode(),
                mainStock.getPurchaseDate(),
                mainStock.getPurchasePrice(),
                mainStock.getQuantity(),
                mainStock.getExpiryDate()
        );
    }

    System.out.format("+----------+------------+-----------------+-------------+--------------+----------------+----------+-------------+%n");
}



    //    public int getUserChoice() {
//        return scanner.nextInt();
//    }
public int getUserChoice() {
    int choice = scanner.nextInt();
    clearInput(); // Consume newline
    return choice;
}

    public void clearInput() {
        scanner.nextLine();
    }
}
