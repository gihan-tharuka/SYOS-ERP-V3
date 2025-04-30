package view;

import java.util.Scanner;


public class AdminMenuView {
    private Scanner scanner;

    public AdminMenuView() {
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Admin Menu:");
        System.out.println("1. Admin Management");
        System.out.println("2. Cashier Management");
        System.out.println("3. Supplier Management");
        System.out.println("4. Item Management");
        System.out.println("5. Main Stock Management");
        System.out.println("6. Shelf Stock Management");
        System.out.println("7. Sales Management");
        System.out.println("8. Report Management"); // New option for report management
        System.out.println("9. Log Out");
        System.out.println("Enter your choice: ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine(); // Clear the buffer
    }

}
