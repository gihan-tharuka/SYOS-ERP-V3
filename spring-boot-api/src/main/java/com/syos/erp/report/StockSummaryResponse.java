package com.syos.erp.report;

public record StockSummaryResponse(
        Long productId,
        String productCode,
        String productName,
        Integer totalMainStockQuantity,
        Integer totalShelfStockQuantity,
        Integer totalAvailableQuantity,
        Integer reorderThreshold,
        StockStatus status
) {
}
