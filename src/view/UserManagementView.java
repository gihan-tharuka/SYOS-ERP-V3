package view;

import model.Admin;
import model.Cashier;
import model.Supplier;
import model.User;

import java.util.List;
import java.util.Scanner;

public class UserManagementView {
    private Scanner scanner;

    public UserManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displayAdminManagementMenu() {
        System.out.println("Admin Management Menu:");
        System.out.println("1. Add Admin");
        System.out.println("2. View All Admins");
        System.out.println("3. Edit Admin");
        System.out.println("4. Delete Admin");
        System.out.println("5. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }

    public void displayCashierManagementMenu() {
        System.out.println("Cashier Management Menu:");
        System.out.println("1. Add Cashier");
        System.out.println("2. View All Cashiers");
        System.out.println("3. Edit Cashier");
        System.out.println("4. Delete Cashier");
        System.out.println("5. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }

    public void displaySupplierManagementMenu() {
        System.out.println("Supplier Management Menu:");
        System.out.println("1. Add Supplier");
        System.out.println("2. View All Suppliers");
        System.out.println("3. Edit Supplier");
        System.out.println("4. Delete Supplier");
        System.out.println("5. Back to Admin Menu");
        System.out.println("Enter your choice: ");
    }

    public User collectUserInfo(String role) {
        scanner.nextLine(); // consume newline left-over

        System.out.println("Enter new " + role + " username: ");
        String username = scanner.nextLine();

        System.out.println("Enter new " + role + " password: ");
        String password = scanner.nextLine();

        System.out.println("Enter new " + role + " email: ");
        String email = scanner.nextLine();

        User user = null;
        if (role.equalsIgnoreCase("cashier")) {
            System.out.println("Enter new cashier full name: ");
            String fullName = scanner.nextLine();

            System.out.println("Enter new cashier mobile: ");
            String mobile = scanner.nextLine();

            user = new Cashier(username, password, fullName, email, mobile);
        } else if (role.equalsIgnoreCase("supplier")) {
            System.out.println("Enter new supplier company name: ");
            String companyName = scanner.nextLine();

            System.out.println("Enter new supplier contact person: ");
            String contactPerson = scanner.nextLine();

            System.out.println("Enter new supplier mobile: ");
            String mobile = scanner.nextLine();

            user = new Supplier(username, password, companyName, contactPerson, email, mobile);
        } else if (role.equalsIgnoreCase("admin")) {
            user = new Admin(username, password, email);
        }

        return user;
    }

    public String getUsernameForDeletion(String role) {
        scanner.nextLine(); // consume newline left-over

        System.out.println("Enter the username of the " + role + " to delete:");
        return scanner.nextLine();
    }

    public void displayDeletionResult(boolean success, String role) {
        if (success) {
            System.out.println(role + " deleted successfully.");
        } else {
            System.out.println(role + " not found or could not be deleted.");
        }
    }

    public void displayAllUsers(List<User> users, String role) {
        System.out.println("List of all " + role + "s:");
        for (User user : users) {
            if (role.equalsIgnoreCase("cashier")) {
                Cashier cashier = (Cashier) user;
                System.out.println("Username: " + cashier.getUsername() + ", Full Name: " + cashier.getFullName() + ", Email: " + cashier.getEmail() + ", Mobile: " + cashier.getMobile());
            } else if (role.equalsIgnoreCase("supplier")) {
                Supplier supplier = (Supplier) user;
                System.out.println("Username: " + supplier.getUsername() + ", Company Name: " + supplier.getCompanyName() + ", Contact Person: " + supplier.getContactPerson() + ", Email: " + supplier.getEmail() + ", Mobile: " + supplier.getMobile());
            } else {
                System.out.println("Username: " + user.getUsername() + ", Email: " + user.getEmail());
            }
        }
    }

    public String getUsernameForEditing(String role) {
        scanner.nextLine(); // consume newline left-over

        System.out.println("Enter the username of the " + role + " to edit:");
        return scanner.nextLine();
    }

    public void displayUserNotFoundMessage(String role) {
        System.out.println(role + " not found.");
    }

    public User collectUpdatedUserInfo(User user, String role) {
        System.out.println("Editing " + role + " details:");
        System.out.println("Leave field empty to keep the current value.");

        System.out.println("New password (current: " + user.getPassword() + "):");
        String password = scanner.nextLine();
        if (!password.isEmpty()) user.setPassword(password);

        System.out.println("New email (current: " + user.getEmail() + "):");
        String email = scanner.nextLine();
        if (!email.isEmpty()) user.setEmail(email);

        return user;
    }

    public void displayUserUpdatedMessage(String role) {
        System.out.println(role + " updated successfully.");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine();
    }
}
