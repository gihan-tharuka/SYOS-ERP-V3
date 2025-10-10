package Testing2.Testing.factory;

import factory.StockFactory;
import model.MainStock;
import model.ShelfStock;
import model.Stock;
import model.WebStock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StockFactoryTest {

    @Test
    public void testCreateStockMain() {
        Stock result = StockFactory.createStock(StockFactory.StockType.MAIN);

        assertNotNull(result);
        assertTrue(result instanceof MainStock);
    }

    @Test
    public void testCreateStockShelf() {
        Stock result = StockFactory.createStock(StockFactory.StockType.SHELF);

        assertNotNull(result);
        assertTrue(result instanceof ShelfStock);
    }

    @Test
    public void testCreateStockWeb() {
        Stock result = StockFactory.createStock(StockFactory.StockType.WEB);

        assertNotNull(result);
        assertTrue(result instanceof WebStock);
    }

    @Test
    public void testCreateStockNullType() {
        assertThrows(IllegalArgumentException.class, () -> {
            StockFactory.createStock(null);
        });
    }

    @Test
    public void testStockTypeEnumValues() {
        StockFactory.StockType[] types = StockFactory.StockType.values();
        assertEquals(3, types.length);
        
        boolean hasMain = false, hasShelf = false, hasWeb = false;
        for (StockFactory.StockType type : types) {
            switch (type) {
                case MAIN:
                    hasMain = true;
                    break;
                case SHELF:
                    hasShelf = true;
                    break;
                case WEB:
                    hasWeb = true;
                    break;
            }
        }
        
        assertTrue(hasMain);
        assertTrue(hasShelf);
        assertTrue(hasWeb);
    }

    @Test
    public void testCreateStockAllTypes() {
        // Test all stock types in one test for full coverage
        Stock mainStock = StockFactory.createStock(StockFactory.StockType.MAIN);
        Stock shelfStock = StockFactory.createStock(StockFactory.StockType.SHELF);
        Stock webStock = StockFactory.createStock(StockFactory.StockType.WEB);

        assertNotNull(mainStock);
        assertNotNull(shelfStock);
        assertNotNull(webStock);
        
        assertTrue(mainStock instanceof MainStock);
        assertTrue(shelfStock instanceof ShelfStock);
        assertTrue(webStock instanceof WebStock);
    }
} 