package template;

import dao.ReportDAO;

import java.sql.Date;
import java.util.List;

public class StockReport extends ReportTemplate {
    private List<Object[]> stockDetails;
    private ReportDAO reportDAO;

public StockReport(ReportDAO reportDAO) {
    this.reportDAO = reportDAO;
}

    @Override
    protected void fetchData() {
        // Fetch stock data
        stockDetails = reportDAO.getStockReport();
    }

    @Override
    protected void processData() {

    }

    @Override
    protected void generateOutput() {
        // Generate the report output
        System.out.println("Stock Report:");
        System.out.println("Item Code | Item Name | Batch Code | Quantity | Expiry Date");
        for (Object[] stock : stockDetails) {
            String itemCode = (String) stock[0];
            String itemName = (String) stock[1];
            String batchCode = (String) stock[2];
            int quantity = (int) stock[3];
            Date expiryDate = (Date) stock[4];

            System.out.printf("%-10s | %-8s | %-10s | %-8d | %-10s\n",
                    itemCode, itemName, batchCode, quantity, expiryDate);
        }
    }

    public List<Object[]> getStockDetails() {
        return stockDetails;
    }
}
