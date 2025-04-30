package Testing2.model;

import model.ShelfStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

public class ShelfStockTest {

    private ShelfStock shelfStock;

    @BeforeEach
    public void setUp() {
        shelfStock = new ShelfStock();
    }

    @Test
    public void testGetSetShelfCapacity() {
        shelfStock.setShelfCapacity(100);
        assertEquals(100, shelfStock.getShelfCapacity());
    }

    @Test
    public void testGetSetCurrentQuantity() {
        shelfStock.setCurrentQuantity(50);
        assertEquals(50, shelfStock.getCurrentQuantity());
    }

    @Test
    public void testGetSetStockId() {
        shelfStock.setStockId(1);
        assertEquals(1, shelfStock.getStockId());
    }

    @Test
    public void testGetSetItemId() {
        shelfStock.setItemId(1);
        assertEquals(1, shelfStock.getItemId());
    }

    @Test
    public void testGetSetBatchCode() {
        shelfStock.setBatchCode("batch1");
        assertEquals("batch1", shelfStock.getBatchCode());
    }

    @Test
    public void testGetSetQuantity() {
        shelfStock.setQuantity(10);
        assertEquals(10, shelfStock.getQuantity());
    }
}
