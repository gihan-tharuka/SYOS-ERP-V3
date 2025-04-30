package Testing2.model;

import model.Cashier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CashierTest {

    private Cashier cashier;

    @BeforeEach
    public void setUp() {
        cashier = new Cashier("cashierUser", "cashierPass", "Cashier Name", "cashier@example.com", "123456789");
    }

    @Test
    public void testGetUsername() {
        assertEquals("cashierUser", cashier.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("cashierPass", cashier.getPassword());
    }

    @Test
    public void testGetEmail() {
        assertEquals("cashier@example.com", cashier.getEmail());
    }

    @Test
    public void testGetFullName() {
        assertEquals("Cashier Name", cashier.getFullName());
    }

    @Test
    public void testGetMobile() {
        assertEquals("123456789", cashier.getMobile());
    }

    @Test
    public void testSetPassword() {
        cashier.setPassword("newPass");
        assertEquals("newPass", cashier.getPassword());
    }

    @Test
    public void testSetEmail() {
        cashier.setEmail("new@example.com");
        assertEquals("new@example.com", cashier.getEmail());
    }

    @Test
    public void testSetFullName() {
        cashier.setFullName("New Cashier Name");
        assertEquals("New Cashier Name", cashier.getFullName());
    }

    @Test
    public void testSetMobile() {
        cashier.setMobile("987654321");
        assertEquals("987654321", cashier.getMobile());
    }
}

