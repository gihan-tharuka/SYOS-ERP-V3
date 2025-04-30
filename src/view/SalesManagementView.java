package view;

import model.Sale;

import java.util.List;
import java.util.Scanner;

public class SalesManagementView {
    private Scanner scanner;

    public SalesManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displaySalesManagementMenu() {
        System.out.println("Sales Management Menu:");
        System.out.println("1. View All Sales");
        System.out.println("2. Create New Sale");
        System.out.println("3. View Bill");
        System.out.println("Enter your choice: ");
    }


    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine();
    }
    public Sale getNewSaleDetails() {
        Sale sale = new Sale();
        System.out.println("Enter transaction type (e.g., over-the-counter, online): ");
        scanner.nextLine(); // Consume newline
        sale.setTransactionType(scanner.nextLine());
        sale.setSaleDate(new java.util.Date());
        return sale;
    }

    public void displayAllSales(List<Sale> sales) {
        System.out.println("All Sales:");
        for (Sale sale : sales) {
            System.out.println("Sale ID: " + sale.getSaleId());
            System.out.println("Sale Date: " + sale.getSaleDate());
            System.out.println("Transaction Type: " + sale.getTransactionType());
            System.out.println("---------------");
        }
    }

    public void showSaleCreatedSuccess() {
        System.out.println("Sale created successfully!");
    }

    public void showBillCreatedSuccess() {
        System.out.println("Bill created successfully!");
    }

    public void showChangeAmount(double changeAmount) {
        System.out.println("Change Amount: " + changeAmount);
    }
}

