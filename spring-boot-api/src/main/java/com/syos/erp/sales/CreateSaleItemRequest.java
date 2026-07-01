package com.syos.erp.sales;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateSaleItemRequest(
        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Integer quantity
) {
}
