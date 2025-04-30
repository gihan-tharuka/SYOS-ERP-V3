package Testing2.model;

import model.MainStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainStockTest {

    private MainStock mainStock;

    @BeforeEach
    public void setUp() {
        mainStock = new MainStock();
    }

    @Test
    public void testGetSetSupplierId() {
        mainStock.setSupplierId(1);
        assertEquals(1, mainStock.getSupplierId());
    }

    @Test
    public void testGetSetPurchaseDate() {
        Date purchaseDate = new Date();
        mainStock.setPurchaseDate(purchaseDate);
        assertEquals(purchaseDate, mainStock.getPurchaseDate());
    }

    @Test
    public void testGetSetPurchasePrice() {
        mainStock.setPurchasePrice(100.0);
        assertEquals(100.0, mainStock.getPurchasePrice());
    }

    @Test
    public void testGetSetExpiryDate() {
        Date expiryDate = new Date();
        mainStock.setExpiryDate(expiryDate);
        assertEquals(expiryDate, mainStock.getExpiryDate());
    }

    @Test
    public void testGetSetStockId() {
        mainStock.setStockId(1);
        assertEquals(1, mainStock.getStockId());
    }

    @Test
    public void testGetSetItemId() {
        mainStock.setItemId(1);
        assertEquals(1, mainStock.getItemId());
    }

    @Test
    public void testGetSetBatchCode() {
        mainStock.setBatchCode("batch1");
        assertEquals("batch1", mainStock.getBatchCode());
    }

    @Test
    public void testGetSetQuantity() {
        mainStock.setQuantity(10);
        assertEquals(10, mainStock.getQuantity());
    }
}
