package controller;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.UserDAO; // Import UserDAO
import factory.StockFactory;
import model.Item;
import model.MainStock;
import view.MainStockManagementView;

import java.util.List;

public class MainStockManagementController {
    private MainStockManagementView view;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO; // Add ItemDAO instance
    private UserDAO userDAO; // Add UserDAO instance

    public MainStockManagementController(MainStockManagementView view, MainStockDAO mainStockDAO, ItemDAO itemDAO, UserDAO userDAO) {
        this.view = view;
        this.mainStockDAO = mainStockDAO;
        this.itemDAO = itemDAO; // Initialize the ItemDAO instance
        this.userDAO = userDAO; // Initialize the UserDAO instance
    }

    public void showMainStockManagementMenu() {
        view.displayMainStockManagementMenu();
        int choice = view.getUserChoice();
        //view.clearInput();//new
        switch (choice) {
            case 1:
                addNewMainStock();
                break;
            case 2:
                viewAllMainStocks();
                break;
            case 3:
                editMainStock();
                break;
            case 4:
                deleteMainStock();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

//    public void addNewMainStock() {
//        MainStock mainStock = (MainStock) StockFactory.createStock(StockFactory.StockType.MAIN);
//        mainStock = view.getNewMainStockDetails(mainStock);
//        if (mainStock != null) {
//            mainStockDAO.addMainStock(mainStock);
//            view.showAddMainStockSuccess();
//        }
//    }
//public void addNewMainStock() {
//    MainStock mainStock = (MainStock) StockFactory.createStock(StockFactory.StockType.MAIN);
//    String itemCode = view.getItemCodeFromUser();
//    if (mainStock != null) {
//        // Retrieve the item ID based on the item code
//        Item item = itemDAO.getItemByCode(itemCode); // Use the itemDAO instance
//        if (item != null) {
//            mainStock.setItemId(item.getItemId()); // Set the item ID
//            mainStock = view.getNewMainStockDetails(mainStock);
//            if (mainStock != null) {
//                mainStockDAO.addMainStock(mainStock);
//                view.showAddMainStockSuccess();
//            }
//        } else {
//            System.out.println("Item with the given code not found.");
//        }
//    }
//}
//public void addNewMainStock() {
//    MainStock mainStock = (MainStock) StockFactory.createStock(StockFactory.StockType.MAIN);
//
//    // Prompt user for item code and retrieve item ID
//    String itemCode = view.getItemCodeFromUser();
//    Item item = itemDAO.getItemByCode(itemCode); // Use the itemDAO instance
//    if (item != null) {
//        mainStock.setItemId(item.getItemId()); // Set the item ID
//    } else {
//        System.out.println("Item with the given code not found.");
//        return;
//    }
//
//    // Prompt user for supplier username and retrieve supplier ID
//    String supplierUsername = view.getSupplierUsernameFromUser();
//    Integer supplierId = userDAO.getSupplierIdByUsername(supplierUsername); // Use the userDAO instance
//    if (supplierId != null) {
//        mainStock.setSupplierId(supplierId); // Set the supplier ID
//    } else {
//        System.out.println("Supplier with the given username not found.");
//        return;
//    }
//
//    // Get other details from the user
//    mainStock = view.getNewMainStockDetails(mainStock);
//
//    if (mainStock != null) {
//        mainStockDAO.addMainStock(mainStock);
//        view.showAddMainStockSuccess();
//    }
//}
public void addNewMainStock() {
    MainStock mainStock = (MainStock) StockFactory.createStock(StockFactory.StockType.MAIN);

    // Prompt user for item code and retrieve item ID
    String itemCode = view.getItemCodeFromUser();
    Item item = itemDAO.getItemByCode(itemCode); // Use the itemDAO instance
    if (item != null) {
        mainStock.setItemId(item.getItemId()); // Set the item ID
    } else {
        System.out.println("Item with the given code not found.");
        return;
    }

    // Prompt user for supplier username and retrieve supplier ID
    String supplierUsername = view.getSupplierUsernameFromUser();
    Integer supplierId = userDAO.getSupplierIdByUsername(supplierUsername); // Use the userDAO instance
    if (supplierId != null) {
        mainStock.setSupplierId(supplierId); // Set the supplier ID
    } else {
        System.out.println("Supplier with the given username not found.");
        return;
    }

    // Get other details from the user
    mainStock = view.getNewMainStockDetails(mainStock);

    if (mainStock != null) {
        // Check for existing entry with the same item ID and batch code
        if (mainStockDAO.doesMainStockExist(mainStock.getItemId(), mainStock.getBatchCode())) {
            System.out.println("An entry with the same item ID and batch code already exists.");
        } else {
            mainStockDAO.addMainStock(mainStock);
            view.showAddMainStockSuccess();
        }
    }
}




//    public void viewAllMainStocks() {
//        List<MainStock> mainStocks = mainStockDAO.getAllMainStocks();
//        view.displayAllMainStocks(mainStocks);
//    }
public void viewAllMainStocks() {
    List<MainStock> mainStocks = mainStockDAO.getAllMainStocks();
    view.displayAllMainStocks(mainStocks, itemDAO, userDAO);
}


    public void editMainStock() {
        System.out.println("Enter the stock ID to edit: ");
        int stockId = view.getUserChoice();
        MainStock mainStock = mainStockDAO.getMainStockById(stockId);
        if (mainStock != null) {
            int previousQuantity = mainStock.getQuantity();
            mainStock = view.getNewMainStockDetails(mainStock);
            if (mainStock != null) {
                mainStockDAO.editMainStock(mainStock);
                mainStockDAO.adjustTotalStock(mainStock.getItemId(), mainStock.getQuantity() - previousQuantity);
                view.showEditMainStockSuccess();
            }
        } else {
            System.out.println("Stock ID not found.");
        }
    }

    public void deleteMainStock() {
        System.out.println("Enter the stock ID to delete: ");
        int stockId = view.getUserChoice();
        mainStockDAO.deleteMainStock(stockId);
        view.showDeleteMainStockSuccess();
    }
}
