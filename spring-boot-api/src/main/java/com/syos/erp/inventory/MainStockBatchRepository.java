package com.syos.erp.inventory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MainStockBatchRepository extends JpaRepository<MainStockBatch, Long> {

    List<MainStockBatch> findByProductId(Long productId);

    Optional<MainStockBatch> findByProductIdAndBatchCode(Long productId, String batchCode);

    List<MainStockBatch> findByExpiryDateBefore(LocalDate date);
}
