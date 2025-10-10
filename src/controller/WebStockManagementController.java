package controller;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.WebStockDAO;
import model.BatchSelection;
import model.MainStock;
import model.WebStock;
import strategy.BatchSelectionStrategy;
import strategy.MultipleBatchesStrategy;
import view.WebStockManagementView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebStockManagementController {
    private WebStockManagementView view;
    private WebStockDAO webStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private ReorderLevelDAO reorderLevelDAO;

    public WebStockManagementController(WebStockManagementView view, WebStockDAO webStockDAO, MainStockDAO mainStockDAO, ItemDAO itemDAO, ReorderLevelDAO reorderLevelDAO) {
        this.view = view;
        this.webStockDAO = webStockDAO;
        this.mainStockDAO = mainStockDAO;
        this.itemDAO = itemDAO;
        this.reorderLevelDAO = reorderLevelDAO;
    }

    // Add getter methods for DAOs
    public WebStockDAO getWebStockDAO() {
        return webStockDAO;
    }

    public ItemDAO getItemDAO() {
        return itemDAO;
    }

    public MainStockDAO getMainStockDAO() {
        return mainStockDAO;
    }

    public ReorderLevelDAO getReorderLevelDAO() {
        return reorderLevelDAO;
    }

    public void showWebStockManagementMenu() {
        view.displayWebStockManagementMenu();
        int choice = view.getUserChoice();
        switch (choice) {
            case 1:
                addNewWebStock();
                break;
            case 2:
                viewAllWebStocks();
                break;
            case 3:
                handleReshelfStock();
                break;
            case 4:
                editWebStock();
                break;
            case 5:
                deleteWebStock();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    public void addNewWebStock() {
        String itemCode = view.getItemCodeForNewWebStock();
        int itemId = itemDAO.getItemIdByCode(itemCode);
        if (itemId == -1) {
            System.out.println("Invalid item code. Please try again.");
            return;
        }
        WebStock webStock = view.getNewWebStockDetails(itemId);
        if (webStock != null) {
            boolean success = webStockDAO.addWebStock(webStock);
            if (success) {
                view.showAddWebStockSuccess();
            } else {
                view.showAddWebStockFailure();
            }
        }
    }

    public void viewAllWebStocks() {
        Map<WebStock, String> webStocksWithItemCodes = webStockDAO.getAllWebStocksWithItemCodes();
        view.displayAllWebStocks(webStocksWithItemCodes);
    }

    public void handleReshelfStock() {
        List<WebStock> allWebStocks = webStockDAO.getAllWebStocks();
        Map<Integer, List<BatchSelection>> reshelvingInfo = webStockDAO.getReshelvingInfo(allWebStocks, mainStockDAO);
        Map<Integer, Integer> reshelfQuantities = webStockDAO.getReshelfQuantities(allWebStocks);
        Map<Integer, String> itemNames = itemDAO.getItemNamesByItemIds(reshelfQuantities.keySet());

        view.displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);

        boolean confirmReshelf = view.confirmReshelfAll();
        if (confirmReshelf) {
            webStockDAO.confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);
            view.showReshelfSuccess();
        } else {
            System.out.println("Reshelf operation canceled.");
        }
    }

    public void deleteWebStock() {
        int itemId = view.getItemIdForDelete();
        boolean confirmDelete = view.confirmDelete(itemId);
        if (confirmDelete) {
            webStockDAO.deleteWebStockByItemId(itemId);
            view.showDeleteSuccess();
        } else {
            System.out.println("Delete operation canceled.");
        }
    }

    public void editWebStock() {
        int itemId = view.getItemIdForEdit();
        WebStock updatedWebStock = view.getUpdatedWebStockDetails(itemId);
        if (updatedWebStock != null) {
            webStockDAO.updateWebStock(updatedWebStock);
            view.showEditSuccess();
        } else {
            System.out.println("Edit operation canceled.");
        }
    }
}
