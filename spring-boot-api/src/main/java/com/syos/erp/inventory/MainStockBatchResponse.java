package com.syos.erp.inventory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MainStockBatchResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        Long supplierId,
        String supplierName,
        String batchCode,
        LocalDate purchaseDate,
        BigDecimal purchasePrice,
        Integer quantity,
        LocalDate expiryDate
) {
}
