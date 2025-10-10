package template;

import dao.ReportDAO;

import java.util.List;

public class ReorderReport extends ReportTemplate {
    private List<Object[]> reorderItems;
    private ReportDAO reportDAO;


public ReorderReport(ReportDAO reportDAO) {
    this.reportDAO = reportDAO;
}

    @Override
    protected void fetchData() {
        // Fetch reorder data
        reorderItems = reportDAO.getReorderItems();
    }

    @Override
    protected void processData() {

    }

    @Override
    protected void generateOutput() {
        // Generate the report output
        System.out.println("Reorder Report:");
        System.out.println("Item Code | Item Name | Current Quantity | Quantity to Reorder");
        for (Object[] item : reorderItems) {
            String itemCode = (String) item[0];
            String itemName = (String) item[1];
            int currentQuantity = (int) item[2];
            int quantityToReorder = (int) item[3];

            System.out.printf("%-10s | %-8s | %-16d | %-18d\n", itemCode, itemName, currentQuantity, quantityToReorder);
        }
    }

    public List<Object[]> getReorderItems() {
        return reorderItems;
    }
}
