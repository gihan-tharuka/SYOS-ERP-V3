package controller;

import view.CashierMenuView;

public class CashierMenuController {
    private CashierMenuView view;
    private MainStockManagementController mainStockManagementController;
    private ShelfStockManagementController shelfStockManagementController;
    private SalesManagementController salesManagementController;
    private ReportManagementController reportManagementController;

    public CashierMenuController(CashierMenuView view,
                                 MainStockManagementController mainStockManagementController,
                                 ShelfStockManagementController shelfStockManagementController,
                                 SalesManagementController salesManagementController,
                                 ReportManagementController reportManagementController) {
        this.view = view;
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
                    handleMainStockManagement();
                    break;
                case 2:
                    handleShelfStockManagement();
                    break;
                case 3:
                    handleSalesManagement();
                    break;
                case 4:
                    handleReportManagement();
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private void handleMainStockManagement() {
        mainStockManagementController.showMainStockManagementMenu();
    }

    private void handleShelfStockManagement() {
        shelfStockManagementController.showShelfStockManagementMenu();
    }

    private void handleSalesManagement() {
        salesManagementController.handleSalesManagement(1);
    }

    private void handleReportManagement() {
        reportManagementController.showReportManagementMenu();
    }
}
