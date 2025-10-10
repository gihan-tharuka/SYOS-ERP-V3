package Testing2.Testing.factory;

import factory.UserFactory;
import model.Admin;
import model.Cashier;
import model.Customer;
import model.Supplier;
import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    @Test
    public void testCreateUserAdmin() {
        User result = UserFactory.createUser("admin", "admin1", "password123", "admin@test.com", null, null, null, null);

        assertNotNull(result);
        assertTrue(result instanceof Admin);
        Admin admin = (Admin) result;
        assertEquals("admin1", admin.getUsername());
        assertEquals("password123", admin.getPassword());
        assertEquals("admin@test.com", admin.getEmail());
    }

    @Test
    public void testCreateUserAdminCaseInsensitive() {
        User result = UserFactory.createUser("ADMIN", "admin1", "password123", "admin@test.com", null, null, null, null);

        assertNotNull(result);
        assertTrue(result instanceof Admin);
        Admin admin = (Admin) result;
        assertEquals("admin1", admin.getUsername());
        assertEquals("password123", admin.getPassword());
        assertEquals("admin@test.com", admin.getEmail());
    }

    @Test
    public void testCreateUserCashier() {
        User result = UserFactory.createUser("cashier", "cashier1", "password123", "cashier@test.com", "John Doe", "1234567890", null, null);

        assertNotNull(result);
        assertTrue(result instanceof Cashier);
        Cashier cashier = (Cashier) result;
        assertEquals("cashier1", cashier.getUsername());
        assertEquals("password123", cashier.getPassword());
        assertEquals("John Doe", cashier.getFullName());
        assertEquals("cashier@test.com", cashier.getEmail());
        assertEquals("1234567890", cashier.getMobile());
    }

    @Test
    public void testCreateUserCashierCaseInsensitive() {
        User result = UserFactory.createUser("CASHIER", "cashier1", "password123", "cashier@test.com", "John Doe", "1234567890", null, null);

        assertNotNull(result);
        assertTrue(result instanceof Cashier);
        Cashier cashier = (Cashier) result;
        assertEquals("cashier1", cashier.getUsername());
        assertEquals("password123", cashier.getPassword());
        assertEquals("John Doe", cashier.getFullName());
        assertEquals("cashier@test.com", cashier.getEmail());
        assertEquals("1234567890", cashier.getMobile());
    }

    @Test
    public void testCreateUserSupplier() {
        User result = UserFactory.createUser("supplier", "supplier1", "password123", "supplier@test.com", null, "0987654321", "Test Company", "Jane Smith");

        assertNotNull(result);
        assertTrue(result instanceof Supplier);
        Supplier supplier = (Supplier) result;
        assertEquals("supplier1", supplier.getUsername());
        assertEquals("password123", supplier.getPassword());
        assertEquals("Test Company", supplier.getCompanyName());
        assertEquals("Jane Smith", supplier.getContactPerson());
        assertEquals("supplier@test.com", supplier.getEmail());
        assertEquals("0987654321", supplier.getMobile());
    }

    @Test
    public void testCreateUserSupplierCaseInsensitive() {
        User result = UserFactory.createUser("SUPPLIER", "supplier1", "password123", "supplier@test.com", null, "0987654321", "Test Company", "Jane Smith");

        assertNotNull(result);
        assertTrue(result instanceof Supplier);
        Supplier supplier = (Supplier) result;
        assertEquals("supplier1", supplier.getUsername());
        assertEquals("password123", supplier.getPassword());
        assertEquals("Test Company", supplier.getCompanyName());
        assertEquals("Jane Smith", supplier.getContactPerson());
        assertEquals("supplier@test.com", supplier.getEmail());
        assertEquals("0987654321", supplier.getMobile());
    }

    @Test
    public void testCreateUserCustomer() {
        User result = UserFactory.createUser("customer", "customer1", "password123", "customer@test.com", null, "5555555555", null, null);

        assertNotNull(result);
        assertTrue(result instanceof Customer);
        Customer customer = (Customer) result;
        assertEquals("customer1", customer.getUsername());
        assertEquals("password123", customer.getPassword());
        assertEquals("customer@test.com", customer.getEmail());
        assertEquals("5555555555", customer.getMobile());
    }

    @Test
    public void testCreateUserCustomerCaseInsensitive() {
        User result = UserFactory.createUser("CUSTOMER", "customer1", "password123", "customer@test.com", null, "5555555555", null, null);

        assertNotNull(result);
        assertTrue(result instanceof Customer);
        Customer customer = (Customer) result;
        assertEquals("customer1", customer.getUsername());
        assertEquals("password123", customer.getPassword());
        assertEquals("customer@test.com", customer.getEmail());
        assertEquals("5555555555", customer.getMobile());
    }

    @Test
    public void testCreateUserInvalidRole() {
        User result = UserFactory.createUser("invalid_role", "user1", "password123", "user@test.com", null, null, null, null);

        assertNull(result);
    }

    @Test
    public void testCreateUserNullRole() {
        assertThrows(NullPointerException.class, () -> {
            UserFactory.createUser(null, "user1", "password123", "user@test.com", null, null, null, null);
        });
    }

    @Test
    public void testCreateUserEmptyRole() {
        User result = UserFactory.createUser("", "user1", "password123", "user@test.com", null, null, null, null);

        assertNull(result);
    }

    @Test
    public void testCreateUserWithNullParameters() {
        // Test that the factory handles null parameters gracefully
        User result = UserFactory.createUser("admin", null, null, null, null, null, null, null);

        assertNotNull(result);
        assertTrue(result instanceof Admin);
        Admin admin = (Admin) result;
        assertNull(admin.getUsername());
        assertNull(admin.getPassword());
        assertNull(admin.getEmail());
    }

    @Test
    public void testCreateUserWithAllParameters() {
        // Test creating a supplier with all parameters to ensure no issues
        User result = UserFactory.createUser("supplier", "supplier1", "password123", "supplier@test.com", "Full Name", "1234567890", "Company Name", "Contact Person");

        assertNotNull(result);
        assertTrue(result instanceof Supplier);
        Supplier supplier = (Supplier) result;
        assertEquals("supplier1", supplier.getUsername());
        assertEquals("password123", supplier.getPassword());
        assertEquals("Company Name", supplier.getCompanyName());
        assertEquals("Contact Person", supplier.getContactPerson());
        assertEquals("supplier@test.com", supplier.getEmail());
        assertEquals("1234567890", supplier.getMobile());
    }
} 