package strategy;

import model.BatchSelection;
import model.MainStock;

import java.util.List;


public interface BatchSelectionStrategy {
    List<BatchSelection> selectBatches(List<MainStock> mainStocks, int requiredQuantity);
}

