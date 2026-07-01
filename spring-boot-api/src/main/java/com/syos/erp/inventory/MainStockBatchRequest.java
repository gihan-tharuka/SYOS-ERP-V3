package com.syos.erp.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MainStockBatchRequest(
        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Supplier id is required")
        Long supplierId,

        @NotBlank(message = "Batch code is required")
        @Size(max = 100)
        String batchCode,

        @NotNull(message = "Purchase date is required")
        LocalDate purchaseDate,

        @NotNull(message = "Purchase price is required")
        @DecimalMin(value = "0.00", message = "Purchase price must be zero or positive")
        @Digits(integer = 10, fraction = 2)
        BigDecimal purchasePrice,

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Stock quantity must be zero or positive")
        Integer quantity,

        LocalDate expiryDate
) {
}
