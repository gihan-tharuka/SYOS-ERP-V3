package controller;

import factory.ReportFactory;
import template.DailySalesReport;
import template.ReportTemplate;
import view.ReportManagementView;
import dao.ShelfStockDAO;
import dao.MainStockDAO;
import dao.ItemDAO;
import dao.ReportDAO;
import strategy.BatchSelectionStrategy;

import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class ReportManagementController {
    private ReportManagementView view;
    private ShelfStockDAO shelfStockDAO;
    private MainStockDAO mainStockDAO;
    private ItemDAO itemDAO;
    private ReportDAO reportDAO;
    private BatchSelectionStrategy strategy;

    public ReportManagementController(ReportManagementView view, ShelfStockDAO shelfStockDAO, MainStockDAO mainStockDAO, ItemDAO itemDAO, ReportDAO reportDAO, BatchSelectionStrategy strategy) {
        this.view = view;
        this.shelfStockDAO = shelfStockDAO;
        this.mainStockDAO = mainStockDAO;
        this.itemDAO = itemDAO;
        this.reportDAO = reportDAO;
        this.strategy = strategy;
    }

    public void showReportManagementMenu() {
        int choice;
        do {
            view.displayReportManagementMenu();
            choice = view.getUserChoice();
            view.clearInput();
            switch (choice) {
                case 1:
                    handleReportOptions(ReportFactory.ReportType.DAILY_SALES);
                    break;
                case 2:
                    handleReportOptions(ReportFactory.ReportType.RESHELVE);
                    break;
                case 3:
                    handleReportOptions(ReportFactory.ReportType.REORDER);
                    break;
                case 4:
                    handleReportOptions(ReportFactory.ReportType.STOCK);
                    break;
                case 5:
                    handleReportOptions(ReportFactory.ReportType.BILL);
                    break;
                case 6:
                    System.out.println("Going back to Admin Menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private void handleReportOptions(ReportFactory.ReportType reportType) {
        view.displayReportOptionsMenu();
        int choice = view.getUserChoice();
        view.clearInput();
        switch (choice) {
            case 1:
                viewAllReports(reportType);
                break;
            case 2:
                createNewReport(reportType);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void viewAllReports(ReportFactory.ReportType reportType) {
        // Logic to view all reports based on reportType
        List<ReportTemplate> reports = new ArrayList<>();
        view.displayAllReports(reports);
    }

    private void createNewReport(ReportFactory.ReportType reportType) {
        ReportTemplate report = ReportFactory.createReport(reportType, shelfStockDAO, mainStockDAO, itemDAO, reportDAO, strategy);
        if (report instanceof DailySalesReport) {
            Date reportDate = view.getReportDateInput();
            ((DailySalesReport) report).setReportDate(reportDate);
        }
        report.generateReport();
        view.showReportCreatedSuccess();
    }
}
