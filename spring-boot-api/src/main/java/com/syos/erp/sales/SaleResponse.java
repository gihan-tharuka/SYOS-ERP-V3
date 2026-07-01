package com.syos.erp.sales;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleResponse(
        Long saleId,
        String cashierName,
        LocalDateTime saleDateTime,
        Long billSerialNumber,
        BigDecimal totalAmount,
        List<BillItemResponse> items
) {
}
