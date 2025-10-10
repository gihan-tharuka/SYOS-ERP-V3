package template;

import dao.ReportDAO;
import model.Bill;

import java.util.List;

public class BillReport extends ReportTemplate {
    private List<Bill> billDetails;
    private ReportDAO reportDAO;

    public BillReport() {
        this.reportDAO = ReportDAO.getInstance();
    }

    @Override
    protected void fetchData() {

        billDetails = reportDAO.getBillReport();
    }

    @Override
    protected void processData() {

    }

    @Override
    protected void generateOutput() {

        System.out.println("Bill Report:");
        System.out.println("Serial Number | Total Price | Cash Tendered | Change Amount | Bill Date | Payment Method");
        for (Bill bill : billDetails) {
            System.out.printf("%-14d | %-11.2f | %-13.2f | %-12.2f | %-9s | %-14s\n",
                    bill.getSerialNumber(), bill.getTotalPrice(), bill.getCashTendered(), bill.getChangeAmount(),
                    bill.getBillDate(), bill.getPaymentMethod());
        }
    }

    public List<Bill> getBillDetails() {
        return billDetails;
    }
}
