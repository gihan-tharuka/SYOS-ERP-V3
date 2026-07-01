package com.syos.erp.sales;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CreateSaleRequest(
        @NotBlank(message = "Cashier name is required")
        @Size(max = 255)
        String cashierName,

        @NotEmpty(message = "Sale must contain at least one item")
        List<@Valid CreateSaleItemRequest> items
) {
}
