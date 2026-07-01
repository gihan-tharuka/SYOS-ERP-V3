package com.syos.erp.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ShelfStockRequest(
        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Shelf capacity is required")
        @Positive(message = "Shelf capacity must be positive")
        Integer shelfCapacity,

        @NotNull(message = "Current quantity is required")
        @Min(value = 0, message = "Shelf current quantity must be zero or positive")
        Integer currentQuantity
) {
}
