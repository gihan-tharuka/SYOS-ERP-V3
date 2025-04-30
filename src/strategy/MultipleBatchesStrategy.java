package strategy;

import model.BatchSelection;
import model.MainStock;

import java.util.ArrayList;
import java.util.List;

public class MultipleBatchesStrategy implements BatchSelectionStrategy {
    @Override
    public List<BatchSelection> selectBatches(List<MainStock> mainStocks, int requiredQuantity) {
        List<BatchSelection> selectedBatches = new ArrayList<>();
        int remainingQuantity = requiredQuantity;

        for (MainStock stock : mainStocks) {
            if (remainingQuantity <= 0) {
                break;
            }
            int reshelfQuantity = Math.min(remainingQuantity, stock.getQuantity());
            remainingQuantity -= reshelfQuantity;
            selectedBatches.add(new BatchSelection(stock, reshelfQuantity));
            stock.setQuantity(stock.getQuantity() - reshelfQuantity);
        }
        return selectedBatches;
    }
}
