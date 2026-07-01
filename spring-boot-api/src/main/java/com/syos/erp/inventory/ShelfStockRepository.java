package com.syos.erp.inventory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShelfStockRepository extends JpaRepository<ShelfStock, Long> {

    Optional<ShelfStock> findByProductId(Long productId);

    @Query("select coalesce(sum(stock.currentQuantity), 0) from ShelfStock stock where stock.product.id = :productId")
    long sumCurrentQuantityByProductId(Long productId);
}
