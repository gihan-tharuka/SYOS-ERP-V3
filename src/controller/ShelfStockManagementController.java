package controller;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ReorderLevelDAO;
import dao.ShelfStockDAO;
import model.BatchSelection;
import model.MainStock;
import model.ShelfStock;
import strategy.BatchSelectionStrategy;
import strategy.MultipleBatchesStrategy;
import view.ShelfStockManagementView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShelfStockManagementController {
    private ShelfStockManagementView view;
    private ShelfStockDAO shelfStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private ReorderLevelDAO reorderLevelDAO;

    public ShelfStockManagementController(ShelfStockManagementView view, ShelfStockDAO shelfStockDAO, MainStockDAO mainStockDAO, ItemDAO itemDAO, ReorderLevelDAO reorderLevelDAO) {
        this.view = view;
        this.shelfStockDAO = shelfStockDAO;
        this.mainStockDAO = mainStockDAO;
        this.itemDAO = itemDAO;
        this.reorderLevelDAO = reorderLevelDAO;
    }

    // Add getter methods for DAOs
    public ShelfStockDAO getShelfStockDAO() {
        return shelfStockDAO;
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

    public void showShelfStockManagementMenu() {
        view.displayShelfStockManagementMenu();
        int choice = view.getUserChoice();
        switch (choice) {
            case 1:
                addNewShelfStock();
                break;
            case 2:
                viewAllShelfStocks();
                break;
            case 3:
                handleReshelfStock();
                break;
            case 4:
                editShelfStock();
                break;
            case 5:
                deleteShelfStock();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

//    public void addNewShelfStock() {
//        ShelfStock shelfStock = view.getNewShelfStockDetails();
//        if (shelfStock != null) {
//            shelfStockDAO.addShelfStock(shelfStock);
//            view.showAddShelfStockSuccess();
//        }
//    }
//public void addNewShelfStock() {
//    String itemCode = view.getItemCodeForNewShelfStock();
//    int itemId = itemDAO.getItemIdByCode(itemCode);
//    if (itemId == -1) {
//        System.out.println("Invalid item code. Please try again.");
//        return;
//    }
//    ShelfStock shelfStock = view.getNewShelfStockDetails(itemId);
//    if (shelfStock != null) {
//        shelfStockDAO.addShelfStock(shelfStock);
//        view.showAddShelfStockSuccess();
//    }
//}
public void addNewShelfStock() {
    String itemCode = view.getItemCodeForNewShelfStock();
    int itemId = itemDAO.getItemIdByCode(itemCode);
    if (itemId == -1) {
        System.out.println("Invalid item code. Please try again.");
        return;
    }
    ShelfStock shelfStock = view.getNewShelfStockDetails(itemId);
    if (shelfStock != null) {
        boolean success = shelfStockDAO.addShelfStock(shelfStock);
        if (success) {
            view.showAddShelfStockSuccess();
        } else {
            view.showAddShelfStockFailure();
        }
    }
}

//    public void viewAllShelfStocks() {
//        List<ShelfStock> shelfStocks = shelfStockDAO.getAllShelfStocks();
//        view.displayAllShelfStocks(shelfStocks);
//    }
public void viewAllShelfStocks() {
    Map<ShelfStock, String> shelfStocksWithItemCodes = shelfStockDAO.getAllShelfStocksWithItemCodes();
    view.displayAllShelfStocks(shelfStocksWithItemCodes);
}

//    public void handleReshelfStock() {
//        List<ShelfStock> allShelfStocks = shelfStockDAO.getAllShelfStocks();
//        Map<Integer, List<BatchSelection>> reshelvingInfo = new HashMap<>();
//        Map<Integer, Integer> reshelfQuantities = new HashMap<>();
//        Map<Integer, String> itemNames = new HashMap<>();
//
//        for (ShelfStock shelfStock : allShelfStocks) {
//            int itemId = shelfStock.getItemId();
//            int reshelfQuantity = shelfStock.getShelfCapacity() - shelfStock.getCurrentQuantity();
//
//            if (reshelfQuantity <= 0) {
//                continue;
//            }
//
//            List<MainStock> mainStocks = mainStockDAO.getMainStocksByItemId(itemId);
//            BatchSelectionStrategy strategy = new MultipleBatchesStrategy();
//            List<BatchSelection> selectedBatches = strategy.selectBatches(mainStocks, reshelfQuantity);
//
//            if (selectedBatches.isEmpty()) {
//                System.out.println("Not enough stock available in the main stock for item ID: " + itemId);
//                continue;
//            }
//
//            // Store reshelving info for later confirmation
//            reshelvingInfo.put(itemId, selectedBatches);
//            reshelfQuantities.put(itemId, reshelfQuantity);
//            itemNames.put(itemId, itemDAO.getItemNameById(itemId));
//        }
//
//        // Display all reshelving info to the user
//        view.displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);
//
//        // Ask for user confirmation to proceed with all reshelving
//        boolean confirmReshelf = view.confirmReshelfAll();
//        if (confirmReshelf) {
//            for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
//                int itemId = entry.getKey();
//                List<BatchSelection> selectedBatches = entry.getValue();
//                ShelfStock shelfStock = shelfStockDAO.getShelfStockByItemId(itemId);
//                int reshelfQuantity = reshelfQuantities.get(itemId);
//
//                shelfStock.setCurrentQuantity(shelfStock.getCurrentQuantity() + reshelfQuantity);
//                shelfStockDAO.updateShelfStock(shelfStock);
//
//                for (BatchSelection selection : selectedBatches) {
//                    mainStockDAO.updateMainStock(selection.getBatch());
//                }
//            }
//            view.showReshelfSuccess();
//        } else {
//            System.out.println("Reshelf operation canceled.");
//        }
//    }
public void handleReshelfStock() {
    List<ShelfStock> allShelfStocks = shelfStockDAO.getAllShelfStocks();
    Map<Integer, List<BatchSelection>> reshelvingInfo = shelfStockDAO.getReshelvingInfo(allShelfStocks, mainStockDAO);
    Map<Integer, Integer> reshelfQuantities = shelfStockDAO.getReshelfQuantities(allShelfStocks);
    Map<Integer, String> itemNames = itemDAO.getItemNamesByItemIds(reshelfQuantities.keySet());

    // Display all reshelving info to the user
    view.displayReshelvingInfo(reshelvingInfo, reshelfQuantities, itemNames);

    // Ask for user confirmation to proceed with all reshelving
//    boolean confirmReshelf = view.confirmReshelfAll();
//    if (confirmReshelf) {
//        shelfStockDAO.confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO);
//        view.showReshelfSuccess();
//    } else {
//        System.out.println("Reshelf operation canceled.");
//    }
    // Ask for user confirmation to proceed with all reshelving
    boolean confirmReshelf = view.confirmReshelfAll();
    if (confirmReshelf) {
        shelfStockDAO.confirmReshelving(reshelvingInfo, reshelfQuantities, mainStockDAO, reorderLevelDAO);
        view.showReshelfSuccess();
    } else {
        System.out.println("Reshelf operation canceled.");
    }
}

    public void deleteShelfStock() {
        int itemId = view.getItemIdForDelete();
        boolean confirmDelete = view.confirmDelete(itemId);
        if (confirmDelete) {
            shelfStockDAO.deleteShelfStockByItemId(itemId);
            view.showDeleteSuccess();
        } else {
            System.out.println("Delete operation canceled.");
        }
    }

    public void editShelfStock() {
        int itemId = view.getItemIdForEdit();
        ShelfStock updatedShelfStock = view.getUpdatedShelfStockDetails(itemId);
        if (updatedShelfStock != null) {
            shelfStockDAO.updateShelfStock(updatedShelfStock);
            view.showEditSuccess();
        } else {
            System.out.println("Edit operation canceled.");
        }
    }




}

