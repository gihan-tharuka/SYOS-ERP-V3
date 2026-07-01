package com.syos.erp.report;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.syos.erp.inventory.MainStockBatchRepository;
import com.syos.erp.inventory.ReorderLevel;
import com.syos.erp.inventory.ReorderLevelRepository;
import com.syos.erp.inventory.ShelfStockRepository;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReportService {

    private final ProductRepository productRepository;
    private final MainStockBatchRepository mainStockBatchRepository;
    private final ShelfStockRepository shelfStockRepository;
    private final ReorderLevelRepository reorderLevelRepository;

    public ReportService(
            ProductRepository productRepository,
            MainStockBatchRepository mainStockBatchRepository,
            ShelfStockRepository shelfStockRepository,
            ReorderLevelRepository reorderLevelRepository
    ) {
        this.productRepository = productRepository;
        this.mainStockBatchRepository = mainStockBatchRepository;
        this.shelfStockRepository = shelfStockRepository;
        this.reorderLevelRepository = reorderLevelRepository;
    }

    public List<ReorderAlertResponse> getReorderAlerts() {
        return reorderLevelRepository.findAll().stream()
                .map(this::toReorderAlert)
                .filter(alert -> alert.currentTotalStock() <= alert.thresholdQuantity())
                .toList();
    }

    public List<StockSummaryResponse> getStockSummary() {
        Map<Long, ReorderLevel> reorderLevelsByProductId = reorderLevelRepository.findAll().stream()
                .collect(Collectors.toMap(level -> level.getProduct().getId(), Function.identity()));

        return productRepository.findAll().stream()
                .map(product -> toStockSummary(product, reorderLevelsByProductId.get(product.getId())))
                .toList();
    }

    private ReorderAlertResponse toReorderAlert(ReorderLevel reorderLevel) {
        Product product = reorderLevel.getProduct();
        int currentTotalStock = calculateCurrentTotalStock(product.getId());
        return new ReorderAlertResponse(
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                reorderLevel.getThresholdQuantity(),
                currentTotalStock,
                status(currentTotalStock, reorderLevel.getThresholdQuantity())
        );
    }

    private StockSummaryResponse toStockSummary(Product product, ReorderLevel reorderLevel) {
        int mainStockQuantity = Math.toIntExact(mainStockBatchRepository.sumQuantityByProductId(product.getId()));
        int shelfStockQuantity = Math.toIntExact(shelfStockRepository.sumCurrentQuantityByProductId(product.getId()));
        int totalAvailableQuantity = mainStockQuantity + shelfStockQuantity;
        Integer reorderThreshold = reorderLevel == null ? null : reorderLevel.getThresholdQuantity();

        return new StockSummaryResponse(
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                mainStockQuantity,
                shelfStockQuantity,
                totalAvailableQuantity,
                reorderThreshold,
                reorderThreshold == null ? StockStatus.OK : status(totalAvailableQuantity, reorderThreshold)
        );
    }

    private int calculateCurrentTotalStock(Long productId) {
        return Math.toIntExact(
                mainStockBatchRepository.sumQuantityByProductId(productId)
                        + shelfStockRepository.sumCurrentQuantityByProductId(productId)
        );
    }

    private StockStatus status(int currentTotalStock, int thresholdQuantity) {
        return currentTotalStock <= thresholdQuantity ? StockStatus.LOW_STOCK : StockStatus.OK;
    }
}
