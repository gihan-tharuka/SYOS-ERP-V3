package com.syos.erp.inventory;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.syos.erp.common.error.BadRequestException;
import com.syos.erp.common.error.ResourceNotFoundException;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;
import com.syos.erp.supplier.Supplier;
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

    @Test
    void reshelvingUpdatesMainAndShelfQuantities() {
        Product product = product(1L, "RICE001", "White rice");
        MainStockBatch batch = mainStockBatch(10L, product, 25);
        ShelfStock shelfStock = shelfStock(20L, product, 50, 10);
        ReshelveStockRequest request = new ReshelveStockRequest(10L, 20L, 15);

        when(mainStockBatchRepository.findById(10L)).thenReturn(Optional.of(batch));
        when(shelfStockRepository.findById(20L)).thenReturn(Optional.of(shelfStock));
        when(mainStockBatchRepository.save(any(MainStockBatch.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(shelfStockRepository.save(any(ShelfStock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReshelveStockResponse response = inventoryService.reshelve(request);

        assertEquals(10, response.updatedMainStockQuantity());
        assertEquals(25, response.updatedShelfStockQuantity());
        assertEquals(15, response.movedQuantity());
    }

    @Test
    void reshelvingRejectsMismatchedProducts() {
        MainStockBatch batch = mainStockBatch(10L, product(1L, "RICE001", "White rice"), 25);
        ShelfStock shelfStock = shelfStock(20L, product(2L, "WATER001", "Water"), 50, 10);
        ReshelveStockRequest request = new ReshelveStockRequest(10L, 20L, 5);

        when(mainStockBatchRepository.findById(10L)).thenReturn(Optional.of(batch));
        when(shelfStockRepository.findById(20L)).thenReturn(Optional.of(shelfStock));

        assertThrows(BadRequestException.class, () -> inventoryService.reshelve(request));
    }

    @Test
    void reshelvingRejectsInsufficientMainStock() {
        Product product = product(1L, "RICE001", "White rice");
        MainStockBatch batch = mainStockBatch(10L, product, 4);
        ShelfStock shelfStock = shelfStock(20L, product, 50, 10);
        ReshelveStockRequest request = new ReshelveStockRequest(10L, 20L, 5);

        when(mainStockBatchRepository.findById(10L)).thenReturn(Optional.of(batch));
        when(shelfStockRepository.findById(20L)).thenReturn(Optional.of(shelfStock));

        assertThrows(BadRequestException.class, () -> inventoryService.reshelve(request));
    }

    @Test
    void reshelvingRejectsShelfCapacityOverflow() {
        Product product = product(1L, "RICE001", "White rice");
        MainStockBatch batch = mainStockBatch(10L, product, 25);
        ShelfStock shelfStock = shelfStock(20L, product, 12, 10);
        ReshelveStockRequest request = new ReshelveStockRequest(10L, 20L, 5);

        when(mainStockBatchRepository.findById(10L)).thenReturn(Optional.of(batch));
        when(shelfStockRepository.findById(20L)).thenReturn(Optional.of(shelfStock));

        assertThrows(BadRequestException.class, () -> inventoryService.reshelve(request));
    }

    private Product product(Long id, String code, String name) {
        Product product = new Product();
        product.setId(id);
        product.setProductCode(code);
        product.setProductName(name);
        product.setPrice(BigDecimal.TEN);
        product.setDiscount(BigDecimal.ZERO);
        return product;
    }

    private MainStockBatch mainStockBatch(Long id, Product product, int quantity) {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        supplier.setCompanyName("Supplier");

        MainStockBatch batch = new MainStockBatch();
        batch.setId(id);
        batch.setProduct(product);
        batch.setSupplier(supplier);
        batch.setBatchCode("BATCH001");
        batch.setPurchaseDate(LocalDate.of(2026, 1, 1));
        batch.setPurchasePrice(BigDecimal.ONE);
        batch.setQuantity(quantity);
        return batch;
    }

    private ShelfStock shelfStock(Long id, Product product, int capacity, int currentQuantity) {
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setId(id);
        shelfStock.setProduct(product);
        shelfStock.setShelfCapacity(capacity);
        shelfStock.setCurrentQuantity(currentQuantity);
        return shelfStock;
    }
}
