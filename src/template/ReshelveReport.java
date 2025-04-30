package template;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ShelfStockDAO;
import model.BatchSelection;
import model.MainStock;
import model.ShelfStock;
import strategy.BatchSelectionStrategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReshelveReport extends ReportTemplate {
    private List<ShelfStock> shelfStocks;
    private MainStockDAO mainStockDAO;
    private ShelfStockDAO shelfStockDAO;
    private ItemDAO itemDAO;
    private Map<Integer, List<BatchSelection>> reshelvingInfo;
    private Map<Integer, Integer> reshelfQuantities;
    private Map<Integer, String> itemNames;
    private BatchSelectionStrategy strategy;

    public ReshelveReport(ShelfStockDAO shelfStockDAO, MainStockDAO mainStockDAO, ItemDAO itemDAO, BatchSelectionStrategy strategy) {
        this.shelfStockDAO = shelfStockDAO;
        this.mainStockDAO = mainStockDAO;
        this.itemDAO = itemDAO;
        this.strategy = strategy;
    }

    @Override
    protected void fetchData() {
        shelfStocks = shelfStockDAO.getAllShelfStocks();
        reshelvingInfo = new HashMap<>();
        reshelfQuantities = new HashMap<>();
        itemNames = new HashMap<>();

        for (ShelfStock shelfStock : shelfStocks) {
            int itemId = shelfStock.getItemId();
            int reshelfQuantity = shelfStock.getShelfCapacity() - shelfStock.getCurrentQuantity();

            if (reshelfQuantity <= 0) {
                continue;
            }

            List<MainStock> mainStocks = mainStockDAO.getMainStocksByItemId(itemId);
            List<BatchSelection> selectedBatches = strategy.selectBatches(mainStocks, reshelfQuantity);

            if (selectedBatches.isEmpty()) {
                System.out.println("Not enough stock available in the main stock for item ID: " + itemId);
                continue;
            }

            reshelvingInfo.put(itemId, selectedBatches);
            reshelfQuantities.put(itemId, reshelfQuantity);
            itemNames.put(itemId, itemDAO.getItemNameById(itemId));
        }
    }

    @Override
    protected void processData() {

    }

    @Override
    protected void generateOutput() {
        System.out.println("Reshelve Report:");
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
}
