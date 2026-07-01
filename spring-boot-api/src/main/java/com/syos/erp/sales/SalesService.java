package com.syos.erp.sales;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.syos.erp.common.error.BadRequestException;
import com.syos.erp.common.error.ResourceNotFoundException;
import com.syos.erp.inventory.ShelfStock;
import com.syos.erp.inventory.ShelfStockRepository;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SalesService {

    private static final String POS_TRANSACTION_TYPE = "POS";
    private static final String CASH_PAYMENT_METHOD = "CASH";

    private final SaleRepository saleRepository;
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final ProductRepository productRepository;
    private final ShelfStockRepository shelfStockRepository;

    public SalesService(
            SaleRepository saleRepository,
            BillRepository billRepository,
            BillItemRepository billItemRepository,
            ProductRepository productRepository,
            ShelfStockRepository shelfStockRepository
    ) {
        this.saleRepository = saleRepository;
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.productRepository = productRepository;
        this.shelfStockRepository = shelfStockRepository;
    }

    @Transactional
    public SaleResponse createSale(CreateSaleRequest request) {
        List<SaleLinePlan> saleLinePlans = planSale(request);
        BigDecimal totalAmount = saleLinePlans.stream()
                .map(SaleLinePlan::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime now = LocalDateTime.now();
        Sale sale = new Sale();
        sale.setCashierName(request.cashierName());
        sale.setSaleTimestamp(now);
        sale.setSaleDate(now.toLocalDate());
        sale.setTransactionType(POS_TRANSACTION_TYPE);
        Sale savedSale = saleRepository.save(sale);

        Bill bill = new Bill();
        bill.setSale(savedSale);
        bill.setTotalPrice(totalAmount);
        bill.setDiscount(BigDecimal.ZERO);
        bill.setCashTendered(totalAmount);
        bill.setChangeAmount(BigDecimal.ZERO);
        bill.setBillDate(LocalDate.now());
        bill.setPaymentMethod(CASH_PAYMENT_METHOD);
        Bill savedBill = billRepository.save(bill);

        List<BillItem> savedBillItems = new ArrayList<>();
        for (SaleLinePlan plan : saleLinePlans) {
            for (ShelfAllocation allocation : plan.allocations()) {
                ShelfStock shelfStock = allocation.shelfStock();
                shelfStock.setCurrentQuantity(shelfStock.getCurrentQuantity() - allocation.quantity());
                shelfStockRepository.save(shelfStock);
            }

            BillItem billItem = new BillItem();
            billItem.setBill(savedBill);
            billItem.setProduct(plan.product());
            billItem.setProductNameSnapshot(plan.product().getProductName());
            billItem.setQuantity(plan.quantity());
            billItem.setUnitPrice(plan.unitPrice());
            billItem.setLineTotal(plan.lineTotal());
            savedBillItems.add(billItemRepository.save(billItem));
        }

        return toSaleResponse(savedSale, savedBill, savedBillItems);
    }

    public List<SaleResponse> findAllSales() {
        return saleRepository.findAllByOrderByIdDesc().stream()
                .map(sale -> {
                    Bill bill = billRepository.findBySaleId(sale.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Bill for sale", sale.getId()));
                    List<BillItem> billItems = billItemRepository.findByBillSerialNumberOrderByIdAsc(bill.getSerialNumber());
                    return toSaleResponse(sale, bill, billItems);
                })
                .toList();
    }

    public SaleResponse findSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
        Bill bill = billRepository.findBySaleId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill for sale", id));
        List<BillItem> billItems = billItemRepository.findByBillSerialNumberOrderByIdAsc(bill.getSerialNumber());
        return toSaleResponse(sale, bill, billItems);
    }

    public BillResponse findBillBySerialNumber(Long serialNumber) {
        Bill bill = billRepository.findById(serialNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", serialNumber));
        List<BillItem> billItems = billItemRepository.findByBillSerialNumberOrderByIdAsc(serialNumber);
        return toBillResponse(bill, billItems);
    }

    private List<SaleLinePlan> planSale(CreateSaleRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new BadRequestException("Sale must contain at least one item");
        }

        Set<Long> productIds = new HashSet<>();
        List<SaleLinePlan> saleLinePlans = new ArrayList<>();

        for (CreateSaleItemRequest itemRequest : request.items()) {
            if (!productIds.add(itemRequest.productId())) {
                throw new BadRequestException("Duplicate product lines are not allowed");
            }

            Product product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", itemRequest.productId()));
            List<ShelfStock> shelfStocks = shelfStockRepository.findByProductIdOrderByIdAsc(product.getId());
            List<ShelfAllocation> allocations = allocateShelfStock(product, itemRequest.quantity(), shelfStocks);
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));

            saleLinePlans.add(new SaleLinePlan(
                    product,
                    itemRequest.quantity(),
                    product.getPrice(),
                    lineTotal,
                    allocations
            ));
        }

        return saleLinePlans;
    }

    private List<ShelfAllocation> allocateShelfStock(Product product, int requestedQuantity, List<ShelfStock> shelfStocks) {
        int remainingQuantity = requestedQuantity;
        List<ShelfAllocation> allocations = new ArrayList<>();

        for (ShelfStock shelfStock : shelfStocks) {
            if (remainingQuantity == 0) {
                break;
            }
            int availableQuantity = shelfStock.getCurrentQuantity();
            if (availableQuantity <= 0) {
                continue;
            }
            int quantityFromShelf = Math.min(availableQuantity, remainingQuantity);
            allocations.add(new ShelfAllocation(shelfStock, quantityFromShelf));
            remainingQuantity -= quantityFromShelf;
        }

        if (remainingQuantity > 0) {
            throw new BadRequestException("Insufficient shelf stock for product " + product.getProductCode());
        }

        return allocations;
    }

    private SaleResponse toSaleResponse(Sale sale, Bill bill, List<BillItem> billItems) {
        return new SaleResponse(
                sale.getId(),
                sale.getCashierName(),
                sale.getSaleTimestamp(),
                bill.getSerialNumber(),
                bill.getTotalPrice(),
                toBillItemResponses(billItems)
        );
    }

    private BillResponse toBillResponse(Bill bill, List<BillItem> billItems) {
        Sale sale = bill.getSale();
        return new BillResponse(
                sale.getId(),
                sale.getCashierName(),
                sale.getSaleTimestamp(),
                bill.getSerialNumber(),
                bill.getTotalPrice(),
                toBillItemResponses(billItems)
        );
    }

    private List<BillItemResponse> toBillItemResponses(List<BillItem> billItems) {
        return billItems.stream()
                .map(item -> new BillItemResponse(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getProductCode(),
                        item.getProductNameSnapshot(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getLineTotal()
                ))
                .toList();
    }

    private record SaleLinePlan(
            Product product,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal,
            List<ShelfAllocation> allocations
    ) {
    }

    private record ShelfAllocation(ShelfStock shelfStock, int quantity) {
    }
}
