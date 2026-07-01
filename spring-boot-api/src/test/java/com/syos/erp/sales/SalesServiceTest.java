package com.syos.erp.sales;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.syos.erp.common.error.BadRequestException;
import com.syos.erp.common.error.ResourceNotFoundException;
import com.syos.erp.inventory.ShelfStock;
import com.syos.erp.inventory.ShelfStockRepository;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SalesServiceTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private BillRepository billRepository;

    @Mock
    private BillItemRepository billItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ShelfStockRepository shelfStockRepository;

    private SalesService salesService;

    @BeforeEach
    void setUp() {
        salesService = new SalesService(
                saleRepository,
                billRepository,
                billItemRepository,
                productRepository,
                shelfStockRepository
        );
    }

    @Test
    void successfulSaleCreatesBillAndDecrementsShelfStock() {
        Product product = product(1L, "ITEM001", "Laptop", BigDecimal.valueOf(1500));
        ShelfStock shelfStock = shelfStock(10L, product, 20, 5);
        CreateSaleRequest request = new CreateSaleRequest(
                "Demo Cashier",
                List.of(new CreateSaleItemRequest(1L, 2))
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(shelfStockRepository.findByProductIdOrderByIdAsc(1L)).thenReturn(List.of(shelfStock));
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale sale = invocation.getArgument(0);
            sale.setId(100L);
            return sale;
        });
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> {
            Bill bill = invocation.getArgument(0);
            bill.setSerialNumber(200L);
            return bill;
        });
        when(billItemRepository.save(any(BillItem.class))).thenAnswer(invocation -> {
            BillItem billItem = invocation.getArgument(0);
            billItem.setId(300L);
            return billItem;
        });

        SaleResponse response = salesService.createSale(request);

        assertEquals(100L, response.saleId());
        assertEquals(200L, response.billSerialNumber());
        assertEquals(BigDecimal.valueOf(3000), response.totalAmount());
        assertEquals(3, shelfStock.getCurrentQuantity());
        assertEquals(1, response.items().size());
        assertEquals("Laptop", response.items().get(0).productName());
    }

    @Test
    void saleRejectsMissingProduct() {
        CreateSaleRequest request = new CreateSaleRequest(
                "Demo Cashier",
                List.of(new CreateSaleItemRequest(999L, 1))
        );
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> salesService.createSale(request));
        verify(shelfStockRepository, never()).save(any(ShelfStock.class));
    }

    @Test
    void saleRejectsInsufficientShelfStock() {
        Product product = product(1L, "ITEM001", "Laptop", BigDecimal.valueOf(1500));
        ShelfStock shelfStock = shelfStock(10L, product, 20, 1);
        CreateSaleRequest request = new CreateSaleRequest(
                "Demo Cashier",
                List.of(new CreateSaleItemRequest(1L, 2))
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(shelfStockRepository.findByProductIdOrderByIdAsc(1L)).thenReturn(List.of(shelfStock));

        assertThrows(BadRequestException.class, () -> salesService.createSale(request));
        assertEquals(1, shelfStock.getCurrentQuantity());
        verify(shelfStockRepository, never()).save(any(ShelfStock.class));
    }

    @Test
    void failedSaleLeavesStockUnchanged() {
        Product product = product(1L, "ITEM001", "Laptop", BigDecimal.valueOf(1500));
        ShelfStock shelfStock = shelfStock(10L, product, 20, 1);
        CreateSaleRequest request = new CreateSaleRequest(
                "Demo Cashier",
                List.of(new CreateSaleItemRequest(1L, 3))
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(shelfStockRepository.findByProductIdOrderByIdAsc(1L)).thenReturn(List.of(shelfStock));

        assertThrows(BadRequestException.class, () -> salesService.createSale(request));
        assertEquals(1, shelfStock.getCurrentQuantity());
    }

    @Test
    void billLookupReturnsExpectedItems() {
        Product product = product(1L, "ITEM001", "Laptop", BigDecimal.valueOf(1500));
        Sale sale = sale(100L);
        Bill bill = bill(200L, sale, BigDecimal.valueOf(3000));
        BillItem billItem = billItem(300L, bill, product, 2, BigDecimal.valueOf(1500));

        when(billRepository.findById(200L)).thenReturn(Optional.of(bill));
        when(billItemRepository.findByBillSerialNumberOrderByIdAsc(200L)).thenReturn(List.of(billItem));

        BillResponse response = salesService.findBillBySerialNumber(200L);

        assertEquals(100L, response.saleId());
        assertEquals(200L, response.billSerialNumber());
        assertEquals(1, response.items().size());
        assertEquals("ITEM001", response.items().get(0).productCode());
        assertEquals(BigDecimal.valueOf(3000), response.items().get(0).lineTotal());
    }

    private Product product(Long id, String code, String name, BigDecimal price) {
        Product product = new Product();
        product.setId(id);
        product.setProductCode(code);
        product.setProductName(name);
        product.setPrice(price);
        product.setDiscount(BigDecimal.ZERO);
        return product;
    }

    private ShelfStock shelfStock(Long id, Product product, int capacity, int quantity) {
        ShelfStock shelfStock = new ShelfStock();
        shelfStock.setId(id);
        shelfStock.setProduct(product);
        shelfStock.setShelfCapacity(capacity);
        shelfStock.setCurrentQuantity(quantity);
        return shelfStock;
    }

    private Sale sale(Long id) {
        Sale sale = new Sale();
        sale.setId(id);
        sale.setCashierName("Demo Cashier");
        sale.setSaleDate(LocalDate.of(2026, 7, 1));
        sale.setSaleTimestamp(LocalDateTime.of(2026, 7, 1, 10, 30));
        sale.setTransactionType("POS");
        return sale;
    }

    private Bill bill(Long serialNumber, Sale sale, BigDecimal total) {
        Bill bill = new Bill();
        bill.setSerialNumber(serialNumber);
        bill.setSale(sale);
        bill.setTotalPrice(total);
        bill.setDiscount(BigDecimal.ZERO);
        bill.setCashTendered(total);
        bill.setChangeAmount(BigDecimal.ZERO);
        bill.setBillDate(sale.getSaleDate());
        bill.setPaymentMethod("CASH");
        return bill;
    }

    private BillItem billItem(Long id, Bill bill, Product product, int quantity, BigDecimal unitPrice) {
        BillItem billItem = new BillItem();
        billItem.setId(id);
        billItem.setBill(bill);
        billItem.setProduct(product);
        billItem.setProductNameSnapshot(product.getProductName());
        billItem.setQuantity(quantity);
        billItem.setUnitPrice(unitPrice);
        billItem.setLineTotal(unitPrice.multiply(BigDecimal.valueOf(quantity)));
        return billItem;
    }
}
