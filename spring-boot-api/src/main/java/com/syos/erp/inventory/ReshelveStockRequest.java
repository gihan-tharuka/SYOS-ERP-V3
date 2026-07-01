package com.syos.erp.inventory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReshelveStockRequest(
        @NotNull(message = "Main stock batch id is required")
        Long mainStockBatchId,

        @NotNull(message = "Shelf stock id is required")
        Long shelfStockId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Integer quantity
) {
}
