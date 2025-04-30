package controller;

import view.*;

public class AdminMenuController {
    private AdminMenuView view;
    private UserManagementView userManagementView;
    private ItemManagementView itemManagementView;
    private MainStockManagementView mainStockManagementView;
    private ShelfStockManagementView shelfStockManagementView;
    private SalesManagementView salesManagementView;
    private UserManagementController userManagementController;
    private ItemManagementController itemManagementController;
    private MainStockManagementController mainStockManagementController;
    private ShelfStockManagementController shelfStockManagementController;
    private SalesManagementController salesManagementController;
    private ReportManagementController reportManagementController;

    public AdminMenuController(AdminMenuView view, UserManagementView userManagementView,
                               ItemManagementView itemManagementView,MainStockManagementView mainStockManagementView,
                               ShelfStockManagementView shelfStockManagementView,
                               SalesManagementView salesManagementView,
                               UserManagementController userManagementController,
                               ItemManagementController itemManagementController,
                               MainStockManagementController mainStockManagementController,
                               ShelfStockManagementController shelfStockManagementController,
                               SalesManagementController salesManagementController,
                               ReportManagementController reportManagementController) {
        this.view = view;
        this.userManagementView = userManagementView;
        this.itemManagementView = itemManagementView;
        this.mainStockManagementView = mainStockManagementView;
        this.shelfStockManagementView = shelfStockManagementView;
        this.salesManagementView = salesManagementView;
        this.userManagementController = userManagementController;
        this.itemManagementController = itemManagementController;
        this.mainStockManagementController = mainStockManagementController;
        this.shelfStockManagementController = shelfStockManagementController;
        this.salesManagementController = salesManagementController;
        this.reportManagementController = reportManagementController;
    }

    public void handleMenu() {
        int choice;
        do {
            view.displayMenu();
            choice = view.getUserChoice();
            view.clearInput();
            switch (choice) {
                case 1:
                    handleAdminManagement();
                    break;
                case 2:
                    handleCashierManagement();
                    break;
                case 3:
                    handleSupplierManagement();
                    break;
                case 4:
                    handleItemManagement();
                    break;
                case 5:
                    handleMainStockManagement();
                    break;
                case 6:
                    handleShelfStockManagement();
                    break;
                case 7:
                    handleSalesManagement();
                    break;
                case 8:
                    handleReportManagement();
                    break;
                case 9:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 9);
    }

    private void handleAdminManagement() {
        int choice;
        do {
            userManagementView.displayAdminManagementMenu();
            choice = userManagementView.getUserChoice();
            userManagementView.clearInput();
            userManagementController.handleUserManagement("admin", choice);
        } while (choice != 5);
    }

    private void handleCashierManagement() {
        int choice;
        do {
            userManagementView.displayCashierManagementMenu();
            choice = userManagementView.getUserChoice();
            userManagementView.clearInput();
            userManagementController.handleUserManagement("cashier", choice);
        } while (choice != 5);
    }

    private void handleSupplierManagement() {
        int choice;
        do {
            userManagementView.displaySupplierManagementMenu();
            choice = userManagementView.getUserChoice();
            userManagementView.clearInput();
            userManagementController.handleUserManagement("supplier", choice);
        } while (choice != 5);
    }

    private void handleItemManagement() {
        int choice;
        do {
            itemManagementView.displayItemManagementMenu();
            choice = itemManagementView.getUserChoice();
            itemManagementView.clearInput();
            itemManagementController.handleItemManagement(choice);
        } while (choice != 5);
    }

//    private void handleMainStockManagement() {
//        int choice;
//        do {
//            mainStockManagementView.displayMainStockManagementMenu();
//            choice = mainStockManagementView.getUserChoice();
//            mainStockManagementView.clearInput();
//            mainStockManagementController.showMainStockManagementMenu();
//        } while (choice != 3);
//    }
    private void handleMainStockManagement() {
        mainStockManagementController.showMainStockManagementMenu();
    }


    private void handleShelfStockManagement() {
        int choice;
        do {
            shelfStockManagementView.displayShelfStockManagementMenu();
            choice = shelfStockManagementView.getUserChoice();
            shelfStockManagementView.clearInput();
            shelfStockManagementController.showShelfStockManagementMenu();
        } while (choice != 3);
    }

    private void handleSalesManagement() {
        int choice;
        do {
            salesManagementView.displaySalesManagementMenu();
            choice = salesManagementView.getUserChoice();
            salesManagementView.clearInput();
            salesManagementController.handleSalesManagement(choice);
        } while (choice != 3);
    }


private void handleReportManagement() {
    reportManagementController.showReportManagementMenu();
}
}
