package com.syos.erp.report;

public record ReorderAlertResponse(
        Long productId,
        String productCode,
        String productName,
        Integer thresholdQuantity,
        Integer currentTotalStock,
        StockStatus status
) {
}
