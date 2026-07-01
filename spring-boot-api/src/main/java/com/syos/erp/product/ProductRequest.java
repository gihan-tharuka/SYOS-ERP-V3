package com.syos.erp.product;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(
        @NotBlank(message = "Product code is required")
        @Size(max = 100)
        String productCode,

        @NotBlank(message = "Product name is required")
        @Size(max = 255)
        String productName,

        @NotNull(message = "Product price is required")
        @DecimalMin(value = "0.00", inclusive = false, message = "Product price must be positive")
        @Digits(integer = 10, fraction = 2)
        BigDecimal price,

        @NotNull(message = "Product discount is required")
        @DecimalMin(value = "0.00", message = "Product discount must be zero or positive")
        @Digits(integer = 10, fraction = 2)
        BigDecimal discount,

        @Size(max = 500)
        String imagePath
) {
}
