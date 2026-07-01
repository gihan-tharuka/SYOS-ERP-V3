package com.syos.erp.supplier;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.syos.erp.common.error.GlobalExceptionHandler;
import com.syos.erp.common.error.ResourceNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SupplierController.class)
@Import(GlobalExceptionHandler.class)
class SupplierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierService supplierService;

    @Test
    void lookupReturnsNotFoundForMissingSupplier() throws Exception {
        when(supplierService.findById(999L)).thenThrow(new ResourceNotFoundException("Supplier", 999L));

        mockMvc.perform(get("/api/suppliers/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Supplier not found with id 999"))
                .andExpect(jsonPath("$.status").value(404));
    }
}
