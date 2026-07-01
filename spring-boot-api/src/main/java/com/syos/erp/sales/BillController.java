package com.syos.erp.sales;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final SalesService salesService;

    public BillController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/{serialNumber}")
    public BillResponse findBySerialNumber(@PathVariable Long serialNumber) {
        return salesService.findBillBySerialNumber(serialNumber);
    }
}
