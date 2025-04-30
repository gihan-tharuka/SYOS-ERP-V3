package Testing2.factory;

import factory.UserFactory;
import model.Admin;
import model.Cashier;
import model.Supplier;
import model.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    @Test
    public void testCreateAdmin() {
        User user = UserFactory.createUser("admin", "adminUser", "adminPass", "admin@example.com", null, null, null, null);
        assertNotNull(user);
        assertTrue(user instanceof Admin);
        assertEquals("adminUser", user.getUsername());
        assertEquals("adminPass", user.getPassword());
        assertEquals("admin@example.com", user.getEmail());
    }

    @Test
    public void testCreateCashier() {
        User user = UserFactory.createUser("cashier", "cashierUser", "cashierPass", "cashier@example.com", "Cashier Name", "123456789", null, null);
        assertNotNull(user);
        assertTrue(user instanceof Cashier);
        assertEquals("cashierUser", user.getUsername());
        assertEquals("cashierPass", user.getPassword());
        assertEquals("cashier@example.com", user.getEmail());
        assertEquals("Cashier Name", ((Cashier) user).getFullName());
        assertEquals("123456789", ((Cashier) user).getMobile());
    }

    @Test
    public void testCreateSupplier() {
        User user = UserFactory.createUser("supplier", "supplierUser", "supplierPass", "supplier@example.com", null, "987654321", "Company Name", "Contact Person");
        assertNotNull(user);
        assertTrue(user instanceof Supplier);
        assertEquals("supplierUser", user.getUsername());
        assertEquals("supplierPass", user.getPassword());
        assertEquals("supplier@example.com", user.getEmail());
        assertEquals("Company Name", ((Supplier) user).getCompanyName());
        assertEquals("Contact Person", ((Supplier) user).getContactPerson());
        assertEquals("987654321", ((Supplier) user).getMobile());
    }

    @Test
    public void testInvalidRole() {
        User user = UserFactory.createUser("invalidRole", "user", "pass", "user@example.com", null, null, null, null);
        assertNull(user);
    }
}
