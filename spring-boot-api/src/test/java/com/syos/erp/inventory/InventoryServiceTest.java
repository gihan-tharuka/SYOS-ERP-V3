package com.syos.erp.inventory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.syos.erp.common.error.BadRequestException;
import com.syos.erp.common.error.ResourceNotFoundException;
import com.syos.erp.product.ProductRepository;
import com.syos.erp.supplier.SupplierRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private MainStockBatchRepository mainStockBatchRepository;

    @Mock
    private ShelfStockRepository shelfStockRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService(
                mainStockBatchRepository,
                shelfStockRepository,
                productRepository,
                supplierRepository
        );
    }

    @Test
    void mainStockCreationRejectsInvalidProductId() {
        MainStockBatchRequest request = new MainStockBatchRequest(
                999L,
                1L,
                "BATCH-001",
                LocalDate.of(2026, 1, 1),
                BigDecimal.valueOf(100),
                10,
                LocalDate.of(2026, 12, 31)
        );
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.createMainStockBatch(request));
    }

    @Test
    void mainStockCreationRejectsInvalidSupplierId() {
        MainStockBatchRequest request = new MainStockBatchRequest(
                1L,
                999L,
                "BATCH-001",
                LocalDate.of(2026, 1, 1),
                BigDecimal.valueOf(100),
                10,
                LocalDate.of(2026, 12, 31)
        );
        when(productRepository.findById(1L)).thenReturn(Optional.of(new com.syos.erp.product.Product()));
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.createMainStockBatch(request));
    }

    @Test
    void mainStockCreationRejectsExpiryBeforePurchaseDate() {
        MainStockBatchRequest request = new MainStockBatchRequest(
                1L,
                1L,
                "BATCH-001",
                LocalDate.of(2026, 1, 10),
                BigDecimal.valueOf(100),
                10,
                LocalDate.of(2026, 1, 1)
        );

        assertThrows(BadRequestException.class, () -> inventoryService.createMainStockBatch(request));
    }

    @Test
    void shelfStockRejectsQuantityGreaterThanCapacity() {
        ShelfStockRequest request = new ShelfStockRequest(1L, 10, 11);

        assertThrows(BadRequestException.class, () -> inventoryService.createShelfStock(request));
    }
}
