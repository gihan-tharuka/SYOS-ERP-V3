package com.syos.erp.report;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import com.syos.erp.inventory.MainStockBatchRepository;
import com.syos.erp.inventory.ReorderLevel;
import com.syos.erp.inventory.ReorderLevelRepository;
import com.syos.erp.inventory.ShelfStockRepository;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MainStockBatchRepository mainStockBatchRepository;

    @Mock
    private ShelfStockRepository shelfStockRepository;

    @Mock
    private ReorderLevelRepository reorderLevelRepository;

    private ReportService reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportService(
                productRepository,
                mainStockBatchRepository,
                shelfStockRepository,
                reorderLevelRepository
        );
    }

    @Test
    void reorderAlertsReturnLowStockProducts() {
        Product product = product(1L, "RICE001", "White rice");
        ReorderLevel level = reorderLevel(10L, product, 50, 0);

        when(reorderLevelRepository.findAll()).thenReturn(List.of(level));
        when(mainStockBatchRepository.sumQuantityByProductId(1L)).thenReturn(20L);
        when(shelfStockRepository.sumCurrentQuantityByProductId(1L)).thenReturn(30L);

        List<ReorderAlertResponse> alerts = reportService.getReorderAlerts();

        assertEquals(1, alerts.size());
        assertEquals(50, alerts.get(0).currentTotalStock());
        assertEquals(StockStatus.LOW_STOCK, alerts.get(0).status());
    }

    @Test
    void stockReportReturnsCalculatedTotals() {
        Product product = product(1L, "RICE001", "White rice");
        ReorderLevel level = reorderLevel(10L, product, 50, 0);

        when(productRepository.findAll()).thenReturn(List.of(product));
        when(reorderLevelRepository.findAll()).thenReturn(List.of(level));
        when(mainStockBatchRepository.sumQuantityByProductId(1L)).thenReturn(60L);
        when(shelfStockRepository.sumCurrentQuantityByProductId(1L)).thenReturn(20L);

        List<StockSummaryResponse> summaries = reportService.getStockSummary();

        assertEquals(1, summaries.size());
        assertEquals(60, summaries.get(0).totalMainStockQuantity());
        assertEquals(20, summaries.get(0).totalShelfStockQuantity());
        assertEquals(80, summaries.get(0).totalAvailableQuantity());
        assertEquals(StockStatus.OK, summaries.get(0).status());
    }

    private Product product(Long id, String code, String name) {
        Product product = new Product();
        product.setId(id);
        product.setProductCode(code);
        product.setProductName(name);
        product.setPrice(BigDecimal.TEN);
        product.setDiscount(BigDecimal.ZERO);
        return product;
    }

    private ReorderLevel reorderLevel(Long id, Product product, int threshold, int totalStock) {
        ReorderLevel reorderLevel = new ReorderLevel();
        reorderLevel.setId(id);
        reorderLevel.setProduct(product);
        reorderLevel.setThresholdQuantity(threshold);
        reorderLevel.setTotalStock(totalStock);
        return reorderLevel;
    }
}
