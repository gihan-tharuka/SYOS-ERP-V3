package Testing2.model;

import model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SupplierTest {

    private Supplier supplier;

    @BeforeEach
    public void setUp() {
        supplier = new Supplier("supplierUser", "supplierPass", "CompanyName", "ContactPerson", "supplier@example.com", "987654321");
    }

    @Test
    public void testGetUsername() {
        assertEquals("supplierUser", supplier.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("supplierPass", supplier.getPassword());
    }

    @Test
    public void testGetEmail() {
        assertEquals("supplier@example.com", supplier.getEmail());
    }

    @Test
    public void testGetCompanyName() {
        assertEquals("CompanyName", supplier.getCompanyName());
    }

    @Test
    public void testGetContactPerson() {
        assertEquals("ContactPerson", supplier.getContactPerson());
    }

    @Test
    public void testGetMobile() {
        assertEquals("987654321", supplier.getMobile());
    }

    @Test
    public void testSetPassword() {
        supplier.setPassword("newPass");
        assertEquals("newPass", supplier.getPassword());
    }

    @Test
    public void testSetEmail() {
        supplier.setEmail("new@example.com");
        assertEquals("new@example.com", supplier.getEmail());
    }

    @Test
    public void testSetCompanyName() {
        supplier.setCompanyName("NewCompanyName");
        assertEquals("NewCompanyName", supplier.getCompanyName());
    }

    @Test
    public void testSetContactPerson() {
        supplier.setContactPerson("NewContactPerson");
        assertEquals("NewContactPerson", supplier.getContactPerson());
    }

    @Test
    public void testSetMobile() {
        supplier.setMobile("123456789");
        assertEquals("123456789", supplier.getMobile());
    }
}

