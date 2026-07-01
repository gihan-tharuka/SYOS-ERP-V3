package com.syos.erp.sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {

    List<BillItem> findByBillSerialNumber(Long serialNumber);

    List<BillItem> findByProductId(Long productId);
}
