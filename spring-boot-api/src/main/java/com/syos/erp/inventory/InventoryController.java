package com.syos.erp.inventory;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/inventory")
@Tag(name = "Inventory", description = "Main stock, shelf stock, and reshelving workflows")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/main-stock")
    public List<MainStockBatchResponse> findAllMainStockBatches() {
        return inventoryService.findAllMainStockBatches();
    }

    @GetMapping("/main-stock/{id}")
    public MainStockBatchResponse findMainStockBatchById(@PathVariable Long id) {
        return inventoryService.findMainStockBatchById(id);
    }

    @PostMapping("/main-stock")
    @ResponseStatus(HttpStatus.CREATED)
    public MainStockBatchResponse createMainStockBatch(@Valid @RequestBody MainStockBatchRequest request) {
        return inventoryService.createMainStockBatch(request);
    }

    @PutMapping("/main-stock/{id}")
    public MainStockBatchResponse updateMainStockBatch(
            @PathVariable Long id,
            @Valid @RequestBody MainStockBatchRequest request
    ) {
        return inventoryService.updateMainStockBatch(id, request);
    }

    @DeleteMapping("/main-stock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMainStockBatch(@PathVariable Long id) {
        inventoryService.deleteMainStockBatch(id);
    }

    @GetMapping("/shelf-stock")
    public List<ShelfStockResponse> findAllShelfStocks() {
        return inventoryService.findAllShelfStocks();
    }

    @GetMapping("/shelf-stock/{id}")
    public ShelfStockResponse findShelfStockById(@PathVariable Long id) {
        return inventoryService.findShelfStockById(id);
    }

    @PostMapping("/shelf-stock")
    @ResponseStatus(HttpStatus.CREATED)
    public ShelfStockResponse createShelfStock(@Valid @RequestBody ShelfStockRequest request) {
        return inventoryService.createShelfStock(request);
    }

    @PutMapping("/shelf-stock/{id}")
    public ShelfStockResponse updateShelfStock(@PathVariable Long id, @Valid @RequestBody ShelfStockRequest request) {
        return inventoryService.updateShelfStock(id, request);
    }

    @DeleteMapping("/shelf-stock/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShelfStock(@PathVariable Long id) {
        inventoryService.deleteShelfStock(id);
    }

    @PostMapping("/shelf-stock/reshelve")
    public ReshelveStockResponse reshelve(@Valid @RequestBody ReshelveStockRequest request) {
        return inventoryService.reshelve(request);
    }
}
