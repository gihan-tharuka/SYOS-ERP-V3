package com.syos.erp.product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String productCode,
        String productName,
        BigDecimal price,
        BigDecimal discount,
        String imagePath
) {
}
