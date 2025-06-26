package template;

import dao.ReportDAO;
import model.BillItem;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailySalesReport extends ReportTemplate {
    private List<BillItem> salesDetails;
    private ReportDAO reportDAO;
    private Date reportDate;
    private Map<String, ItemAggregate> itemAggregates;
    private int totalQuantitySold;
    private double totalRevenue;

    public DailySalesReport() {
        this.reportDAO = ReportDAO.getInstance();
        this.itemAggregates = new HashMap<>();
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    @Override
    protected void fetchData() {

        salesDetails = reportDAO.getDailySalesDetails(reportDate);
    }

    @Override
    protected void processData() {

        totalQuantitySold = 0;
        totalRevenue = 0.0;

        for (BillItem billItem : salesDetails) {
            String itemCode = billItem.getItemId() + ""; // Assuming itemId is unique and used as item code
            String itemName = billItem.getItemName();
            int quantity = billItem.getQuantity();
            double totalPrice = billItem.getItemTotalPrice();

            itemAggregates.putIfAbsent(itemCode, new ItemAggregate(itemCode, itemName, 0, 0.0));
            ItemAggregate aggregate = itemAggregates.get(itemCode);
            aggregate.addQuantity(quantity);
            aggregate.addTotalPrice(totalPrice);

            totalQuantitySold += quantity;
            totalRevenue += totalPrice;
        }
    }

    @Override
    protected void generateOutput() {
        // Generate the report output
        System.out.println("Daily Sales Report for " + reportDate + ":");
        System.out.println("Item Code | Item Name | Quantity | Total Price");
        for (ItemAggregate aggregate : itemAggregates.values()) {
            System.out.printf("%-10s | %-10s | %-8d | %-11.2f\n",
                    aggregate.getItemCode(), aggregate.getItemName(),
                    aggregate.getQuantity(), aggregate.getTotalPrice());
        }
        System.out.println("---------------");
        System.out.println("Total Quantity Sold: " + totalQuantitySold);
        System.out.println("Total Revenue: " + totalRevenue);
    }

    public Map<String, ItemAggregate> getItemAggregates() {
        return itemAggregates;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    // Inner class to store aggregated item data
    public static class ItemAggregate {
        private String itemCode;
        private String itemName;
        private int quantity;
        private double totalPrice;

        public ItemAggregate(String itemCode, String itemName, int quantity, double totalPrice) {
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public void addQuantity(int quantity) {
            this.quantity += quantity;
        }

        public void addTotalPrice(double totalPrice) {
            this.totalPrice += totalPrice;
        }

        public String getItemCode() {
            return itemCode;
        }

        public String getItemName() {
            return itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalPrice() {
            return totalPrice;
        }
    }
}
