package com.syos.erp.sales;

import java.math.BigDecimal;

public record BillItemResponse(
        Long id,
        Long productId,
        String productCode,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
}
