import authentication.AdminAuthenticationHandler;
import authentication.CashierAuthenticationHandler;
import controller.*;
import dao.*;
import observer.ReorderObserver;
import observer.ReorderSubject;
import strategy.BatchSelectionStrategy;
import strategy.MultipleBatchesStrategy;
import view.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SynexOutletStoreSystem {
    public void startApp(Connection connection) {
        if (connection == null) {
            //System.out.println("Unable to connect to the database. Please ensure the database server is running and try again.");
            return;
        }

        ReorderSubject reorderSubject = initializeReorderSubject();
        UserDAO userDAO = new UserDAO(connection);

        // Instantiate DAOs
        ItemDAO itemDAO = new ItemDAO(connection, reorderSubject);
        MainStockDAO mainStockDAO = new MainStockDAO(connection);
        ShelfStockDAO shelfStockDAO = new ShelfStockDAO(connection);
        SaleDAO saleDAO = new SaleDAO(connection);
        BillDAO billDAO = new BillDAO(connection);
        BillItemDAO billItemDAO = new BillItemDAO(connection);
        ReportDAO reportDAO = new ReportDAO();
        ReorderLevelDAO reorderLevelDAO = new ReorderLevelDAO(connection);

        // Instantiate Views
        AdminMenuView adminMenuView = new AdminMenuView();
        UserManagementView userManagementView = new UserManagementView();
        ItemManagementView itemManagementView = new ItemManagementView();

        CashierMenuView cashierMenuView = new CashierMenuView();
        MainStockManagementView mainStockManagementView = new MainStockManagementView();
        ShelfStockManagementView shelfStockManagementView = new ShelfStockManagementView();
        SalesManagementView salesManagementView = new SalesManagementView();
        UserAuthenticationView userAuthenticationView = new UserAuthenticationView();
        ReportManagementView reportManagementView = new ReportManagementView();

        // Instantiate Controllers
        MainStockManagementController mainStockManagementController = new MainStockManagementController(
                mainStockManagementView, mainStockDAO, itemDAO, userDAO);
        ShelfStockManagementController shelfStockManagementController = new ShelfStockManagementController(
                shelfStockManagementView, shelfStockDAO, mainStockDAO, itemDAO, reorderLevelDAO);
        SalesManagementController salesManagementController = new SalesManagementController(
                salesManagementView, saleDAO, billDAO, billItemDAO, shelfStockDAO, new ItemDAO(connection, new ReorderSubject()));
        ItemManagementController itemManagementController = new ItemManagementController(itemDAO, itemManagementView);

        BatchSelectionStrategy batchSelectionStrategy = new MultipleBatchesStrategy(); // Example strategy
        ReportManagementController reportManagementController = new ReportManagementController(
                reportManagementView, shelfStockDAO, mainStockDAO, itemDAO, reportDAO, batchSelectionStrategy);

        UserManagementController userManagementController = new UserManagementController(userDAO, userManagementView);
        AdminMenuController adminMenuController = new AdminMenuController(adminMenuView, userManagementView,
                itemManagementView, mainStockManagementView, shelfStockManagementView, salesManagementView,
                userManagementController, itemManagementController, mainStockManagementController,
                shelfStockManagementController, salesManagementController, reportManagementController);
        CashierMenuController cashierMenuController = new CashierMenuController(cashierMenuView,
                mainStockManagementController, shelfStockManagementController, salesManagementController,
                reportManagementController);

        // Authentication setup
        AdminAuthenticationHandler adminHandler = new AdminAuthenticationHandler(userDAO);
        CashierAuthenticationHandler cashierHandler = new CashierAuthenticationHandler(userDAO);
        adminHandler.setNextHandler(cashierHandler);

        AuthenticationController authenticationController = new AuthenticationController(
                userAuthenticationView, adminHandler, adminMenuController, cashierMenuController);

        authenticationController.authenticateUser();
    }

    private ReorderSubject initializeReorderSubject() {
        ReorderSubject reorderSubject = new ReorderSubject();
        try {
            Connection connection = DatabaseConnection.getInstance().getConnection();
            ReorderLevelDAO reorderLevelDAO = new ReorderLevelDAO(connection);
            ReorderObserver reorderObserver = new ReorderObserver(reorderLevelDAO);
            reorderSubject.addObserver(reorderObserver);
        } catch (SQLException e) {
            System.err.println("Error initializing reorder subject: " + e.getMessage());
            e.printStackTrace();
        }
        return reorderSubject;
    }
}
