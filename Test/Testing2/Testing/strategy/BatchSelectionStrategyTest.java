package Testing2.Testing.strategy;

import model.BatchSelection;
import model.MainStock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.BatchSelectionStrategy;
import strategy.ClosestExpiryDateStrategy;
import strategy.MultipleBatchesStrategy;
import strategy.OldestBatchStrategy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class BatchSelectionStrategyTest {
    
    private MultipleBatchesStrategy multipleBatchesStrategy;
    private ClosestExpiryDateStrategy closestExpiryDateStrategy;
    private OldestBatchStrategy oldestBatchStrategy;
    private List<MainStock> testStocks;
    private MainStock stock1, stock2, stock3;

    @BeforeEach
    public void setUp() {
        multipleBatchesStrategy = new MultipleBatchesStrategy();
        closestExpiryDateStrategy = new ClosestExpiryDateStrategy();
        oldestBatchStrategy = new OldestBatchStrategy();
        
        // Create test stocks
        stock1 = new MainStock();
        stock1.setItemId(1);
        stock1.setQuantity(50);
        stock1.setBatchCode("BATCH001");
        stock1.setPurchaseDate(java.sql.Date.valueOf("2024-01-01"));
        stock1.setExpiryDate(java.sql.Date.valueOf("2024-06-01"));
        
        stock2 = new MainStock();
        stock2.setItemId(1);
        stock2.setQuantity(30);
        stock2.setBatchCode("BATCH002");
        stock2.setPurchaseDate(java.sql.Date.valueOf("2024-02-01"));
        stock2.setExpiryDate(java.sql.Date.valueOf("2024-05-01"));
        
        stock3 = new MainStock();
        stock3.setItemId(1);
        stock3.setQuantity(20);
        stock3.setBatchCode("BATCH003");
        stock3.setPurchaseDate(java.sql.Date.valueOf("2024-03-01"));
        stock3.setExpiryDate(java.sql.Date.valueOf("2024-04-01"));
        
        testStocks = new ArrayList<>();
        testStocks.add(stock1);
        testStocks.add(stock2);
        testStocks.add(stock3);
    }

    @Test
    public void testMultipleBatchesStrategySelectBatches() {
        List<BatchSelection> result = multipleBatchesStrategy.selectBatches(testStocks, 80);
        
        assertNotNull(result);
        assertEquals(2, result.size()); // Only 2 batches used (50 + 30 = 80)
        
        // Verify quantities are correctly allocated
        assertEquals(50, result.get(0).getReshelfQuantity());
        assertEquals(30, result.get(1).getReshelfQuantity());
        
        // Verify stock quantities are updated
        assertEquals(0, stock1.getQuantity());
        assertEquals(0, stock2.getQuantity());
        assertEquals(20, stock3.getQuantity()); // Should remain unchanged
    }

    @Test
    public void testMultipleBatchesStrategyExactQuantity() {
        List<BatchSelection> result = multipleBatchesStrategy.selectBatches(testStocks, 100);
        
        assertNotNull(result);
        assertEquals(3, result.size()); // All 3 batches used (50 + 30 + 20 = 100)
        
        // Verify all quantities are used
        assertEquals(50, result.get(0).getReshelfQuantity());
        assertEquals(30, result.get(1).getReshelfQuantity());
        assertEquals(20, result.get(2).getReshelfQuantity());
        
        // Verify all stock quantities are zero
        assertEquals(0, stock1.getQuantity());
        assertEquals(0, stock2.getQuantity());
        assertEquals(0, stock3.getQuantity());
    }

    @Test
    public void testMultipleBatchesStrategyLessThanAvailable() {
        List<BatchSelection> result = multipleBatchesStrategy.selectBatches(testStocks, 25);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        
        // Verify only first batch is used
        assertEquals(25, result.get(0).getReshelfQuantity());
        assertEquals(25, stock1.getQuantity()); // 50 - 25
        assertEquals(30, stock2.getQuantity()); // Unchanged
        assertEquals(20, stock3.getQuantity()); // Unchanged
    }

    @Test
    public void testClosestExpiryDateStrategySelectBatches() {
        List<BatchSelection> result = closestExpiryDateStrategy.selectBatches(testStocks, 80);
        
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // Verify order: stock3 (Apr), stock2 (May), stock1 (Jun)
        assertEquals("BATCH003", result.get(0).getBatch().getBatchCode());
        assertEquals("BATCH002", result.get(1).getBatch().getBatchCode());
        assertEquals("BATCH001", result.get(2).getBatch().getBatchCode());
        
        // Verify quantities
        assertEquals(20, result.get(0).getReshelfQuantity());
        assertEquals(30, result.get(1).getReshelfQuantity());
        assertEquals(30, result.get(2).getReshelfQuantity());
    }

    @Test
    public void testClosestExpiryDateStrategySorting() {
        List<BatchSelection> result = closestExpiryDateStrategy.selectBatches(testStocks, 100);
        
        // Verify sorting by expiry date (closest first)
        java.util.Date firstExpiry = result.get(0).getBatch().getExpiryDate();
        java.util.Date secondExpiry = result.get(1).getBatch().getExpiryDate();
        java.util.Date thirdExpiry = result.get(2).getBatch().getExpiryDate();
        
        assertTrue(firstExpiry.before(secondExpiry));
        assertTrue(secondExpiry.before(thirdExpiry));
    }

    @Test
    public void testOldestBatchStrategySelectBatches() {
        List<BatchSelection> result = oldestBatchStrategy.selectBatches(testStocks, 80);
        
        assertNotNull(result);
        assertEquals(2, result.size()); // Only 2 batches used (50 + 30 = 80)
        
        // Verify order: stock1 (Jan), stock2 (Feb), stock3 (Mar)
        assertEquals("BATCH001", result.get(0).getBatch().getBatchCode());
        assertEquals("BATCH002", result.get(1).getBatch().getBatchCode());
        
        // Verify quantities
        assertEquals(50, result.get(0).getReshelfQuantity());
        assertEquals(30, result.get(1).getReshelfQuantity());
    }

    @Test
    public void testOldestBatchStrategySorting() {
        List<BatchSelection> result = oldestBatchStrategy.selectBatches(testStocks, 100);
        
        // Verify sorting by purchase date (oldest first)
        java.util.Date firstPurchase = result.get(0).getBatch().getPurchaseDate();
        java.util.Date secondPurchase = result.get(1).getBatch().getPurchaseDate();
        java.util.Date thirdPurchase = result.get(2).getBatch().getPurchaseDate();
        
        assertTrue(firstPurchase.before(secondPurchase));
        assertTrue(secondPurchase.before(thirdPurchase));
    }

    @Test
    public void testStrategyWithEmptyList() {
        List<MainStock> emptyList = new ArrayList<>();
        
        List<BatchSelection> result1 = multipleBatchesStrategy.selectBatches(emptyList, 50);
        List<BatchSelection> result2 = closestExpiryDateStrategy.selectBatches(emptyList, 50);
        List<BatchSelection> result3 = oldestBatchStrategy.selectBatches(emptyList, 50);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(0, result1.size());
        assertEquals(0, result2.size());
        assertEquals(0, result3.size());
    }

    @Test
    public void testStrategyWithZeroQuantity() {
        List<BatchSelection> result1 = multipleBatchesStrategy.selectBatches(testStocks, 0);
        List<BatchSelection> result2 = closestExpiryDateStrategy.selectBatches(testStocks, 0);
        List<BatchSelection> result3 = oldestBatchStrategy.selectBatches(testStocks, 0);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(0, result1.size());
        assertEquals(0, result2.size());
        assertEquals(0, result3.size());
        
        // Verify stock quantities remain unchanged
        assertEquals(50, stock1.getQuantity());
        assertEquals(30, stock2.getQuantity());
        assertEquals(20, stock3.getQuantity());
    }

    @Test
    public void testStrategyWithNegativeQuantity() {
        List<BatchSelection> result1 = multipleBatchesStrategy.selectBatches(testStocks, -10);
        List<BatchSelection> result2 = closestExpiryDateStrategy.selectBatches(testStocks, -10);
        List<BatchSelection> result3 = oldestBatchStrategy.selectBatches(testStocks, -10);
        
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals(0, result1.size());
        assertEquals(0, result2.size());
        assertEquals(0, result3.size());
    }

    @Test
    public void testStrategyInterfaceImplementation() {
        // Test that all strategies implement BatchSelectionStrategy interface
        assertTrue(multipleBatchesStrategy instanceof BatchSelectionStrategy);
        assertTrue(closestExpiryDateStrategy instanceof BatchSelectionStrategy);
        assertTrue(oldestBatchStrategy instanceof BatchSelectionStrategy);
    }

    @Test
    public void testBatchSelectionCreation() {
        List<BatchSelection> result = multipleBatchesStrategy.selectBatches(testStocks, 25);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        
        BatchSelection selection = result.get(0);
        assertNotNull(selection);
        assertEquals(stock1, selection.getBatch());
        assertEquals(25, selection.getReshelfQuantity());
    }

    @Test
    public void testStrategyWithNullList() {
        assertThrows(NullPointerException.class, () -> {
            multipleBatchesStrategy.selectBatches(null, 50);
        });
        
        assertThrows(NullPointerException.class, () -> {
            closestExpiryDateStrategy.selectBatches(null, 50);
        });
        
        assertThrows(NullPointerException.class, () -> {
            oldestBatchStrategy.selectBatches(null, 50);
        });
    }
} 