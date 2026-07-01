package com.syos.erp.inventory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MainStockBatchRepository extends JpaRepository<MainStockBatch, Long> {

    List<MainStockBatch> findByProductId(Long productId);

    Optional<MainStockBatch> findByProductIdAndBatchCode(Long productId, String batchCode);

    List<MainStockBatch> findByExpiryDateBefore(LocalDate date);

    @Query("select coalesce(sum(batch.quantity), 0) from MainStockBatch batch where batch.product.id = :productId")
    long sumQuantityByProductId(Long productId);
}
