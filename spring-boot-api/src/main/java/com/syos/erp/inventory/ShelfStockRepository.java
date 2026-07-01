package com.syos.erp.inventory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfStockRepository extends JpaRepository<ShelfStock, Long> {

    Optional<ShelfStock> findByProductId(Long productId);
}
