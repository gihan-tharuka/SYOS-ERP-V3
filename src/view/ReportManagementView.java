package view;

import java.util.Scanner;
import java.sql.Date;
import model.BatchSelection;
import template.ReportTemplate;

import java.util.List;
import java.util.Map;

public class ReportManagementView {
    private Scanner scanner;

    public ReportManagementView() {
        scanner = new Scanner(System.in);
    }

    public void displayReportManagementMenu() {
        System.out.println("Report Management Menu:");
        System.out.println("1. Daily Sales Reports");
        System.out.println("2. Reshelve Reports");
        System.out.println("3. Reorder Reports");
        System.out.println("4. Stock Reports");
        System.out.println("5. Bill Reports");
        System.out.println("Enter your choice: ");
    }

    public void displayReportOptionsMenu() {
        System.out.println("Report Options:");
        System.out.println("1. View All Reports");
        System.out.println("2. Create New Report");
        System.out.println("Enter your choice: ");
    }

    public int getUserChoice() {
        return scanner.nextInt();
    }

    public void clearInput() {
        scanner.nextLine(); // Clear the buffer
    }

    public void displayAllReports(List<ReportTemplate> reports) {
        System.out.println("All Reports:");
        for (ReportTemplate report : reports) {
            System.out.println("Report Name: " + report.getReportName());
            System.out.println("Report Date: " + report.getReportDate());
            System.out.println("---------------");
        }
    }

    public void showReportCreatedSuccess() {
        System.out.println("Report created successfully!");
    }

    public Date getReportDateInput() {
        System.out.println("Enter the report date (yyyy-mm-dd): ");
        String dateInput = scanner.next();
        return Date.valueOf(dateInput);
    }

    public void displayReshelvingInfo(Map<Integer, List<BatchSelection>> reshelvingInfo, Map<Integer, Integer> reshelfQuantities, Map<Integer, String> itemNames) {
        System.out.println("Reshelving Info:");
        for (Map.Entry<Integer, List<BatchSelection>> entry : reshelvingInfo.entrySet()) {
            int itemId = entry.getKey();
            List<BatchSelection> selectedBatches = entry.getValue();
            int reshelfQuantity = reshelfQuantities.get(itemId);
            String itemName = itemNames.get(itemId);

            System.out.println("Item ID: " + itemId + ", Item Name: " + itemName + ", Reshelf Quantity: " + reshelfQuantity);
            for (BatchSelection selection : selectedBatches) {
                System.out.println("  Batch Code: " + selection.getBatch().getBatchCode() + ", Reshelf Quantity: " + selection.getReshelfQuantity() + ", Expiry Date: " + selection.getBatch().getExpiryDate());
            }
            System.out.println("---------------");
        }
    }

    public void showReshelveReportGenerated() {
        System.out.println("Reshelve report generated successfully!");
    }
}
