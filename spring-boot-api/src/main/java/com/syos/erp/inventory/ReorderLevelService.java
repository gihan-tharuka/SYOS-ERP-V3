package com.syos.erp.inventory;

import java.util.List;

import com.syos.erp.common.error.ResourceNotFoundException;
import com.syos.erp.product.Product;
import com.syos.erp.product.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ReorderLevelService {

    private final ReorderLevelRepository reorderLevelRepository;
    private final ProductRepository productRepository;

    public ReorderLevelService(ReorderLevelRepository reorderLevelRepository, ProductRepository productRepository) {
        this.reorderLevelRepository = reorderLevelRepository;
        this.productRepository = productRepository;
    }

    public List<ReorderLevelResponse> findAll() {
        return reorderLevelRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ReorderLevelResponse findById(Long id) {
        return toResponse(getReorderLevel(id));
    }

    @Transactional
    public ReorderLevelResponse create(ReorderLevelRequest request) {
        ReorderLevel reorderLevel = new ReorderLevel();
        applyRequest(reorderLevel, request);
        return toResponse(reorderLevelRepository.save(reorderLevel));
    }

    @Transactional
    public ReorderLevelResponse update(Long id, ReorderLevelRequest request) {
        ReorderLevel reorderLevel = getReorderLevel(id);
        applyRequest(reorderLevel, request);
        return toResponse(reorderLevelRepository.save(reorderLevel));
    }

    @Transactional
    public void delete(Long id) {
        ReorderLevel reorderLevel = getReorderLevel(id);
        reorderLevelRepository.delete(reorderLevel);
    }

    private void applyRequest(ReorderLevel reorderLevel, ReorderLevelRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

        reorderLevel.setProduct(product);
        reorderLevel.setThresholdQuantity(request.thresholdQuantity());
        reorderLevel.setTotalStock(request.totalStock());
    }

    private ReorderLevel getReorderLevel(Long id) {
        return reorderLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reorder level", id));
    }

    private ReorderLevelResponse toResponse(ReorderLevel reorderLevel) {
        Product product = reorderLevel.getProduct();
        return new ReorderLevelResponse(
                reorderLevel.getId(),
                product.getId(),
                product.getProductCode(),
                product.getProductName(),
                reorderLevel.getThresholdQuantity(),
                reorderLevel.getTotalStock(),
                reorderLevel.getTotalStock() < reorderLevel.getThresholdQuantity()
        );
    }
}
