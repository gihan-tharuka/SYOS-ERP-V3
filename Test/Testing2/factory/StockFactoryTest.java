package Testing2.factory;

import factory.StockFactory;
import model.MainStock;
import model.ShelfStock;
import model.Stock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StockFactoryTest {

    @Test
    public void testCreateMainStock() {
        Stock stock = StockFactory.createStock(StockFactory.StockType.MAIN);
        assertNotNull(stock);
        assertTrue(stock instanceof MainStock);
    }

    @Test
    public void testCreateShelfStock() {
        Stock stock = StockFactory.createStock(StockFactory.StockType.SHELF);
        assertNotNull(stock);
        assertTrue(stock instanceof ShelfStock);
    }

    @Test
    public void testInvalidStockType() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            StockFactory.createStock(null);
        });
        assertEquals("Invalid stock type", exception.getMessage());
    }
}
