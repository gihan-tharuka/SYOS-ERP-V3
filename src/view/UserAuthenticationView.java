package view;

import java.util.Scanner;

public class UserAuthenticationView {
    private Scanner scanner = new Scanner(System.in);

    public String[] getUserCredentials() {
        System.out.println("Enter your role (admin/cashier): ");
        String role = scanner.nextLine();

        System.out.println("Enter your username: ");
        String username = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        return new String[]{role, username, password};
    }

    // New method to get the scanner
    public Scanner getScanner() {
        return scanner;
    }
}

