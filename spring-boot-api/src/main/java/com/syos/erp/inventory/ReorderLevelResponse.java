package com.syos.erp.inventory;

public record ReorderLevelResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        Integer thresholdQuantity,
        Integer totalStock,
        boolean reorderRequired
) {
}
