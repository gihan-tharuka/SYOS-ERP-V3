package view;

import java.util.Scanner;

public class CashierMenuView {
    private Scanner scanner;

    public CashierMenuView() {
        scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("Cashier Menu:");
        System.out.println("1. Main Stock Management");
        System.out.println("2. Shelf Stock Management");
        System.out.println("3. Sales Management");
        System.out.println("4. Report Management");
        System.out.println("5. Log Out");
        System.out.println("Enter your choice: ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine();
    }
}
