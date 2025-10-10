package Testing2.Testing.view;

import view.UserManagementView;
import model.Admin;
import model.Cashier;
import model.Supplier;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

public class UserManagementViewTest {
    private UserManagementView view;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        System.setIn(new ByteArrayInputStream("".getBytes()));
        view = new UserManagementView();
    }

    @Test
    void testDisplayAdminManagementMenu() {
        view.displayAdminManagementMenu();
        String output = outputStream.toString();
        assertTrue(output.contains("Admin Management Menu:"));
    }

    @Test
    void testDisplayCashierManagementMenu() {
        view.displayCashierManagementMenu();
        String output = outputStream.toString();
        assertTrue(output.contains("Cashier Management Menu:"));
    }

    @Test
    void testDisplaySupplierManagementMenu() {
        view.displaySupplierManagementMenu();
        String output = outputStream.toString();
        assertTrue(output.contains("Supplier Management Menu:"));
    }

    @Test
    void testCollectUserInfo_Admin() {
        String input = "\nadminuser\nadminpass\nadmin@email.com\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        User user = view.collectUserInfo("admin");
        assertNotNull(user);
        assertTrue(user instanceof Admin);
        assertEquals("adminuser", user.getUsername());
        assertEquals("adminpass", user.getPassword());
        assertEquals("admin@email.com", user.getEmail());
    }

    @Test
    void testCollectUserInfo_Cashier() {
        String input = "\ncashuser\ncashpass\ncash@email.com\nCashier Name\n1234567890\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        User user = view.collectUserInfo("cashier");
        assertNotNull(user);
        assertTrue(user instanceof Cashier);
        assertEquals("cashuser", user.getUsername());
        assertEquals("cashpass", user.getPassword());
        assertEquals("cash@email.com", user.getEmail());
        assertEquals("Cashier Name", ((Cashier) user).getFullName());
        assertEquals("1234567890", ((Cashier) user).getMobile());
    }

    @Test
    void testCollectUserInfo_Supplier() {
        String input = "\nsuppuser\nsupppass\nsupp@email.com\nSuppCo\nSuppContact\n9876543210\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        User user = view.collectUserInfo("supplier");
        assertNotNull(user);
        assertTrue(user instanceof Supplier);
        assertEquals("suppuser", user.getUsername());
        assertEquals("supppass", user.getPassword());
        assertEquals("supp@email.com", user.getEmail());
        assertEquals("SuppCo", ((Supplier) user).getCompanyName());
        assertEquals("SuppContact", ((Supplier) user).getContactPerson());
        assertEquals("9876543210", ((Supplier) user).getMobile());
    }

    @Test
    void testGetUsernameForDeletion() {
        String input = "\nuserdel\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        String username = view.getUsernameForDeletion("admin");
        assertEquals("userdel", username);
    }

    @Test
    void testDisplayDeletionResult_Success() {
        view.displayDeletionResult(true, "admin");
        assertTrue(outputStream.toString().contains("admin deleted successfully."));
    }

    @Test
    void testDisplayDeletionResult_Failure() {
        view.displayDeletionResult(false, "admin");
        assertTrue(outputStream.toString().contains("not found or could not be deleted"));
    }

    @Test
    void testDisplayAllUsers_Admin() {
        User admin = new Admin("adminuser", "pass", "admin@email.com");
        List<User> users = Arrays.asList(admin);
        view.displayAllUsers(users, "admin");
        String output = outputStream.toString();
        assertTrue(output.contains("Username: adminuser"));
        assertTrue(output.contains("Email: admin@email.com"));
    }

    @Test
    void testDisplayAllUsers_Cashier() {
        Cashier cashier = new Cashier("cashuser", "pass", "Cash Name", "cash@email.com", "1234567890");
        List<User> users = Arrays.asList(cashier);
        view.displayAllUsers(users, "cashier");
        String output = outputStream.toString();
        assertTrue(output.contains("Full Name: Cash Name"));
        assertTrue(output.contains("Mobile: 1234567890"));
    }

    @Test
    void testDisplayAllUsers_Supplier() {
        Supplier supplier = new Supplier("suppuser", "pass", "SuppCo", "SuppContact", "supp@email.com", "9876543210");
        List<User> users = Arrays.asList(supplier);
        view.displayAllUsers(users, "supplier");
        String output = outputStream.toString();
        assertTrue(output.contains("Company Name: SuppCo"));
        assertTrue(output.contains("Contact Person: SuppContact"));
    }

    @Test
    void testGetUsernameForEditing() {
        String input = "\nuseredit\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        String username = view.getUsernameForEditing("admin");
        assertEquals("useredit", username);
    }

    @Test
    void testDisplayUserNotFoundMessage() {
        view.displayUserNotFoundMessage("admin");
        assertTrue(outputStream.toString().contains("admin not found."));
    }

    @Test
    void testCollectUpdatedUserInfo() {
        User user = new Admin("adminuser", "oldpass", "old@email.com");
        String input = "newpass\nnew@email.com\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        User updated = view.collectUpdatedUserInfo(user, "admin");
        assertEquals("newpass", updated.getPassword());
        assertEquals("new@email.com", updated.getEmail());
    }

    @Test
    void testDisplayUserUpdatedMessage() {
        view.displayUserUpdatedMessage("admin");
        assertTrue(outputStream.toString().contains("admin updated successfully."));
    }

    @Test
    void testGetUserChoice() {
        String input = "3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        int choice = view.getUserChoice();
        assertEquals(3, choice);
    }

    @Test
    void testClearInput() {
        String input = "test\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        view = new UserManagementView();
        view.clearInput();
        assertTrue(true);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
} 