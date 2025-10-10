package factory;

import model.MainStock;
import model.ShelfStock;
import model.Stock;
import model.WebStock;

public class StockFactory {
    public enum StockType {
        MAIN, SHELF, WEB
    }

    public static Stock createStock(StockType stockType) {
        if (stockType == null) {
            throw new IllegalArgumentException("Invalid stock type");
        }
        switch (stockType) {
            case MAIN:
                return new MainStock();
            case SHELF:
                return new ShelfStock();
            case WEB:
                return new WebStock();
            default:
                throw new IllegalArgumentException("Invalid stock type");
        }
    }
}
