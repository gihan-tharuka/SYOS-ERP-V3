package com.syos.erp.supplier;

import java.util.List;

import com.syos.erp.common.error.ResourceNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<SupplierResponse> findAll() {
        return supplierRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public SupplierResponse findById(Long id) {
        return toResponse(getSupplier(id));
    }

    @Transactional
    public SupplierResponse create(SupplierRequest request) {
        Supplier supplier = new Supplier();
        applyRequest(supplier, request);
        return toResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public SupplierResponse update(Long id, SupplierRequest request) {
        Supplier supplier = getSupplier(id);
        applyRequest(supplier, request);
        return toResponse(supplierRepository.save(supplier));
    }

    @Transactional
    public void delete(Long id) {
        Supplier supplier = getSupplier(id);
        supplierRepository.delete(supplier);
    }

    private Supplier getSupplier(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier", id));
    }

    private void applyRequest(Supplier supplier, SupplierRequest request) {
        supplier.setUsername(request.username());
        supplier.setCompanyName(request.companyName());
        supplier.setContactPerson(request.contactPerson());
        supplier.setEmail(request.email());
        supplier.setMobile(request.mobile());
    }

    private SupplierResponse toResponse(Supplier supplier) {
        return new SupplierResponse(
                supplier.getId(),
                supplier.getUsername(),
                supplier.getCompanyName(),
                supplier.getContactPerson(),
                supplier.getEmail(),
                supplier.getMobile()
        );
    }
}
