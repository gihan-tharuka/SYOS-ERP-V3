package com.syos.erp.inventory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReorderLevelRepository extends JpaRepository<ReorderLevel, Long> {

    Optional<ReorderLevel> findByProductId(Long productId);

    List<ReorderLevel> findByTotalStockLessThan(Integer thresholdQuantity);
}
