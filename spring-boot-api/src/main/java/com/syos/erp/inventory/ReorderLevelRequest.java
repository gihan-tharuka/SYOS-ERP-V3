package com.syos.erp.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ReorderLevelRequest(
        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Reorder threshold is required")
        @Min(value = 0, message = "Reorder threshold must be zero or positive")
        Integer thresholdQuantity,

        @NotNull(message = "Total stock is required")
        @Min(value = 0, message = "Total stock must be zero or positive")
        Integer totalStock
) {
}
