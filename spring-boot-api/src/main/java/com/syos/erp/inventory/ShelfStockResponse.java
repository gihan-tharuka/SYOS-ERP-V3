package com.syos.erp.inventory;

public record ShelfStockResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        Integer shelfCapacity,
        Integer currentQuantity
) {
}
