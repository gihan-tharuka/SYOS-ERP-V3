package com.syos.erp.supplier;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SupplierRequest(
        @NotBlank(message = "Supplier username is required")
        @Size(max = 100)
        String username,

        @NotBlank(message = "Supplier name is required")
        @Size(max = 255)
        String companyName,

        @Size(max = 255)
        String contactPerson,

        @NotBlank(message = "Supplier email is required")
        @Email(message = "Supplier email must be valid")
        @Size(max = 255)
        String email,

        @Size(max = 20)
        String mobile
) {
}
