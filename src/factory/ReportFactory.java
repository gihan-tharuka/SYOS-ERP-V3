package factory;

import dao.ItemDAO;
import dao.MainStockDAO;
import dao.ShelfStockDAO;
import dao.ReportDAO;
import strategy.BatchSelectionStrategy;
import template.*;

public class ReportFactory {
    public enum ReportType {
        DAILY_SALES, RESHELVE, REORDER, STOCK, BILL
    }

    public static ReportTemplate createReport(ReportType reportType, ShelfStockDAO shelfStockDAO, MainStockDAO mainStockDAO, ItemDAO itemDAO, ReportDAO reportDAO, BatchSelectionStrategy strategy) {
        switch (reportType) {
            case DAILY_SALES:
                return new DailySalesReport();
            case RESHELVE:
                return new ReshelveReport(shelfStockDAO, mainStockDAO, itemDAO, strategy);
            case REORDER:
                return new ReorderReport(reportDAO);
            case STOCK:
                return new StockReport(reportDAO);
            case BILL:
                return new BillReport();
            default:
                throw new IllegalArgumentException("Invalid report type");
        }
    }
}
