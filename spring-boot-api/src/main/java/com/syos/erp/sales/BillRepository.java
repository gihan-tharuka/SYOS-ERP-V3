package com.syos.erp.sales;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<Bill> findBySaleId(Long saleId);

    List<Bill> findByBillDate(LocalDate billDate);
}
