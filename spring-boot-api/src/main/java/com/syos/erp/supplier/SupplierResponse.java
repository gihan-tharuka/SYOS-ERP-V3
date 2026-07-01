package com.syos.erp.supplier;

public record SupplierResponse(
        Long id,
        String username,
        String companyName,
        String contactPerson,
        String email,
        String mobile
) {
}
