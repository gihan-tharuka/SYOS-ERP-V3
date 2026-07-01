package com.syos.erp.sales;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public List<SaleResponse> findAll() {
        return salesService.findAllSales();
    }

    @GetMapping("/{id}")
    public SaleResponse findById(@PathVariable Long id) {
        return salesService.findSaleById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponse create(@Valid @RequestBody CreateSaleRequest request) {
        return salesService.createSale(request);
    }
}
