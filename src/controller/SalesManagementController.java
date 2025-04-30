package controller;

import dao.*;
import factory.PaymentFactory;
import model.*;
import view.SalesManagementView;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SalesManagementController {
    private SalesManagementView view;
    private SaleDAO saleDAO;
    private BillDAO billDAO;
    private BillItemDAO billItemDAO;
    private ShelfStockDAO shelfStockDAO;
    private ItemDAO itemDAO;

    public SalesManagementController(SalesManagementView view, SaleDAO saleDAO, BillDAO billDAO, BillItemDAO billItemDAO, ShelfStockDAO shelfStockDAO, ItemDAO itemDAO) {
        this.view = view;
        this.saleDAO = saleDAO;
        this.billDAO = billDAO;
        this.billItemDAO = billItemDAO;
        this.shelfStockDAO = shelfStockDAO;
        this.itemDAO = itemDAO;
    }

    public void handleSalesManagement(int choice) {
        switch (choice) {
            case 1:
                viewAllSales();
                break;
            case 2:
                createNewSale();
                break;
            case 3:
                viewBill();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void viewAllSales() {
        List<Sale> sales = saleDAO.getAllSales();
        view.displayAllSales(sales);
    }

    private void createNewSale() {
        Sale sale = view.getNewSaleDetails();
        saleDAO.addSale(sale);
        List<BillItem> billItems = getBillItemsFromUser();
        double totalPrice = calculateTotalPrice(billItems);
        double discount = calculateDiscount(totalPrice);
        double finalPrice = totalPrice - discount;

        System.out.println("Total Price: " + totalPrice);

        Payment payment = PaymentFactory.createPayment(PaymentFactory.PaymentType.CASH);
        payment.setAmount(finalPrice);
        double cashTendered = getCashTenderedFromUser();
        double changeAmount = payment.calculateChange(cashTendered);

        Bill bill = new Bill();
        bill.setSaleId(sale.getSaleId());
        bill.setTotalPrice(totalPrice);
        bill.setDiscount(discount);
        bill.setCashTendered(cashTendered);
        bill.setChangeAmount(changeAmount);
        bill.setBillDate(new java.util.Date());
        bill.setPaymentMethod("Cash");

        billDAO.addBill(bill);
        view.showChangeAmount(changeAmount);
        view.showBillCreatedSuccess();

        for (BillItem billItem : billItems) {
            billItem.setBillId(bill.getSerialNumber());
            billItemDAO.addBillItem(billItem);
            reduceShelfStock(billItem.getItemId(), billItem.getQuantity());
        }
        displayBillDetails(bill, billItems);
    }

    private List<BillItem> getBillItemsFromUser() {
        List<BillItem> billItems = new ArrayList<>();
        boolean moreItems = true;
        while (moreItems) {
            BillItem billItem = new BillItem();
            System.out.println("Enter item code: ");
            String itemCode = new Scanner(System.in).nextLine(); // Assuming item code is unique
            Item item = itemDAO.getItemByCode(itemCode);
            if (item == null) {
                System.out.println("Item not found. Please try again.");
                continue;
            }
            billItem.setItemId(item.getItemId());
            billItem.setItemName(item.getItemName());
            System.out.println("Enter quantity: ");
            int quantity = new Scanner(System.in).nextInt();
            billItem.setQuantity(quantity);
            billItem.setItemTotalPrice(quantity * item.getPrice());
            billItems.add(billItem);
            System.out.println("Add more items? (yes/no): ");
            moreItems = new Scanner(System.in).nextLine().equalsIgnoreCase("yes");
        }
        return billItems;
    }



    private double calculateTotalPrice(List<BillItem> billItems) {
        return billItems.stream().mapToDouble(BillItem::getItemTotalPrice).sum();
    }

    private double calculateDiscount(double totalPrice) {

        return 0;
    }

    private double getCashTenderedFromUser() {
        System.out.println("Enter cash tendered: ");
        return new Scanner(System.in).nextDouble();
    }

    private void reduceShelfStock(int itemId, int quantity) {
        ShelfStock shelfStock = shelfStockDAO.getShelfStockByItemId(itemId);
        if (shelfStock != null) {
            shelfStock.setCurrentQuantity(shelfStock.getCurrentQuantity() - quantity);
            shelfStockDAO.updateShelfStock(shelfStock);
        }
    }

    private void displayBillDetails(Bill bill, List<BillItem> billItems) {
        System.out.println("Bill Details:");
        System.out.println("Serial Number: " + bill.getSerialNumber());
        System.out.println("Item Name  | Quantity | Total Price");
        for (BillItem billItem : billItems) {
            System.out.printf("%-10s | %-8d | %-12.2f\n", billItem.getItemName(), billItem.getQuantity(), billItem.getItemTotalPrice());
        }
        System.out.println("Total Price: " + bill.getTotalPrice());
        System.out.println("Discount: " + bill.getDiscount());
        System.out.println("Cash Tendered: " + bill.getCashTendered());
        System.out.println("Change Amount: " + bill.getChangeAmount());
    }
    private void viewBill() {
        System.out.println("Enter bill serial number: ");
        int serialNumber = new Scanner(System.in).nextInt();
        Bill bill = billDAO.getBillBySerialNumber(serialNumber);
        if (bill == null) {
            System.out.println("Bill not found.");
            return;
        }
        List<BillItem> billItems = billItemDAO.getBillItemsBySerialNumber(serialNumber);
        displayBillDetails(bill, billItems);
    }
}

