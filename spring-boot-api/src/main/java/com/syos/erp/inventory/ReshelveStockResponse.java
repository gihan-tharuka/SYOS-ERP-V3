package com.syos.erp.inventory;

public record ReshelveStockResponse(
        Long productId,
        String productCode,
        String productName,
        Long mainStockBatchId,
        Integer updatedMainStockQuantity,
        Long shelfStockId,
        Integer updatedShelfStockQuantity,
        Integer movedQuantity
) {
}
