package com.syos.erp.inventory;

import java.util.List;

import com.syos.erp.common.error.BadRequestException;
import com.syos.erp.common.error.ResourceNotFoundException;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;
import com.syos.erp.supplier.Supplier;
import com.syos.erp.supplier.SupplierRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InventoryService {

    private final MainStockBatchRepository mainStockBatchRepository;
    private final ShelfStockRepository shelfStockRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public InventoryService(
            MainStockBatchRepository mainStockBatchRepository,
            ShelfStockRepository shelfStockRepository,
            ProductRepository productRepository,
            SupplierRepository supplierRepository
    ) {
        this.mainStockBatchRepository = mainStockBatchRepository;
        this.shelfStockRepository = shelfStockRepository;
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<MainStockBatchResponse> findAllMainStockBatches() {
        return mainStockBatchRepository.findAll().stream()
                .map(this::toMainStockBatchResponse)
                .toList();
    }

    public MainStockBatchResponse findMainStockBatchById(Long id) {
        return toMainStockBatchResponse(getMainStockBatch(id));
    }

    @Transactional
    public MainStockBatchResponse createMainStockBatch(MainStockBatchRequest request) {
        validateExpiryDate(request);

        MainStockBatch batch = new MainStockBatch();
        applyMainStockBatchRequest(batch, request);
        return toMainStockBatchResponse(mainStockBatchRepository.save(batch));
    }

    @Transactional
    public MainStockBatchResponse updateMainStockBatch(Long id, MainStockBatchRequest request) {
        validateExpiryDate(request);

        MainStockBatch batch = getMainStockBatch(id);
        applyMainStockBatchRequest(batch, request);
        return toMainStockBatchResponse(mainStockBatchRepository.save(batch));
    }

    @Transactional
    public void deleteMainStockBatch(Long id) {
        MainStockBatch batch = getMainStockBatch(id);
        mainStockBatchRepository.delete(batch);
    }

    public List<ShelfStockResponse> findAllShelfStocks() {
        return shelfStockRepository.findAll().stream()
                .map(this::toShelfStockResponse)
                .toList();
    }

    public ShelfStockResponse findShelfStockById(Long id) {
        return toShelfStockResponse(getShelfStock(id));
    }

    @Transactional
    public ShelfStockResponse createShelfStock(ShelfStockRequest request) {
        validateShelfQuantity(request);

        ShelfStock shelfStock = new ShelfStock();
        applyShelfStockRequest(shelfStock, request);
        return toShelfStockResponse(shelfStockRepository.save(shelfStock));
    }

    @Transactional
    public ShelfStockResponse updateShelfStock(Long id, ShelfStockRequest request) {
        validateShelfQuantity(request);

        ShelfStock shelfStock = getShelfStock(id);
        applyShelfStockRequest(shelfStock, request);
        return toShelfStockResponse(shelfStockRepository.save(shelfStock));
    }

    @Transactional
    public void deleteShelfStock(Long id) {
        ShelfStock shelfStock = getShelfStock(id);
        shelfStockRepository.delete(shelfStock);
    }

    private void applyMainStockBatchRequest(MainStockBatch batch, MainStockBatchRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));
        Supplier supplier = supplierRepository.findById(request.supplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.supplierId()));

        batch.setProduct(product);
        batch.setSupplier(supplier);
        batch.setBatchCode(request.batchCode());
        batch.setPurchaseDate(request.purchaseDate());
        batch.setPurchasePrice(request.purchasePrice());
        batch.setQuantity(request.quantity());
        batch.setExpiryDate(request.expiryDate());
    }

    private void applyShelfStockRequest(ShelfStock shelfStock, ShelfStockRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        shelfStock.setProduct(product);
        shelfStock.setShelfCapacity(request.shelfCapacity());
        shelfStock.setCurrentQuantity(request.currentQuantity());
    }

    private MainStockBatch getMainStockBatch(Long id) {
        return mainStockBatchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Main stock batch", id));
    }

    private ShelfStock getShelfStock(Long id) {
        return shelfStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelf stock", id));
    }

    private void validateExpiryDate(MainStockBatchRequest request) {
        if (request.purchaseDate() != null
                && request.expiryDate() != null
                && request.expiryDate().isBefore(request.purchaseDate())) {
            throw new BadRequestException("Expiry date must not be before purchase date");
        }
    }

    private void validateShelfQuantity(ShelfStockRequest request) {
        if (request.currentQuantity() != null
                && request.shelfCapacity() != null
                && request.currentQuantity() > request.shelfCapacity()) {
            throw new BadRequestException("Shelf current quantity must not exceed shelf capacity");
        }
    }

    private MainStockBatchResponse toMainStockBatchResponse(MainStockBatch batch) {
        Product product = batch.getProduct();
        Supplier supplier = batch.getSupplier();
        return new MainStockBatchResponse(
                batch.getId(),
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                supplier.getId(),
                supplier.getCompanyName(),
                batch.getBatchCode(),
                batch.getPurchaseDate(),
                batch.getPurchasePrice(),
                batch.getQuantity(),
                batch.getExpiryDate()
        );
    }

    private ShelfStockResponse toShelfStockResponse(ShelfStock shelfStock) {
        Product product = shelfStock.getProduct();
        return new ShelfStockResponse(
                shelfStock.getId(),
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                shelfStock.getShelfCapacity(),
                shelfStock.getCurrentQuantity()
        );
    }
}
