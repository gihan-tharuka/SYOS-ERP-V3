package Testing2.model;

import model.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AdminTest {

    private Admin admin;

    @BeforeEach
    public void setUp() {
        admin = new Admin("adminUser", "adminPass", "admin@example.com");
    }

    @Test
    public void testGetUsername() {
        assertEquals("adminUser", admin.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("adminPass", admin.getPassword());
    }

    @Test
    public void testGetEmail() {
        assertEquals("admin@example.com", admin.getEmail());
    }

    @Test
    public void testSetPassword() {
        admin.setPassword("newPass");
        assertEquals("newPass", admin.getPassword());
    }

    @Test
    public void testSetEmail() {
        admin.setEmail("new@example.com");
        assertEquals("new@example.com", admin.getEmail());
    }
}

